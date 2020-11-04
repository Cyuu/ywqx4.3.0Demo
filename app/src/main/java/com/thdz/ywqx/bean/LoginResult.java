package com.thdz.ywqx.bean;

import java.io.Serializable;

/**
 * Created by ZZX on 2016-08-16.
 */

public class LoginResult implements Serializable {
    private String UserId;

    public LoginResult() {
    }

    public LoginResult(String userId) {
        UserId = userId;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    @Override
    public String toString() {
        return "LoginResult{" +
                "UserId='" + UserId + '\'' +
                '}';
    }
}
