package com.thdz.ywqx.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;

import java.io.Serializable;

/**
 * Code列表，根据CodeId，展示CodeName :CodeValue
 */
@Entity
public class StateCodeBean implements Serializable {

    private static final long serialVersionUID = 536435672l;

    @Id(autoincrement = true)
    private Long id; // 必须用Long，而不能用long
    @Unique
    private String CodeId; //  20
    private String CodeString; // 预警
    private String CodeName; // 预警
    private String CodeValue; // 预警
    private String CodeTable; // T_StationRealStatus
    private String CodeField; // UpAlarmState/DownAlarmState
    /**
     * TODO,<5,表示异常，显示红色字体，>=5 表示正常，显示黑色字体。<br/>
     * 例外：UpAlarmState,  DownAlarmState  ==10 ，表示正常，其他都是异常<br/>
     */
    private String GroupId; //  12, TODO,<5,表示异常，显示红色字体，>=5 表示正常，显示黑色。例外：UpAlarmState,  DownAlarmState
    private String CodeGroup; // 报警
    public String getCodeGroup() {
        return this.CodeGroup;
    }
    public void setCodeGroup(String CodeGroup) {
        this.CodeGroup = CodeGroup;
    }
    public String getGroupId() {
        return this.GroupId;
    }
    public void setGroupId(String GroupId) {
        this.GroupId = GroupId;
    }
    public String getCodeField() {
        return this.CodeField;
    }
    public void setCodeField(String CodeField) {
        this.CodeField = CodeField;
    }
    public String getCodeTable() {
        return this.CodeTable;
    }
    public void setCodeTable(String CodeTable) {
        this.CodeTable = CodeTable;
    }
    public String getCodeValue() {
        return this.CodeValue;
    }
    public void setCodeValue(String CodeValue) {
        this.CodeValue = CodeValue;
    }
    public String getCodeName() {
        return this.CodeName;
    }
    public void setCodeName(String CodeName) {
        this.CodeName = CodeName;
    }
    public String getCodeString() {
        return this.CodeString;
    }
    public void setCodeString(String CodeString) {
        this.CodeString = CodeString;
    }
    public String getCodeId() {
        return this.CodeId;
    }
    public void setCodeId(String CodeId) {
        this.CodeId = CodeId;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    @Generated(hash = 592870889)
    public StateCodeBean(Long id, String CodeId, String CodeString,
            String CodeName, String CodeValue, String CodeTable, String CodeField,
            String GroupId, String CodeGroup) {
        this.id = id;
        this.CodeId = CodeId;
        this.CodeString = CodeString;
        this.CodeName = CodeName;
        this.CodeValue = CodeValue;
        this.CodeTable = CodeTable;
        this.CodeField = CodeField;
        this.GroupId = GroupId;
        this.CodeGroup = CodeGroup;
    }
    @Generated(hash = 380470722)
    public StateCodeBean() {
    }

    @Override
    public String toString() {
        return "StateCodeBean{" +
                "id=" + id +
                ", CodeId='" + CodeId + '\'' +
                ", CodeString='" + CodeString + '\'' +
                ", CodeName='" + CodeName + '\'' +
                ", CodeValue='" + CodeValue + '\'' +
                ", CodeTable='" + CodeTable + '\'' +
                ", CodeField='" + CodeField + '\'' +
                ", GroupId='" + GroupId + '\'' +
                ", CodeGroup='" + CodeGroup + '\'' +
                '}';
    }
}