package com.thdz.ywqx.bean;

import java.io.Serializable;

/**
 * 监控点升级包版本信息bean
 */
public class StnUpdateBean implements Serializable {

    private String index;
    private String monitor_name; // 监控点名称
    private String update_type; // 类型
    private String SID; // 监控单元NO
    private String update_info; // 监控单元NO

    public StnUpdateBean(){}

    public StnUpdateBean(String index, String monitor_name, String update_type, String SID, String update_info) {
        this.index = index;
        this.monitor_name = monitor_name;
        this.update_type = update_type;
        this.SID = SID;
        this.update_info = update_info;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getMonitor_name() {
        return monitor_name;
    }

    public void setMonitor_name(String monitor_name) {
        this.monitor_name = monitor_name;
    }

    public String getUpdate_type() {
        return update_type;
    }

    public void setUpdate_type(String update_type) {
        this.update_type = update_type;
    }

    public String getSID() {
        return SID;
    }

    public void setSID(String SID) {
        this.SID = SID;
    }

    public String getUpdate_info() {
        return update_info;
    }

    public void setUpdate_info(String update_info) {
        this.update_info = update_info;
    }

    @Override
    public String toString() {
        return "StnUpdateBean{" +
                "index='" + index + '\'' +
                ", monitor_name='" + monitor_name + '\'' +
                ", update_type='" + update_type + '\'' +
                ", SID='" + SID + '\'' +
                ", update_info='" + update_info + '\'' +
                '}';
    }
}
