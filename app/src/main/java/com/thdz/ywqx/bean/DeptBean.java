package com.thdz.ywqx.bean;

import java.io.Serializable;

public class DeptBean implements Serializable {

    private static final long serialVersionUID = 6823096235945474192L;
    private String RBId;
    private String RBName;
    private String RBNo;

    public DeptBean() {
        super();
    }

    public DeptBean(String RBId, String RBName, String RBNo) {
        this.RBId = RBId;
        this.RBName = RBName;
        this.RBNo = RBNo;
    }

    public String getRBId() {
        return RBId;
    }

    public void setRBId(String RBId) {
        this.RBId = RBId;
    }

    public String getRBName() {
        return RBName;
    }

    public void setRBName(String RBName) {
        this.RBName = RBName;
    }

    public String getRBNo() {
        return RBNo;
    }

    public void setRBNo(String RBNo) {
        this.RBNo = RBNo;
    }

    @Override
    public String toString() {
        return "DeptBean{" +
                "RBId='" + RBId + '\'' +
                ", RBName='" + RBName + '\'' +
                ", RBNo='" + RBNo + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object arg0) {
        if (!(arg0 instanceof DeptBean)) {
            return false;
        } else {
            DeptBean item = (DeptBean) arg0;
            return item.getRBId().equals(this.RBId);
        }
    }
}
