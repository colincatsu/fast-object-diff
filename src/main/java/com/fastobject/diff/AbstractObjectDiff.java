package com.fastobject.diff;


import com.fastobject.diff.DiffConfig.FieldConfig;
import java.time.format.DateTimeFormatter;
import org.apache.commons.lang.StringUtils;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by colinsu
 *
 */
public abstract class AbstractObjectDiff {

    public static final Field[] EMPTY_FIELD_ARRAY = {};

    protected abstract String genDiffStr(Object sourceObject, Object targetObject) throws Exception;

    protected abstract String genDiffStr(DiffConfig diffConfig,Object sourceObject, Object targetObject) throws Exception;


    public static String genChineseDiffStr(Object sourceObject, Object targetObject) throws Exception {
        List<DiffWapper> diffWappers = generateDiff(sourceObject, targetObject);
        return DiffUtils.genDiffStr(diffWappers);
    }

    public static String genChineseDiffStr(DiffConfig diffConfig,Object sourceObject, Object targetObject) throws Exception {
        List<DiffWapper> diffWappers = generateDiff(diffConfig,sourceObject, targetObject);
        return DiffUtils.genDiffStr(diffWappers);
    }

    public static List<DiffWapper> generateDiff(Object sourceObject, Object targetObject) throws Exception {
        return generateDiff("", "", sourceObject, targetObject,null);
    }

    public static List<DiffWapper> generateDiff(DiffConfig diffConfig,Object sourceObject, Object targetObject) throws Exception {
        return generateDiff("", "", sourceObject, targetObject,diffConfig);
    }

