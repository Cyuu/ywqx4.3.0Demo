package com.thdz.ywqx.bean;

import java.io.Serializable;

/**
 * Created by ZZX on 2016-09-03.
 */

public class AlarmDetailStatusBean implements Serializable {

    private String StnRSId; // 表ID：自增
    private String StnVersion; // 监控点：版本 433
    private String StnGmdtStartTime; //  Gmdt启动时间
    private String StnHeartBeat; //  心跳
    private String StnConnState; //  连接状态	0断线，1连接正常
    private String GmdtRunState;  // Gmdt运行状态	3995：实时 3996：强制报警 3997：强制消警
    private String GmdtRunMode;  // Gmdt运行模式	3991：正常 3992：系统测试 3993：单元测试 3994：手动模式
    private String GmdtMonitorState; // Gmdt监测状态	3998：正常 3999：错误  -- 设备监测状态
    private String UpAlarmState;  // 上行报警状态	10：正常 30：报警
    private String DownAlarmState;  // 下行报警状态	10：正常 30：报警
    private String UpLightState; //  上行灯状态	3304：正常 3305：异常   --- 对应pc里的设备状态
    private String UpLightRealState; //  上行灯实际状态	3940：开 3941：关  --- 对应pc里的运行状态
    private String DownLightState;  // 下行灯状态	3306：正常 3307：异常
    private String DownLightRealState;  // 下行灯实际状态	3942：开 3943：关
    private String AlcState; //  列调状态	3308：正常 3309：异常
    private String UpAlcRealState; //  上行列调实际工作状态	3944：开 3945：关
    private String DownAlcRealState; //  下行列调实际工作状态	3946：开 3947：关
    private String Gmdt2AdrConnectState;  // Gmdt和adr连接状态	3554：正常 3555：异常
    private String Latitude;  // -1
    private String Longitude; //  -1
    private String GmdtWorkState; // 软件监测状态 4000 正常 4001 异常
    private String UpLightExistState; //  -1
    private String DownLightExistState; //  -1
    private String GPSState; //  -1
    private String AlarmDevState; //  -1
    private String TrainPassing; //  -1

    // 2016.9.03，zzx说 下面的字段app用不到
    private String SmsTpId; //  0
    private String SmsTpName;  // null
    private String SmsNo;  // null
    private String SmsTpId2; //  0
    private String SmsTpName2; //  null
    private String SmsNo2;  // null
    private String SmsTpId3; //
    private String SmsTpName3;  // null
    private String SmsNo3;  // null
    private String SmsTpId4; //  0
    private String SmsTpName4;  // null
    private String SmsNo4;  // null

    private String RBId; //  1
    private String RBNo; //  99
    private String RBName; //  天河局
    private String RSId; //  工务段：ID
    private String RSName; //  研发段
    private String RWIId;  // 1
    private String RWIName;  // 工区：名称
    private String StnId;  // 1
    private String StnNo;  // 999999
    private String StnName; //  测试点
    private String RLId; //  1
    private String RLName; //  测试单线
    private String RLRowType; //  铁路线：类型 0上行，1下行，2单线
    private String AppId; //  1
    private String AppName;  // 异物侵限
    private String RunningStatus;  // true - 开通状态

    public AlarmDetailStatusBean() {
    }

    public String getStnRSId() {
        return StnRSId;
    }

    public void setStnRSId(String stnRSId) {
        StnRSId = stnRSId;
    }

    public String getStnVersion() {
        return StnVersion;
    }

    public void setStnVersion(String stnVersion) {
        StnVersion = stnVersion;
    }

    public String getStnGmdtStartTime() {
        return StnGmdtStartTime;
    }

    public void setStnGmdtStartTime(String stnGmdtStartTime) {
        StnGmdtStartTime = stnGmdtStartTime;
    }

    public String getStnHeartBeat() {
        return StnHeartBeat;
    }

    public void setStnHeartBeat(String stnHeartBeat) {
        StnHeartBeat = stnHeartBeat;
    }

    public String getStnConnState() {
        return StnConnState;
    }

    public void setStnConnState(String stnConnState) {
        StnConnState = stnConnState;
    }

    public String getGmdtRunState() {
        return GmdtRunState;
    }

    public void setGmdtRunState(String gmdtRunState) {
        GmdtRunState = gmdtRunState;
    }

    public String getGmdtRunMode() {
        return GmdtRunMode;
    }

    public void setGmdtRunMode(String gmdtRunMode) {
        GmdtRunMode = gmdtRunMode;
    }

    public String getGmdtMonitorState() {
        return GmdtMonitorState;
    }

    public void setGmdtMonitorState(String gmdtMonitorState) {
        GmdtMonitorState = gmdtMonitorState;
    }

    public String getUpAlarmState() {
        return UpAlarmState;
    }

    public void setUpAlarmState(String upAlarmState) {
        UpAlarmState = upAlarmState;
    }

