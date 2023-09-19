package com.fastobject.diff.model;

import com.fastobject.diff.DiffLog;

/**
 * Created by colinsu
 *
 * @date 2023/9/19.
 */

public class BeanD {

    @DiffLog(name = "nameD")
    private String nameD;

    public BeanD(String nameD) {
        this.nameD = nameD;
    }

    public String getNameD() {
        return nameD;
    }

    public void setNameD(String nameD) {
        this.nameD = nameD;
    }
}
