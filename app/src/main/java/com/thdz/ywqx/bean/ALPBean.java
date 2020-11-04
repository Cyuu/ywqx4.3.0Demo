package com.thdz.ywqx.bean;

import java.io.Serializable;

/**
 * 告警详情页-- 抠图 --需要的参数类
 */

public class ALPBean implements Serializable {

    private String GstrSid; // unitNo
    private String IpcSid; // 镜头号
    private String Pic_Id; // 图片id
    private String FPP; // 预置位
    private String Res; // 为空， 传0

    public ALPBean(){}

    public String getGstrSid() {
        return GstrSid;
    }

    public void setGstrSid(String GstrSid) {
        this.GstrSid = GstrSid;
    }

    public String getIpcSid() {
        return IpcSid;
    }

    public void setIpcSid(String IpcSid) {
        this.IpcSid = IpcSid;
    }

    public String getPic_Id() {
        return Pic_Id;
    }

    public void setPic_Id(String Pic_Id) {
        this.Pic_Id = Pic_Id;
    }

    public String getFPP() {
        return FPP;
    }

    public void setFPP(String FPP) {
        this.FPP = FPP;
    }

    public String getRes() {
        return Res;
    }

    public void setRes(String Res) {
        this.Res = Res;
    }

    @Override
    public String toString() {
        return "ALPBean{" +
                "GstrSid='" + GstrSid + '\'' +
                ", IpcSid='" + IpcSid + '\'' +
                ", Pic_Id='" + Pic_Id + '\'' +
                ", FPP='" + FPP + '\'' +
                ", Res='" + Res + '\'' +
                '}';
    }

}
