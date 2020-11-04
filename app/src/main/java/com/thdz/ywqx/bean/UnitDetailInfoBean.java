package com.thdz.ywqx.bean;

import java.io.Serializable;

/**
 * 监控单元详情页基本信息
 */

public class UnitDetailInfoBean implements Serializable {

    private String UnitId; // 1
    private String UnitNo; // 11
    private String UnitName; // 单元11
    private String FacNo; // 11
    private String AlarmCnt; // 单元11发生落石告警
    private String AlarmCanCnt; // 单元11落石告警取消
    private String EnableState; // 1
    private String RBId; // 1
    private String RBNo; // 99
    private String RBName; // 天河局
    private String RSId; // 1
    private String RSName; // 研发段
    private String RWIId; // 1
    private String RWIName; // 异物侵限区
    private String StnId; // 1
    private String StnNo; // 999999
    private String StnName; // 测试点
    private String PcdtId; // 1
    private String PcdtSid; // 91
    private String PcdtName; // 91服务器
    private String RLId; // 1
    private String RLName; // 测试单线
    private String RLRowType; // 2
    private String AppId; // 1
    private String AppName; // 异物侵限

    public UnitDetailInfoBean (){}

    public String getUnitId() {
        return UnitId;
    }

    public void setUnitId(String unitId) {
        UnitId = unitId;
    }

    public String getUnitNo() {
        return UnitNo;
    }

    public void setUnitNo(String unitNo) {
        UnitNo = unitNo;
    }

    public String getUnitName() {
        return UnitName;
    }

    public void setUnitName(String unitName) {
        UnitName = unitName;
    }

    public String getFacNo() {
        return FacNo;
    }

    public void setFacNo(String facNo) {
        FacNo = facNo;
    }

    public String getAlarmCnt() {
        return AlarmCnt;
    }

    public void setAlarmCnt(String alarmCnt) {
        AlarmCnt = alarmCnt;
    }

    public String getAlarmCanCnt() {
        return AlarmCanCnt;
    }

    public void setAlarmCanCnt(String alarmCanCnt) {
        AlarmCanCnt = alarmCanCnt;
    }

    public String getEnableState() {
        return EnableState;
    }

    public void setEnableState(String enableState) {
        EnableState = enableState;
    }

    public String getRBId() {
        return RBId;
    }

    public void setRBId(String RBId) {
        this.RBId = RBId;
    }

    public String getRBNo() {
        return RBNo;
    }

    public void setRBNo(String RBNo) {
        this.RBNo = RBNo;
    }

    public String getRBName() {
        return RBName;
    }

    public void setRBName(String RBName) {
        this.RBName = RBName;
    }

    public String getRSId() {
        return RSId;
    }

    public void setRSId(String RSId) {
        this.RSId = RSId;
    }

    public String getRSName() {
        return RSName;
    }

    public void setRSName(String RSName) {
        this.RSName = RSName;
    }

    public String getRWIId() {
        return RWIId;
    }

    public void setRWIId(String RWIId) {
        this.RWIId = RWIId;
    }

    public String getRWIName() {
        return RWIName;
    }

    public void setRWIName(String RWIName) {
        this.RWIName = RWIName;
    }

    public String getStnId() {
        return StnId;
    }

    public void setStnId(String stnId) {
        StnId = stnId;
    }

    public String getStnNo() {
        return StnNo;
    }

    public void setStnNo(String stnNo) {
        StnNo = stnNo;
    }

    public String getStnName() {
        return StnName;
    }

    public void setStnName(String stnName) {
        StnName = stnName;
    }

    public String getPcdtId() {
        return PcdtId;
    }

    public void setPcdtId(String pcdtId) {
        PcdtId = pcdtId;
    }

    public String getPcdtSid() {
        return PcdtSid;
    }

    public void setPcdtSid(String pcdtSid) {
        PcdtSid = pcdtSid;
    }

    public String getPcdtName() {
        return PcdtName;
    }

    public void setPcdtName(String pcdtName) {
        PcdtName = pcdtName;
    }

    public String getRLId() {
        return RLId;
    }

    public void setRLId(String RLId) {
        this.RLId = RLId;
    }

    public String getRLName() {
        return RLName;
    }

    public void setRLName(String RLName) {
        this.RLName = RLName;
    }

    public String getRLRowType() {
        return RLRowType;
    }

    public void setRLRowType(String RLRowType) {
        this.RLRowType = RLRowType;
    }

    public String getAppId() {
        return AppId;
    }

    public void setAppId(String appId) {
        AppId = appId;
    }

    public String getAppName() {
        return AppName;
    }

    public void setAppName(String appName) {
        AppName = appName;
    }
}
