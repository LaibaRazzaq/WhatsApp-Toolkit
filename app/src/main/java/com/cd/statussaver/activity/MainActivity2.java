package com.cd.statussaver.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cd.statussaver.NativeTemplateStyle;
import com.cd.statussaver.R;
import com.cd.statussaver.TemplateView;
import com.cd.statussaver.databinding.ActivityMain2Binding;
import com.cd.statussaver.util.Admenager;
import com.cd.statussaver.util.Utils;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;

public class MainActivity2 extends AppCompatActivity implements View.OnClickListener {

    ActivityMain2Binding binding;
    SharedPreferences sp ;
    Admenager admenager;
    String TAG = "facebook ads";

    NativeAd mNativeAd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMain2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ActionBar actionBar=getSupportActionBar();
        if (actionBar!=null){
            actionBar.hide();
        }

        admenager=new Admenager(this,this);
        admenager.loadBannerAd(binding.adView,getString(R.string.admobe_banner_main));
        binding.autoReply.setOnClickListener(this);
        binding.whatsAppStatus.setOnClickListener(this);
        binding.customChat.setOnClickListener(this);
        binding.businerCleaner.setOnClickListener(this);
        binding.whatsAppBusniesStatus.setOnClickListener(this);
        binding.directChat.setOnClickListener(this);
        binding.gallery.setOnClickListener(this);
        binding.setting.setOnClickListener(this);
        binding.whatsappCleaner.setOnClickListener(this);
        binding.businerCleaner.setOnClickListener(this);
        binding.share.setOnClickListener(this);
        binding.whatsAppBusniesStatus.setOnClickListener(this);
        binding.whatsappWeb.setOnClickListener(this);


        boolean BuisnessWhatsappFound = isAppInstalled(getApplicationContext(), "com.whatsapp.w4b");
        boolean WhatsAppFound = isAppInstalled(getApplicationContext(), "com.whatsapp");
        sp = getSharedPreferences("your_prefs", Activity.MODE_PRIVATE);
        sp.edit().putInt("resume_check",1).apply();
        if (!WhatsAppFound){
            binding.whatsappCleaner.setClickable(false);
            binding.whatsappCleaner.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.grey), PorterDuff.Mode.SRC_IN);


        }else {
            binding.whatsappCleaner.setClickable(true);
        }
        if (!BuisnessWhatsappFound){
            binding.businerCleaner.setClickable(false);
            binding.businerCleaner.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.grey), PorterDuff.Mode.SRC_IN);


        }else {
            binding.businerCleaner.setClickable(true);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            checkPermission();
        }

loadNativeAd();
    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        if (id==binding.autoReply.getId()){
            callAutoActvity();
        }else if (id==binding.customChat.getId()){
            callCustomActivty();
        }else if (id==binding.directChat.getId()){
            callDirectMsgActivity();
        }else if (id==binding.whatsappWeb.getId()) {
            callWebActivity();
        }else if (id==binding.gallery.getId()) {
            callGalleryActivity();
        }else if (id==binding.whatsAppStatus.getId()) {
            callWhatsappActivity("whatsapp");
        }else if (id==binding.setting.getId()) {
            callSettingActivity();
        }else if (id==binding.whatsappCleaner.getId()){
            callCleanerActivity("whatsapp");
        }else if (id==binding.businerCleaner.getId()){
            callCleanerActivity("whatsappBussiness");
        } else if (id==binding.share.getId()) {
            Utils.ShareApp(MainActivity2.this);

        } else if (id==binding.whatsAppBusniesStatus.getId()) {
            callWhatsappActivity("business");
        }

    }

    private void callWebActivity() {
        Intent i = new Intent(MainActivity2.this, WebviewAcitivity.class);
        startActivity(i);
    }

    private void callSettingActivity() {
        Intent i = new Intent(MainActivity2.this, SettingActivity.class);
        startActivity(i);
    }

    private void callAutoActvity() {
        Intent i = new Intent(MainActivity2.this, AutoReplyActivity.class);
        startActivity(i);
    }

    private void callCustomActivty() {
        Intent i = new Intent(MainActivity2.this, CustomReplyActivity.class);
        startActivity(i);
    }

    private void callDirectMsgActivity() {
        Intent i = new Intent(MainActivity2.this, DirectSendActivity.class);
        startActivity(i);
    }


    public void callWhatsappActivity(String from) {
        Intent i = new Intent(MainActivity2.this, WhatsappActivity.class);
        i.putExtra("from",from);
        startActivity(i);
    }


    public void callGalleryActivity() {
        Intent i = new Intent(MainActivity2.this, GalleryActivity.class);
        startActivity(i);
    }
    public void  callCleanerActivity(String from){
        Intent intent=new Intent(MainActivity2.this,WhatsAppCleaner.class);
        intent.putExtra("from",from);
        startActivity(intent);
    }
    public static boolean isAppInstalled(Context context, String packageName) {
        try {
            context.getPackageManager().getApplicationInfo(packageName, 0);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    public void checkPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission not granted, request it
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.POST_NOTIFICATIONS},
                    12
            );
        }
    }

    @Override
    public void onBackPressed() {
        final AlertDialog.Builder exitBuilderDialog = new AlertDialog.Builder(this);

        View dialogView = getLayoutInflater().inflate(R.layout.exit_layout, null);

        Button yesBtm = dialogView.findViewById(R.id.id_exit_pos_btm);
        Button noBtm = dialogView.findViewById(R.id.id_exit_neg_btm);
        TextView txtTitle = dialogView.findViewById(R.id.id_exit_title);
        TextView txtMessage = dialogView.findViewById(R.id.id_exit_message);
        TemplateView template = dialogView.findViewById(R.id.my_template);

        template.setVisibility(View.GONE);

        if (mNativeAd != null) {
            NativeTemplateStyle styles = new
                    NativeTemplateStyle.Builder().build();


            template.setStyles(styles);
            template.setVisibility(View.VISIBLE);
            template.setNativeAd(mNativeAd);
        }


        txtTitle.setText("Exit");
        txtMessage.setText("Do you want to exit?");

        exitBuilderDialog.setView(dialogView);
        final AlertDialog alertDialog = exitBuilderDialog.create();
        alertDialog.setCanceledOnTouchOutside(false);

        alertDialog.show();

        yesBtm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                finish();
                System.exit(0);
            }
        });

        noBtm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mNativeAd != null) {
                    mNativeAd.destroy();
                }
                loadNativeAd();
                alertDialog.cancel();
            }
        });

    }

    private void loadNativeAd() {
        AdLoader adLoader = new AdLoader.Builder(MainActivity2.this, getString(R.string.admobe_back))

                .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {

                    @Override
                    public void onNativeAdLoaded(@NonNull NativeAd nativeAd) {
                        Log.d(TAG, "Native Ad Loaded");


                        if (isDestroyed()) {
                            nativeAd.destroy();
                            Log.d(TAG, "Native Ad Destroyed");
                            return;
                        }

                        mNativeAd = nativeAd;

                    }
                })

                .withAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(LoadAdError adError) {
                        // Handle the failure by logging, altering the UI, and so on.
                        Log.d(TAG, "Native Ad Failed To Load");

                    }
                })
                .withNativeAdOptions(new NativeAdOptions.Builder()
                        .build())
                .build();

        adLoader.loadAd(new AdRequest.Builder().build());

    }
}