package com.cd.statussaver.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.OpenableColumns;
import android.text.format.Formatter;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.cd.statussaver.R;
import com.cd.statussaver.interfaces.FileSizeListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class WhatsappDocAdapter extends RecyclerView.Adapter<WhatsappDocAdapter.myViewholder> {


    static List<Uri> whatsappStatusModelList;
    private Context mContext;

    private SparseBooleanArray mSelectedItemsIds;
    String from;
    FileSizeListener fileSizeListener;
    long totalSize;
    TotalFileSizeTask fileSizeTask;

   public void setFileSizeListener(FileSizeListener listener){
       this.fileSizeListener=listener;
   }

    public WhatsappDocAdapter(List<Uri> reciveArrayList, FragmentActivity activity,String from) {
        this.whatsappStatusModelList = reciveArrayList;
        this.mContext = activity;
        mSelectedItemsIds = new SparseBooleanArray();
        this.from = from;
    }

    @NonNull
    @Override
    public myViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.clean_doc_item, parent, false);
        return new myViewholder(view);
    }

    @SuppressLint("Range")
    @Override
    public void onBindViewHolder(@NonNull myViewholder holder, int position) {
        Uri pdfUri = Uri.parse(String.valueOf(whatsappStatusModelList.get(position)));
        String name = null;
        String[] projection = { DocumentsContract.Document.COLUMN_DISPLAY_NAME };
        Cursor cursor = mContext.getContentResolver().query(pdfUri, projection, null, null, null);
        try {
            if (cursor != null && cursor.moveToFirst()) {
                name = cursor.getString(cursor.getColumnIndex(DocumentsContract.Document.COLUMN_DISPLAY_NAME));
            }
        } finally {
            if (cursor != null) {
                int maxLength = 28; // Maximum length of text to display
                if (name != null && name.length() > maxLength) {
                    name = name.substring(0, maxLength) + "..."; // Truncate text and add ellipsis
                }
                holder.name.setText(name);
                cursor.close();
            }
        }

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });




        holder.checkBox.setChecked(mSelectedItemsIds.get(position));

        holder.checkBox.setOnClickListener(view -> {
            int adapterPosition = holder.getAdapterPosition();
            if (mSelectedItemsIds.get(adapterPosition, false)) {
                mSelectedItemsIds.delete(adapterPosition);
                Uri image =whatsappStatusModelList.get(position);
                long size =getFileSize(image);
                totalSize+=size;
                if (fileSizeListener!=null){
                    fileSizeListener.selectImageSize("Selected File Size:"+" " + Formatter.formatFileSize(mContext,totalSize));

                }
            } else {
                mSelectedItemsIds.put(adapterPosition, true);
                Uri image =whatsappStatusModelList.get(position);
                long size =getFileSize(image);
                totalSize+=size;
                if (fileSizeListener!=null){
                    fileSizeListener.selectImageSize("Selected File Size:"+" " + Formatter.formatFileSize(mContext,totalSize));

                }
            }
        });




    }


    @Override
    public  int getItemCount() {
        return whatsappStatusModelList.size();
    }

    public class myViewholder extends RecyclerView.ViewHolder {
        CheckBox checkBox;
        TextView name;
        public myViewholder(@NonNull View itemView) {
            super(itemView);
            checkBox=itemView.findViewById(R.id.checkBox);
            name = itemView.findViewById(R.id.fileName);
//            progressBar =itemView.findViewById(R.id.ProgressBar);


        }
    }

    public void selectAllItems() {
        totalSize = 0;

        List<Uri> uriList = new ArrayList<>();

        for (int i = 0; i < whatsappStatusModelList.size(); i++) {
            mSelectedItemsIds.put(i, true);
            Uri image = whatsappStatusModelList.get(i);
            uriList.add(image);
        }

        fileSizeTask=new TotalFileSizeTask();
        // Execute file size calculation in the background
        fileSizeTask.execute(uriList.toArray(new Uri[0]));
    }

    public void clearAllItems() {
        mSelectedItemsIds.clear();
        totalSize=0;
        if (fileSizeTask != null && fileSizeTask.getStatus() == AsyncTask.Status.RUNNING) {
            // AsyncTask is running, cancel it
            fileSizeTask.cancel(true);
        }
        if (fileSizeListener!=null){
            fileSizeListener.selectImageSize("");

        }
        notifyDataSetChanged();
    }

    public boolean isSelected(int position) {
        return mSelectedItemsIds.get(position);
    }
    public void RemoveItem(int position) {

       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){

           Uri ImgUri = Uri.parse(String.valueOf(whatsappStatusModelList.get(position)));


           boolean isFileExists = false;
           Cursor cursor = mContext.getContentResolver().query(ImgUri, null, null, null, null);
           if (cursor != null && cursor.moveToFirst()) {
               @SuppressLint("Range") int size = cursor.getInt(cursor.getColumnIndex(DocumentsContract.Document.COLUMN_SIZE));
               isFileExists = size > 0;
               cursor.close();
           }
// If file exists, call the deleteDocument method
           if (isFileExists) {
               boolean isDeleted = false;
               try {
                   isDeleted = DocumentsContract.deleteDocument(mContext.getContentResolver(),ImgUri);
               } catch (FileNotFoundException e) {
                   throw new RuntimeException(e);
               }
               if (isDeleted) {
                   Log.d("DELETE", "File deleted successfully");
               } else {
                   Log.d("DELETE", "Failed to delete file");
               }
           } else {
               Log.d("DELETE", "File doesn't exist");
           }

       }else {
           Uri ImgUri = Uri.parse(String.valueOf(whatsappStatusModelList.get(position)));

// Get the file path from the Uri
           String filePath = ImgUri.getPath();

           if (filePath != null) {
               File file = new File(filePath);

               if (file.exists()) {
                   if (file.delete()) {
                       Log.d("File Deleted:", file.getAbsolutePath());
                   } else {
                       Log.d("File not Deleted:", file.getAbsolutePath());
                   }
               } else {
                   Log.d("File not Deleted", "No Exist: " + file.getAbsolutePath());
               }
           } else {
               Log.d("File not Deleted", "Invalid Uri");
           }

       }





        whatsappStatusModelList.remove(position);
        mSelectedItemsIds.delete(position);
        notifyItemRemoved(position);
    }
    public Uri getItem(int position) {
        return whatsappStatusModelList.get(position);
    }
    public long getFileSize(Uri uri) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            try {
                Cursor cursor = mContext.getContentResolver().query(uri, null, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);
                    long fileSize = cursor.getLong(sizeIndex);
                    cursor.close();
                    return fileSize;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            try {
                String filePATH=uri.getPath();
                if (filePATH != null) {
                    return filePATH.length();
                }

            }catch (Exception e){
                e.printStackTrace();
            }

        }

        return 0;
    }
    private class TotalFileSizeTask extends AsyncTask<Uri, Void, Long> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (fileSizeListener != null) {
                fileSizeListener.selectImageSize("Calculating Total Size.....");
            }
        }

        @Override
        protected Long doInBackground(Uri... uris) {
            long totalSize = 0;

            for (Uri uri : uris) {
                totalSize += getFileSize(uri);
            }

            return totalSize;
        }

        @Override
        protected void onPostExecute(Long size) {
            if (!isCancelled()){
                if (fileSizeListener != null) {
                    fileSizeListener.selectImageSize("Selected Total File Size: " +
                            Formatter.formatFileSize(mContext, size));
                }

                // Notify adapter or perform any other necessary actions
                notifyDataSetChanged();
            }

        }
    }


}