package com.cd.statussaver.model;

public class AutoReply {
    String ReceiveMsg;
    String SendMsg;

    public AutoReply(String str, String str2) {
        this.ReceiveMsg = str;
        this.SendMsg = str2;
    }

    public String getReceiveMsg() {
        return this.ReceiveMsg;
    }

    public void setReceiveMsg(String str) {
        this.ReceiveMsg = str;
    }

    public String getSendMsg() {
        return this.SendMsg;
    }

    public void setSendMsg(String str) {
        this.SendMsg = str;
    }
}
