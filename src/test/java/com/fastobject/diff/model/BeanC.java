package com.fastobject.diff.model;



import com.fastobject.diff.DiffLog;
import com.fastobject.diff.DiffLogKey;
import java.util.Date;

/**
 * Created by colinsu
 *
 * @date 2019/9/6.
 */
public class BeanC {

    @DiffLog(name = "cid")
    private Long cid ;

    @DiffLog(name = "C名称")
    private String cname;

    @DiffLog(name = "开始时间",dateFormat = "yyyy-MM-dd hh:mm:ss:SSS")
    private Date startDate;

    @DiffLog(name = "D")
    private BeanD beanD;


    public BeanC(Long cid, String cname, Date startDate, BeanD beanD) {
        this.cid = cid;
        this.cname = cname;
        this.startDate = startDate;
        this.beanD = beanD;
    }

    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public BeanD getBeanD() {
        return beanD;
    }

    public void setBeanD(BeanD beanD) {
        this.beanD = beanD;
    }
}
