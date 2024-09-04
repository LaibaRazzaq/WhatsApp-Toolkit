package com.cd.statussaver.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cd.statussaver.BuildConfig;
import com.cd.statussaver.R;
import com.cd.statussaver.activity.FullImageViewActivity;
import com.cd.statussaver.activity.VideoPlayerActivity;
import com.cd.statussaver.interfaces.FileSizeListener;
import com.google.android.material.imageview.ShapeableImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class whatsAppCleanerAdatpter extends RecyclerView.Adapter<whatsAppCleanerAdatpter.myViewHolder> {
    List<Uri> whatsappStatusModelList;
    private Context mContext;
    TotalFileSizeTask fileSizeTask;


    String from;
    private SparseBooleanArray mSelectedItemsIds;
    long totalSize;
    FileSizeListener fileSizeListener;


    public void setFileSizeListener(FileSizeListener listener){
        this.fileSizeListener=listener;
    }




    public whatsAppCleanerAdatpter(List<Uri> whatsappStatusModelList, Context mContext, String from) {
        this.whatsappStatusModelList = whatsappStatusModelList;
        this.mContext = mContext;
        mSelectedItemsIds = new SparseBooleanArray();
        this.from = from;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.clean_item, parent, false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        Uri video = Uri.parse(String.valueOf(whatsappStatusModelList.get(position)));
        String vd = video.toString();
        if (vd.endsWith(".mp4")) {
            holder.VdPlay.setVisibility(View.VISIBLE);
        } else {
            holder.VdPlay.setVisibility(View.GONE);
        }

        Glide.with(mContext)
                .load(whatsappStatusModelList.get(position))
                .into(holder.imageView);


        holder.VdPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, VideoPlayerActivity.class);
                intent.putExtra("PathVideo", vd);
                mContext.startActivity(intent);

            }
        });


        Uri FuLLImgUri = Uri.parse(String.valueOf(whatsappStatusModelList.get(position)));
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (vd.endsWith(".mp4")) {
                    Intent intent = new Intent(mContext, VideoPlayerActivity.class);
                    intent.putExtra("PathVideo", vd);
                    mContext.startActivity(intent);
                } else {
                    Intent i = new Intent(mContext, FullImageViewActivity.class);
                    i.putExtra("FullImage", whatsappStatusModelList.get(position).toString());
                    mContext.startActivity(i);
                }
            }
        });

        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    if (vd.endsWith(".mp4")) {
                        shareIntent.setType("video/*");

                    }else {
                        shareIntent.setType("image/*");
                    }

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                        shareIntent.putExtra(Intent.EXTRA_STREAM, FuLLImgUri);
                    }else {
                        if (FuLLImgUri.getPath()!=null) {


                            Uri contentUri = FileProvider.getUriForFile(mContext, BuildConfig.APPLICATION_ID +".fileprovider", new File(FuLLImgUri.getPath()));
                            shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
                        }
                        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    }
                    mContext.startActivity(Intent.createChooser(shareIntent, "Share image"));


                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(mContext, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }



            }
        });


        holder.checkBox.setChecked(mSelectedItemsIds.get(position));

        // Handle checkbox click event to update the stored status
        holder.checkBox.setOnClickListener(view -> {
            int adapterPosition = holder.getAdapterPosition();
            if (mSelectedItemsIds.get(adapterPosition, false)) {
                mSelectedItemsIds.delete(adapterPosition);
                Uri image =whatsappStatusModelList.get(position);
                long size =getFileSize(image);
                totalSize-=size;
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


    @Override
    public int getItemCount() {
        return whatsappStatusModelList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateData(List<Uri> reciveArrayList) {
        whatsappStatusModelList.clear();
        whatsappStatusModelList = reciveArrayList;
        notifyDataSetChanged();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        ImageView share, view, VdPlay;
        CheckBox checkBox;
        ShapeableImageView imageView;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            share = itemView.findViewById(R.id.shareImage);
            view = itemView.findViewById(R.id.viewImage);
            checkBox = itemView.findViewById(R.id.checkBox);
            imageView = itemView.findViewById(R.id.image_list);
            VdPlay = itemView.findViewById(R.id.iv_play);

        }
    }


    public boolean isSelected(int position) {
        return mSelectedItemsIds.get(position);
    }

    public void removeItem(int position) {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            Uri ImgUri = Uri.parse(whatsappStatusModelList.get(position).toString());

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
        public Uri getItem (int position){
            return whatsappStatusModelList.get(position);
        }

    public long getTotalSelectedItemsSize() {
        long totalSize = 0;

        for (int i = 0; i < whatsappStatusModelList.size(); i++) {
            if (mSelectedItemsIds.get(i)) {
                Uri mediaUri = whatsappStatusModelList.get(i);
                long fileSize = getFileSize(mediaUri);
                totalSize += fileSize;

            }
        }

        return totalSize;
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
    // Example using AsyncTask
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