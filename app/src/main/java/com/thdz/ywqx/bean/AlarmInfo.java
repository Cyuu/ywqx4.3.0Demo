package com.thdz.ywqx.bean;

import java.io.Serializable;

public class AlarmInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6269071689127747356L;
	/** 监控点 **/
	private String tid;
	/** 监控点名称 **/
	private String tidName;
	/** 单元号 **/
	private String sid;
	/** 告警级别 **/
	private String level;
	/** 告警时间 **/
	private String alarmTime;
	/** 取消时间 **/
	private String cancelTime;
	/** 告警灯状态 **/
	private String lightState;
	/** 告警图片是否来了 **/
	private String picCome;
	/** 告警图片已收到 **/
	private String picReceiveDone;
	/** 告警图片存放位置 **/
	private String picPath;
	/** 雷达图片是否来了 **/
	private String radarPicCome;
	/** 雷达图片已收到 **/
	private String radarPicReceiveDone;
	/** 告警图片存放位置 **/
	private String radarPicPath;

	public AlarmInfo() {

	}

	public AlarmInfo(String tid, String tidName, String sid, String level,
			String alarmTime, String cancelTime, String lightState,
			String picCome, String picReceiveDone, String picPath,
			String radarPicCome, String radarPicReceiveDone, String radarPicPath) {
		super();
		this.tid = tid;
		this.tidName = tidName;
		this.sid = sid;
		this.level = level;
		this.alarmTime = alarmTime;
		this.cancelTime = cancelTime;
		this.lightState = lightState;
		this.picCome = picCome;
		this.picReceiveDone = picReceiveDone;
		this.picPath = picPath;
		this.radarPicCome = radarPicCome;
		this.radarPicReceiveDone = radarPicReceiveDone;
		this.radarPicPath = radarPicPath;
	}

	public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}

	public String getTidName() {
		return tidName;
	}

	public void setTidName(String tidName) {
		this.tidName = tidName;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getAlarmTime() {
		return alarmTime;
	}

	public void setAlarmTime(String alarmTime) {
		this.alarmTime = alarmTime;
	}

	public String getCancelTime() {
		return cancelTime;
	}

	public void setCancelTime(String cancelTime) {
		this.cancelTime = cancelTime;
	}

	public String getLightState() {
		return lightState;
	}

	public void setLightState(String lightState) {
		this.lightState = lightState;
	}

	public String getPicCome() {
		return picCome;
	}

	public void setPicCome(String picCome) {
		this.picCome = picCome;
	}

	public String getPicReceiveDone() {
		return picReceiveDone;
	}

	public void setPicReceiveDone(String picReceiveDone) {
		this.picReceiveDone = picReceiveDone;
	}

	public String getPicPath() {
		return picPath;
	}

	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}

	public String getRadarPicCome() {
		return radarPicCome;
	}

	public void setRadarPicCome(String radarPicCome) {
		this.radarPicCome = radarPicCome;
	}

	public String getRadarPicReceiveDone() {
		return radarPicReceiveDone;
	}

	public void setRadarPicReceiveDone(String radarPicReceiveDone) {
		this.radarPicReceiveDone = radarPicReceiveDone;
	}

	public String getRadarPicPath() {
		return radarPicPath;
	}

	public void setRadarPicPath(String radarPicPath) {
		this.radarPicPath = radarPicPath;
	}

	@Override
	public String toString() {
		return "AlarmInfo [tid=" + tid + ", tidName=" + tidName + ", sid="
				+ sid + ", level=" + level + ", alarmTime=" + alarmTime
				+ ", cancelTime=" + cancelTime + ", lightState=" + lightState
				+ ", picCome=" + picCome + ", picReceiveDone=" + picReceiveDone
				+ ", picPath=" + picPath + ", radarPicCome=" + radarPicCome
				+ ", radarPicReceiveDone=" + radarPicReceiveDone
				+ ", radarPicPath=" + radarPicPath + "]";
	}

}
