package com.thdz.ywqx.bean;

import java.io.Serializable;

/**
 * 单元
 */
public class UnitBean implements Serializable { // Parcelable

    private String RBId; // 父层--父层--父层 局id
    private String RLId; // 父--父层 线路id
    private String StnId; // 父层 站点id
    private String StnNo; // 父层 站点No
    private String PcdtId; // 父层 pcdtid

    private String UnitId;// 3
    private String UnitName;// 单元1-3(K256+835)(无巡视)
    private String UnitNo;// 13
    //	private String FacNo;// 出厂编号
    private String EnableState; // 是否可用

    private String RSId;
    private String RWIid;

    public UnitBean() {
        super();
    }

    public String getRSId() {
        return RSId;
    }

    public void setRSId(String RSId) {
        this.RSId = RSId;
    }

    public String getRWIid() {
        return RWIid;
    }

    public void setRWIid(String RWIid) {
        this.RWIid = RWIid;
    }

    public String getPcdtId() {
        return PcdtId;
    }

    public void setPcdtId(String pcdtId) {
        PcdtId = pcdtId;
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

    public String getUnitId() {
        return UnitId;
    }

    public void setUnitId(String unitId) {
        UnitId = unitId;
    }

    public String getUnitName() {
        return UnitName;
    }

    public void setUnitName(String unitName) {
        UnitName = unitName;
    }

    public String getUnitNo() {
        return UnitNo;
    }

    public void setUnitNo(String unitNo) {
        UnitNo = unitNo;
    }

    public String getEnableState() {
        return EnableState;
    }

    public void setEnableState(String enableState) {
        EnableState = enableState;
    }

    public String getStnNo() {
        return StnNo;
    }

    public void setStnNo(String stnNo) {
        StnNo = stnNo;
    }

    @Override
    public String toString() {
        return "UnitBean{" +
                "RBId='" + RBId + '\'' +
                ", RLId='" + RLId + '\'' +
                ", StnId='" + StnId + '\'' +
                ", StnNo='" + StnNo + '\'' +
                ", PcdtId='" + PcdtId + '\'' +
                ", UnitId='" + UnitId + '\'' +
                ", UnitName='" + UnitName + '\'' +
                ", UnitNo='" + UnitNo + '\'' +
                ", EnableState='" + EnableState + '\'' +
                ", RSId='" + RSId + '\'' +
                ", RWIid='" + RWIid + '\'' +
                '}';
    }

    //    @Override
//    public boolean equals(Object arg0) {
//        if (!(arg0 instanceof UnitBean)) {
//            return false;
//        } else {
//            UnitBean item = (UnitBean) arg0;
//            return item.getUnitId().equals(this.UnitId);
//        }
//    }

}
