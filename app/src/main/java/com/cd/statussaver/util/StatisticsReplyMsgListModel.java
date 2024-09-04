package com.cd.statussaver.util;

public class StatisticsReplyMsgListModel {
    private String PersonName;
    private int i;
    private String replyMsg;
    private long time;

    public StatisticsReplyMsgListModel(int i2, String str, String str2, long j) {
        this.i = i2;
        this.replyMsg = str;
        this.PersonName = str2;
        this.time = j;
    }

    public int getI() {
        return this.i;
    }

    public void setI(int i2) {
        this.i = i2;
    }

    public String getReplyMsg() {
        return this.replyMsg;
    }

    public void setReplyMsg(String str) {
        this.replyMsg = str;
    }

    public String getPersonName() {
        return this.PersonName;
    }

    public void setPersonName(String str) {
        this.PersonName = str;
    }

    public long getTime() {
        return this.time;
    }

    public void setTime(long j) {
        this.time = j;
    }
}
