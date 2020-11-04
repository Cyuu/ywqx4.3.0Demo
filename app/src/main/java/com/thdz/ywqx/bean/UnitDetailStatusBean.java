package com.thdz.ywqx.bean;

import java.io.Serializable;

/**
 * 监控单元详情页状态信息
 */
public class UnitDetailStatusBean implements Serializable {

    private String UnitRSId; //  1,
    private String Pcdt2GstrConncetState; //  3514 正常, 3515 异常
    private String Gstr2IacConnectState; //  3518 正常, 3519 异常
    private String IacState; //  3302, 震动检测系统(IAC)运行正常   3303 异常
    private String Gstr2RadarConncetState; //  3510 正常,  3511  异常
    private String RadarState; //  3300 正常, 3301 异常
    private String Gstr2IoConnectState; //  3520, 连接正常  3521  连接异常
    private String IoState; //  3326 运行正常,  3327 运行异常
    private String Gstr2NspchConnectState; //  3524 正常,  3525  异常
    private String NspchState; //  3324,  正常,  3325 运行异常
    private String Nspch485State; //  3338,  3339
    private String AlertState; //  3322, 3323
    private String OccludeState; //  3330  雷达遮挡取消 -- 正常,   3331  雷达被遮挡
    private String ALFState; //  3316,  3317
    private String VSBState; //  3318,  3319
    private String ACBState; //  3320,  3321
    private String GstrRealState; //  3950, 3951  gstr工作状态
    private String GstrRunState; //  3964,   3965     运行状态
    private String GstrSysState; //  3966,   3967     系统状态，不需要展示
    private String GstrMonitorState; //  3970,   3971  检测状态
    private String AlertRealState; //  3948 开，    3949 关
    private String GstrStartTime; //  2016/9/5 10:32:01 ,
    private String GstrVersion; //  413,
    private String IoStartTime; //  2016/9/5 10:31:53 ,
    private String IOVersion; //  408,
    private String IacStartTime; //  1970/1/1 8:00:13 ,
    private String IacVersion; //  406,
    private String RadarStartTime; //   ,
    private String RadarVersion; //  423,
    private String UnitWorkState; //  4004  地面监测单元主控(GSTR)运行状态为正常,  4405 异常
    private String RadarCheckState; //  6522,
    private String RBId; //  1,
    private String RBNo; // 99
    private String RBName; //   天河局
    private String RSId; //  1,
    private String RSName; //   研发段
    private String RWIId; //  1,
    private String RWIName; //   异物侵限区　
    private String StnId; //  1,
    private String StnNo; //  　 999999　
    private String StnName; //   测试点
    private String PcdtId; //  1,
    private String PcdtSid; //  91,
    private String PcdtName; // 91服务器 ,
    private String UnitId; //  1,
    private String UnitNo; //  11
    private String UnitName; //   单元11
    private String RLId; //  1,
    private String RLName; //    测试单线 ,
    private String RLRowType; //  2,
    private String AppId; //  1,
    private String AppName; //   异物侵限
    private String RunningStatus; //  true

    public UnitDetailStatusBean(){}

    public String getUnitRSId() {
        return UnitRSId;
    }

    public void setUnitRSId(String unitRSId) {
        UnitRSId = unitRSId;
    }

    public String getPcdt2GstrConncetState() {
        return Pcdt2GstrConncetState;
    }

    public void setPcdt2GstrConncetState(String pcdt2GstrConncetState) {
        Pcdt2GstrConncetState = pcdt2GstrConncetState;
    }

    public String getGstr2IacConnectState() {
        return Gstr2IacConnectState;
    }

    public void setGstr2IacConnectState(String gstr2IacConnectState) {
        Gstr2IacConnectState = gstr2IacConnectState;
    }

    public String getIacState() {
        return IacState;
    }

    public void setIacState(String iacState) {
        IacState = iacState;
    }

    public String getGstr2RadarConncetState() {
        return Gstr2RadarConncetState;
    }

    public void setGstr2RadarConncetState(String gstr2RadarConncetState) {
        Gstr2RadarConncetState = gstr2RadarConncetState;
    }

    public String getRadarState() {
        return RadarState;
    }

    public void setRadarState(String radarState) {
        RadarState = radarState;
    }

