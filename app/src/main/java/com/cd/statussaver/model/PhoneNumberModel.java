package com.cd.statussaver.model;

public class PhoneNumberModel {
    private String Number;
    private long time;

    public PhoneNumberModel(String str, Long l) {
        this.Number = str;
        this.time = l.longValue();
    }

    public String getNumber() {
        return this.Number;
    }

    public void setNumber(String str) {
        this.Number = str;
    }

    public long getTime() {
        return this.time;
    }

    public void setTime(long j) {
        this.time = j;
    }
}
