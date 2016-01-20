package com.ksoft.webrepairtool.Beans;

/**
 * Created by Laci on 2015.12.29..
 */
public class ConnectionRecord {

    private String ID;
    private String host;
    private String userName;
    private String password;

    /*public ConnectionRecord(String host, String userName, String password) {
        this.host = host;
        this.userName = userName;
        this.password = password;
    }*/

    public ConnectionRecord(String ID, String host, String userName, String password) {
        this.ID = ID;
        this.host = host;
        this.userName = userName;
        this.password = password;
    }

    public ConnectionRecord() {
        this.host = null;
        this.userName = null;
        this.password = null;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getHost() {
        return host;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return host + '\\' + userName;
    }
}
