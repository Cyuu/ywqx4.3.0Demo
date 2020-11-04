package com.thdz.ywqx.bean;

/**
 * Created by ZZX on 2016-09-09.
 */
public class UserBean {

    private Long id;
    private String uid;
    private String username;
    private String password;
    private String ip;
    private String clientid;

    public UserBean(){}

    public UserBean(Long id, String uid, String username, String password,
            String ip, String clientid) {
        this.id = id;
        this.uid = uid;
        this.username = username;
        this.password = password;
        this.ip = ip;
        this.clientid = clientid;
    }

    public Long getId() {
        return id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getClientid() {
        return clientid;
    }

    public void setClientid(String clientid) {
        this.clientid = clientid;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
