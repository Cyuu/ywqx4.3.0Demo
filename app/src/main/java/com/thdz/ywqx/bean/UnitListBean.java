package com.thdz.ywqx.bean;

import java.io.Serializable;

/**
 * 单元列表类
 */
public class UnitListBean implements Serializable { // Parcelable

	private String unit_id;// 单元id
	private String unit_name;// 单元名称
	private String alarm_state;// 告警状态
	private String run_state;// 运行状态

	public UnitListBean() {
		super();
	}

	public UnitListBean(String unit_id, String unit_name, String alarm_state, String run_state) {
		this.unit_id = unit_id;
		this.unit_name = unit_name;
		this.alarm_state = alarm_state;
		this.run_state = run_state;
	}

	public String getUnit_id() {
		return unit_id;
	}

	public String getUnit_name() {
		return unit_name;
	}

	public String getAlarm_state() {
		return alarm_state;
	}

	public String getRun_state() {
		return run_state;
	}

	public void setUnit_id(String unit_id) {
		this.unit_id = unit_id;
	}

	public void setUnit_name(String unit_name) {
		this.unit_name = unit_name;
	}

	public void setAlarm_state(String alarm_state) {
		this.alarm_state = alarm_state;
	}

	public void setRun_state(String run_state) {
		this.run_state = run_state;
	}


}
