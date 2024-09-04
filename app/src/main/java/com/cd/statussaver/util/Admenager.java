package com.cd.statussaver.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.appbrain.AdId;
import com.appbrain.InterstitialBuilder;
import com.cd.statussaver.NativeTemplateStyle;
import com.cd.statussaver.R;
import com.cd.statussaver.TemplateView;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;


public class Admenager {
    private final Context mContext;
    private final Activity sActivity;
    SharedPreferences sharedPreferences;
    private InterstitialBuilder interstitialBuilder = null;
    boolean isAdLoaded,isAdLoaded1;
    SharedPreferences.Editor editor;
    SharedPreferences sp;


    public Admenager(Context context, Activity activity) {

        mContext = context;
        sActivity = activity;
        sharedPreferences = context.getSharedPreferences("appReview", Context.MODE_PRIVATE);
        isAdLoaded = sharedPreferences.getBoolean("isAdLoaded", false);
        sp=context.getSharedPreferences("your_prefs",Context.MODE_PRIVATE);


    }

    private static final String TAG = "--->NativeAd";
    private TemplateView template;
    InterstitialAd mInterstitialAd;
    AdRequest adRequest1;

    // private InterstitialBuilder interstitialBuilder=null;




    public void showadmobeInterstitialAd() {

        Log.d("add",""+isAdLoaded);

        Log.d("add",""+isAdLoaded);
        SharedPreferences sharedPreferences1= mContext.getSharedPreferences("appReview", Context.MODE_PRIVATE);
       SharedPreferences.Editor editor1 =sharedPreferences1.edit();
       isAdLoaded1=sharedPreferences1.getBoolean("isAdLoaded", false);

        if (isAdLoaded1){
           if (mInterstitialAd != null) {
               mInterstitialAd.show(sActivity);

               mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
                   @Override
                   public void onAdDismissedFullScreenContent() {

                       sp.edit().putInt("resume_check",1).apply();
                       finishad();
                       Log.d("TAG", "The ad was dismissed.");

                   }

                   @Override
                   public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                       // Called when fullscreen content failed to show.
                       Log.d("TAG", "The ad failed to show.");

                       finishad();

                   }

                   @Override
                   public void onAdShowedFullScreenContent() {

                       editor1.putBoolean("isAdLoaded", false);
                       editor1.apply();
                       editor = sp.edit();
                       editor.putInt("resume_check", 0);
                       editor.apply();

                       mInterstitialAd = null;

                   }
               });
           } else {

               // facbookads=new facbookads(mContext,sActivity);
               if (interstitialBuilder!=null) {
                   editor = sp.edit();
                   editor.putInt("resume_check", 1);
                   editor.apply();
                   editor1.putBoolean("isAdLoaded", false);
                   editor1.apply();
                   interstitialBuilder.show(sActivity);
                   editor.putBoolean("fromBrain", true).apply();



               }else {
                   editor = sp.edit();
                   editor.putInt("resume_check", 1);
                   editor.apply();
               }

               finishad();

               // finishad();
           }
       }else {
            editor = sp.edit();
            editor.putInt("resume_check", 1);
            editor.apply();
        }

    }
    public void load_InterstitialAd(String adId) {


        if(isAdLoaded){
            adLoadCount(adId);
        }else {
            editor=sharedPreferences.edit();
            editor.putBoolean("isAdLoaded", true);
            editor.apply();
        }




    }

    private void adLoadCount(String adId) {

        interstitialBuilder = InterstitialBuilder.create()
                .setAdId(AdId.LEVEL_COMPLETE)
                .setOnDoneCallback(new Runnable() {
                    @Override
                    public void run() {
                        // Preload again, so we can use interstitialBuilder again.
                        interstitialBuilder.preload(mContext);

                    }
                })
                .preload(mContext);

        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(sActivity,adId, adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                 mInterstitialAd = interstitialAd;

                Log.i(TAG, "onAdLoaded");
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                Log.i(TAG, loadAdError.getMessage());
                mInterstitialAd = null;

            }
        });

    }



    void loadAd(final String adID) {
        template = sActivity.findViewById(R.id.my_template);

        VideoOptions videoOptions = new VideoOptions.Builder()
                .setStartMuted(false)
                .build();

        NativeAdOptions adOptions = new NativeAdOptions.Builder()
                .setVideoOptions(videoOptions)
                .build();


            AdLoader adLoader = new AdLoader.Builder(mContext, adID).forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                        @Override
                        public void onNativeAdLoaded(@NonNull NativeAd nativeAd) {
                            Log.d(TAG, "Native Ad Loaded");

                            if (sActivity.isDestroyed()) {
                                nativeAd.destroy();
                                Log.d(TAG, "Native Ad Destroyed");
                                return;
                            }


                            if (nativeAd.getMediaContent() != null) {
                                nativeAd.getMediaContent().getVideoController();
                                nativeAd.getMediaContent().getVideoController().setVideoLifecycleCallbacks(new VideoController.VideoLifecycleCallbacks() {
                                    @Override
                                    public void onVideoStart() {
                                        super.onVideoStart();
                                        Log.d(TAG, "Video Started");
                                    }

                                    @Override
                                    public void onVideoPlay() {
                                        super.onVideoPlay();
                                        Log.d(TAG, "Video Played");
                                    }

                                    @Override
                                    public void onVideoPause() {
                                        super.onVideoPause();
                                        Log.d(TAG, "Video Paused");
                                    }

                                    @Override
                                    public void onVideoEnd() {
                                        super.onVideoEnd();
                                        Log.d(TAG, "Video Finished");
                                    }

                                    @Override
                                    public void onVideoMute(boolean b) {
                                        super.onVideoMute(b);
                                        Log.d(TAG, "Video Mute : " + b);
                                    }
                                });
                            }
                            NativeTemplateStyle styles = new
                                    NativeTemplateStyle.Builder().build();


                            template.setStyles(styles);
                            template.setVisibility(View.VISIBLE);
                            template.setNativeAd(nativeAd);

                        }
                    })

                    .withAdListener(new AdListener() {
                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                            // Handle the failure by logging, altering the UI, and so on.
                            Log.d(TAG, "Native Ad Failed To Load");
                            template.setVisibility(View.GONE);


                        }
                    })
                    // .withNativeAdOptions(new NativeAdOptions.Builder().build())
                    .withNativeAdOptions(adOptions)
                    .build();

            adLoader.loadAd(new AdRequest.Builder().build());




    }

    void finishad() {

            sActivity.finish();
        interstitialBuilder = InterstitialBuilder.create().setAdId(AdId.EXIT)
                .setFinishOnExit(sActivity).preload(sActivity);

    }
    public void loadBannerAd(FrameLayout adviewContainner , String adLiveID){

        AdView adView=new AdView(mContext);

        adView.setAdUnitId(adLiveID);


        loadBanner(adView);


        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                adviewContainner.setVisibility(View.VISIBLE);
                adviewContainner.removeAllViews();
                adviewContainner.addView(adView);

            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
            }
        });



    }
    private void loadBanner(AdView adView) {
        // Create an ad request. Check your logcat output for the hashed device ID
        // to get test ads on a physical device, e.g.,
        // "Use AdRequest.Builder.addTestDevice("ABCDE0123") to get test ads on this
        // device."
        AdRequest adRequest =
                new AdRequest.Builder()
                        .build();

        AdSize adSize = getAdSize();
        // Step 4 - Set the adaptive ad size on the ad view.
        adView.setAdSize(adSize);


        // Step 5 - Start loading the ad in the background.
        adView.loadAd(adRequest);
    }

    private AdSize getAdSize() {
        // Step 2 - Determine the screen width (less decorations) to use for the ad width.
        Display display = sActivity.getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;

        int adWidth = (int) (widthPixels / density);

        // Step 3 - Get adaptive ad size and return for setting on the ad view.
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(sActivity, adWidth);
    }


}

