package com.fastobject.diff.model;

import com.fastobject.diff.DiffLog;

/**
 * @author Charles94jp
 * @since 2023-10-16
 */
public class BeanE extends BeanBase{
    @DiffLog(name = "名称")
    public String name;

    public BeanE() {
    }

    public BeanE(String name) {
        this.name = name;
    }

    public BeanE(Long id, String name) {
        super(id);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
