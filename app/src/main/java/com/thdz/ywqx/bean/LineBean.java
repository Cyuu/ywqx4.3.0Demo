package com.thdz.ywqx.bean;

import java.io.Serializable;

/**
 * 站点bean
 */
public class LineBean implements Serializable {

    private String RBId; // 父层 局id

    private String RLId;// 1
    private String RLName;// 黔桂线

    public LineBean() {
        super();
    }

    public LineBean(String RBId, String RLId, String RLName ) {
        this.RBId = RBId;
        this.RLId = RLId;
        this.RLName = RLName;
    }

    public String getRBId() {
        return RBId;
    }

    public void setRBId(String RBId) {
        this.RBId = RBId;
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


    @Override
    public String toString() {
        return "LineBean{" +
                "RBId='" + RBId + '\'' +
                ", RLId='" + RLId + '\'' +
                ", RLName='" + RLName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object arg0) {
        if (!(arg0 instanceof LineBean)) {
            return false;
        } else {
            LineBean item = (LineBean) arg0;
            return item.getRLId().equals(this.RLId);
        }
    }

}
