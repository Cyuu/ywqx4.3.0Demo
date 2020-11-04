package com.thdz.ywqx.bean;

import java.io.Serializable;

/**
 * desc:    雷达告警目标
 * author:  Administrator
 * date:    2018/8/9  8:27
 */
public class RadarTargetBean implements Serializable {
    public float x; // 横坐标
    public float y; // 纵坐标
    // 两个type共同判断展示的文本内容
    public int type;    // 最外层返回的一个type
    public int objType; // 告警目标层里返回的一个type
    public int cpRad;   // 标签上展示的半径值

    public RadarTargetBean() {

    }

    public RadarTargetBean(float x, float y, int type, int objType, int cpRad) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.objType = objType;
        this.cpRad = cpRad;
    }

    public String getTypeValue() {
        String rr = "(" + cpRad + ")";
        if (cpRad == 0) {
            rr = "";
        }
        switch (type) {
            case 3038:
                return "预警" + rr;
            case 3040:
                return "工务" + rr;
            case 3041:
                if (objType == 100) {
                    return "工务" + rr;
                } else if (objType == 150) {
                    return "行车" + rr;
                } else if (objType == 200) {
                    return "弹飞" + rr;
                }
                break;
        }
        return "";
    }

}
