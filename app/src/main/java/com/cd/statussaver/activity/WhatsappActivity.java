package com.cd.statussaver.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.cd.statussaver.databinding.DialogWhatsappPermissionBinding;
import com.cd.statussaver.fragment.WhatsappImageFragment;
import com.cd.statussaver.fragment.WhatsappQImageFragment;
import com.cd.statussaver.fragment.WhatsappQVideoFragment;
import com.cd.statussaver.fragment.WhatsappVideoFragment;
import com.cd.statussaver.R;
import com.cd.statussaver.databinding.ActivityWhatsappBinding;
import com.cd.statussaver.util.Admenager;
import com.cd.statussaver.util.Utils;
import com.google.android.material.tabs.TabLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static androidx.fragment.app.FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;
import static com.cd.statussaver.util.Utils.createFileFolder;
import static com.cd.statussaver.util.Utils.setToast;

public class WhatsappActivity extends AppCompatActivity {
    private ActivityWhatsappBinding binding;
    private WhatsappActivity activity;

    SharedPreferences sharedPreferences;

    SharedPreferences.Editor editor;
    private ArrayList<Uri> fileArrayList;
    ProgressDialog progressDialog;
    Admenager admenager;
    String from;
    public static final File STATUS_DIRECTORY = new File(Environment.getExternalStorageDirectory() +
            File.separator + "WhatsApp/Media/.Statuses");
    public static final File BUSINESS_STATUS_DIRECTORY = new File(Environment.getExternalStorageDirectory() +
            File.separator + "WhatsApp Business/Media/.Statuses");;

    @Override
    public void onBackPressed() {

        admenager.showadmobeInterstitialAd();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("Folder", MODE_PRIVATE);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_whatsapp);
        activity = this;
        createFileFolder();
        initViews();
        admenager = new Admenager(this, this);

