package com.thdz.ywqx.event;

import com.thdz.ywqx.bean.PushBeanBase;

import java.io.Serializable;

/**
 * 相应推送的图片到达命令
 */

public class PicEvent implements Serializable {

    private PushBeanBase pushBean;

    public PicEvent (){}

    public PicEvent(PushBeanBase pushBean) {
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
        return "PicEvent{" +
                "pushBean=" + pushBean +
                '}';
    }
}
