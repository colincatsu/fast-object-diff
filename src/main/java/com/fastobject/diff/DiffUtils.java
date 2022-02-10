package com.fastobject.diff;

import java.util.List;

/**
 * Created by colinsu
 *
 */
public class DiffUtils<T> {
    public final static String CHANGE = "CHANGE";
    public final static String REMOVE = "REMOVE";
    public final static String ADD = "ADD";

    public DiffWapper get(String path,String nameCn,T oldValue,T newValue){
        if (oldValue == newValue && oldValue == null) {
            return null;
        }
        if (oldValue == null || newValue ==null){
            return getDiffWapper(path, nameCn, oldValue, newValue);
        }
        if(!newValue.equals(oldValue)){
            return getDiffWapper(path, nameCn, oldValue, newValue);
        }
        return null;
    }


    public static DiffWapper getDiffWapper(String path, String nameCn, Object oldStr, Object newStr) {
        String op = CHANGE;
        if (newStr==null && oldStr!=null){
            op = REMOVE;
        }
        if (oldStr==null && newStr!=null){
            op = ADD;
        }
        return new DiffWapper(path,nameCn,op,new Difference(oldStr,newStr));
    }


    public static String genDiffStr(List<DiffWapper> diffWapperList){
        StringBuffer sb = new StringBuffer();

        if (diffWapperList!=null && diffWapperList.size()>0){
            for (DiffWapper diffWapper : diffWapperList) {
                String op = diffWapper.getOp();
                String opCn = "修改为";
                if (op.equals(ADD)){
                    opCn = "被添加成";
                    sb.append(String.format("「%s」%s[%s]",diffWapper.getLogName(),opCn,diffWapper.getDiffValue().getNewValue()));
                }else if (op.equals(REMOVE)){
                    opCn = "被删除";
                    sb.append(String.format("「%s」由[%s]%s",diffWapper.getLogName(),diffWapper.getDiffValue().getOldValue(),opCn));
                }else{
                    sb.append(String.format("「%s」由[%s]%s[%s]",diffWapper.getLogName(),diffWapper.getDiffValue().getOldValue(),opCn,diffWapper.getDiffValue().getNewValue()));
                }
                sb.append("\n");
            }
        }
        return sb.toString();
    }


}
