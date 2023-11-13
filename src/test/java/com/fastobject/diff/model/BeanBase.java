package com.fastobject.diff.model;

import com.fastobject.diff.DiffLog;

import java.util.Date;

/**
 * @author Charles94jp
 * @since 2023-10-16
 */
public abstract class BeanBase {
    /**
     * 实体编号（唯一标识）
     */
    @DiffLog(name = "id")
    protected Long id;

    protected Date createDate;

    public BeanBase() {
    }

    public BeanBase(Long id) {
        this.id = id;
    }

    public BeanBase(Long id, Date createDate) {
        this.id = id;
        this.createDate = createDate;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
