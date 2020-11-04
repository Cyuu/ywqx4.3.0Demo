package com.thdz.ywqx.bean;

import java.io.Serializable;

/**
 * Created by ZZX on 2016-08-16.
 */

public class RespBase implements Serializable {

    private String Result;
    private String Msg;
    private String Data;

    public RespBase() {
    }

    public RespBase(String result, String msg, String data) {
        Result = result;
        Msg = msg;
        Data = data;
    }

    public String getResult() {
        return Result;
    }

    public void setResult(String result) {
        Result = result;
    }

    public String getMsg() {
        return Msg;
    }

    public void setMsg(String msg) {
        Msg = msg;
    }

    public String getData() {
        return Data;
    }

    public void setData(String data) {
        Data = data;
    }

    @Override
    public String toString() {
        return "RespBase{" +
                "Result='" + Result + '\'' +
                ", Msg='" + Msg + '\'' +
                ", Data='" + Data + '\'' +
                '}';
    }
}
