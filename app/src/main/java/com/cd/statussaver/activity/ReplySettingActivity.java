package com.cd.statussaver.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;

import com.cd.statussaver.R;
import com.cd.statussaver.databinding.ActivityReplySettingBinding;
import com.cd.statussaver.util.SharedPreference;

public class ReplySettingActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityReplySettingBinding thisb;
    private SharedPreference preference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityReplySettingBinding  activitySettingsBinding = (ActivityReplySettingBinding) DataBindingUtil.setContentView(this, R.layout.activity_reply_setting);
        thisb = activitySettingsBinding;
        ActionBar actionBar=getSupportActionBar();
        if (actionBar!=null){
            actionBar.hide();
        }
        activitySettingsBinding.linearReplyTime.setOnClickListener(this);
        preference = new SharedPreference(this);
        thisb.linearReplyHeader.setOnClickListener(this);
        thisb.linearReplyRules.setOnClickListener(this);
        thisb.linearGroupMsg.setOnClickListener(this);
        thisb.imgSettigsBack.setOnClickListener(this);

        thisb.switchgroups.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                preference.addToPref_Boolean("GroupEnable", b);
            }
        });
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_ACTIVITIES);

            Log.e("VERSION", packageInfo.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        thisb.switchgroups.setChecked(preference.getFromPref_Boolean("GroupEnable"));
    }

    public void onClick(View view) {
        int viewId = view.getId();

        if (viewId == R.id.imgSettigsBack) {
            finish();
        } else if (viewId == R.id.linearReplyHeader) {
            startActivity(new Intent(this, ReplyHeaderActivity.class));
        } else if (viewId == R.id.linearReplyTime) {
            startActivity(new Intent(this, ReplyTimeActivity.class));
        } else if (viewId == R.id.linearReplyRules) {
            startActivity(new Intent(this, ReplyRulesActivity.class));
        }
    }

    public void rateApp() {
        try {
            startActivity(rateFromUrl("market://details"));
        } catch (Exception unused) {
            startActivity(rateFromUrl("https://play.google.com/store/apps/details"));
        }
    }

    private Intent rateFromUrl(String str) {

        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(String.format("%s?id=%s", new Object[]{str, getPackageName()})));

        intent.addFlags(1208483840);
        return intent;
    }


}