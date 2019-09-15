package com.fastobject.diff;


import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by colinsu
 *
 */
public abstract class AbstractObjectDiff {


    protected abstract String genDiffStr(Object sourceObject, Object targetObject) throws Exception;

    public static String genChineseDiffStr(Object sourceObject, Object targetObject) throws Exception {
        List<DiffWapper> diffWappers = generateDiff(sourceObject, targetObject);
        return DiffUtils.genDiffStr(diffWappers);
    }

    public static List<DiffWapper> generateDiff(Object sourceObject, Object targetObject) throws Exception {
        return generateDiff("", "", sourceObject, targetObject);
    }

    private static List<DiffWapper> generateDiff(String path, String cnName, Object sourceObject, Object targetObject)
            throws Exception {
        List<DiffWapper> diffWappers = new ArrayList<>();

        if (sourceObject == null && targetObject == null) {
            return null;
        }

        if (sourceObject == null || targetObject == null) {
            DiffWapper diffWapper = DiffUtils
                    .getDiffWapper(path, cnName, (sourceObject == null ? null : getObjectString(sourceObject)),
                            targetObject == null ? null : getObjectString(targetObject));
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

        Field[] fields = sourceObject.getClass().getDeclaredFields();

        for (int i = 0; i < fields.length; i++) {
            final Field field = fields[i];
            Class<?> type = field.getType();
            String newPath = path + "/" + field.getName();
            String nameCn = newPath;
            field.setAccessible(true);
            if (field.isAnnotationPresent(DiffLog.class)) {
                DiffLog logVo = field.getAnnotation(DiffLog.class);
                if (cnName == null || cnName.equals("")) {
                    nameCn = logVo.name();
                } else {
                    nameCn = cnName + "." + logVo.name();
                }
                if (logVo.ignore()){
                    continue;
                }
            }
            if (Collection.class.isAssignableFrom(type)) {
                //先判断一下集合
                List<?> oldList = (List) field.get(sourceObject);
                List<?> newList = (List) field.get(targetObject);
                Map<Object, Object> oldFilterMap = new HashMap<>();
                Map<Object, Object> newFilterMap = new HashMap<>();
                Field[] collFields = oldList.get(0).getClass().getDeclaredFields();
                Field keyField = null;
                String keyCnName = "";
                for (int j = 0; j < collFields.length; j++) {
                    if (collFields[j].isAnnotationPresent(DiffLogKey.class)) {
                        keyField = collFields[j];
                        DiffLogKey keyFieldAnnotation = keyField.getAnnotation(DiffLogKey.class);
                        keyCnName = keyFieldAnnotation.name();
                        break;
                    }
                }
                keyField.setAccessible(true);
                for (Object o : newList) {
                    Object o1 = keyField.get(o);
                    newFilterMap.put(o1, o);
                }
                for (Object old : oldList) {
                    Object o2 = keyField.get(old);
                    oldFilterMap.put(o2, old);
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
                    String oBPath = newPath + "/" + result.toString();
                    String oBcnName = nameCn + "." + keyCnName + "[" + result.toString() + "]";
                    List<DiffWapper> collectDiff = generateDiff(oBPath, oBcnName, oldOb, newOb);
                    if (collectDiff != null) {
                        diffWappers.addAll(collectDiff);
                    }
                }
            } else {
                DiffWapper diffWapper = generateOneDiffs(newPath, nameCn, field, sourceObject, targetObject);
                if (diffWapper != null) {
                    diffWappers.add(diffWapper);
                }
            }

        }
        return diffWappers;
    }



    private static DiffWapper generateOneDiffs(String path, String nameCn, Field field, Object source, Object target)
            throws Exception {
        //判断是普通Object还是Collection
        //过滤一些不需要的key
        //Collection需要根据某个key进行排序,然后比较
        DiffUtils diffUtils = new DiffUtils();
        String typeName = field.getType().getName();
        Class<?> type = field.getType();
        field.setAccessible(true);
        DiffLog logVo = field.getAnnotation(DiffLog.class);
        String dateFormat = "";
        if (logVo != null) {
            dateFormat = logVo.dateFormat();
            if (logVo.ignore()){
                return null;
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
                return diffUtils.getDiffWapper(path, nameCn, oldTime==null ?null:oldTimeTimeStr, newTime==null?null: newTempTimeStr);
            }

            if (!StringUtils.equals(newTempTimeStr, oldTimeTimeStr)) {
                return diffUtils.getDiffWapper(path, nameCn, format.format(oldTime), format.format(newTime));
            }
        } else if ("java.lang.Long".equals(typeName) || Long.TYPE == type) {
            Long oldValue = (Long) field.get(source);
            Long newValue = (Long) field.get(target);
            return diffUtils.get(path, nameCn, oldValue, newValue);

        } else if ("java.lang.Integer".equals(typeName) || Integer.TYPE == type) {
            Integer oldValue = field.getInt(source);
            Integer newValue = field.getInt(target);
            return diffUtils.get(path, nameCn, oldValue, newValue);

        } else if ("java.lang.Boolean".equals(typeName) || Boolean.TYPE == type) {
            Boolean oldValue = field.getBoolean(source);
            Boolean newValue = field.getBoolean(target);
            return diffUtils.get(path, nameCn, oldValue, newValue);

        } else if ("java.math.BigDecimal".equals(typeName)) {
            //
            BigDecimal oldValue = (BigDecimal)field.get(source);
            BigDecimal newValue = (BigDecimal)field.get(target);
            return diffUtils.get(path, nameCn, oldValue, newValue);
        } else if ("java.lang.Byte".equals(typeName) || Byte.TYPE == type) {
            //预留不处理
        } else if ("java.lang.Short".equals(typeName) || Short.TYPE == type) {
            //预留不处理 有需要在处理
        } else if ("java.lang.Float".equals(typeName) || Float.TYPE == type) {
            Float oldValue = field.getFloat(source);
            Float newValue = field.getFloat(target);
            return diffUtils.get(path, nameCn, oldValue, newValue);

        } else if ("java.lang.Double".equals(typeName) || Double.TYPE == type) {
            Double oldValue = field.getDouble(source);
            Double newValue = field.getDouble(target);
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
                return diffUtils.getDiffWapper(path, nameCn, oldTime==null ?null:oldTimeTimeStr, newTime==null?null: newTempTimeStr);
            }
            if (!StringUtils.equals(newTempTimeStr, oldTimeTimeStr)) {
                return diffUtils.getDiffWapper(path, nameCn, oldTimeTimeStr, newTempTimeStr);
            }
        }
        return null;
    }