    public String getGstr2IoConnectState() {
        return Gstr2IoConnectState;
    }

    public void setGstr2IoConnectState(String gstr2IoConnectState) {
        Gstr2IoConnectState = gstr2IoConnectState;
    }

    public String getIoState() {
        return IoState;
    }

    public void setIoState(String ioState) {
        IoState = ioState;
    }

    public String getGstr2NspchConnectState() {
        return Gstr2NspchConnectState;
    }

    public void setGstr2NspchConnectState(String gstr2NspchConnectState) {
        Gstr2NspchConnectState = gstr2NspchConnectState;
    }

    public String getNspchState() {
        return NspchState;
    }

    public void setNspchState(String nspchState) {
        NspchState = nspchState;
    }

    public String getNspch485State() {
        return Nspch485State;
    }

    public void setNspch485State(String nspch485State) {
        Nspch485State = nspch485State;
    }

    public String getAlertState() {
        return AlertState;
    }

    public void setAlertState(String alertState) {
        AlertState = alertState;
    }

    public String getOccludeState() {
        return OccludeState;
    }

    public void setOccludeState(String occludeState) {
        OccludeState = occludeState;
    }

    public String getALFState() {
        return ALFState;
    }

    public void setALFState(String ALFState) {
        this.ALFState = ALFState;
    }

    public String getVSBState() {
        return VSBState;
    }

    public void setVSBState(String VSBState) {
        this.VSBState = VSBState;
    }

    public String getACBState() {
        return ACBState;
    }

    public void setACBState(String ACBState) {
        this.ACBState = ACBState;
    }

    public String getGstrRealState() {
        return GstrRealState;
    }

    public void setGstrRealState(String gstrRealState) {
        GstrRealState = gstrRealState;
    }

    public String getGstrRunState() {
        return GstrRunState;
    }

    public void setGstrRunState(String gstrRunState) {
        GstrRunState = gstrRunState;
    }

    public String getGstrSysState() {
        return GstrSysState;
    }

    public void setGstrSysState(String gstrSysState) {
        GstrSysState = gstrSysState;
    }

    public String getGstrMonitorState() {
        return GstrMonitorState;
    }

    public void setGstrMonitorState(String gstrMonitorState) {
        GstrMonitorState = gstrMonitorState;
    }

    public String getAlertRealState() {
        return AlertRealState;
    }

    public void setAlertRealState(String alertRealState) {
        AlertRealState = alertRealState;
    }

    public String getGstrStartTime() {
        return GstrStartTime;
    }

    public void setGstrStartTime(String gstrStartTime) {
        GstrStartTime = gstrStartTime;
    }

    public String getGstrVersion() {
        return GstrVersion;
    }

    public void setGstrVersion(String gstrVersion) {
        GstrVersion = gstrVersion;
    }

    public String getIoStartTime() {
        return IoStartTime;
    }

    public void setIoStartTime(String ioStartTime) {
        IoStartTime = ioStartTime;
    }

    public String getIOVersion() {
        return IOVersion;
    }

    public void setIOVersion(String IOVersion) {
        this.IOVersion = IOVersion;
    }

    public String getIacStartTime() {
        return IacStartTime;
    }

    public void setIacStartTime(String iacStartTime) {
        IacStartTime = iacStartTime;
    }

    public String getIacVersion() {
        return IacVersion;
    }

    public void setIacVersion(String iacVersion) {
        IacVersion = iacVersion;
    }

    public String getRadarStartTime() {
        return RadarStartTime;
    }

    public void setRadarStartTime(String radarStartTime) {
        RadarStartTime = radarStartTime;
    }

    public String getRadarVersion() {
        return RadarVersion;
    }

    public void setRadarVersion(String radarVersion) {
        RadarVersion = radarVersion;
    }

    public String getUnitWorkState() {
        return UnitWorkState;
    }

    public void setUnitWorkState(String unitWorkState) {
        UnitWorkState = unitWorkState;
    }

    public String getRadarCheckState() {
        return RadarCheckState;
    }

    public void setRadarCheckState(String radarCheckState) {
        RadarCheckState = radarCheckState;
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

    public String getRunningStatus() {
        return RunningStatus;
    }

    public void setRunningStatus(String runningStatus) {
        RunningStatus = runningStatus;
    }
}
