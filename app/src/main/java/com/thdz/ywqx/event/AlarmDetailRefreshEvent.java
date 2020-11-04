package com.thdz.ywqx.event;

import com.thdz.ywqx.bean.PushBeanBase;

import java.io.Serializable;

/**
 * 用于EventBus，处理推送消息<br/>
 * 告警详情页 -- 状态发生变化，需要更新页面状态
 */
public class AlarmDetailRefreshEvent implements Serializable{

    private PushBeanBase pushBean;

    public AlarmDetailRefreshEvent(){}

    public AlarmDetailRefreshEvent(PushBeanBase pushBean) {
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
        return "AlarmDetailRefreshEvent{" +
                "pushBean=" + pushBean +
                '}';
    }
}
