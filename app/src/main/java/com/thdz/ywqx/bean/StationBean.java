package com.thdz.ywqx.bean;

import java.io.Serializable;

/**
 * 站点bean
 */
public class StationBean implements Serializable {

    private String RBId; // 父--父层 局id
    private String RLId;// 父层 线路id
    private String RLName;// 父层 线路name

    private String RWIId; // 工区
    private String RSId; // 工务段

    private String StnId; // 1
    private String StnName; // 泗亭1号
    private String StnNo; // 131001

    public StationBean() {
        super();
    }

    public StationBean(String RBId, String RLId, String RWIId, String RSId, String stnId, String stnNo) {
        this.RBId = RBId;
        this.RLId = RLId;
        this.RWIId = RWIId;
        this.RSId = RSId;
        StnId = stnId;
        StnNo = stnNo;
    }

    public String getRWIId() {
        return RWIId;
    }

    public void setRWIId(String RWIId) {
        this.RWIId = RWIId;
    }

    public String getRSId() {
        return RSId;
    }

    public void setRSId(String RSId) {
        this.RSId = RSId;
    }

    public String getRBId() {
        return RBId;
    }

    public void setRBId(String RBId) {
        this.RBId = RBId;
    }

    public String getRLId() {
        return RLId;
    }

    public void setRLId(String RLId) {
        this.RLId = RLId;
    }

    public String getStnId() {
        return StnId;
    }

    public void setStnId(String stnId) {
        StnId = stnId;
    }

    public String getStnName() {
        return StnName;
    }

    public void setStnName(String stnName) {
        StnName = stnName;
    }

    public String getStnNo() {
        return StnNo;
    }

    public void setStnNo(String stnNo) {
        StnNo = stnNo;
    }

    public String getRLName() {
        return RLName;
    }

    public void setRLName(String RLName) {
        this.RLName = RLName;
    }

    @Override
    public String toString() {
        return "StationBean{" +
                "RBId='" + RBId + '\'' +
                ", RLId='" + RLId + '\'' +
                ", RLName='" + RLName + '\'' +
                ", StnId='" + StnId + '\'' +
                ", StnName='" + StnName + '\'' +
                ", StnNo='" + StnNo + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object arg0) {
        if (!(arg0 instanceof StationBean)) {
            return false;
        } else {
            StationBean item = (StationBean) arg0;
            return item.getStnId().equals(this.StnId);
        }
    }

}
