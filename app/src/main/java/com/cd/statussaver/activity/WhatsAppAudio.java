package com.cd.statussaver.activity;

import static androidx.fragment.app.FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.provider.DocumentsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.cd.statussaver.R;
import com.cd.statussaver.adapter.AudioAdapter;
import com.cd.statussaver.databinding.DialogWhatsappPermissionBinding;
import com.cd.statussaver.fragment.recvAudFragment;
import com.cd.statussaver.fragment.sendAudFragment;
import com.cd.statussaver.util.Admenager;
import com.cd.statussaver.util.Utils;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WhatsAppAudio extends AppCompatActivity {

    ViewPager viewPager;
    TabLayout tabLayout;
    Gson gson;
    List<Uri> sendDocList;
    List<Uri>  recvDocList;

    TextView tvAllowAccess;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ProgressDialog progressDialog;
    WhatsAppAudio activity;
    AudioAdapter audioAdapter;
    String from;
    Admenager admenager;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whats_app_audio);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Audio");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        Objects.requireNonNull(getSupportActionBar()).setElevation(0);
        tvAllowAccess = findViewById(R.id.tvAllowAccess);
        admenager=new Admenager(this,this);
        admenager.load_InterstitialAd(getString(R.string.admobe_intertesial_whatsAppAudio));
        viewPager = findViewById(R.id.viewPagerImages);
        sharedPreferences = getSharedPreferences("Folder", MODE_PRIVATE);
        from = getIntent().getStringExtra("from");
        audioAdapter = new AudioAdapter();

        tabLayout = findViewById(R.id.tabs);

        activity = this;
        initViews();

    }


    @Override
    protected void onResume() {
        super.onResume();
        activity = this;
    }

    private void initViews() {



        sendDocList = new ArrayList<>();
        recvDocList = new ArrayList<>();
        initProgress();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            if (from.equals("whatsapp")) {
                boolean whatsappPermissiongranted = sharedPreferences.getBoolean("isWhatsAppFolderPermisstionGranted", false);
                if (whatsappPermissiongranted) {
                    progressDialog.show();
                    new LoadAllFiles().execute();
                    tvAllowAccess.setVisibility(View.GONE);
                } else {
                    tvAllowAccess.setVisibility(View.VISIBLE);
                }
            } else if (from.equals("whatsappBussiness")) {
                boolean whatsappBusinessPermissiongranted = sharedPreferences.getBoolean("isWhatsAppBusinessFolderPermisstionGranted", false);
                if (whatsappBusinessPermissiongranted) {
                    progressDialog.show();
                    new LoadAllFiles().execute();
                    tvAllowAccess.setVisibility(View.GONE);
                } else {
                    tvAllowAccess.setVisibility(View.VISIBLE);
                }

            }
        }else {
            if (ActivityCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED&&ActivityCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED){
                progressDialog.show();
                new LoadAllFiles().execute();
                tvAllowAccess.setVisibility(View.GONE);
            }else {
                tvAllowAccess.setVisibility(View.VISIBLE);
            }

        }

        tvAllowAccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(activity, R.style.SheetDialog);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                DialogWhatsappPermissionBinding dialogWhatsappPermissionBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.dialog_whatsapp_permission, null, false);
                dialog.setContentView(dialogWhatsappPermissionBinding.getRoot());
                dialogWhatsappPermissionBinding.tvAllow.setOnClickListener(v -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                        if (from.equals("whatsapp")) {
                            requestPeemision();
                        } else if (from.equals("whatsappBussiness")) {
                            requestPermistionForWhatsappBussines();
                        }
                    }else {
                        requestPeemision();
                    }

                    dialog.dismiss();
                });
                dialog.show();
            }
        });


    }

    private void requestPeemision() {
        StorageManager sm = (StorageManager) getSystemService(Context.STORAGE_SERVICE);
        Intent intent = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            intent = sm.getPrimaryStorageVolume().createOpenDocumentTreeIntent();
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
        }else{
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},21);

        }





    }

    private void requestPermistionForWhatsappBussines(){
        StorageManager sm = (StorageManager) getSystemService(Context.STORAGE_SERVICE);
        Intent intent = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            intent = sm.getPrimaryStorageVolume().createOpenDocumentTreeIntent();
        }else {
            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        }
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


    }



    public void initProgress(){
        progressDialog = new ProgressDialog(activity,R.style.AppCompatAlertDialogStyle);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Loading, Please wait...");
        progressDialog.setIndeterminate(true);
        progressDialog.setCanceledOnTouchOutside(false);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == 2001 && resultCode == RESULT_OK) {
                Uri dataUri = data.getData();

                if(dataUri!=null){
                    if (dataUri.toString().endsWith("WhatsApp")) {
                        editor=sharedPreferences.edit();
                        editor.putString("WhatsAppFolderUri", dataUri.toString());
                        editor.putBoolean("isWhatsAppFolderPermisstionGranted",true);
                        editor.apply();

                        getContentResolver().takePersistableUriPermission(dataUri,
                                Intent.FLAG_GRANT_READ_URI_PERMISSION |
                                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

                        new LoadAllFiles().execute();

                    }else{
                        Utils.infoDialog(activity,activity.getResources().getString(R.string.wrong_folder), activity.getResources().getString(R.string.selected_wrong_folder));
                    }
                }


            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    class LoadAllFiles extends AsyncTask<String, String, String> {
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

                    Utils.handleData(WhatsAppAudio.this, treeUri, parentPath, recvDocList, "", false);
                    Utils.handleData(WhatsAppAudio.this, treeUri, parentPath, sendDocList, "Sent", true);
                } else {


                    String parentPath = (from.equals("whatsapp")) ? "WhatsApp/Media/" : "WhatsApp Business/Media/";
                    String childPath = (from.equals("whatsapp")) ? "WhatsApp Audio" : "WhatsApp Business Audio";
                    File[] recvList = Utils.getWhatsAppPath(parentPath, childPath).listFiles();
                    File[] senList = Utils.getWhatsAppPath(parentPath, childPath + "/Sent").listFiles();
                    recvDocList.clear();
                    sendDocList.clear();
                    Utils.below10HandleImage(recvList, recvDocList, false);
                    Utils.below10HandleImage(senList, sendDocList, true);


                }
            } catch (Exception e) {
                e.printStackTrace();
            }







            return null;

        }

        @Override
        protected void onProgressUpdate(String... progress) {

        }

        @Override
        protected void onPostExecute(String s) {
            if (!isFinishing()){
                progressDialog.dismiss();
                WhatsappActivity.ViewPagerAdapter adapter = new WhatsappActivity.ViewPagerAdapter(activity.getSupportFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
                adapter.addFragment(new recvAudFragment(recvDocList, from), "Received");
                adapter.addFragment(new sendAudFragment(sendDocList,from),"Sent");
                viewPager.setAdapter(adapter);
                viewPager.setOffscreenPageLimit(1);
                tabLayout.setupWithViewPager(viewPager);
                tvAllowAccess.setVisibility(View.GONE);
            }




        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            if (!isFinishing()){
                progressDialog.dismiss();
            }

        }
    }

    @Override
    public void onBackPressed() {
        // Stop the media player in the audio adapter if it is currently playing
        if (audioAdapter != null) {
            audioAdapter.stopMediaPlayer();
        }
       admenager.showadmobeInterstitialAd();
        // Call the super class method to finish the activity
        super.onBackPressed();
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}