    public String getDownAlarmState() {
        return DownAlarmState;
    }

    public void setDownAlarmState(String downAlarmState) {
        DownAlarmState = downAlarmState;
    }

    public String getUpLightState() {
        return UpLightState;
    }

    public void setUpLightState(String upLightState) {
        UpLightState = upLightState;
    }

    public String getUpLightRealState() {
        return UpLightRealState;
    }

    public void setUpLightRealState(String upLightRealState) {
        UpLightRealState = upLightRealState;
    }

    public String getDownLightState() {
        return DownLightState;
    }

    public void setDownLightState(String downLightState) {
        DownLightState = downLightState;
    }

    public String getDownLightRealState() {
        return DownLightRealState;
    }

    public void setDownLightRealState(String downLightRealState) {
        DownLightRealState = downLightRealState;
    }

    public String getAlcState() {
        return AlcState;
    }

    public void setAlcState(String alcState) {
        AlcState = alcState;
    }

    public String getUpAlcRealState() {
        return UpAlcRealState;
    }

    public void setUpAlcRealState(String upAlcRealState) {
        UpAlcRealState = upAlcRealState;
    }

    public String getDownAlcRealState() {
        return DownAlcRealState;
    }

    public void setDownAlcRealState(String downAlcRealState) {
        DownAlcRealState = downAlcRealState;
    }

    public String getGmdt2AdrConnectState() {
        return Gmdt2AdrConnectState;
    }

    public void setGmdt2AdrConnectState(String gmdt2AdrConnectState) {
        Gmdt2AdrConnectState = gmdt2AdrConnectState;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public String getGmdtWorkState() {
        return GmdtWorkState;
    }

    public void setGmdtWorkState(String gmdtWorkState) {
        GmdtWorkState = gmdtWorkState;
    }

    public String getUpLightExistState() {
        return UpLightExistState;
    }

    public void setUpLightExistState(String upLightExistState) {
        UpLightExistState = upLightExistState;
    }

    public String getDownLightExistState() {
        return DownLightExistState;
    }

    public void setDownLightExistState(String downLightExistState) {
        DownLightExistState = downLightExistState;
    }

    public String getGPSState() {
        return GPSState;
    }

    public void setGPSState(String GPSState) {
        this.GPSState = GPSState;
    }

    public String getAlarmDevState() {
        return AlarmDevState;
    }

    public void setAlarmDevState(String alarmDevState) {
        AlarmDevState = alarmDevState;
    }

    public String getTrainPassing() {
        return TrainPassing;
    }

    public void setTrainPassing(String trainPassing) {
        TrainPassing = trainPassing;
    }

    public String getSmsTpId() {
        return SmsTpId;
    }

    public void setSmsTpId(String smsTpId) {
        SmsTpId = smsTpId;
    }

    public String getSmsTpName() {
        return SmsTpName;
    }

    public void setSmsTpName(String smsTpName) {
        SmsTpName = smsTpName;
    }

    public String getSmsNo() {
        return SmsNo;
    }

    public void setSmsNo(String smsNo) {
        SmsNo = smsNo;
    }

    public String getSmsTpId2() {
        return SmsTpId2;
    }

    public void setSmsTpId2(String smsTpId2) {
        SmsTpId2 = smsTpId2;
    }

    public String getSmsTpName2() {
        return SmsTpName2;
    }

    public void setSmsTpName2(String smsTpName2) {
        SmsTpName2 = smsTpName2;
    }

    public String getSmsNo2() {
        return SmsNo2;
    }

    public void setSmsNo2(String smsNo2) {
        SmsNo2 = smsNo2;
    }

    public String getSmsTpId3() {
        return SmsTpId3;
    }

    public void setSmsTpId3(String smsTpId3) {
        SmsTpId3 = smsTpId3;
    }

    public String getSmsTpName3() {
        return SmsTpName3;
    }

    public void setSmsTpName3(String smsTpName3) {
        SmsTpName3 = smsTpName3;
    }

    public String getSmsNo3() {
        return SmsNo3;
    }

    public void setSmsNo3(String smsNo3) {
        SmsNo3 = smsNo3;
    }

    public String getSmsTpId4() {
        return SmsTpId4;
    }

    public void setSmsTpId4(String smsTpId4) {
        SmsTpId4 = smsTpId4;
    }

    public String getSmsTpName4() {
        return SmsTpName4;
    }

    public void setSmsTpName4(String smsTpName4) {
        SmsTpName4 = smsTpName4;
    }

    public String getSmsNo4() {
        return SmsNo4;
    }

    public void setSmsNo4(String smsNo4) {
        SmsNo4 = smsNo4;
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

    public String getRunningStatus() {
        return RunningStatus;
    }

    public void setRunningStatus(String runningStatus) {
        RunningStatus = runningStatus;
    }
}
