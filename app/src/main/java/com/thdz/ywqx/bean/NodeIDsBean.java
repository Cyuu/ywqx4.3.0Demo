package com.thdz.ywqx.bean;

import java.io.Serializable;

/**
 * 改造请求接口的节点
 */
public class NodeIDsBean implements Serializable {
    private String nAppId;
    private String nRBId;
    private String nRLId;
    private String nRSId;
    private String nRWIId;
    private String nStnId;
    private String nPcdtId;
    private String nUnitId;

    public NodeIDsBean(){}

    public NodeIDsBean(String nAppId, String nRBId, String nRLId, String nRSId, String nRWIId, String nStnId, String nPcdtId, String nUnitId) {
        this.nAppId = nAppId;
        this.nRBId = nRBId;
        this.nRLId = nRLId;
        this.nRSId = nRSId;
        this.nRWIId = nRWIId;
        this.nStnId = nStnId;
        this.nPcdtId = nPcdtId;
        this.nUnitId = nUnitId;
    }

    public String getnAppId() {
        return nAppId;
    }

    public void setnAppId(String nAppId) {
        this.nAppId = nAppId;
    }

    public String getnRBId() {
        return nRBId;
    }

    public void setnRBId(String nRBId) {
        this.nRBId = nRBId;
    }

    public String getnRLId() {
        return nRLId;
    }

    public void setnRLId(String nRLId) {
        this.nRLId = nRLId;
    }

    public String getnRSId() {
        return nRSId;
    }

    public void setnRSId(String nRSId) {
        this.nRSId = nRSId;
    }

    public String getnRWIId() {
        return nRWIId;
    }

    public void setnRWIId(String nRWIId) {
        this.nRWIId = nRWIId;
    }

    public String getnStnId() {
        return nStnId;
    }

    public void setnStnId(String nStnId) {
        this.nStnId = nStnId;
    }

    public String getnPcdtId() {
        return nPcdtId;
    }

    public void setnPcdtId(String nPcdtId) {
        this.nPcdtId = nPcdtId;
    }

    public String getnUnitId() {
        return nUnitId;
    }

    public void setnUnitId(String nUnitId) {
        this.nUnitId = nUnitId;
    }

    @Override
    public String toString() {
        return "NodeIDsBean{" +
                "nAppId='" + nAppId + '\'' +
                ", nRBId='" + nRBId + '\'' +
                ", nRLId='" + nRLId + '\'' +
                ", nRSId='" + nRSId + '\'' +
                ", nRWIId='" + nRWIId + '\'' +
                ", nStnId='" + nStnId + '\'' +
                ", nPcdtId='" + nPcdtId + '\'' +
                ", nUnitId='" + nUnitId + '\'' +
                '}';
    }
}
