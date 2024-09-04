package com.cd.statussaver.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;


import com.cd.statussaver.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class Utils {
    public static Dialog customDialog;
    private static Context context;


    public static String RootDirectoryFacebook = "/StatusSaver/Facebook/";
    public static String RootDirectoryInsta = "/StatusSaver/Insta/";
    public static String RootDirectoryTikTok = "/StatusSaver/TikTok/";
    public static String RootDirectoryTwitter ="/StatusSaver/Twitter/";
    public static String RootDirectoryLikee ="/StatusSaver/Likee/";
    public static String RootDirectoryShareChat ="/StatusSaver/ShareChat/";
    public static String RootDirectoryRoposo ="/StatusSaver/Roposo/";
    public static String RootDirectorySnackVideo ="/StatusSaver/SnackVideo/";
    public static final String ROOTDIRECTORYJOSH ="/StatusSaver/Josh/";
    public static final String ROOTDIRECTORYCHINGARI ="/StatusSaver/Chingari/";
    public static final String ROOTDIRECTORYMITRON ="/StatusSaver/Mitron/";

    public static final String ROOTDIRECTORYMX ="/StatusSaver/Mxtakatak/";
    public static final String ROOTDIRECTORYMOJ ="/StatusSaver/Moj/";

    public static File RootDirectoryFacebookShow = new File(Environment.getExternalStorageDirectory() + "/Download/StatusSaver/Facebook");
    public static File RootDirectoryInstaShow = new File(Environment.getExternalStorageDirectory() + "/Download/StatusSaver/Insta");
    public static File RootDirectoryTikTokShow = new File(Environment.getExternalStorageDirectory() + "/Download/StatusSaver/TikTok");
    public static File RootDirectoryTwitterShow = new File(Environment.getExternalStorageDirectory() + "/Download/StatusSaver/Twitter");
    public static File RootDirectoryWhatsappShow = new File(Environment.getExternalStorageDirectory() + "/Download/StatusSaver/Whatsapp");
    public static File RootDirectoryLikeeShow = new File(Environment.getExternalStorageDirectory() + "/Download/StatusSaver/Likee");
    public static File RootDirectoryShareChatShow = new File(Environment.getExternalStorageDirectory() + "/Download/StatusSaver/ShareChat");
    public static File RootDirectoryRoposoShow = new File(Environment.getExternalStorageDirectory() + "/Download/StatusSaver/Roposo");
    public static File RootDirectorySnackVideoShow = new File(Environment.getExternalStorageDirectory() + "/Download/StatusSaver/SnackVideo");
    public static final File ROOTDIRECTORYJOSHSHOW = new File(Environment.getExternalStorageDirectory() + "/Download/StatusSaver/Josh");
    public static final File ROOTDIRECTORYCHINGARISHOW = new File(Environment.getExternalStorageDirectory() + "/Download/StatusSaver/Chingari");
    public static final File ROOTDIRECTORYMITRONSHOW = new File(Environment.getExternalStorageDirectory() + "/Download/StatusSaver/Mitron");
    public static final File ROOTDIRECTORYMXSHOW = new File(Environment.getExternalStorageDirectory() + "/Download/StatusSaver/Mxtakatak");
    public static final File ROOTDIRECTORYMOJSHOW = new File(Environment.getExternalStorageDirectory() + "/Download/StatusSaver/Moj");
    public static final File WHATS_APP_DIRECTORY = new File(Environment.getExternalStorageDirectory() +
            File.separator + "WhatsApp");


    public static String PrivacyPolicyUrl = "http://codingdunia.com/ccprojects/statussaver/privacy_policy.html";
    public static String TikTokUrl = "http://androidqueue.com/tiktokapi/api.php";

    public Utils(Context _mContext) {
        context = _mContext;
    }

    public static void setToast(Context _mContext, String str) {
        Toast toast = Toast.makeText(_mContext, str, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static void createFileFolder() {
        if (!RootDirectoryFacebookShow.exists()) {
            RootDirectoryFacebookShow.mkdirs();
        }
        if (!RootDirectoryInstaShow.exists()) {
            RootDirectoryInstaShow.mkdirs();
        }
        if (!RootDirectoryTikTokShow.exists()) {
            RootDirectoryTikTokShow.mkdirs();
        }
        if (!RootDirectoryTwitterShow.exists()) {
            RootDirectoryTwitterShow.mkdirs();
        }
        if (!RootDirectoryWhatsappShow.exists()) {
            RootDirectoryWhatsappShow.mkdirs();
        }
        if (!RootDirectoryLikeeShow.exists()){
            RootDirectoryLikeeShow.mkdirs();
        }
        if (!RootDirectoryLikeeShow.exists()){
            RootDirectoryLikeeShow.mkdirs();
        }
        if (!RootDirectoryShareChatShow.exists()){
            RootDirectoryShareChatShow.mkdirs();
        }
        if (!RootDirectoryRoposoShow.exists()){
            RootDirectoryRoposoShow.mkdirs();
        }
        if (!RootDirectorySnackVideoShow.exists()){
            RootDirectorySnackVideoShow.mkdirs();
        }
        if (!ROOTDIRECTORYJOSHSHOW.exists()){
            ROOTDIRECTORYJOSHSHOW.mkdirs();
        }
        if (!ROOTDIRECTORYCHINGARISHOW.exists()){
            ROOTDIRECTORYCHINGARISHOW.mkdirs();
        }
        if (!ROOTDIRECTORYMITRONSHOW.exists()){
            ROOTDIRECTORYMITRONSHOW.mkdirs();
        }

        if (!ROOTDIRECTORYMXSHOW.exists()){
            ROOTDIRECTORYMXSHOW.mkdirs();
        }
        if (!ROOTDIRECTORYMOJSHOW.exists()){
            ROOTDIRECTORYMOJSHOW.mkdirs();
        }


    }

    public static void showProgressDialog(Activity activity) {
        System.out.println("Show");
        if (customDialog != null) {
            customDialog.dismiss();
            customDialog = null;
        }
        customDialog = new Dialog(activity);
        LayoutInflater inflater = LayoutInflater.from(activity);
        View mView = inflater.inflate(R.layout.progress_dialog, null);
        customDialog.setCancelable(false);
        customDialog.setContentView(mView);
        if (!customDialog.isShowing() && !activity.isFinishing()) {
            customDialog.show();
        }
    }

    public static void hideProgressDialog(Activity activity) {
        System.out.println("Hide");
        if (customDialog != null && customDialog.isShowing()) {
            customDialog.dismiss();
        }
    }



    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void startDownload(String downloadPath, String destinationPath, Context context, String FileName) {
        setToast(context, "Download Started");
        Uri uri = Uri.parse(downloadPath); // Path where you want to download file.
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);  // Tell on which network you want to download file.
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);  // This will show notification on top when downloading the file.
        request.setTitle(FileName+""); // Title for notification.
        request.setVisibleInDownloadsUi(true);
        request.setDestinationInExternalPublicDir(DIRECTORY_DOWNLOADS,destinationPath+FileName);  // Storage directory path
        ((DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE)).enqueue(request); // This will start downloading
        try {
            MediaScannerConnection.scanFile(context, new String[]{new File(DIRECTORY_DOWNLOADS + "/" + destinationPath + FileName).getAbsolutePath()},
                    null, new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                        }
                    });
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public static void shareImage(Context context, String filePath) {
        try {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_TEXT, context.getResources().getString(R.string.share_txt));
            String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), filePath, "", null);
            Uri screenshotUri = Uri.parse(path);
            intent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
            intent.setType("image/*");
            context.startActivity(Intent.createChooser(intent, context.getResources().getString(R.string.share_image_via)));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public static void shareImageVideoOnWhatsapp(Context context, String filePath, boolean isVideo) {
        Uri imageUri = Uri.parse(filePath);
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setPackage("com.whatsapp");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "");
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        if (isVideo) {
            shareIntent.setType("video/*");
        }else {
            shareIntent.setType("image/*");
        }
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try {
            context.startActivity(shareIntent);
        } catch (Exception e) {
            Utils.setToast(context,"Whtasapp not installed.");
        }
    }

    public static void shareVideo(Context context, String filePath) {
        Uri mainUri = Uri.parse(filePath);
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("video/mp4");
        sharingIntent.putExtra(Intent.EXTRA_STREAM, mainUri);
        sharingIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try {
            context.startActivity(Intent.createChooser(sharingIntent, "Share Video using"));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "No application found to open this file.", Toast.LENGTH_LONG).show();
        }
    }

    public static void RateApp(Context context) {
        try {
            final String appName = context.getPackageName();
            try {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appName)));
            } catch (ActivityNotFoundException anfe) {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appName)));
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }
    public static void OtherApp(Context context) {
        final String appName = context.getPackageName();
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.nextsimpledesign.wasticker.stickerislamicswhatsapp")));
        } catch (ActivityNotFoundException anfe) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.nextsimpledesign.wasticker.stickerislamicswhatsapp")));
        }
    }
    public static void MoreApp(Context context) {
        final String appName = context.getPackageName();
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/developer?id=Video+Downloader+%26+Status+Saver+%26+Stories+Download")));
        } catch (ActivityNotFoundException anfe) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/developer?id=Video+Downloader+%26+Status+Saver+%26+Stories+Download")));
        }
    }

    public static void ShareApp(Context context) {
        try {
            final String appLink = "\nhttps://play.google.com/store/apps/details?id=" + context.getPackageName();
            Intent sendInt = new Intent(Intent.ACTION_SEND);
            sendInt.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.app_name));
            sendInt.putExtra(Intent.EXTRA_TEXT, context.getString(R.string.share_app_message) + appLink);
            sendInt.setType("text/plain");
            context.startActivity(Intent.createChooser(sendInt, "Share"));

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public static void OpenApp(Context context,String Package) {
        Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(Package);
        if (launchIntent != null) {
            context.startActivity(launchIntent);
        } else {
            setToast(context,"App Not Available.");
        }
    }

    public static boolean isNullOrEmpty(String s) {
        return (s == null) || (s.length() == 0) || (s.equalsIgnoreCase("null"))||(s.equalsIgnoreCase("0"));
    }

    public static List<String> extractUrls(String text) {
        List<String> containedUrls = new ArrayList<String>();
        String urlRegex = "((https?|ftp|gopher|telnet|file):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
        Pattern pattern = Pattern.compile(urlRegex, Pattern.CASE_INSENSITIVE);
        Matcher urlMatcher = pattern.matcher(text);

        while (urlMatcher.find()) {
            containedUrls.add(text.substring(urlMatcher.start(0),
                    urlMatcher.end(0)));
        }

        return containedUrls;
    }


    public static void infoDialog(Context context, String title, String msg){
        new AlertDialog.Builder(context).setTitle(title)
                .setMessage(msg)
                .setPositiveButton(context.getResources().getString(R.string.ok),
                        (dialog, which) -> dialog.dismiss()).create().show();
    }

    public static File getWhatsAppPath(String parentPath,String child){
        return new File(Environment.getExternalStorageDirectory() +
                File.separator + parentPath+child);



    }



    public static  void handleData(Context context,Uri treeUri, String parentPath, List<Uri> imgList, String childPath, boolean isSent) {
        String path= parentPath + "/" + childPath;


        Log.d("Path",path);
        Uri imagesUri = DocumentsContract.buildChildDocumentsUriUsingTree(treeUri, DocumentsContract.getTreeDocumentId(treeUri) + path);

        String[] projection = new String[]{
                DocumentsContract.Document.COLUMN_DOCUMENT_ID,
                DocumentsContract.Document.COLUMN_DISPLAY_NAME,
        };

        try (Cursor cursor = context.getContentResolver().query(imagesUri, projection, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    @SuppressLint("Range") String documentId = cursor.getString(cursor.getColumnIndex(DocumentsContract.Document.COLUMN_DOCUMENT_ID));
                    @SuppressLint("Range") String displayName = cursor.getString(cursor.getColumnIndex(DocumentsContract.Document.COLUMN_DISPLAY_NAME));

                    Uri documentUri = DocumentsContract.buildDocumentUriUsingTree(treeUri, documentId);
                    if (!displayName.contains(".nomedia")) {
                        if (!isSent){
                            if (!documentUri.toString().contains("Sent") && !documentUri.toString().contains("Private")){
                                imgList.add(documentUri);
                            }

                        }else {

                            imgList.add(documentUri);
                        }


                    }
                    Log.d("imageUri",""+documentUri);

                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void below10HandleImage(File[] imageListPath, List<Uri> imageList, boolean isSend) {
        if (imageListPath != null && imageListPath.length > 0) {

            Arrays.sort(imageListPath);
            for (File file : imageListPath) {

                if (!file.getName().contains(".nomedia")) {
                    if (!isSend) {
                        if (!file.getAbsolutePath().contains("Sent") && !file.getAbsolutePath().contains("Private")){
                            imageList.add(Uri.fromFile(file));
                        }

                    }else {
                        imageList.add(Uri.fromFile(file));
                    }
                }




            }

        }
    }



    public static void below10VoiceNote(File[] imageListPath, List<Uri> imageList, boolean isSend) {



        if (imageListPath != null && imageListPath.length > 0) {
            Arrays.sort(imageListPath);

            for (File file : imageListPath) {
                Log.d("filesPath",file.getPath());
                if (!file.getName().contains(".nomedia")){
                if (file.isDirectory()) {


                        processFilesInDirectory(file, imageList, isSend);
                    }
                    // If the file is a directory, loop inside it

                }
            }
            }else {
            Log.d("filesPath","null");
        }

    }

    private static  void iterateChildDocuments(Context context,Uri treeUri, String parentDocumentId,List<Uri> recvList) {
        String[] projection = new String[] {
                DocumentsContract.Document.COLUMN_DOCUMENT_ID,
                DocumentsContract.Document.COLUMN_DISPLAY_NAME,
                DocumentsContract.Document.COLUMN_MIME_TYPE,
                DocumentsContract.Document.COLUMN_LAST_MODIFIED,
                DocumentsContract.Document.COLUMN_SIZE
        };
        String[] selectionArgs = new String[] { parentDocumentId };

        try (Cursor cursor = context.getContentResolver().query(DocumentsContract.buildChildDocumentsUriUsingTree(treeUri, parentDocumentId), projection, null, selectionArgs, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    @SuppressLint("Range") String documentId = cursor.getString(cursor.getColumnIndex(DocumentsContract.Document.COLUMN_DOCUMENT_ID));
                    @SuppressLint("Range") String displayName = cursor.getString(cursor.getColumnIndex(DocumentsContract.Document.COLUMN_DISPLAY_NAME));
                    @SuppressLint("Range") String mimeType = cursor.getString(cursor.getColumnIndex(DocumentsContract.Document.COLUMN_MIME_TYPE));
                    @SuppressLint("Range") long lastModified = cursor.getLong(cursor.getColumnIndex(DocumentsContract.Document.COLUMN_LAST_MODIFIED));
                    @SuppressLint("Range") long fileSize = cursor.getLong(cursor.getColumnIndex(DocumentsContract.Document.COLUMN_SIZE));


                    Uri documentUri = DocumentsContract.buildDocumentUriUsingTree(treeUri, documentId);

                    if (DocumentsContract.Document.MIME_TYPE_DIR.equals(mimeType)) {
                        // Child document is a directory, call the function recursively with the child document's URI
                        iterateChildDocuments(context,treeUri, documentId,recvList);
                    } else if (mimeType.startsWith("audio/")) {
                        // Child document is an audio file, process it as required
                        if (!displayName.contains(".nomedia")) {

                            recvList.add(documentUri);
                            // This is not a .nomedia file, process it as required
                            // ...

                        }
                    }
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void handleVoiceNotes(Uri treeUri, String path,List<Uri> recvList,Context context) {
    String     parentDocumentId = DocumentsContract.getTreeDocumentId(treeUri) + path;

        iterateChildDocuments(context,treeUri,parentDocumentId,recvList);
    }
    private static void processFilesInDirectory(File directory, List<Uri> uris, boolean isSend) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    // Recursively process subdirectories
                    processFilesInDirectory(file, uris, isSend);
                } else if (!file.getName().contains(".nomedia")) {
                    if (!isSend) {
                        if (!file.getAbsolutePath().contains("Sent") && !file.getAbsolutePath().contains("Private")) {
                            uris.add(Uri.fromFile(file));
                        }
                    } else {
                        uris.add(Uri.fromFile(file));
                    }
                }
            }
        }
    }
}