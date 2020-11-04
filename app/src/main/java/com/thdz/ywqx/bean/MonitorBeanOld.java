package com.thdz.ywqx.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;

import java.io.Serializable;

/**
 * 站点信息类 (全部)
 */
public class MonitorBeanOld implements Serializable {

    private static final long serialVersionUID = 536871022l;

    private Long id;
    private String UnitId;
    private String UnitNo;
    private String UnitName;
    private String FacNo;
    private String AlarmCnt;
    private String AlarmCanCnt;
    private String EnableState;
    private String RBId;
    private String RBNo;
    private String RBName;
    private String RSId;
    private String RSName;
    private String RWIId;
    private String RWIName;
    private String StnId;
    private String StnNo;
    private String StnName;
    private String PcdtId;
    private String PcdtSid;
    private String PcdtName;
    private String RLId;
    private String RLName;
    private String RLRowType;
    private String AppId;
    private String AppName;

    public String getAppName() {
        return this.AppName;
    }

    public void setAppName(String AppName) {
        this.AppName = AppName;
    }

    public String getAppId() {
        return this.AppId;
    }

    public void setAppId(String AppId) {
        this.AppId = AppId;
    }

    public String getRLRowType() {
        return this.RLRowType;
    }

    public void setRLRowType(String RLRowType) {
        this.RLRowType = RLRowType;
    }

    public String getRLName() {
        return this.RLName;
    }

    public void setRLName(String RLName) {
        this.RLName = RLName;
    }

    public String getRLId() {
        return this.RLId;
    }

    public void setRLId(String RLId) {
        this.RLId = RLId;
    }

    public String getPcdtName() {
        return this.PcdtName;
    }

    public void setPcdtName(String PcdtName) {
        this.PcdtName = PcdtName;
    }

    public String getPcdtSid() {
        return this.PcdtSid;
    }

    public void setPcdtSid(String PcdtSid) {
        this.PcdtSid = PcdtSid;
    }

    public String getPcdtId() {
        return this.PcdtId;
    }

    public void setPcdtId(String PcdtId) {
        this.PcdtId = PcdtId;
    }

    public String getStnName() {
        return this.StnName;
    }

    public void setStnName(String StnName) {
        this.StnName = StnName;
    }

    public String getStnNo() {
        return this.StnNo;
    }

    public void setStnNo(String StnNo) {
        this.StnNo = StnNo;
    }

    public String getStnId() {
        return this.StnId;
    }

    public void setStnId(String StnId) {
        this.StnId = StnId;
    }

    public String getRWIName() {
        return this.RWIName;
    }

    public void setRWIName(String RWIName) {
        this.RWIName = RWIName;
    }

    public String getRWIId() {
        return this.RWIId;
    }

    public void setRWIId(String RWIId) {
        this.RWIId = RWIId;
    }

    public String getRSName() {
        return this.RSName;
    }

    public void setRSName(String RSName) {
        this.RSName = RSName;
    }

    public String getRSId() {
        return this.RSId;
    }

    public void setRSId(String RSId) {
        this.RSId = RSId;
    }

    public String getRBName() {
        return this.RBName;
    }

    public void setRBName(String RBName) {
        this.RBName = RBName;
    }

    public String getRBNo() {
        return this.RBNo;
    }

    public void setRBNo(String RBNo) {
        this.RBNo = RBNo;
    }

    public String getRBId() {
        return this.RBId;
    }

    public void setRBId(String RBId) {
        this.RBId = RBId;
    }

    public String getEnableState() {
        return this.EnableState;
    }

    public void setEnableState(String EnableState) {
        this.EnableState = EnableState;
    }

    public String getAlarmCanCnt() {
        return this.AlarmCanCnt;
    }

    public void setAlarmCanCnt(String AlarmCanCnt) {
        this.AlarmCanCnt = AlarmCanCnt;
    }

    public String getAlarmCnt() {
        return this.AlarmCnt;
    }

    public void setAlarmCnt(String AlarmCnt) {
        this.AlarmCnt = AlarmCnt;
    }

    public String getFacNo() {
        return this.FacNo;
    }

    public void setFacNo(String FacNo) {
        this.FacNo = FacNo;
    }

    public String getUnitName() {
        return this.UnitName;
    }

    public void setUnitName(String UnitName) {
        this.UnitName = UnitName;
    }

    public String getUnitNo() {
        return this.UnitNo;
    }

    public void setUnitNo(String UnitNo) {
        this.UnitNo = UnitNo;
    }

    public String getUnitId() {
        return this.UnitId;
    }

    public void setUnitId(String UnitId) {
        this.UnitId = UnitId;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MonitorBeanOld(Long id, String UnitId, String UnitNo, String UnitName,
                          String FacNo, String AlarmCnt, String AlarmCanCnt, String EnableState,
                          String RBId, String RBNo, String RBName, String RSId, String RSName,
                          String RWIId, String RWIName, String StnId, String StnNo,
                          String StnName, String PcdtId, String PcdtSid, String PcdtName,
                          String RLId, String RLName, String RLRowType, String AppId,
                          String AppName) {
        this.id = id;
        this.UnitId = UnitId;
        this.UnitNo = UnitNo;
        this.UnitName = UnitName;
        this.FacNo = FacNo;
        this.AlarmCnt = AlarmCnt;
        this.AlarmCanCnt = AlarmCanCnt;
        this.EnableState = EnableState;
        this.RBId = RBId;
        this.RBNo = RBNo;
        this.RBName = RBName;
        this.RSId = RSId;
        this.RSName = RSName;
        this.RWIId = RWIId;
        this.RWIName = RWIName;
        this.StnId = StnId;
        this.StnNo = StnNo;
        this.StnName = StnName;
        this.PcdtId = PcdtId;
        this.PcdtSid = PcdtSid;
        this.PcdtName = PcdtName;
        this.RLId = RLId;
        this.RLName = RLName;
        this.RLRowType = RLRowType;
        this.AppId = AppId;
        this.AppName = AppName;
    }

    public MonitorBeanOld() {
    }

}
