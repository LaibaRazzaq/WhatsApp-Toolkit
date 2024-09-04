package com.cd.statussaver.activity;

import static com.cd.statussaver.util.Utils.setToast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.provider.DocumentsContract;
import android.provider.Settings;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.cd.statussaver.R;
import com.cd.statussaver.model.ImageResult;
import com.cd.statussaver.util.Admenager;
import com.cd.statussaver.util.Utils;

import java.io.File;
import java.util.Objects;


public class WhatsAppCleaner extends AppCompatActivity  implements View.OnClickListener{
    WhatsAppCleaner activity;
    RelativeLayout imagesLayout;

      LinearLayout videosLayout;

    RelativeLayout documetsLayout;
 SharedPreferences sharedPreferences;

 SharedPreferences.Editor editor;
    RelativeLayout audioLayout;
    Admenager admenager;

    RelativeLayout wallpaperLayout;

    RelativeLayout voiceLayout;

    RelativeLayout giftLayout;

    //progresssBar binding

    ProgressBar imagesProgress;

    ProgressBar videoProgress;

    ProgressBar documentProgress;

    ProgressBar audioProgress;

    ProgressBar wallpaperProgress;

    ProgressBar voiceProgress;

    ProgressBar giftProgress;


    //Images binding


    ImageView imagesDone;


    ImageView videosDone;

    ImageView documentDone;


    ImageView audioDone;

    ImageView wallpaperDone;

    ImageView voiceDone;

    ImageView giftDone;

    String from;

    TextView storage,Tfiles;
    TextView imgSize, VdSize, audSize, tdocSize,voiceSize,tgifSize,twallSize;
    TextView imageSizeMb, VdSizeMb, audSizeMb, tdocSizeMb,viceSizeMb,tgifSizeMb,twallSizeMb;

    int imgCur = 0,vdCur = 0,audCur = 0,voiCur = 0,docCur = 0,wallCur = 0,gifCur = 0, totalFiles = 0,imgSizeMb=0,videoSizeMb=0,documentSizeMb=0,audioSizeMb=0,walpaperSizeMb=0,voiceSizeMb=0,giftSizeMb=0;