    private static List<DiffWapper> generateDiff(String path, String cnName, Object sourceObject, Object targetObject,DiffConfig diffConfig)
        throws Exception {
        List<DiffWapper> diffWappers = new ArrayList<>();

        if (sourceObject == null && targetObject == null) {
            return null;
        }

        if (sourceObject == null || targetObject == null) {
            DiffWapper diffWapper = DiffUtils
                .getDiffWapper(path, cnName, (sourceObject == null ? null : getObjectString(sourceObject,diffConfig)),
                    targetObject == null ? null : getObjectString(targetObject,diffConfig));
            diffWappers.add(diffWapper);
            return diffWappers;
        }

        //先判断object类型
        if (!sourceObject.getClass().getName().equals(targetObject.getClass().getName())) {
            return null;
        }

        if (sourceObject.hashCode() == targetObject.hashCode()) {
            return null;
        }

        Field[] fields = getAllFields(sourceObject.getClass());

        for (int i = 0; i < fields.length; i++) {
            final Field field = fields[i];
            Class<?> type = field.getType();
            String newPath = path + "/" + field.getName();
            String nameCn = newPath;
            field.setAccessible(true);
            FieldConfig fieldConfig = null;
            if (diffConfig != null ) {
                Map<String, FieldConfig> classFieldConfig = diffConfig.getClassFields().get(sourceObject.getClass().getName());
                if (classFieldConfig == null){
                    continue;
                }
                fieldConfig = classFieldConfig.get(field.getName());
                if (fieldConfig!=null){
                    if (cnName == null || cnName.equals("")) {
                        nameCn = fieldConfig.getFullName();
                    } else {
                        nameCn = cnName + "." + fieldConfig.getFullName();
                    }
                    if (fieldConfig.isIgnore()){
                        continue;
                    }
                }else{
                    continue;
                }
            }
            else if (field.isAnnotationPresent(DiffLog.class)) {
                DiffLog logVo = field.getAnnotation(DiffLog.class);
                if (cnName == null || cnName.equals("")) {
                    nameCn = logVo.name();
                } else {
                    nameCn = cnName + "." + logVo.name();
                }
                if (logVo.ignore()){
                    continue;
                }
            }else{
                continue;
            }
            if (Collection.class.isAssignableFrom(type)) {
                //先判断一下集合
                List<?> oldList = (List) field.get(sourceObject);
                List<?> newList = (List) field.get(targetObject);
                Map<Object, Object> oldFilterMap = new HashMap<>();
                Map<Object, Object> newFilterMap = new HashMap<>();
                Class<?> genricClass = null;
                if (field.getGenericType() instanceof ParameterizedType) {
                    // 获取泛型 Class
                    genricClass = (Class<?>) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
                }
                if (genricClass == null) {
                    continue;
                }
                Field[] collFields = getAllFields(genricClass);
                Field keyField = null;
                String keyCnName = "";
                Map<String, FieldConfig> collFieldConfig = null ;
                if (diffConfig != null ){
                    collFieldConfig = diffConfig.getClassFields().get(genricClass.getName());
                }

                for (int j = 0; j < collFields.length; j++) {
                    if (diffConfig != null && collFieldConfig !=null) {
                        keyField = collFields[j];
                        FieldConfig fieldKeyConfig = collFieldConfig.get(keyField.getName());
                        if (fieldKeyConfig!=null && fieldKeyConfig.getDiffLogKey()!=null){
                            keyCnName = fieldKeyConfig.getDiffLogKey().getName();
                            break;
                        }
                    }
                    else if (collFields[j].isAnnotationPresent(DiffLogKey.class)) {
                        keyField = collFields[j];
                        DiffLogKey keyFieldAnnotation = keyField.getAnnotation(DiffLogKey.class);
                        keyCnName = keyFieldAnnotation.name();
                        break;
                    }
                }
                if (keyField == null){
                    continue;
                }
                keyField.setAccessible(true);
                if (newList != null) {
                    for (Object o : newList) {
                        Object o1 = keyField.get(o);
                        newFilterMap.put(o1, o);
                    }
                }
                if (oldList != null) {
                    for (Object old : oldList) {
                        Object o2 = keyField.get(old);
                        oldFilterMap.put(o2, old);
                    }
                }
                Set<Object> oldKeySets = oldFilterMap.keySet();
                Set<Object> newKeySets = newFilterMap.keySet();
                Set<Object> resultSet = new HashSet<>();
                resultSet.clear();
                resultSet.addAll(oldKeySets);
                resultSet.addAll(newKeySets);
                //取两个之间的并集,然后统一输出
                for (Object result : resultSet) {
                    Object oldOb = oldFilterMap.get(result);
                    Object newOb = newFilterMap.get(result);
                    String oBPath = newPath + "/" + (result == null ? "null" : result.toString());
                    String oBcnName = nameCn + "." + keyCnName + "[" + (result == null ? "null" : result.toString()) + "]";
                    List<DiffWapper> collectDiff = generateDiff(oBPath, oBcnName, oldOb, newOb,diffConfig);
                    if (collectDiff != null) {
                        diffWappers.addAll(collectDiff);
                    }
                }
            } else {
                //判断是否java内部类
                if (isJavaClass(type)){
                    DiffWapper diffWapper = generateOneDiffs(newPath, nameCn, field, sourceObject, targetObject,fieldConfig);
                    if (diffWapper != null) {
                        diffWappers.add(diffWapper);
                    }
                } else {
                    //如自定义bean则走递归方法
                    List<DiffWapper> collectDiff = generateDiff(newPath, nameCn,
                        field.get(sourceObject), field.get(targetObject),diffConfig);
                    if (collectDiff != null) {
                        diffWappers.addAll(collectDiff);
                    }
                }
            }

        }
        return diffWappers;
    }



