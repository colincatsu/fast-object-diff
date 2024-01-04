package com.fastobject.diff.model;



import com.fastobject.diff.DiffLog;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * Created by colinsu
 *
 * @date 2019/9/6.
 */
public class BeanA {


    @DiffLog(name = "测试a")
    private String a;

    @DiffLog(name = "测试b", ignore = true)
    private String b;

    @DiffLog(name = "BList集合")
    private List<BeanB> bList;

    @DiffLog(name = "开始时间",dateFormat = "yyyy-MM-dd hh:mm:ss")
    private Date start;

    @DiffLog(name = "价格")
    private BigDecimal price;

    @DiffLog(name = "bit")
    private Byte bit;

    @DiffLog(name = "aBoolean")
    private Boolean aBoolean;

    @DiffLog(name = "时间LocalDateTime")
    private LocalDateTime localDateTime;

    @DiffLog(name = "short")
    private Short unit;

    @DiffLog(name = "beanC")
    private BeanC beanC;

    @DiffLog(name = "beanE")
    private BeanE beanE;

    public BeanA(String a, String b, List<BeanB> bList) {
        this.a = a;
        this.b = b;
        this.bList = bList;
    }

    public BeanA(String a, String b, List<BeanB> bList, Date start, BigDecimal price,
        Byte bit, Short unit, BeanC beanC) {
        this.a = a;
        this.b = b;
        this.bList = bList;
        this.start = start;
        this.price = price;
        this.bit = bit;
        this.unit = unit;
        this.beanC = beanC;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public String getB() {
        return b;
    }

    public void setB(String b) {
        this.b = b;
    }

    public List<BeanB> getbList() {
        return bList;
    }

    public void setbList(List<BeanB> bList) {
        this.bList = bList;
    }

    public Byte getBit() {
        return bit;
    }

    public void setBit(Byte bit) {
        this.bit = bit;
    }

    public Short getUnit() {
        return unit;
    }

    public void setUnit(Short unit) {
        this.unit = unit;
    }

    public BeanC getBeanC() {
        return beanC;
    }

    public void setBeanC(BeanC beanC) {
        this.beanC = beanC;
    }

    public BeanE getBeanE() {
        return beanE;
    }

    public void setBeanE(BeanE beanE) {
        this.beanE = beanE;
    }

    public Boolean getaBoolean() {
        return aBoolean;
    }

    public void setaBoolean(Boolean aBoolean) {
        this.aBoolean = aBoolean;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }
}
