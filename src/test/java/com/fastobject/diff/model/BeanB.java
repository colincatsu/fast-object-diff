package com.fastobject.diff.model;



import com.fastobject.diff.DiffLog;
import com.fastobject.diff.DiffLogKey;
import java.util.Date;

/**
 * Created by colinsu
 *
 * @date 2019/9/6.
 */
public class BeanB {

    @DiffLogKey(name = "订单编号")
    @DiffLog(name = "主键")
    private Long id ;

    @DiffLog(name = "机场")
    private String name;

    @DiffLog(name = "开始时间",dateFormat = "yyyy-MM-dd hh:mm:ss")
    private Date startDate;





    public BeanB(Long id, String name,Date startDate) {
        this.id = id;
        this.name = name;
        this.startDate = startDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
