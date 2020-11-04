package com.thdz.ywqx.bean;

import java.io.Serializable;

/**
 * 监控点详情-- 基本信息
 */

public class StnDetailInfoBean implements Serializable {

    private String StnId; //  1,
    private String StnNo; //
    private String StnName; //  站点
    private String WatchKeeper; //  责任人
    private String WatchKeeperTel; //  责任人电话
    private String StnSim; //  现场sim卡
    private String StnHasLamp; //  true,
    private String StnHasRadio; //  true,
    private String SendToGFS; //  true,
    private String SendSmsFlag; //  true,
    private String RunningStatus; //  开通组昂太
    private String EnableState; //  1,
    private int SysVersion;
    private String RBId; //  1,
    private String RBNo; //  99
    private String RBName; //  局
    private String RSId; //  1,
    private String RSName; //  工务段
    private String RWIId; //  1,
    private String RWIName; //  工区
    private String RLId; //  1,
    private String RLName; //  线路
    private String RLRowType; //  2
    private String AppId; //  1,
    private String AppName; //  异物侵限
    private String StnPath; // 路线

    public StnDetailInfoBean(){}

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

    public String getWatchKeeper() {
        return WatchKeeper;
    }

    public void setWatchKeeper(String watchKeeper) {
        WatchKeeper = watchKeeper;
    }

    public String getWatchKeeperTel() {
        return WatchKeeperTel;
    }

    public void setWatchKeeperTel(String watchKeeperTel) {
        WatchKeeperTel = watchKeeperTel;
    }

    public String getStnSim() {
        return StnSim;
    }

    public void setStnSim(String stnSim) {
        StnSim = stnSim;
    }

    public String getStnHasLamp() {
        return StnHasLamp;
    }

    public void setStnHasLamp(String stnHasLamp) {
        StnHasLamp = stnHasLamp;
    }

    public String getStnHasRadio() {
        return StnHasRadio;
    }

    public void setStnHasRadio(String stnHasRadio) {
        StnHasRadio = stnHasRadio;
    }

    public String getSendToGFS() {
        return SendToGFS;
    }

    public void setSendToGFS(String sendToGFS) {
        SendToGFS = sendToGFS;
    }

    public String getSendSmsFlag() {
        return SendSmsFlag;
    }

    public void setSendSmsFlag(String sendSmsFlag) {
        SendSmsFlag = sendSmsFlag;
    }

    public String getRunningStatus() {
        return RunningStatus;
    }

    public void setRunningStatus(String runningStatus) {
        RunningStatus = runningStatus;
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

    public int getSysVersion() {
        return SysVersion;
    }

    public void setSysVersion(int sysVersion) {
        SysVersion = sysVersion;
    }

    public String getStnPath() {
        return StnPath;
    }

    public void setStnPath(String stnPath) {
        StnPath = stnPath;
    }

    @Override
    public String toString() {
        return "StnDetailInfoBean{" +
                "StnId='" + StnId + '\'' +
                ", StnNo='" + StnNo + '\'' +
                ", StnName='" + StnName + '\'' +
                ", WatchKeeper='" + WatchKeeper + '\'' +
                ", WatchKeeperTel='" + WatchKeeperTel + '\'' +
                ", StnSim='" + StnSim + '\'' +
                ", StnHasLamp='" + StnHasLamp + '\'' +
                ", StnHasRadio='" + StnHasRadio + '\'' +
                ", SendToGFS='" + SendToGFS + '\'' +
                ", SendSmsFlag='" + SendSmsFlag + '\'' +
                ", RunningStatus='" + RunningStatus + '\'' +
                ", EnableState='" + EnableState + '\'' +
                ", RBId='" + RBId + '\'' +
                ", RBNo='" + RBNo + '\'' +
                ", RBName='" + RBName + '\'' +
                ", RSId='" + RSId + '\'' +
                ", RSName='" + RSName + '\'' +
                ", RWIId='" + RWIId + '\'' +
                ", RWIName='" + RWIName + '\'' +
                ", RLId='" + RLId + '\'' +
                ", RLName='" + RLName + '\'' +
                ", RLRowType='" + RLRowType + '\'' +
                ", AppId='" + AppId + '\'' +
                ", AppName='" + AppName + '\'' +
                '}';
    }
}