    private static DiffWapper generateOneDiffs(String path, String nameCn, Field field, Object source, Object target,FieldConfig fieldConfig)
        throws Exception {
        //判断是普通Object还是Collection
        //过滤一些不需要的key
        //Collection需要根据某个key进行排序,然后比较
        DiffUtils diffUtils = new DiffUtils();
        String typeName = field.getType().getName();
        Class<?> type = field.getType();
        field.setAccessible(true);
        String dateFormat = "";
        if (fieldConfig!=null){
            dateFormat = fieldConfig.getDateFormat();
            if (fieldConfig.isIgnore()){
                return null;
            }
        }else{
            DiffLog logVo = field.getAnnotation(DiffLog.class);
            if (logVo != null) {
                dateFormat = logVo.dateFormat();
                if (logVo.ignore()){
                    return null;
                }
            }
        }

        if ("java.lang.String".equals(typeName)) {
            String oldStr = (String) field.get(source);
            String newStr = (String) field.get(target);
            return diffUtils.get(path, nameCn, oldStr, newStr);
        } else if ("java.sql.Timestamp".equals(typeName)) {
            DateFormat format =
                new SimpleDateFormat(StringUtils.isBlank(dateFormat) ? "yyyy-MM-dd HH:mm:ss" : dateFormat);
            java.sql.Timestamp newTime = (java.sql.Timestamp) field.get(target);
            java.sql.Timestamp oldTime = (java.sql.Timestamp) field.get(source);
            String newTempTimeStr = "";
            String oldTimeTimeStr = "";
            if (newTime != null) {
                newTempTimeStr = format.format(newTime);
            }
            if (oldTime != null) {
                oldTimeTimeStr = format.format(oldTime);
            }
            if (oldTime == newTime && oldTime == null) {
                return null;
            }
            if (oldTime == null || newTime == null) {
                return DiffUtils.getDiffWapper(path, nameCn, oldTime==null ?null:oldTimeTimeStr, newTime==null?null: newTempTimeStr);
            }

            if (!StringUtils.equals(newTempTimeStr, oldTimeTimeStr)) {
                return DiffUtils.getDiffWapper(path, nameCn, format.format(oldTime), format.format(newTime));
            }
        } else if ("java.lang.Long".equals(typeName) || Long.TYPE == type) {
            Long oldValue = (Long) field.get(source);
            Long newValue = (Long) field.get(target);
            return diffUtils.get(path, nameCn, oldValue, newValue);

        } else if ("java.lang.Integer".equals(typeName) || Integer.TYPE == type) {
            Integer oldValue =(Integer) field.get(source);
            Integer newValue = (Integer) field.get(target);
            return diffUtils.get(path, nameCn, oldValue, newValue);

        } else if ("java.lang.Boolean".equals(typeName) || Boolean.TYPE == type) {
            Boolean oldValue = (Boolean)field.get(source);
            Boolean newValue = (Boolean)field.get(target);
            return diffUtils.get(path, nameCn, oldValue, newValue);

        } else if ("java.math.BigDecimal".equals(typeName)) {
            BigDecimal oldValue = (BigDecimal)field.get(source);
            BigDecimal newValue = (BigDecimal)field.get(target) ;
            if (oldValue!=null && newValue!=null && oldValue.compareTo(newValue) == 0){
                newValue = oldValue;
            }
            return diffUtils.get(path, nameCn, oldValue, newValue);
        } else if ("java.lang.Byte".equals(typeName) || Byte.TYPE == type) {
            //预留不处理
            Byte oldValue = (Byte) field.get(source);
            Byte newValue = (Byte) field.get(target);
            if (oldValue!=null && newValue!=null && oldValue.compareTo(newValue) == 0){
                newValue = oldValue;
            }
            return diffUtils.get(path, nameCn, oldValue, newValue);
        } else if ("java.lang.Short".equals(typeName) || Short.TYPE == type) {
            Short oldValue = (Short) field.get(source);
            Short newValue = (Short) field.get(target);
            if (oldValue!=null && newValue!=null && oldValue.compareTo(newValue) == 0){
                newValue = oldValue;
            }
            return diffUtils.get(path, nameCn, oldValue, newValue);
            //预留不处理 有需要在处理
        } else if ("java.lang.Float".equals(typeName) || Float.TYPE == type) {
            Float oldValue = field.getFloat(source);
            Float newValue = field.getFloat(target);
            return diffUtils.get(path, nameCn, oldValue, newValue);

        } else if ("java.lang.Double".equals(typeName) || Double.TYPE == type) {
            String oldValue = field.get(source) == null? null :String.valueOf(field.get(source));
            String newValue =  field.get(target) == null ? null :String.valueOf(field.get(target));
            return diffUtils.get(path, nameCn, oldValue, newValue);

        }
        else if ("java.util.Date".equals(typeName)) {
            DateFormat format = new SimpleDateFormat(StringUtils.isBlank(dateFormat) ? "yyyy-MM-dd" : dateFormat);
            java.util.Date newTime = (java.util.Date) field.get(target);
            java.util.Date oldTime = (java.util.Date) field.get(source);
            String newTempTimeStr = "";
            String oldTimeTimeStr = "";
            if (newTime != null) {
                newTempTimeStr = format.format(newTime);
            }
            if (oldTime != null) {
                oldTimeTimeStr = format.format(oldTime);
            }
            if (oldTime == newTime && oldTime == null) {
                return null;
            }
            if (oldTime == null || newTime == null) {
                return DiffUtils.getDiffWapper(path, nameCn, oldTime==null ?null:oldTimeTimeStr, newTime==null?null: newTempTimeStr);
            }
            if (!StringUtils.equals(newTempTimeStr, oldTimeTimeStr)) {
                return DiffUtils.getDiffWapper(path, nameCn, oldTimeTimeStr, newTempTimeStr);
            }
        }
        else if ("java.time.LocalDateTime".equals(typeName)) {
            DateTimeFormatter format =  DateTimeFormatter.ofPattern(StringUtils.isBlank(dateFormat) ? "yyyy-MM-dd hh:mm:ss" : dateFormat);
            java.time.LocalDateTime newTime = (java.time.LocalDateTime) field.get(target);
            java.time.LocalDateTime oldTime = (java.time.LocalDateTime) field.get(source);
            String newTempTimeStr = "";
            String oldTimeTimeStr = "";
            if (newTime != null) {
                newTempTimeStr = format.format(newTime);
            }
            if (oldTime != null) {
                oldTimeTimeStr = format.format(oldTime);
            }
            if (oldTime == newTime && oldTime == null) {
                return null;
            }
            if (oldTime == null || newTime == null) {
                return DiffUtils.getDiffWapper(path, nameCn, oldTime==null ?null:oldTimeTimeStr, newTime==null?null: newTempTimeStr);
            }
            if (!StringUtils.equals(newTempTimeStr, oldTimeTimeStr)) {
                return DiffUtils.getDiffWapper(path, nameCn, oldTimeTimeStr, newTempTimeStr);
            }
        }
        return null;
    }


