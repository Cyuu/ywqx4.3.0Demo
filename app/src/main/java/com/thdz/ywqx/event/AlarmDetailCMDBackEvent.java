package com.thdz.ywqx.event;

import com.thdz.ywqx.bean.PushBeanBase;

import java.io.Serializable;

/**
 * 用于EventBus，处理推送消息<br/>
 * 告警详情页 -- 控制命令发送成功/失败的结果
 */
public class AlarmDetailCMDBackEvent implements Serializable{

    private String alarm_id;
    private PushBeanBase pushBean;

    public AlarmDetailCMDBackEvent(){}

    public AlarmDetailCMDBackEvent(PushBeanBase pushBean) {
        this.pushBean = pushBean;
    }

    public String getAlarm_id() {
        return alarm_id;
    }

    public void setAlarm_id(String alarm_id) {
        this.alarm_id = alarm_id;
    }

    public PushBeanBase getPushBean() {
        return pushBean;
    }

    public void setPushBean(PushBeanBase pushBean) {
        this.pushBean = pushBean;
    }

    @Override
    public String toString() {
        return "AlarmDetailCMDBackEvent{" +
                "alarm_id='" + alarm_id + '\'' +
                ", pushBean=" + pushBean +
                '}';
    }
}
