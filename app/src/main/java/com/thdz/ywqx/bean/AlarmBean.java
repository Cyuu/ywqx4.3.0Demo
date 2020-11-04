package com.thdz.ywqx.bean;

import java.io.Serializable;

/**
 * zzx返回的最新告警列表的item类
 */

public class AlarmBean implements Serializable{
    private String AlarmRcdId; //                                ---- 告警记录id
    private String AlarmCode; // 30                              ---- 告警类型编码
    private String CodeName; // 落石告警                          ---- 告警类型名称
    private String CodeLevel; // 2                               ---- 告警等级
    private String AlarmNo; // 95893                             ---- 告警好
    private String AlarmOpenTm; // 2016-08-19 08; //32; //28     ---- 告警打开事件
    private String AlarmCancelTm; // 2016-08-19 08; //44; //44   ---- 告警取消事件
    private String IsSentToGFC; // false     ---- 是否发送给GFC
    private String IsSentToGMC; // true      ---- 是否发送给GMC
    private String Handle; // false          ---- 是否已处理
    private String HandleUser; //            ---- 处理用户
    private String HandleTime; //            ---- 处理事件
    private String HandleType; // 0          ---- 处理类型
    private String HandleInfo; //            ---- 处理信息
    private String RBId; // 21               ---- 局id
    private String RBNo; // 12
    private String RBName; // 广铁集团       ---- 局
    private String RSId; // 1016            ---- 工务段
    private String RSName; // 怀化工务段
    private String RWIId; // 1018
    private String RWIName; // 孟溪工区      ---- 工区
    private String StnId; // 1050           ---- 站点id
    private String StnNo; // 121001         ---- 站点号
    private String StnName; // 渝怀线K438+100-424  ---- 站点名称
    private String RunningStatus; // false  ---- 地面监测单元运行状态
    private String PcdtId; // 1064          ----
    private String PcdtSid; // 91           ---- pcdt
    private String PcdtName; // PCDT-91     ---- pcdt名称
    private String UnitId; // 278           ---- 单元id
    private String UnitNo; // 15            ---- 单元号
    private String UnitName; // 单元1-5(K438+262)  ---- 单元名称
    private String RLId; // 1019            ---- 线路id
    private String RLName; // 渝怀线         ---- 线路名称
    private String RLRowType; // 2          ---- 线路类型
    private String AppId; // 1              ---- 所属应用程序id
    private String AppName; // 异物侵限      ---- 所属应用程序

    public AlarmBean (){}



    public String getAlarmRcdId() {
        return AlarmRcdId;
    }

    public void setAlarmRcdId(String alarmRcdId) {
        AlarmRcdId = alarmRcdId;
    }

    public String getAlarmCode() {
        return AlarmCode;
    }

    public void setAlarmCode(String alarmCode) {
        AlarmCode = alarmCode;
    }

    public String getCodeName() {
        return CodeName;
    }

    public void setCodeName(String codeName) {
        CodeName = codeName;
    }

    public String getCodeLevel() {
        return CodeLevel;
    }

    public void setCodeLevel(String codeLevel) {
        CodeLevel = codeLevel;
    }

    public String getAlarmNo() {
        return AlarmNo;
    }

    public void setAlarmNo(String alarmNo) {
        AlarmNo = alarmNo;
    }

    public String getAlarmOpenTm() {
        return AlarmOpenTm;
    }

    public void setAlarmOpenTm(String alarmOpenTm) {
        AlarmOpenTm = alarmOpenTm;
    }

    public String getAlarmCancelTm() {
        return AlarmCancelTm;
    }

    public void setAlarmCancelTm(String alarmCancelTm) {
        AlarmCancelTm = alarmCancelTm;
    }

    public String getIsSentToGFC() {
        return IsSentToGFC;
    }

    public void setIsSentToGFC(String isSentToGFC) {
        IsSentToGFC = isSentToGFC;
    }

    public String getIsSentToGMC() {
        return IsSentToGMC;
    }

    public void setIsSentToGMC(String isSentToGMC) {
        IsSentToGMC = isSentToGMC;
    }

    public String getHandle() {
        return Handle;
    }

    public void setHandle(String handle) {
        Handle = handle;
    }

    public String getHandleUser() {
        return HandleUser;
    }

    public void setHandleUser(String handleUser) {
        HandleUser = handleUser;
    }

    public String getHandleTime() {
        return HandleTime;
    }

    public void setHandleTime(String handleTime) {
        HandleTime = handleTime;
    }

    public String getHandleType() {
        return HandleType;
    }

    public void setHandleType(String handleType) {
        HandleType = handleType;
    }

    public String getHandleInfo() {
        return HandleInfo;
    }

    public void setHandleInfo(String handleInfo) {
        HandleInfo = handleInfo;
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

    public String getRunningStatus() {
        return RunningStatus;
    }

    public void setRunningStatus(String runningStatus) {
        RunningStatus = runningStatus;
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