    private static String getObjectString(Object source,DiffConfig diffConfig) throws Exception {
        if (source == null) {
            return "";
        }
        List<String> logList = new ArrayList<>();
        Field[] fields = getAllFields(source.getClass());
        Map<String, FieldConfig> classFieldConfig = null;
        if (diffConfig!=null){
            classFieldConfig = diffConfig.getClassFields().get(source.getClass().getName());
        }
        for (int i = 0; i < fields.length; i++) {
            String logStr = "";
            Field field = fields[i];
            String typeName = field.getType().getName();
            field.setAccessible(true);
            String nameCn = field.getName();
            String dateFormat = "";
            if (classFieldConfig!=null){
                FieldConfig fieldConfig = classFieldConfig.get(field.getName());
                if (fieldConfig == null){
                    continue;
                }
                nameCn = fieldConfig.getFullName();
                dateFormat = fieldConfig.getDateFormat();
            }else{
                DiffLog logVo = field.getAnnotation(DiffLog.class);
                if (logVo != null) {
                    nameCn = logVo.name();
                    dateFormat = logVo.dateFormat();
                } else {
                    continue;
                }
            }

            if ("java.lang.String".equals(typeName)) {
                String oldStr = (String) field.get(source);
                logStr = "[" + nameCn + "]=" + oldStr + " ";
                logList.add(logStr);
            } else if ("java.util.Date".equals(typeName)) {
                DateFormat format =
                    new SimpleDateFormat(StringUtils.isBlank(dateFormat) ? "yyyy-MM-dd HH:mm:ss" : dateFormat);
                java.util.Date oldTime = (java.util.Date) field.get(source);
                if (oldTime != null) {
                    logStr = "[" + nameCn + "]=" + format.format(oldTime) + " ";
                    logList.add(logStr);
                }
            } else {
                Object oldValue = (Object) field.get(source);
                logStr = "[" + nameCn + "]=" + (oldValue == null ? "null" : oldValue.toString()) + " ";
                logList.add(logStr);
            }


        }
        return StringUtils.join(logList.iterator(),",").trim();

    }


    public static boolean isJavaClass(Class<?> clz) {
        return clz != null && clz.getClassLoader() == null;
    }

    /**
     * org.apache.commons.lang3.reflect.FieldUtils.getAllFields(),version 3.13.0
     */
    private static Field[] getAllFields(final Class<?> cls) {
        if (cls == null)
            throw new NullPointerException("cls");
        final List<Field> allFields = new ArrayList<>();
        Class<?> currentClass = cls;
        while (currentClass != null) {
            final Field[] declaredFields = currentClass.getDeclaredFields();
            Collections.addAll(allFields, declaredFields);
            currentClass = currentClass.getSuperclass();
        }
        return allFields.toArray(EMPTY_FIELD_ARRAY);
    }

}
