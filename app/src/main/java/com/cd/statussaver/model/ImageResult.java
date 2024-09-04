package com.cd.statussaver.model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ImageResult {
    // Define a class to hold the result
    public static class ImageProcessingResult {
        public long imgCur;
        public long imgSizeMb;

        public ImageProcessingResult(long imgCur, long imgSizeMb) {
            this.imgCur = imgCur;
            this.imgSizeMb = imgSizeMb;
        }

        public long getImgCur() {
            return imgCur;
        }

        public void setImgCur(long imgCur) {
            this.imgCur = imgCur;
        }

        public long getImgSizeMb() {
            return imgSizeMb;
        }

        public void setImgSizeMb(long imgSizeMb) {
            this.imgSizeMb = imgSizeMb;
        }
    }

    // Modify the function to return ImageProcessingResult
    public static ImageProcessingResult processImages(Context context, Uri treeUri, String parentPath,String childPath,boolean isSent) {
        long imgCur = 0;
        long imgSizeMb = 0;

        String path = parentPath + "/" + childPath;
        Uri sentUri = DocumentsContract.buildChildDocumentsUriUsingTree(
                treeUri,
                DocumentsContract.getTreeDocumentId(treeUri) + path

        );
        String[] projection = new String[] {
                DocumentsContract.Document.COLUMN_DOCUMENT_ID,
                DocumentsContract.Document.COLUMN_DISPLAY_NAME,
                DocumentsContract.Document.COLUMN_MIME_TYPE,
                DocumentsContract.Document.COLUMN_LAST_MODIFIED,
                DocumentsContract.Document.COLUMN_SIZE
        };
        try (Cursor cursor = context.getContentResolver().query(sentUri, projection, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    @SuppressLint("Range") String documentId = cursor.getString(cursor.getColumnIndex(DocumentsContract.Document.COLUMN_DOCUMENT_ID));
                    @SuppressLint("Range") String displayName = cursor.getString(cursor.getColumnIndex(DocumentsContract.Document.COLUMN_DISPLAY_NAME));
                    @SuppressLint("Range") String mimeType = cursor.getString(cursor.getColumnIndex(DocumentsContract.Document.COLUMN_MIME_TYPE));
                    @SuppressLint("Range") long lastModified = cursor.getLong(cursor.getColumnIndex(DocumentsContract.Document.COLUMN_LAST_MODIFIED));
                    @SuppressLint("Range") long fileSize = cursor.getLong(cursor.getColumnIndex(DocumentsContract.Document.COLUMN_SIZE));


                    Uri documentUri = DocumentsContract.buildDocumentUriUsingTree(treeUri, documentId);

                    if (!displayName.contains(".nomedia")) {
                        if (!isSent){

                            if (!documentUri.toString().contains("Sent") && !documentUri.toString().contains("Private")){
                                imgSizeMb+=fileSize;
                                imgCur++;
                            }

                        }else {
                            imgSizeMb+=fileSize;
                            imgCur++;
                        }
                        // This is not a .nomedia file, process it as required
                        // ...

                        Log.d("img", "hh");
                        Log.d("image", "Uri" + documentUri);




                    }
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        // Return the result
        return new ImageProcessingResult(imgCur, imgSizeMb);
    }

// Call this method from your activity/fragment passing the appropriate arguments
public static ImageProcessingResult below10HandleImage(File[] imageListPath, boolean isSend) {
    long imgCur = 0;
    long imgSizeMb = 0;

    if (imageListPath != null && imageListPath.length > 0) {
        Arrays.sort(imageListPath);
        for (File file : imageListPath) {
            if (!file.getName().contains(".nomedia")) {
                if (!isSend) {
                    if (!file.getAbsolutePath().contains("Sent") && !file.getAbsolutePath().contains("Private")) {
                        imgSizeMb += file.length(); // Convert bytes to megabytes
                        imgCur++;
                    }
                } else {
                    imgSizeMb += file.length(); // Convert bytes to megabytes
                    imgCur++;
                }
            }
        }
    }

    return new ImageProcessingResult(imgCur, imgSizeMb);
}

    public static  ImageProcessingResult below10VoiceNote(File[] imageListPath, boolean isSend){
        long imgCur = 0;
        long imgSizeMb = 0;

        if (imageListPath != null && imageListPath.length > 0) {
            Arrays.sort(imageListPath);

            for (File file : imageListPath) {
                if (!file.getName().contains(".nomedia")) {
                    if (file.isDirectory()) {

                        File[] files = file.listFiles();
                        if (files != null) {
                            for (File file1 : files) {
                                if (file1.isDirectory()) {
                                    // Recursively process subdirectories
                                    processFilesInDirectory(file, isSend);
                                } else if (!file1.getName().contains(".nomedia")) {
                                    if (!isSend) {
                                        if (!file1.getAbsolutePath().contains("Sent") && !file1.getAbsolutePath().contains("Private")) {

                                            imgCur++;
                                            imgSizeMb+=file.length();
                                        }
                                    } else {
                                        imgCur++;
                                        imgSizeMb+=file.length();

                                    }
                                }
                            }
                        }

                    }
                }


            }
        }
        return new ImageProcessingResult(imgCur,imgSizeMb);
    }
    private static void processFilesInDirectory(File directory, boolean isSend) {

    }

}
