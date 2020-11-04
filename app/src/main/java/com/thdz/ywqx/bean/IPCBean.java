package com.thdz.ywqx.bean;

import java.io.Serializable;
/**
 * 球机列表类
 */
public class IPCBean implements Serializable {

	private String IpcRSId; //  1
	private String IpcSid; //  51
	private String NvrChannel; //  0
	private String Nvr2IpcConnectState; //  3536
	private String IpcSignalState; //  3380
	private String NvrId; //  1
	private String NvrSid; //  910
	private String RBId; //  1
	private String RBNo; // 99
	private String RBName; // 天河局
	private String RSId; //  1
	private String RSName; // 研发段
	private String RWIId; //  1
	private String RWIName; // 异物侵限区
	private String StnId; //  1
	private String StnNo; // 999999
	private String StnName; // 测试点
	private String PcdtId; //  1
	private String PcdtSid; //  91
	private String PcdtName; // 91服务器
	private String RLId; //  1
	private String RLName; // 测试单线
	private String RLRowType; //  2
	private String AppId; //  1
	private String AppName; // 异物侵限
	private String RunningStatus; //  true

	public IPCBean () {}

	public String getIpcRSId() {
		return IpcRSId;
	}

	public void setIpcRSId(String ipcRSId) {
		IpcRSId = ipcRSId;
	}

	public String getIpcSid() {
		return IpcSid;
	}

	public void setIpcSid(String ipcSid) {
		IpcSid = ipcSid;
	}

	public String getNvrChannel() {
		return NvrChannel;
	}

	public void setNvrChannel(String nvrChannel) {
		NvrChannel = nvrChannel;
	}

	public String getNvr2IpcConnectState() {
		return Nvr2IpcConnectState;
	}

	public void setNvr2IpcConnectState(String nvr2IpcConnectState) {
		Nvr2IpcConnectState = nvr2IpcConnectState;
	}

	public String getIpcSignalState() {
		return IpcSignalState;
	}

	public void setIpcSignalState(String ipcSignalState) {
		IpcSignalState = ipcSignalState;
	}

	public String getNvrId() {
		return NvrId;
	}

	public void setNvrId(String nvrId) {
		NvrId = nvrId;
	}

	public String getNvrSid() {
		return NvrSid;
	}

	public void setNvrSid(String nvrSid) {
		NvrSid = nvrSid;
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

	public String getRunningStatus() {
		return RunningStatus;
	}

	public void setRunningStatus(String runningStatus) {
		RunningStatus = runningStatus;
	}

	@Override
	public String toString() {
		return "IPCBean{" +
				"IpcRSId='" + IpcRSId + '\'' +
				", IpcSid='" + IpcSid + '\'' +
				", NvrChannel='" + NvrChannel + '\'' +
				", Nvr2IpcConnectState='" + Nvr2IpcConnectState + '\'' +
				", IpcSignalState='" + IpcSignalState + '\'' +
				", NvrId='" + NvrId + '\'' +
				", NvrSid='" + NvrSid + '\'' +
				", RBId='" + RBId + '\'' +
				", RBNo='" + RBNo + '\'' +
				", RBName='" + RBName + '\'' +
				", RSId='" + RSId + '\'' +
				", RSName='" + RSName + '\'' +
				", RWIId='" + RWIId + '\'' +
				", RWIName='" + RWIName + '\'' +
				", StnId='" + StnId + '\'' +
				", StnNo='" + StnNo + '\'' +
				", StnName='" + StnName + '\'' +
				", PcdtId='" + PcdtId + '\'' +
				", PcdtSid='" + PcdtSid + '\'' +
				", PcdtName='" + PcdtName + '\'' +
				", RLId='" + RLId + '\'' +
				", RLName='" + RLName + '\'' +
				", RLRowType='" + RLRowType + '\'' +
				", AppId='" + AppId + '\'' +
				", AppName='" + AppName + '\'' +
				", RunningStatus='" + RunningStatus + '\'' +
				'}';
	}
}
