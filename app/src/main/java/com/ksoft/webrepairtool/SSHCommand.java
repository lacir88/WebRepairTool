package com.ksoft.webrepairtool;

/**
 * Created by Laci on 2016.01.19..
 */
public class SSHCommand {

    String commandName, commandString, userName, password, host;

    public SSHCommand(String host, String userName, String password, String commandName, String commandString) {
        this.host = host;
        this.userName = userName;
        this.password = password;
        this.commandName = commandName;
        this.commandString = commandString;
    }

    public SSHCommand() {
    }

    public String getCommandName() {
        return commandName;
    }

    public void setCommandName(String commandName) {
        this.commandName = commandName;
    }

    public String getCommandString() {
        return commandString;
    }

    public void setCommandString(String commandString) {
        this.commandString = commandString;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    @Override
    public String toString() {
        return host + '\'' + commandName;
    }
}
