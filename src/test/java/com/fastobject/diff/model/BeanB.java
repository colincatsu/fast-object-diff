package com.fastobject.diff.model;



import com.fastobject.diff.DiffLog;
import com.fastobject.diff.DiffLogKey;

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





    public BeanB(Long id, String name) {
        this.id = id;
        this.name = name;
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
