package com.thdz.ywqx.event;

import com.thdz.ywqx.bean.PushBeanBase;

import java.io.Serializable;

/**
 * 升级包版本信息： 用于EventBus，处理推送消息：
 */
public class UpdateInfoEvent implements Serializable{

    private PushBeanBase pushBean;

    public UpdateInfoEvent(){}

    public UpdateInfoEvent(PushBeanBase pushBean) {
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
                ",pushBean=" + pushBean +
                '}';
    }
}
