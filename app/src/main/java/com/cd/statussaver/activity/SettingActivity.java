package com.cd.statussaver.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.cd.statussaver.BuildConfig;
import com.cd.statussaver.R;
import com.cd.statussaver.databinding.ActivitySettingBinding;
import com.cd.statussaver.util.Admenager;
import com.cd.statussaver.util.SharedPreference;

public class  SettingActivity extends AppCompatActivity implements View.OnClickListener {
    ActivitySettingBinding thisb;
    private SharedPreference preference;
        Admenager admenager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        admenager=new Admenager(this,this);
        admenager.load_InterstitialAd(getString(R.string.admobe_intertesial_setting));
        ActionBar actionBar=getSupportActionBar();
        if (actionBar!=null){
            actionBar.hide();
        }
        thisb = (ActivitySettingBinding) DataBindingUtil.setContentView(this, R.layout.activity_setting);

        preference = new SharedPreference(this);
        thisb.imgSettigsBack.setOnClickListener(this);
        thisb.linearRateApp.setOnClickListener(this);
        thisb.linearShareApp.setOnClickListener(this);

        thisb.linearTermAndCondition.setOnClickListener(this);
        /*thisb.switchgroups.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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
        }*/
    }

    @Override
    public void onResume() {
        super.onResume();
        //thisb.switchgroups.setChecked(preference.getFromPref_Boolean("GroupEnable"));
    }

    public void onClick(View view) {
        int viewId = view.getId();

        if (viewId == R.id.imgSettigsBack) {
            finish();
        } else if (viewId == R.id.linearRateApp) {
            rateApp();
        } else if (viewId == R.id.linearShareApp) {
            Intent intent = new Intent("android.intent.action.SEND");
            intent.setType("text/plain");
            intent.putExtra("android.intent.extra.TEXT", "App:https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);

            try {
                startActivity(Intent.createChooser(intent, "Share Via"));

            }catch (Exception e){
                e.printStackTrace();
            }
        } else if (viewId == R.id.linearTermAndCondition) {

            try{
                startActivity(privacy());

            }catch(Exception e){
                e.printStackTrace();
            }
        } else {
            // Handle other cases if needed
        }
    }

    public void rateApp() {
        try {
            startActivity(rateFromUrl());

        } catch (Exception unused) {
            unused.printStackTrace();
        }
    }

    private Intent rateFromUrl() {


        return new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id="+ BuildConfig.APPLICATION_ID));
    }
    private Intent privacy() {


        return new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.boxingessential.com/MasterToolkitPrivacyPolicy.html"));
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        admenager.showadmobeInterstitialAd();
    }
}