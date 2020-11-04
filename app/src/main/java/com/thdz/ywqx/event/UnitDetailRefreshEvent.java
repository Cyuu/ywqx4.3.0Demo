package com.thdz.ywqx.event;

import com.thdz.ywqx.bean.PushBeanBase;

import java.io.Serializable;

/**
 * 用于EventBus，处理推送消息：具体某条告警信息和状态发生变化，
 */
public class UnitDetailRefreshEvent implements Serializable{

    private PushBeanBase pushBean;

    public UnitDetailRefreshEvent(){}

    public UnitDetailRefreshEvent(PushBeanBase pushBean) {
        this.pushBean = pushBean;
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
                "pushBean=" + pushBean +
                '}';
    }
}