    private static String getObjectString(Object source) throws Exception {
        if (source == null) {
            return "";
        }
        List<String> logList = new ArrayList<>();
        Field[] fields = source.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            String logStr = "";
            Field field = fields[i];
            String typeName = field.getType().getName();
            field.setAccessible(true);
            DiffLog logVo = field.getAnnotation(DiffLog.class);
            String nameCn = field.getName();
            String dateFormat = "";
            if (logVo != null) {
                nameCn = logVo.name();
                dateFormat = logVo.dateFormat();
            }
            if ("java.lang.String".equals(typeName)) {
                String oldStr = (String) field.get(source);
                logStr = "[" + nameCn + "]=" + oldStr + " ";
                logList.add(logStr);
            } else if ("java.sql.Timestamp".equals(typeName) || "java.util.Date".equals(typeName)) {
                DateFormat format =
                        new SimpleDateFormat(StringUtils.isBlank(dateFormat) ? "yyyy-MM-dd HH:mm:ss" : dateFormat);
                java.sql.Timestamp oldTime = (java.sql.Timestamp) field.get(source);
                if (oldTime != null) {
                    logStr = "[" + nameCn + "]=" + format.format(oldTime) + " ";
                    logList.add(logStr);
                }
            } else {
                Object oldValue = (Object) field.get(source);
                logStr = "[" + nameCn + "]=" + oldValue.toString() + " ";
                logList.add(logStr);
            }


        }
        return StringUtils.join(logList.iterator(),",");

    }





}
