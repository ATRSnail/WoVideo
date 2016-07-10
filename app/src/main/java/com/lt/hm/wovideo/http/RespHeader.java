package com.lt.hm.wovideo.http;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/6/12
 */
public class RespHeader {
    private String appVersion;
    private String dataVersion;
    private String deployVersion;
    private String msgCount;
    private String rspCode;
    private String rspMsg;

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getDataVersion() {
        return dataVersion;
    }

    public void setDataVersion(String dataVersion) {
        this.dataVersion = dataVersion;
    }

    public String getDeployVersion() {
        return deployVersion;
    }

    public void setDeployVersion(String deployVersion) {
        this.deployVersion = deployVersion;
    }

    public String getMsgCount() {
        return msgCount;
    }

    public void setMsgCount(String msgCount) {
        this.msgCount = msgCount;
    }

    public String getRspCode() {
        return rspCode;
    }

    public void setRspCode(String rspCode) {
        this.rspCode = rspCode;
    }

    public String getRspMsg() {
        return rspMsg;
    }

    public void setRspMsg(String rspMsg) {
        this.rspMsg = rspMsg;
    }

    @Override
    public String toString() {
        return "RespHeader{" +
                "appVersion='" + appVersion + '\'' +
                ", dataVersion='" + dataVersion + '\'' +
                ", deployVersion='" + deployVersion + '\'' +
                ", msgCount='" + msgCount + '\'' +
                ", rspCode='" + rspCode + '\'' +
                ", rspMsg='" + rspMsg + '\'' +
                '}';
    }
}
