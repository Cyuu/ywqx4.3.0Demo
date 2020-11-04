package com.thdz.ywqx.event;

import com.thdz.ywqx.bean.PushBeanBase;

import java.io.Serializable;

/**
 * 最新告警列表发生变化，其中某条告警的状态和信息发生变化, BUT,目前使用的是：通知栏，不用EvnentBus分发了
 */
public class AlarmListEvent implements Serializable{

    private PushBeanBase pushBean;

    public AlarmListEvent(){}

    public AlarmListEvent(PushBeanBase pushBean) {
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
        return "AlarmListEvent{" +
                "pushBean=" + pushBean +
                '}';
    }
}
