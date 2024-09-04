package com.cd.statussaver.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cd.statussaver.NativeTemplateStyle;
import com.cd.statussaver.R;
import com.cd.statussaver.TemplateView;
import com.cd.statussaver.databinding.ActivityMainBinding;
import com.cd.statussaver.util.Admenager;
import com.cd.statussaver.util.ClipboardListener;
import com.cd.statussaver.util.Utils;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.cd.statussaver.util.Utils.createFileFolder;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    MainActivity activity;

    ActivityMainBinding binding;
    boolean doubleBackToExitPressedOnce = false;
    private ClipboardManager clipBoard;

    int ratingCheck;
    AlertDialog alertDialog;
    long availableBytes = 0;

    ArrayList<String> List;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    String[] permissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    String CopyKey = "";
    String CopyValue = "";

    String TAG = "facebook ads";

    NativeAd mNativeAd;
    Admenager admenager;
    AdView adView;

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

                Intent intentExit = new Intent(Intent.ACTION_MAIN);
                intentExit.addCategory(Intent.CATEGORY_HOME);
                intentExit.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentExit);
                editor = sp.edit();

                editor.putInt("resume_check", 0);

                editor.apply();
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
        AdLoader adLoader = new AdLoader.Builder(MainActivity.this, getString(R.string.admobe_back))

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ActionBar actionBar=getSupportActionBar();
        if (actionBar!=null){
            actionBar.hide();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            checkPermission();
        }
        activity = this;
        admenager = new Admenager(this, this);
        AdRequest adRequest = new AdRequest.Builder().build();
        binding.adView.loadAd(adRequest);
        binding.adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {


               binding.adView.setVisibility(View.VISIBLE);

            }
        });
        binding.appAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.nextsimpledesign.wasticker.stickerislamicswhatsapp"));
                    startActivity(intent);

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        loadNativeAd();
        initViews();
        sp = getSharedPreferences("your_prefs", Activity.MODE_PRIVATE);
        editor = sp.edit();
        editor.putInt("resume_check", 1);
        editor.apply();



        boolean BuisnessWhatsappFound = isAppInstalled(getApplicationContext(), "com.whatsapp.w4b");
        boolean WhatsAppFound = isAppInstalled(getApplicationContext(), "com.whatsapp");


        if (!BuisnessWhatsappFound) {
            binding.view9.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.grey), PorterDuff.Mode.SRC_IN);
            binding.imageView33.setColorFilter(ContextCompat.getColor(this, R.color.grey), PorterDuff.Mode.SRC_IN);
        }else {
            binding.buisnessIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(MainActivity.this,WhatsAppCleaner.class);
                    intent.putExtra("from","whatsappBussiness");
                    startActivity(intent);
                }
            });
        }

        if (!WhatsAppFound) {
            binding.view8.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.grey), PorterDuff.Mode.SRC_IN);
            binding.imageView24.setColorFilter(ContextCompat.getColor(this, R.color.grey), PorterDuff.Mode.SRC_IN);
        }else {
            binding.view8.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(MainActivity.this,WhatsAppCleaner.class);
                    intent.putExtra("from","whatsapp");
                    startActivity(intent);
                 //   startActivity(new Intent(MainActivity.this,WhatsAppCleaner.class));

                }
            });
        }



    }

    @Override
    protected void onResume() {
        super.onResume();
        activity = this;
        clipBoard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
    }

    public void initViews() {
        clipBoard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        if (activity.getIntent().getExtras() != null) {
            for (String key : activity.getIntent().getExtras().keySet()) {
                CopyKey = key;
                String value = activity.getIntent().getExtras().getString(CopyKey);
                if (CopyKey.equals("android.intent.extra.TEXT")) {
                    CopyValue = activity.getIntent().getExtras().getString(CopyKey);
                    callText(value);
                } else {
                    CopyValue = "";
                    callText(value);
                }
            }
        }
        if (clipBoard != null) {
            clipBoard.addPrimaryClipChangedListener(new ClipboardListener() {
                @Override
                public void onPrimaryClipChanged() {
                    try {
                        ClipData clipData = clipBoard.getPrimaryClip();
                        if (clipData != null && clipData.getItemCount() > 0) {
                            CharSequence text = clipData.getItemAt(0).getText();
                            if (text != null) {
                                showNotification(text.toString());
                            } else {
                                // Handle the case where the text is null
                                // You may choose to show a default notification or take other actions
                            }
                        } else {
                            // Handle the case where the clipboard data is null or empty
                            // You may choose to show a default notification or take other actions
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }


        checkPermissions();


        binding.rvWhatsApp.setOnClickListener(this);
        binding.rvGallery.setOnClickListener(this);
        binding.rvShareApp.setOnClickListener(this);
        binding.rvRateApp.setOnClickListener(this);
        binding.chatIcon.setOnClickListener(this);
        binding.customIcon.setOnClickListener(this);
        binding.webIcon.setOnClickListener(this);
        binding.autoIcon.setOnClickListener(this);
        binding.settingBtn.setOnClickListener(this);
        createFileFolder();

    }

    private void callText(String CopiedText) {
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        Intent i = null;
        int viewId = v.getId();

        if (viewId == R.id.rvWhatsApp) {
            callWhatsappActivity();
        } else if (viewId == R.id.rvGallery) {
            callGalleryActivity();
        } else if (viewId == R.id.rvShareApp) {
            Utils.ShareApp(activity);
        } else if (viewId == R.id.rvRateApp) {
            Utils.RateApp(activity);
        } else if (viewId == R.id.chat_icon) {
            callDirectMsgActivity();
        } else if (viewId == R.id.custom_icon) {
            callCustomActivty();
        } else if (viewId == R.id.auto_icon) {
            callAutoActvity();
        } else if (viewId == R.id.settingBtn) {
            callSettingActivity();
        } else if (viewId == R.id.web_icon) {
            callWebActivity();
        }

    }

    private void callWebActivity() {
        Intent i = new Intent(MainActivity.this, WebviewAcitivity.class);
        startActivity(i);
    }

    private void callSettingActivity() {
        Intent i = new Intent(MainActivity.this, SettingActivity.class);
        startActivity(i);
    }

    private void callAutoActvity() {
        Intent i = new Intent(MainActivity.this, AutoReplyActivity.class);
        startActivity(i);
    }

    private void callCustomActivty() {
        Intent i = new Intent(MainActivity.this, CustomReplyActivity.class);
        startActivity(i);
    }

    private void callDirectMsgActivity() {
        Intent i = new Intent(MainActivity.this, DirectSendActivity.class);
        startActivity(i);
    }


    public void callWhatsappActivity() {
        Intent i = new Intent(MainActivity.this, WhatsappActivity.class);
        startActivity(i);
    }


    public void callGalleryActivity() {
        Intent i = new Intent(MainActivity.this, GalleryActivity.class);
        startActivity(i);
    }

    public void showNotification(String Text) {
        if (Text.contains("instagram.com") || Text.contains("facebook.com") || Text.contains("tiktok.com") || Text.contains("twitter.com")) {
            Intent intent = new Intent(activity, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("Notification", Text);
            PendingIntent pendingIntent = PendingIntent.getActivity(activity, 0, intent,PendingIntent.FLAG_IMMUTABLE);
            NotificationManager notificationManager = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel mChannel = new NotificationChannel(getResources().getString(R.string.app_name),
                        getResources().getString(R.string.app_name), NotificationManager.IMPORTANCE_HIGH);
                mChannel.enableLights(true);
                mChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
                notificationManager.createNotificationChannel(mChannel);
            }
            NotificationCompat.Builder notificationBuilder;
            notificationBuilder = new NotificationCompat.Builder(activity, getResources().getString(R.string.app_name))
                    .setAutoCancel(true)
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setColor(getResources().getColor(R.color.black))
                    .setLargeIcon(BitmapFactory.decodeResource(activity.getResources(),
                            R.mipmap.ic_launcher_round))
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setContentTitle("Copied text")
                    .setContentText(Text)
                    .setChannelId(getResources().getString(R.string.app_name))
                    .setFullScreenIntent(pendingIntent, true);
            notificationManager.notify(1, notificationBuilder.build());
        }
    }

    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(activity, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions((Activity) (activity),
                    listPermissionsNeeded.toArray(new
                            String[listPermissionsNeeded.size()]), 0);
            return false;
        } else {
            if (0 == 102) {
                callWhatsappActivity();
            } else if (0 == 105) {
                callGalleryActivity();
            }

        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 102) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callWhatsappActivity();
            } else {
            }
            return;
        } else if (requestCode == 105) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callGalleryActivity();
            } else {

            }
            return;
        }

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

}




