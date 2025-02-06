package com.fastobject.diff.model;

import com.fastobject.diff.DiffLog;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by colinsu
 *
 * @date 2025/2/6.
 */
public class BeanF {

//    @DiffLog(name = "测试a")
    private String a;

//    @DiffLog(name = "测试b", ignore = true)
    private String b;

//    @DiffLog(name = "BList集合")
    private List<BeanG> bList;

//    @DiffLog(name = "开始时间",dateFormat = "yyyy-MM-dd hh:mm:ss")
    private Date start;

//    @DiffLog(name = "价格")
    private BigDecimal price;

    public BeanF() {


    }

    public BeanF(String a, String b, List<BeanG> bList) {
        this.a = a;
        this.b = b;
        this.bList = bList;
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

    public List<BeanG> getbList() {
        return bList;
    }

    public void setbList(List<BeanG> bList) {
        this.bList = bList;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
