package com.thdz.ywqx.bean;

import java.io.Serializable;

/**
 * zzx返回的最新告警列表的item类
 */

public class PreviewUrlBean implements Serializable {

    private String id;  //
    private String StaId;    // 1
    private String cameraIndexCode;  //
    private String cameraName; //
    private String parentName; //
    private String sPreviewUrl; // 预览url

    public PreviewUrlBean(){}

    public PreviewUrlBean(String id, String staId, String cameraIndexCode, String cameraName, String parentName, String sPreviewUrl) {
        this.id = id;
        StaId = staId;
        this.cameraIndexCode = cameraIndexCode;
        this.cameraName = cameraName;
        this.parentName = parentName;
        this.sPreviewUrl = sPreviewUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStaId() {
        return StaId;
    }

    public void setStaId(String staId) {
        StaId = staId;
    }

    public String getCameraIndexCode() {
        return cameraIndexCode;
    }

    public void setCameraIndexCode(String cameraIndexCode) {
        this.cameraIndexCode = cameraIndexCode;
    }

    public String getCameraName() {
        return cameraName;
    }

    public void setCameraName(String cameraName) {
        this.cameraName = cameraName;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getsPreviewUrl() {
        return sPreviewUrl;
    }

    public void setsPreviewUrl(String sPreviewUrl) {
        this.sPreviewUrl = sPreviewUrl;
    }

    @Override
    public String toString() {
        return "PreviewUrlBean{" +
                "id='" + id + '\'' +
                ", StaId='" + StaId + '\'' +
                ", cameraIndexCode='" + cameraIndexCode + '\'' +
                ", cameraName='" + cameraName + '\'' +
                ", parentName='" + parentName + '\'' +
                ", sPreviewUrl='" + sPreviewUrl + '\'' +
                '}';
    }
}
