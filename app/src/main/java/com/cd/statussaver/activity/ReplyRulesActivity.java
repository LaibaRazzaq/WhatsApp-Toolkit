package com.cd.statussaver.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;

import com.cd.statussaver.R;
import com.cd.statussaver.databinding.ActivityReplyRulesBinding;
import com.cd.statussaver.util.SharedPreference;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class ReplyRulesActivity extends AppCompatActivity implements View.OnClickListener {

    public SharedPreference preference;

    ActivityReplyRulesBinding thisb;

    private TimePickerDialog timePickerDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        thisb = (ActivityReplyRulesBinding) DataBindingUtil.setContentView(this, R.layout.activity_reply_rules);
        ActionBar actionBar=getSupportActionBar();
        if (actionBar!=null){
            actionBar.hide();
        }        preference = new SharedPreference(this);

        thisb.chkContain.setOnClickListener(this);
        thisb.chkExact.setOnClickListener(this);
        thisb.imgTimeback.setOnClickListener(this);


        bannerAd();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    private void checkBoxText() {
        thisb.chkContain.setText(Html.fromHtml("<font color='black'>Message that contains</font><br><font color='#808080'> Auto replay while get message containing specific words </font>"));
        thisb.chkExact.setText(Html.fromHtml("<font color='black'>Message with exact match</font><br><font color='#808080'> Auto reply while get message with exact matching text </font>"));
    }

    private void bannerAd() {
        MobileAds.initialize((Context) this, (OnInitializationCompleteListener) new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {
                Log.e("Adssss", initializationStatus.toString());
            }
        });
        thisb.bannerAd.loadAd(new AdRequest.Builder().build());
    }

    @Override
    public void onResume() {
        super.onResume();
        checkBoxText();
        if (preference.getFromPref_Boolean("exact")) {
            thisb.chkExact.setChecked(true);
            thisb.chkContain.setChecked(false);
        } else if (preference.getFromPref_Boolean("contain")) {
            thisb.chkExact.setChecked(false);
            thisb.chkContain.setChecked(true);
        }
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();

        if (viewId == R.id.chkContain) {
            thisb.chkContain.setChecked(true);
            thisb.chkExact.setChecked(false);
            preference.addToPref_Boolean("contain", true);
            preference.addToPref_Boolean("exact", false);
        } else if (viewId == R.id.chkExact) {
            thisb.chkExact.setChecked(true);
            thisb.chkContain.setChecked(false);
            preference.addToPref_Boolean("exact", true);
            preference.addToPref_Boolean("contain", false);
        } else if (viewId == R.id.imgTimeback) {
            finish();
        } else {
            // Handle other cases if needed
        }


    }


}