        admenager.load_InterstitialAd(getString(R.string.admobe_intertesial_whatsAppActivity));
        Toast.makeText(this, "Please Watch full status from whatsapp then you can download from this app ", Toast.LENGTH_LONG).show();
        //AdsUtils.showFBBannerAd(activity,binding.bannerContainer);
    }

    @Override
    protected void onResume() {
        super.onResume();
        activity = this;
    }

    private void initViews() {
        binding.imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        /*binding.LLOpenWhatsapp.setOnClickListener(v -> {
            Utils.OpenApp(activity,"com.whatsapp");
        });*/
        fileArrayList = new ArrayList<Uri>();

        initProgress();
        boolean whatsappPermissiongranted = sharedPreferences.getBoolean("isWhatsAppFolderPermisstionGranted", false);
        boolean whatsappBusinesPermissiongranted = sharedPreferences.getBoolean("isWhatsAppBusinessFolderPermisstionGranted", false);

      from =getIntent().getStringExtra("from");
        if (from!=null){
            if (from.equals("whatsapp")){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                    if (whatsappPermissiongranted) {
                        progressDialog.show();
                        new LoadAllFiles().execute();
                        binding.tvAllowAccess.setVisibility(View.GONE);
                    } else {
                        binding.tvAllowAccess.setVisibility(View.VISIBLE);
                    }
                }else {
                    if (ActivityCompat.checkSelfPermission(WhatsappActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED&&ActivityCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED){
                        progressDialog.show();
                        new LoadAllFiles().execute();
                        binding.tvAllowAccess.setVisibility(View.GONE);
                    }else
                    {
                        binding.tvAllowAccess.setVisibility(View.VISIBLE);

                    }
                }
            }else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                    if (whatsappBusinesPermissiongranted) {
                        progressDialog.show();
                        new LoadAllFiles().execute();
                        binding.tvAllowAccess.setVisibility(View.GONE);
                    } else {
                        binding.tvAllowAccess.setVisibility(View.VISIBLE);
                    }
                }else {
                    if (ActivityCompat.checkSelfPermission(WhatsappActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED&&ActivityCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED){
                        progressDialog.show();
                        new LoadAllFiles().execute();
                        binding.tvAllowAccess.setVisibility(View.GONE);
                    }else
                    {
                        binding.tvAllowAccess.setVisibility(View.VISIBLE);

                    }
                }
            }
        }




        binding.tvAllowAccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(activity, R.style.SheetDialog);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                DialogWhatsappPermissionBinding dialogWhatsappPermissionBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.dialog_whatsapp_permission, null, false);
                dialog.setContentView(dialogWhatsappPermissionBinding.getRoot());
                dialogWhatsappPermissionBinding.tvAllow.setOnClickListener(v -> {
                    requestPeemision();
                    dialog.dismiss();
                        });
            dialog.show();
            }
        });
    }
    private void requestPeemision() {

        StorageManager sm = (StorageManager) getSystemService(Context.STORAGE_SERVICE);
        Intent intent = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            intent = sm.getPrimaryStorageVolume().createOpenDocumentTreeIntent();

            String startDir ;
            if (from.equals("whatsapp")){
                startDir="Android%2Fmedia%2Fcom.whatsapp%2FWhatsApp";
            }else{
                startDir="Android%2Fmedia%2Fcom.whatsapp.w4b%2FWhatsApp Business";
            }

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
            if (ActivityCompat.shouldShowRequestPermissionRationale(WhatsappActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)|| ActivityCompat.shouldShowRequestPermissionRationale(WhatsappActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)) {
                AlertDialog.Builder alertDialog=new AlertDialog.Builder(WhatsappActivity.this);
                alertDialog.setTitle("Permission Required");
                alertDialog.setMessage("We need this permission to record audio.");

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
                        dialog.dismiss();

                    }
                });

                alertDialog.create().show();



            }else {
                ActivityCompat.requestPermissions(WhatsappActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},21);

            }
        }


    }
    public void initProgress(){
        progressDialog = new ProgressDialog(activity,R.style.AppCompatAlertDialogStyle);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Loading Status. Please wait...");
        progressDialog.setIndeterminate(true);
        progressDialog.setCanceledOnTouchOutside(false);
    }

    static class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        ViewPagerAdapter(FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == 2001 && resultCode == RESULT_OK) {
                Uri dataUri = data.getData();

                if (from.equals("whatsapp")){
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
                }else {
                    if(dataUri!=null){
                        if (dataUri.toString().endsWith("Business")) {
                            editor=sharedPreferences.edit();
                            editor.putString("WhatsAppBussinesFolderUri", dataUri.toString());
                            editor.putBoolean("isWhatsAppBusinessFolderPermisstionGranted",true);
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



            } else if (requestCode==122) {
                if (ActivityCompat.checkSelfPermission(WhatsappActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED&&ActivityCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED) {
                    progressDialog.show();
                    new LoadAllFiles().execute();
                    binding.tvAllowAccess.setVisibility(View.GONE);
                } else {
                    setToast(WhatsappActivity.this,"Permission Required");
                    binding.tvAllowAccess.setVisibility(View.VISIBLE);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    class LoadAllFiles extends AsyncTask<String, String, ArrayList<Uri>> {
        @Override
        protected ArrayList<Uri> doInBackground(String... furl) {
            try{
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q){
                    Uri treeUri;
                    String uriString;
                    if (from.equals("whatsapp")){
                        uriString =sharedPreferences.getString("WhatsAppFolderUri" ,null);

                    }else {
                        uriString =sharedPreferences.getString("WhatsAppBussinesFolderUri" ,null);

                    }
                    if (uriString != null && !uriString.isEmpty()) {
                        treeUri = Uri.parse(uriString);
                        ///YAHN ASA KRO JO URI HAI USKO DOCUMENT FILE MA CONVERY KRK JO METHOD COMMENT KYA HAI WO KRO
                        String   path = "/Media/.Statuses";

                        Uri sentUri = DocumentsContract.buildChildDocumentsUriUsingTree(
                                treeUri,
                                DocumentsContract.getTreeDocumentId(treeUri) + path

                        );
                        Log.d("path","Uri "+sentUri);
                        String[] projection = new String[] {
                                DocumentsContract.Document.COLUMN_DOCUMENT_ID,
                                DocumentsContract.Document.COLUMN_DISPLAY_NAME,
                                DocumentsContract.Document.COLUMN_MIME_TYPE,
                                DocumentsContract.Document.COLUMN_LAST_MODIFIED,
                                DocumentsContract.Document.COLUMN_SIZE
                        };

                        try (Cursor cursor = getContentResolver().query(sentUri, projection, null, null, null)) {
                            if (cursor != null && cursor.moveToFirst()) {

                                do {
                                    @SuppressLint("Range") String documentId = cursor.getString(cursor.getColumnIndex(DocumentsContract.Document.COLUMN_DOCUMENT_ID));
                                    @SuppressLint("Range") String displayName = cursor.getString(cursor.getColumnIndex(DocumentsContract.Document.COLUMN_DISPLAY_NAME));
                                    @SuppressLint("Range") String mimeType = cursor.getString(cursor.getColumnIndex(DocumentsContract.Document.COLUMN_MIME_TYPE));
                                    @SuppressLint("Range") long lastModified = cursor.getLong(cursor.getColumnIndex(DocumentsContract.Document.COLUMN_LAST_MODIFIED));
                                    @SuppressLint("Range") long fileSize = cursor.getLong(cursor.getColumnIndex(DocumentsContract.Document.COLUMN_SIZE));
                                    Log.d("img", "hh");

                                    // Use the documentUri to access the file in the Sent folder
                                    // ...
                                    //reciveImages.add(documentUri);
                                    Uri documentUri = DocumentsContract.buildDocumentUriUsingTree(treeUri, documentId);
                                    Log.d("path","uriss"+documentUri);
                                    if (!displayName.equals(".nomedia")){
//                                DocumentFile documentFile = DocumentFile.fromSingleUri(activity, documentUri);
                                        fileArrayList.add(documentUri);

                                    }
                                }while (cursor.moveToNext());



                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }else {
                    File[] statusFiles;
                    if (from.equals("whatsapp")){
                        statusFiles = STATUS_DIRECTORY.listFiles();
                    }else {
                        statusFiles=BUSINESS_STATUS_DIRECTORY.listFiles();
                    }

                    fileArrayList.clear();
                    if (statusFiles != null && statusFiles.length > 0) {

                        Arrays.sort(statusFiles);
                        for (File file : statusFiles) {

                            if (file.getName().contains(".nomedia"))
                                continue;
                            if ( file.getName().endsWith(".mp4")||file.getName().endsWith(".jpg")) {
                                fileArrayList.add(Uri.fromFile(file));
                            }


                        }
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }





            return fileArrayList;
        }

        @Override
        protected void onProgressUpdate(String... progress) {

        }
        @Override
        protected void onPostExecute(ArrayList<Uri> fileArrayList) {
            try {
                progressDialog.dismiss();
                ViewPagerAdapter adapter = new ViewPagerAdapter(activity.getSupportFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
                adapter.addFragment(new WhatsappQImageFragment(fileArrayList), "Images");
                adapter.addFragment(new WhatsappQVideoFragment(fileArrayList),"Videos");
                binding.viewpager.setAdapter(adapter);
                binding.viewpager.setOffscreenPageLimit(1);
                binding.tabs.setupWithViewPager(binding.viewpager);
                binding.tvAllowAccess.setVisibility(View.GONE);
            }catch (Exception e){
                e.printStackTrace();
            }





        }
        @Override
        protected void onCancelled() {
            super.onCancelled();
            progressDialog.dismiss();
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
                    progressDialog.show();
                    binding.tvAllowAccess.setVisibility(View.GONE);
                    new LoadAllFiles().execute();
                }

            }
        }

    }
}
