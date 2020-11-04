package com.thdz.ywqx.bean;

import java.io.Serializable;

/**
 * Created by ZZX on 2016-09-22.
 */

public class Dot implements Serializable {
    private String X;
    private String Y;

    public Dot() {
    }

    public Dot(String x, String y) {
        X = x;
        Y = y;
    }

    public String getX() {
        return X;
    }

    public void setX(String x) {
        X = x;
    }

    public String getY() {
        return Y;
    }

    public void setY(String y) {
        Y = y;
    }
}