    int trackCounter=0;
    int totalTask= 7;
    Long checkGB = 0L;
    LinearLayout layout;
    TextView tvAllowAccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whats_app_cleaner);
        init();
        admenager = new Admenager(this, this);
        admenager.load_InterstitialAd(getString(R.string.admobe_intertesial_whatsAppCleaner));
        Objects.requireNonNull(getSupportActionBar()).setTitle("Cleaner");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        Objects.requireNonNull(getSupportActionBar()).setElevation(0);
        from = getIntent().getStringExtra("from");
        sharedPreferences = getSharedPreferences("Folder", MODE_PRIVATE);


        activity = this;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (from.equals("whatsapp")) {
                boolean whatsappPermissiongranted = sharedPreferences.getBoolean("isWhatsAppFolderPermisstionGranted", false);



                if (!whatsappPermissiongranted) {
                    layout.setVisibility(View.GONE);
                    tvAllowAccess.setVisibility(View.VISIBLE);

                } else {
                    tvAllowAccess.setVisibility(View.GONE);
                    layout.setVisibility(View.VISIBLE);
                    new findImages().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    new sentDocument().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    new sentVideo().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    new sentAudio().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    new findWallpaper().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    new voiceNotes().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    new sentGif().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


                }
            } else if (from.equals("whatsappBussiness")) {
                boolean whatsappBusinessPermissiongranted = sharedPreferences.getBoolean("isWhatsAppBusinessFolderPermisstionGranted", false);
                if (!whatsappBusinessPermissiongranted) {
                    layout.setVisibility(View.GONE);
                    tvAllowAccess.setVisibility(View.VISIBLE);
                } else {
                    layout.setVisibility(View.VISIBLE);
                    tvAllowAccess.setVisibility(View.GONE);
                    new findImages().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    new sentDocument().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    new sentVideo().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    new sentAudio().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    new findWallpaper().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    new voiceNotes().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    new sentGif().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }

            }
        }else {
            if (ActivityCompat.checkSelfPermission(WhatsAppCleaner.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                layout.setVisibility(View.VISIBLE);
                tvAllowAccess.setVisibility(View.GONE);
                new findImages().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                new sentDocument().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                new sentVideo().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                new sentAudio().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                new findWallpaper().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                new voiceNotes().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                new sentGif().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }else {
                layout.setVisibility(View.GONE);
                tvAllowAccess.setVisibility(View.VISIBLE);
            }

        }

        tvAllowAccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (from.equals("whatsapp")) {
                    requestPeemision();
                }else {
                    requestPermistionForWhatsappBussines();
                }
            }
        });



    }

    private void init() {
        layout=findViewById(R.id.l);
        tvAllowAccess=findViewById(R.id.tvAllowAccess);

        imagesProgress=findViewById(R.id.imageProgress);
        videoProgress=findViewById(R.id.videoProgress);
        documentProgress=findViewById(R.id.documentProgress);
        audioProgress=findViewById(R.id.audioProgress);
        voiceProgress=findViewById(R.id.voiceProgress);
        wallpaperProgress=findViewById(R.id.wallpaperProgress);
        giftProgress=findViewById(R.id.giftProgress);


        imageSizeMb=findViewById(R.id.imagesSizeMb);
        VdSizeMb=findViewById(R.id.VdSizeMb);
        tdocSizeMb=findViewById(R.id.docSizeMb);
        audSizeMb=findViewById(R.id.audSizeMb);
        viceSizeMb=findViewById(R.id.voiceSizeMb);
        twallSizeMb=findViewById(R.id.wallSizeMb);
        tgifSizeMb=findViewById(R.id.gifSizeMb);

        imagesDone=findViewById(R.id.imagesDone);
        videosDone=findViewById(R.id.videosDone);
        documentDone=findViewById(R.id.documentDone);
        audioDone=findViewById(R.id.audioDone);
        voiceDone=findViewById(R.id.voiceDone);
        wallpaperDone=findViewById(R.id.wallpaperDone);
        giftDone=findViewById(R.id.giftDone);
        storage = findViewById(R.id.storage);
        Tfiles = findViewById(R.id.SizeFile);

        imgSize = findViewById(R.id.imagesSize);
        VdSize = findViewById(R.id.VdSize);
        tdocSize = findViewById(R.id.docSize);
        audSize = findViewById(R.id.audSize);
        voiceSize = findViewById(R.id.voiceSize);
        twallSize = findViewById(R.id.wallSize);
        tgifSize = findViewById(R.id.gifSize);



        imagesLayout=findViewById(R.id.imagesLayout);
        videosLayout=findViewById(R.id.videosLayout);
        documetsLayout=findViewById(R.id.documentLayout);
        audioLayout=findViewById(R.id.audioLayout);
        voiceLayout=findViewById(R.id.voiceLayout);
        wallpaperLayout=findViewById(R.id.wallpaperLayout);
        giftLayout=findViewById(R.id.giftLayout);

    }

    private void requestPeemision() {


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            StorageManager sm = (StorageManager) getSystemService(Context.STORAGE_SERVICE);
           Intent intent = sm.getPrimaryStorageVolume().createOpenDocumentTreeIntent();
            String startDir = "Android%2Fmedia%2Fcom.whatsapp%2FWhatsApp";
            Uri uri = intent.getParcelableExtra("android.provider.extra.INITIAL_URI");
            String scheme = null;
            if (uri != null) {
                scheme = uri.toString();
                scheme = scheme.replace("/root/", "/document/");
                scheme += "%3A" + startDir;
                uri = Uri.parse(scheme);
                intent.putExtra("android.provider.extra.INITIAL_URI", uri);

                startActivityForResult(intent, 2001);
            }
        }else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(WhatsAppCleaner.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)|| ActivityCompat.shouldShowRequestPermissionRationale(WhatsAppCleaner.this,Manifest.permission.READ_EXTERNAL_STORAGE)) {
                AlertDialog.Builder alertDialog=new AlertDialog.Builder(WhatsAppCleaner.this);
                alertDialog.setTitle("Permission Required");
                alertDialog.setMessage("We need this permission to access photo.");

                alertDialog.setPositiveButton("Setting", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package",getPackageName(), null);
                        intent.setData(uri);


                        startActivityForResult(intent,122);


                    }
                });
                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setToast(WhatsAppCleaner.this,"Permission required");
                        dialog.dismiss();
                        finish();

                    }
                });

                alertDialog.create().show();



            }else {
                ActivityCompat.requestPermissions(WhatsAppCleaner.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},21);

            }
        }





    }

    private void requestPermistionForWhatsappBussines(){



        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            StorageManager sm = (StorageManager) getSystemService(Context.STORAGE_SERVICE);
            Intent intent = sm.getPrimaryStorageVolume().createOpenDocumentTreeIntent();
            String startDir = "Android%2Fmedia%2Fcom.whatsapp.w4b%2FWhatsApp Business";
            Uri uri = intent.getParcelableExtra("android.provider.extra.INITIAL_URI");
            String scheme = null;
            if (uri != null) {
                scheme = uri.toString();
                scheme = scheme.replace("/root/", "/document/");
                scheme += "%3A" + startDir;
                uri = Uri.parse(scheme);
                intent.putExtra("android.provider.extra.INITIAL_URI", uri);

                startActivityForResult(intent, 2002);
        }


        }else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(WhatsAppCleaner.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)|| ActivityCompat.shouldShowRequestPermissionRationale(WhatsAppCleaner.this,Manifest.permission.READ_EXTERNAL_STORAGE)) {
                AlertDialog.Builder alertDialog=new AlertDialog.Builder(WhatsAppCleaner.this);
                alertDialog.setTitle("Permission Required");
                alertDialog.setMessage("We need this permission to access photo.");

                alertDialog.setPositiveButton("Setting", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package",getPackageName(), null);
                        intent.setData(uri);


                        startActivityForResult(intent,122);


                    }
                });
                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setToast(WhatsAppCleaner.this,"Permission required");
                        dialog.dismiss();
                        finish();

                    }
                });

                alertDialog.create().show();



            }else {
                ActivityCompat.requestPermissions(WhatsAppCleaner.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},21);

            }
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2001 && resultCode == RESULT_OK) {
            Uri treeUri;
            if (data != null) {

                treeUri = data.getData();
                Log.d("uri", "hh" + treeUri);
                if (treeUri != null && treeUri.toString().endsWith("WhatsApp")) {
                    editor = sharedPreferences.edit();
                    editor.putString("WhatsAppFolderUri", treeUri.toString());
                    editor.putBoolean("isWhatsAppFolderPermisstionGranted", true);
                    editor.apply();

                    getContentResolver().takePersistableUriPermission(treeUri,
                            Intent.FLAG_GRANT_READ_URI_PERMISSION |
                                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    layout.setVisibility(View.VISIBLE);
                    tvAllowAccess.setVisibility(View.GONE);
                    new findImages().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    new sentDocument().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    new sentVideo().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    new sentAudio().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    new findWallpaper().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    new voiceNotes().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    new sentGif().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


                }else {
                    layout.setVisibility(View.GONE);
                    tvAllowAccess.setVisibility(View.VISIBLE);
                    Utils.infoDialog(activity,activity.getResources().getString(R.string.wrong_folder), activity.getResources().getString(R.string.selected_wrong_folder));
                }


            }else {
                layout.setVisibility(View.GONE);
                tvAllowAccess.setVisibility(View.VISIBLE);
                Utils.infoDialog(activity,activity.getResources().getString(R.string.wrong_folder), activity.getResources().getString(R.string.selected_wrong_folder));

                      }
        } else if (requestCode==2002 && resultCode==RESULT_OK) {
            if (data!=null){
                layout.setVisibility(View.VISIBLE);
                tvAllowAccess.setVisibility(View.GONE);
                Uri businessUri=data.getData();
                Log.d("businessUri","uri"+businessUri);
                if (businessUri != null && businessUri.toString().endsWith("Business")) {
                    editor = sharedPreferences.edit();
                    editor.putString("WhatsAppBussinesFolderUri", businessUri.toString());
                    editor.putBoolean("isWhatsAppBusinessFolderPermisstionGranted", true);
                    editor.apply();
                    getContentResolver().takePersistableUriPermission(businessUri,
                            Intent.FLAG_GRANT_READ_URI_PERMISSION |
                                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    new findImages().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    new sentDocument().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    new sentVideo().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    new sentAudio().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    new findWallpaper().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    new voiceNotes().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    new sentGif().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                }else {
                    layout.setVisibility(View.GONE);
                    tvAllowAccess.setVisibility(View.VISIBLE);
                    Utils.infoDialog(activity,activity.getResources().getString(R.string.wrong_folder), activity.getResources().getString(R.string.selected_wrong_bussiness_folder));
                }
            }else {
                layout.setVisibility(View.GONE);
                tvAllowAccess.setVisibility(View.VISIBLE);
                Utils.infoDialog(activity,activity.getResources().getString(R.string.wrong_folder), activity.getResources().getString(R.string.selected_wrong_bussiness_folder));


            }
        } else if (requestCode==122) {
            if (ActivityCompat.checkSelfPermission(WhatsAppCleaner.this,Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED&&ActivityCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED) {

                layout.setVisibility(View.VISIBLE);
                tvAllowAccess.setVisibility(View.GONE);
                new findImages().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                new sentDocument().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                new sentVideo().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                new sentAudio().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                new findWallpaper().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                new voiceNotes().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                new sentGif().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                layout.setVisibility(View.GONE);
                setToast(WhatsAppCleaner.this,"Permission required");
                tvAllowAccess.setVisibility(View.VISIBLE);
            }
        }
    }



    private  class findImages extends AsyncTask<String , String, String> {

        @Override
        protected String doInBackground(String... strings) {


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                Uri treeUri = null;
                String uriKey = (from.equals("whatsapp")) ? "WhatsAppFolderUri" : "WhatsAppBussinesFolderUri";
                String uriString = sharedPreferences.getString(uriKey, null);

                if (uriString != null && !uriString.isEmpty()) {
                    treeUri = Uri.parse(uriString);
                }

                if (treeUri != null) {
                    getContentResolver().takePersistableUriPermission(
                            treeUri,
                            Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                    );
                }

                String parentPath=(from.equals("whatsapp")) ? "/Media/WhatsApp Images" : "/Media/WhatsApp Business Images";


               ImageResult.ImageProcessingResult imageProcessingResult= ImageResult.processImages(WhatsAppCleaner.this,treeUri,parentPath,"",false);
               ImageResult.ImageProcessingResult imageProcessingResult1= ImageResult.processImages(WhatsAppCleaner.this,treeUri,parentPath,"Sent",true);

                imgSizeMb+=imageProcessingResult.getImgSizeMb()+imageProcessingResult1.getImgSizeMb();
                checkGB+=imageProcessingResult.getImgSizeMb()+imageProcessingResult1.getImgSizeMb();
                imgCur+=imageProcessingResult.getImgCur()+imageProcessingResult1.getImgCur();



            } else {


                String parentPath = (from.equals("whatsapp")) ? "WhatsApp/Media/" : "WhatsApp Business/Media/";
                String childPath = (from.equals("whatsapp")) ? "WhatsApp Images" : "WhatsApp Business Images";
                File[] recvList = Utils.getWhatsAppPath(parentPath, childPath).listFiles();
                File[] senList = Utils.getWhatsAppPath(parentPath, childPath + "/Sent").listFiles();

                ImageResult.ImageProcessingResult imageProcessingResult= ImageResult.below10HandleImage(recvList, false);
               ImageResult.ImageProcessingResult imageProcessingResult1= ImageResult.below10HandleImage(senList, true);

                imgSizeMb+=imageProcessingResult.imgSizeMb+imageProcessingResult1.imgSizeMb;
                checkGB+=imageProcessingResult.imgSizeMb+imageProcessingResult1.imgSizeMb;
                imgCur+=imageProcessingResult.imgCur+imageProcessingResult1.imgCur;






            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (!isFinishing() ){
                trackCounter++;
                checkAllTaskComplete();
            }

        }
    }






    private class sentDocument extends AsyncTask<String, String,String>{

        @Override
        protected String doInBackground(String... strings) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    Uri treeUri = null;
                    String uriKey = (from.equals("whatsapp")) ? "WhatsAppFolderUri" : "WhatsAppBussinesFolderUri";
                    String uriString = sharedPreferences.getString(uriKey, null);

                    if (uriString != null && !uriString.isEmpty()) {
                        treeUri = Uri.parse(uriString);
                    }

                    if (treeUri != null) {
                        getContentResolver().takePersistableUriPermission(
                                treeUri,
                                Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                        );
                    }

                    String parentPath = (from.equals("whatsapp")) ? "/Media/WhatsApp Documents" : "/Media/WhatsApp Business Documents";

                    ImageResult.ImageProcessingResult imageProcessingResult= ImageResult.processImages(WhatsAppCleaner.this,treeUri,parentPath,"",false);
                    ImageResult.ImageProcessingResult imageProcessingResult1= ImageResult.processImages(WhatsAppCleaner.this,treeUri,parentPath,"Sent",true);

                    documentSizeMb+=imageProcessingResult.getImgSizeMb()+imageProcessingResult1.getImgSizeMb();
                    checkGB+=imageProcessingResult.getImgSizeMb()+imageProcessingResult1.getImgSizeMb();
                    docCur+=imageProcessingResult.getImgCur()+imageProcessingResult1.getImgCur();



                } else {


                    String parentPath = (from.equals("whatsapp")) ? "WhatsApp/Media/" : "WhatsApp Business/Media/";
                    String childPath = (from.equals("whatsapp")) ? "WhatsApp Documents" : "WhatsApp Business Documents";
                    File[] recvList = Utils.getWhatsAppPath(parentPath, childPath).listFiles();
                    File[] senList = Utils.getWhatsAppPath(parentPath, childPath + "/Sent").listFiles();
                    ImageResult.ImageProcessingResult imageProcessingResult= ImageResult.below10HandleImage(recvList, false);
                    ImageResult.ImageProcessingResult imageProcessingResult1= ImageResult.below10HandleImage(senList, true);

                    documentSizeMb+=imageProcessingResult.imgSizeMb+imageProcessingResult1.imgSizeMb;
                    checkGB+=imageProcessingResult.imgSizeMb+imageProcessingResult1.imgSizeMb;
                    docCur+=imageProcessingResult.imgCur+imageProcessingResult1.imgCur;






                }

            }catch (Exception e){
                e.printStackTrace();
            }






            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (!isFinishing() ){
                trackCounter++;
                checkAllTaskComplete();
            }
        }
    }

    private class sentVideo extends AsyncTask<String ,String ,String>{

        @Override
        protected String doInBackground(String... strings) {

            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    Uri treeUri = null;
                    String uriKey = (from.equals("whatsapp")) ? "WhatsAppFolderUri" : "WhatsAppBussinesFolderUri";
                    String uriString = sharedPreferences.getString(uriKey, null);

                    if (uriString != null && !uriString.isEmpty()) {
                        treeUri = Uri.parse(uriString);
                    }

                    if (treeUri != null) {
                        getContentResolver().takePersistableUriPermission(
                                treeUri,
                                Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                        );
                    }

                    String parentPath=(from.equals("whatsapp")) ? "/Media/WhatsApp Video" : "/Media/WhatsApp Business Video";

                    ImageResult.ImageProcessingResult imageProcessingResult= ImageResult.processImages(WhatsAppCleaner.this,treeUri,parentPath,"",false);
                    ImageResult.ImageProcessingResult imageProcessingResult1= ImageResult.processImages(WhatsAppCleaner.this,treeUri,parentPath,"Sent",true);

                    videoSizeMb+=imageProcessingResult.getImgSizeMb()+imageProcessingResult1.getImgSizeMb();
                    checkGB+=imageProcessingResult.getImgSizeMb()+imageProcessingResult1.getImgSizeMb();
                    vdCur+=imageProcessingResult.getImgCur()+imageProcessingResult1.getImgCur();



                } else {


                    String parentPath = (from.equals("whatsapp")) ? "WhatsApp/Media/" : "WhatsApp Business/Media/";
                    String childPath = (from.equals("whatsapp")) ? "WhatsApp Video" : "WhatsApp Business Video";
                    File[] recvList = Utils.getWhatsAppPath(parentPath, childPath).listFiles();
                    File[] senList = Utils.getWhatsAppPath(parentPath, childPath + "/Sent").listFiles();

                    ImageResult.ImageProcessingResult imageProcessingResult= ImageResult.below10HandleImage(recvList, false);
                    ImageResult.ImageProcessingResult imageProcessingResult1= ImageResult.below10HandleImage(senList, true);

                    videoSizeMb+=imageProcessingResult.imgSizeMb+imageProcessingResult1.imgSizeMb;
                    checkGB+=imageProcessingResult.imgSizeMb+imageProcessingResult1.imgSizeMb;
                    vdCur+=imageProcessingResult.imgCur+imageProcessingResult1.imgCur;






                }
            }catch (Exception e){
                e.printStackTrace();
            }











            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (!isFinishing() ){
                trackCounter++;
                checkAllTaskComplete();
            }

        }
    }


    private class sentAudio extends AsyncTask<String ,String ,String>{

        @Override
        protected String doInBackground(String... strings) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    Uri treeUri = null;
                    String uriKey = (from.equals("whatsapp")) ? "WhatsAppFolderUri" : "WhatsAppBussinesFolderUri";
                    String uriString = sharedPreferences.getString(uriKey, null);

                    if (uriString != null && !uriString.isEmpty()) {
                        treeUri = Uri.parse(uriString);
                    }

                    if (treeUri != null) {
                        getContentResolver().takePersistableUriPermission(
                                treeUri,
                                Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                        );
                    }

                    String parentPath = (from.equals("whatsapp")) ? "/Media/WhatsApp Audio" : "/Media/WhatsApp Business Audio";

                    ImageResult.ImageProcessingResult imageProcessingResult= ImageResult.processImages(WhatsAppCleaner.this,treeUri,parentPath,"",false);
                    ImageResult.ImageProcessingResult imageProcessingResult1= ImageResult.processImages(WhatsAppCleaner.this,treeUri,parentPath,"Sent",true);

                    audioSizeMb+=imageProcessingResult.getImgSizeMb()+imageProcessingResult1.getImgSizeMb();
                    checkGB+=imageProcessingResult.getImgSizeMb()+imageProcessingResult1.getImgSizeMb();
                    audCur+=imageProcessingResult.getImgCur()+imageProcessingResult1.getImgCur();



                } else {

                    String parentPath = (from.equals("whatsapp")) ? "WhatsApp/Media/" : "WhatsApp Business/Media/";
                    String childPath = (from.equals("whatsapp")) ? "WhatsApp Audio" : "WhatsApp Business Audio";
                    File[] recvList = Utils.getWhatsAppPath(parentPath, childPath).listFiles();
                    File[] senList = Utils.getWhatsAppPath(parentPath, childPath + "/Sent").listFiles();
                    ImageResult.ImageProcessingResult imageProcessingResult= ImageResult.below10HandleImage(recvList, false);
                    ImageResult.ImageProcessingResult imageProcessingResult1= ImageResult.below10HandleImage(senList, true);

                    audioSizeMb+=imageProcessingResult.imgSizeMb+imageProcessingResult1.imgSizeMb;
                    checkGB+=imageProcessingResult.imgSizeMb+imageProcessingResult1.imgSizeMb;
                    audCur+=imageProcessingResult.imgCur+imageProcessingResult1.imgCur;






                }
            }catch (Exception e){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (!isFinishing() ){
                trackCounter++;
                checkAllTaskComplete();
            }
        }
    }


    private class voiceNotes extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... strings) {


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                Uri treeUri = null;
                String uriString = null;
                if (from.equals("whatsapp")) {
                    uriString = sharedPreferences.getString("WhatsAppFolderUri", null);

                } else if (from.equals("whatsappBussiness")) {
                    uriString = sharedPreferences.getString("WhatsAppBussinesFolderUri", null);
                }

                if (uriString != null && !uriString.isEmpty()) {
                    treeUri = Uri.parse(uriString);
                }

                String path = null;
                if (from.equals("whatsapp")) {
                    path = "/Media/WhatsApp Voice Notes";
                } else if (from.equals("whatsappBussiness")) {
                    path = "/Media/WhatsApp Business Voice Notes";
                }

                //activity.getContentResolver().getPersistedUriPermissions().get(0).getUri();


                if (treeUri != null) {
                    getContentResolver().takePersistableUriPermission(
                            treeUri,
                            Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                    );
                }


                String  parentDocumentId = null;
                if (from.equals("whatsapp")) {
                    parentDocumentId = DocumentsContract.getTreeDocumentId(treeUri) + "/Media/WhatsApp Voice Notes";
                } else if (from.equals("whatsappBussiness")) {
                    parentDocumentId = DocumentsContract.getTreeDocumentId(treeUri) + "/Media/WhatsApp Business Voice Notes";
                }
                iterateChildDocuments(treeUri, parentDocumentId);
            }else {
                String parentPath = (from.equals("whatsapp")) ? "WhatsApp/Media/" : "WhatsApp Business/Media/";
                String childPath = (from.equals("whatsapp")) ? "WhatsApp Voice Notes" : "WhatsApp Business Voice Notes";
                File[] recvList1 = Utils.getWhatsAppPath(parentPath, childPath).listFiles();

                //  Utils.below10HandleImage(recvList1, recvList, false);
              ImageResult.ImageProcessingResult imageProcessingResult= ImageResult.below10VoiceNote(recvList1, false);

                voiCur+=imageProcessingResult.imgCur;
                checkGB+=imageProcessingResult.imgSizeMb;
                voiceSizeMb+=imageProcessingResult.imgSizeMb;

            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (!isFinishing() ){
                trackCounter++;
                checkAllTaskComplete();
            }
        }
    }

    private void iterateChildDocuments(Uri treeUri, String parentDocumentId) {
        String[] projection = new String[] {
                DocumentsContract.Document.COLUMN_DOCUMENT_ID,
                DocumentsContract.Document.COLUMN_DISPLAY_NAME,
                DocumentsContract.Document.COLUMN_MIME_TYPE,
                DocumentsContract.Document.COLUMN_LAST_MODIFIED,
                DocumentsContract.Document.COLUMN_SIZE
        };
        String[] selectionArgs = new String[] { parentDocumentId };

        try (Cursor cursor = getContentResolver().query(DocumentsContract.buildChildDocumentsUriUsingTree(treeUri, parentDocumentId), projection, null, selectionArgs, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    @SuppressLint("Range") String documentId = cursor.getString(cursor.getColumnIndex(DocumentsContract.Document.COLUMN_DOCUMENT_ID));
                    @SuppressLint("Range") String displayName = cursor.getString(cursor.getColumnIndex(DocumentsContract.Document.COLUMN_DISPLAY_NAME));
                    @SuppressLint("Range") String mimeType = cursor.getString(cursor.getColumnIndex(DocumentsContract.Document.COLUMN_MIME_TYPE));
                    @SuppressLint("Range") long lastModified = cursor.getLong(cursor.getColumnIndex(DocumentsContract.Document.COLUMN_LAST_MODIFIED));
                    @SuppressLint("Range") long fileSize = cursor.getLong(cursor.getColumnIndex(DocumentsContract.Document.COLUMN_SIZE));
                    checkGB += fileSize;

                    Uri documentUri = DocumentsContract.buildDocumentUriUsingTree(treeUri, documentId);

                    if (DocumentsContract.Document.MIME_TYPE_DIR.equals(mimeType)) {
                        // Child document is a directory, call the function recursively with the child document's URI
                        iterateChildDocuments(treeUri, documentId);
                    } else if (mimeType.startsWith("audio/")) {
                        // Child document is an audio file, process it as required
                        if (!displayName.contains(".nomedia")) {

                            voiCur++;
                            voiceSizeMb+=fileSize;

                        }
                    }
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    private  class findWallpaper extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... strings) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    Uri treeUri = null;
                    String uriKey = (from.equals("whatsapp")) ? "WhatsAppFolderUri" : "WhatsAppBussinesFolderUri";
                    String uriString = sharedPreferences.getString(uriKey, null);

                    if (uriString != null && !uriString.isEmpty()) {
                        treeUri = Uri.parse(uriString);
                    }

                    if (treeUri != null) {
                        getContentResolver().takePersistableUriPermission(
                                treeUri,
                                Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                        );
                    }

                    String parentPath = "/Media/WallPaper";
                    ImageResult.ImageProcessingResult imageProcessingResult= ImageResult.processImages(WhatsAppCleaner.this,treeUri,parentPath,"",false);
                 //   ImageResult.ImageProcessingResult imageProcessingResult1= ImageResult.processImages(WhatsAppCleaner.this,treeUri,parentPath,"Sent",true);

                    walpaperSizeMb+=imageProcessingResult.getImgSizeMb();
                    checkGB+=imageProcessingResult.getImgSizeMb();
                    wallCur+=imageProcessingResult.getImgCur();



                } else {


                    String parentPath = (from.equals("whatsapp")) ? "WhatsApp/Media/" : "WhatsApp Business/Media/";
                    String childPath = "WallPaper";
                    File[] recvList = Utils.getWhatsAppPath(parentPath, childPath).listFiles();
                    ImageResult.ImageProcessingResult imageProcessingResult= ImageResult.below10HandleImage(recvList, false);

                    walpaperSizeMb+=imageProcessingResult.imgSizeMb;
                    checkGB+=imageProcessingResult.imgSizeMb;
                    wallCur+=imageProcessingResult.imgCur;






                }

            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (!isFinishing() ){
                trackCounter++;
                checkAllTaskComplete();
            }
        }
    }

    private class sentGif extends AsyncTask<String ,String ,String>{

        @Override
        protected String doInBackground(String... strings) {


            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    Uri treeUri = null;
                    String uriKey = (from.equals("whatsapp")) ? "WhatsAppFolderUri" : "WhatsAppBussinesFolderUri";
                    String uriString = sharedPreferences.getString(uriKey, null);

                    if (uriString != null && !uriString.isEmpty()) {
                        treeUri = Uri.parse(uriString);
                    }

                    if (treeUri != null) {
                        getContentResolver().takePersistableUriPermission(
                                treeUri,
                                Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                        );
                    }

                    String parentPath=(from.equals("whatsapp")) ? "/Media/WhatsApp Animated Gifs" : "/Media/WhatsApp Business Animated Gifs";

                    ImageResult.ImageProcessingResult imageProcessingResult= ImageResult.processImages(WhatsAppCleaner.this,treeUri,parentPath,"",false);
                    ImageResult.ImageProcessingResult imageProcessingResult1= ImageResult.processImages(WhatsAppCleaner.this,treeUri,parentPath,"Sent",true);

                    giftSizeMb+=imageProcessingResult.getImgSizeMb()+imageProcessingResult1.getImgSizeMb();
                    checkGB+=imageProcessingResult.getImgSizeMb()+imageProcessingResult1.getImgSizeMb();
                    gifCur+=imageProcessingResult.getImgCur()+imageProcessingResult1.getImgCur();



                } else {

                    String parentPath = (from.equals("whatsapp")) ? "WhatsApp/Media/" : "WhatsApp Business/Media/";
                    String childPath = (from.equals("whatsapp")) ? "WhatsApp Animated Gifs" : "WhatsApp Business Animated Gifs";
                    File[] recvList = Utils.getWhatsAppPath(parentPath, childPath).listFiles();
                    File[] senList = Utils.getWhatsAppPath(parentPath, childPath + "/Sent").listFiles();
                    ImageResult.ImageProcessingResult imageProcessingResult= ImageResult.below10HandleImage(recvList, false);
                    ImageResult.ImageProcessingResult imageProcessingResult1= ImageResult.below10HandleImage(senList, true);

                    giftSizeMb+=imageProcessingResult.imgSizeMb+imageProcessingResult1.imgSizeMb;
                    checkGB+=imageProcessingResult.imgSizeMb+imageProcessingResult1.imgSizeMb;
                    gifCur+=imageProcessingResult.imgCur+imageProcessingResult1.imgCur;






                }
            }catch (Exception e){
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (!isFinishing() ){
                trackCounter++;
                checkAllTaskComplete();
            }
        }
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        int viewId = v.getId();

        if (viewId == R.id.imagesLayout) {
            Intent intent = new Intent(WhatsAppCleaner.this, WhatsAppImages.class);
            intent.putExtra("from", from);
            startActivity(intent);
        } else if (viewId == R.id.videosLayout) {
            Intent intent1 = new Intent(WhatsAppCleaner.this, WhatsAppVideos.class);
            intent1.putExtra("from", from);
            startActivity(intent1);
        } else if (viewId == R.id.audioLayout) {
            Intent intent2 = new Intent(WhatsAppCleaner.this, WhatsAppAudio.class);
            intent2.putExtra("from", from);
            startActivity(intent2);
        } else if (viewId == R.id.voiceLayout) {
            Intent intent3 = new Intent(WhatsAppCleaner.this, WhatsappVoice.class);
            intent3.putExtra("from", from);
            startActivity(intent3);
        } else if (viewId == R.id.wallpaperLayout) {
            Intent intent4 = new Intent(WhatsAppCleaner.this, WhatsappWallpaper.class);
            intent4.putExtra("from", from);
            startActivity(intent4);
        } else if (viewId == R.id.documentLayout) {
            Intent intent5 = new Intent(WhatsAppCleaner.this, WhatsappDoc.class);
            intent5.putExtra("from", from);
            startActivity(intent5);
        } else if (viewId == R.id.giftLayout) {
            Intent intent6 = new Intent(WhatsAppCleaner.this, WhatsAppGifs.class);
            intent6.putExtra("from", from);
            startActivity(intent6);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                if (requestCode==21){
                    if (grantResults.length>0){
                        boolean write= grantResults[0] == PackageManager.PERMISSION_GRANTED;
                        boolean read= grantResults[1] == PackageManager.PERMISSION_GRANTED;
                        if (read&&write){
                            layout.setVisibility(View.VISIBLE);
                            tvAllowAccess.setVisibility(View.GONE);
                            new findImages().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                            new sentDocument().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                            new sentVideo().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                            new sentAudio().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                            new findWallpaper().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                            new voiceNotes().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                            new sentGif().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        }else {
                            layout.setVisibility(View.GONE);
                            setToast(WhatsAppCleaner.this,"Permission required");

                            tvAllowAccess.setVisibility(View.VISIBLE);

                        }

                    }else {
                        layout.setVisibility(View.GONE);
                        setToast(WhatsAppCleaner.this,"Permission required");

                        tvAllowAccess.setVisibility(View.VISIBLE);                    }
                }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        admenager.showadmobeInterstitialAd();
    }
    private void checkAllTaskComplete() {
        if (trackCounter==totalTask){
            imagesProgress.setVisibility(View.GONE);
            imagesDone.setVisibility(View.VISIBLE);
            videoProgress.setVisibility(View.GONE);
            videosDone.setVisibility(View.VISIBLE);
            documentProgress.setVisibility(View.GONE);
            documentDone.setVisibility(View.VISIBLE);
            audioProgress.setVisibility(View.GONE);
            audioDone.setVisibility(View.VISIBLE);
            voiceProgress.setVisibility(View.GONE);
            voiceDone.setVisibility(View.VISIBLE);
            wallpaperProgress.setVisibility(View.GONE);
            wallpaperDone.setVisibility(View.VISIBLE);
            giftProgress.setVisibility(View.GONE);
            giftDone.setVisibility(View.VISIBLE);

            imgSize.setText(getFileCountString(imgCur));
            VdSize.setText(getFileCountString(vdCur));
            audSize.setText(getFileCountString(audCur));
            tdocSize.setText(getFileCountString(docCur));
            voiceSize.setText(getFileCountString(voiCur));
            twallSize.setText(getFileCountString(wallCur));
            tgifSize.setText(getFileCountString(gifCur));



            imgSize.setVisibility(View.VISIBLE);
            VdSize.setVisibility(View.VISIBLE);
            tdocSize.setVisibility(View.VISIBLE);
            audSize.setVisibility(View.VISIBLE);
            voiceSize.setVisibility(View.VISIBLE);
            twallSize.setVisibility(View.VISIBLE);
            tgifSize.setVisibility(View.VISIBLE);

            imagesLayout.setOnClickListener(this);
            videosLayout.setOnClickListener(this);
            audioLayout.setOnClickListener(this);
            voiceLayout.setOnClickListener(this);
            wallpaperLayout.setOnClickListener(this);
            documetsLayout.setOnClickListener(this);
            giftLayout.setOnClickListener(this);

            VdSizeMb.setVisibility(View.VISIBLE);
            imageSizeMb.setVisibility(View.VISIBLE);
            twallSizeMb.setVisibility(View.VISIBLE);
            tgifSizeMb.setVisibility(View.VISIBLE);
            tdocSizeMb.setVisibility(View.VISIBLE);
            audSizeMb.setVisibility(View.VISIBLE);
            viceSizeMb.setVisibility(View.VISIBLE);


            imageSizeMb.setText("File Size:"+Formatter.formatFileSize(this,imgSizeMb));
            VdSizeMb.setText("File Size:"+Formatter.formatFileSize(this,videoSizeMb));
            tdocSizeMb.setText("File Size:"+Formatter.formatFileSize(this,documentSizeMb));
            audSizeMb.setText("File Size:"+Formatter.formatFileSize(this,audioSizeMb));
            viceSizeMb.setText("File Size:"+Formatter.formatFileSize(this,voiceSizeMb));
            tgifSizeMb.setText("File Size:"+Formatter.formatFileSize(this,giftSizeMb));
            twallSizeMb.setText("File Size:"+Formatter.formatFileSize(this,walpaperSizeMb));
            String formatFileSize = Formatter.formatFileSize(getApplicationContext(), checkGB);
            storage.setText(formatFileSize);

            totalFiles = imgCur+vdCur+docCur+audCur+voiCur+wallCur+gifCur;

            Tfiles.setText(getFileCountString(totalFiles));

            Log.d("image",""+Formatter.formatFileSize(this,imgSizeMb));
            Log.d("image",""+Formatter.formatFileSize(this,videoSizeMb));
            Log.d("image",""+Formatter.formatFileSize(this,documentSizeMb));
            Log.d("image",""+Formatter.formatFileSize(this,voiceSizeMb));
            Log.d("image",""+Formatter.formatFileSize(this,audioSizeMb));
            Log.d("image",""+Formatter.formatFileSize(this,walpaperSizeMb));
            Log.d("image",""+Formatter.formatFileSize(this,giftSizeMb));




        }
    }

    private String  getFileCountString(int count) {
        if (count == 1) {
            return count + " file";
        } else if (count == 0) {
            return "Empty";
        } else {
            return count + " files";
        }
    }

}