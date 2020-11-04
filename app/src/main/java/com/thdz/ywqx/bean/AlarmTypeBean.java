package com.thdz.ywqx.bean;

import java.io.Serializable;

/**
 * Created by ZZX on 2016-09-08.
 */

public class AlarmTypeBean implements Serializable {
    private String name;
    private String value;

    public AlarmTypeBean() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
