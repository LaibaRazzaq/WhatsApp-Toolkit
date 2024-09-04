package com.cd.statussaver.model;

public class ReplyMessageListModel {

    private String date;
    private String name;
    private String sameDate;
    private String time;

    public ReplyMessageListModel(String str, String str2, String str3, String str4) {
        this.name = str;
        this.date = str2;
        this.time = str3;
        this.sameDate = str4;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public String getTime() {
        return this.time;
    }

    public void setTime(String str) {
        this.time = str;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String str) {
        this.date = str;
    }

    public String getSameDate() {
        return this.sameDate;
    }

    public void setSameDate(String str) {
        this.sameDate = str;
    }
}
