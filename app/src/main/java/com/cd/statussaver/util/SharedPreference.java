package com.cd.statussaver.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.cd.statussaver.model.AutoReply;
import com.cd.statussaver.model.PhoneNumberModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class SharedPreference {
    private Context context;
    SharedPreferences.Editor editor;
    private SharedPreferences preferences;

    public SharedPreference(Context context2) {
        context = context2;
        preferences = context2.getSharedPreferences("mypref", 0);
        editor = preferences.edit();
    }

    public long addToPref_Long(String str, long j) {
        preferences= PreferenceManager.getDefaultSharedPreferences(context);
        editor = preferences.edit();
        editor.putLong(str, j);
        editor.commit();
        return 0;
    }

    public long getFromPref_Long(String str) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getLong(str, 0);
    }

    public boolean addToPref_Boolean(String str, boolean z) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);

        editor = preferences.edit();
        editor.putBoolean(str, z);
        editor.commit();
        return z;
    }

    public boolean getFromPref_Boolean(String str) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(str, false);
    }

    public long addToPref_String(String str, String str2) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = preferences.edit();
        editor.putString(str, str2);
        editor.commit();
        return 0;
    }

    public String getFromPref_String(String str) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(str, "");
    }

    public <T> void setList(String str, List<T> list) {
        set(str, new Gson().toJson((Object) list));
    }

    public <T> void setReplyList(String str, List<T> list) {
        set(str, new Gson().toJson((Object) list));
    }

    public <T> void setAutoTextList(String str, List<T> list) {
        String json = new Gson().toJson((Object) list);
        Log.e("PRINTJSON", json);
        set(str, json);
    }

    public <T> void setUserList(String str, List<T> list) {
        String json = new Gson().toJson((Object) list);
        Log.e("PRINTJSON", json);
        set(str, json);
    }

    public <T> void setNumberList(String str, List<T> list) {
        String json = new Gson().toJson((Object) list);
        Log.e("PRINTJSON", json);
        set(str, json);
    }

    public <T> void setContactList(String str, List<T> list) {
        String json = new Gson().toJson((Object) list);
        Log.e("PRINTJSON Contact", json);
        set(str, json);
    }

    public void set(String str, String str2) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = preferences.edit();
        editor.putString(str, str2);
        editor.commit();
    }

    public List<AutoReply> getList(String str) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return (List) new Gson().fromJson(preferences.getString(str, ""), new TypeToken<List<AutoReply>>() {
        }.getType());
    }

    public List<StatisticsReplyMsgListModel> getReplyList(String str) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return (List) new Gson().fromJson(preferences.getString(str, ""), new TypeToken<List<StatisticsReplyMsgListModel>>() {
        }.getType());
    }

    public List<String> getAutoTextList(String str) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String string = preferences.getString(str, "");
        return (List) gson.fromJson(string, new TypeToken<List<String>>() {
        }.getType());
    }

    public List<String> getUserList(String str) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String string = preferences.getString(str, "");
        Log.e("GETJSON", string);
        return (List) gson.fromJson(string, new TypeToken<List<String>>() {
        }.getType());
    }

    public List<PhoneNumberModel> getNumberList(String str) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String string = preferences.getString(str, "");
        Log.e("GETJSON", string);
        return (List) gson.fromJson(string, new TypeToken<List<PhoneNumberModel>>() {
        }.getType());
    }

    public List<String> getContactList(String str) {
        this.preferences = PreferenceManager.getDefaultSharedPreferences(this.context);
        Gson gson = new Gson();
        String string = this.preferences.getString(str, "");
        Log.e("GETJSON Contact", string);
        return (List) gson.fromJson(string, new TypeToken<List<String>>() {
        }.getType());
    }

    public void setStartTime(String startTime){
        this.preferences=PreferenceManager.getDefaultSharedPreferences(this.context);
        this.preferences.edit().putString("StartTime",startTime).apply();
    }    public void setEndTime(String endTime){
        this.preferences=PreferenceManager.getDefaultSharedPreferences(this.context);
        this.preferences.edit().putString("EndTime",endTime).apply();
    }
}
