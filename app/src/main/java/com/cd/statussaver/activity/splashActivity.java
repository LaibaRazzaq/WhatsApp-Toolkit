package com.cd.statussaver.activity;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ProcessLifecycleOwner;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;


import com.appbrain.AdId;
import com.appbrain.InterstitialBuilder;


import com.cd.statussaver.R;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;

import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.AdapterStatus;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;


import java.util.Map;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;


public class splashActivity extends AppCompatActivity {
    private static final String TAG = "";
    boolean doubleBackExitPressedOnc = false;
    Boolean checkads=true;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    int myIntValue = 0;
    boolean check_admob;
    InterstitialAd mInterstitialAd;
    Thread timerThread;
    AdRequest adRequest1;
    private InterstitialBuilder interstitialBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        MobileAds.initialize(this,
                new OnInitializationCompleteListener() {
                    @Override
                    public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {
                    }
                });



        // ProcessLifecycleOwner.get().getLifecycle().addObserver(new splashobserver());

        sp = getSharedPreferences("your_prefs", Activity.MODE_PRIVATE);
        sp.edit().putInt("resume_check",0).apply();
        load_InterstitialAd();
        // FIREBASE INTERSTICIAL

        myIntValue = sp.getInt("ad_value", 5);
        if (myIntValue == 10) {
            check_admob = false;

        } else {
            check_admob =true;
        }


            // Toast.makeText(this,"* Necessário Acesso a Internet *",Toast.LENGTH_LONG).show();

            timerThread = new Thread() {
                public void run() {
                    try {
                        sleep(6000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if (mInterstitialAd != null) {
                                    mInterstitialAd.show(splashActivity.this);

                                    mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
                                        @Override
                                        public void onAdDismissedFullScreenContent() {
                                            sp.edit().putInt("resume_check",1).apply();
                                            // Code to be executed when when the interstitial ad is closed.
                                            Intent intent = new Intent(splashActivity.this, MainActivity2.class);
                                            try {
                                                intent.addFlags(FLAG_ACTIVITY_NEW_TASK);


                                                startActivity(intent);
                                            } catch (RuntimeException e) {

                                            }
                                            finish();
                                            // Called when fullscreen content is dismissed.
                                            Log.d("TAG", "The ad was dismissed.");

                                        }

                                        @Override
                                        public void onAdFailedToShowFullScreenContent(AdError adError) {
                                            // Called when fullscreen content failed to show.
                                            sp.edit().putInt("resume_check",1).apply();
                                            // Code to be executed when when the interstitial ad is closed.
                                            Intent intent = new Intent(splashActivity.this, MainActivity2.class);
                                            try {
                                                startActivity(intent);
                                            } catch (RuntimeException e) {

                                            }
                                            finish();
                                        }

                                        @Override
                                        public void onAdShowedFullScreenContent() {
                                            // Called when fullscreen content is shown.
                                            // Make sure to set your reference to null so you don't
                                            // show it a second time.
                                            mInterstitialAd = null;
                                            sp.edit().putInt("resume_check",0).apply();
                                            Log.d("TAG", "The ad was shown.");
                                        }
                                    });
                                } else {
                                    sp.edit().putInt("resume_check",1).apply();
                                    Intent intent = new Intent(splashActivity.this, MainActivity2.class);
                                    try {
                                        startActivity(intent);
                                    } catch (RuntimeException e) {

                                    }
                                    finish();
                                }




                            }
                        });

                    }

                }
            };
            timerThread.start();
        }




    @Override
    public void onBackPressed() {
        if (this.doubleBackExitPressedOnc) {

            finish();
            System.exit(0);
            super.onBackPressed();
            return;
        }

        this.doubleBackExitPressedOnc = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new timer(), 2000);
    }
    class timer implements Runnable {
        timer() {
        }

        public void run() {
            splashActivity.this.doubleBackExitPressedOnc = false;
        }
    }
    public void load_InterstitialAd() {

        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(this,


                getString(R.string.admobe_interestitial_splash), adRequest, new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        Log.i(TAG, "onAdLoaded");
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.i(TAG, loadAdError.getMessage());
                        mInterstitialAd = null;
                    }
                });

    }
}