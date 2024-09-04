package com.cd.statussaver.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
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

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.cd.statussaver.BuildConfig;
import com.cd.statussaver.R;
import com.cd.statussaver.interfaces.FileSizeListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AudioAdapter extends RecyclerView.Adapter<AudioAdapter.myViewHolder> {

    List<Uri> whatsappStatusModelList;
    private Context mContext;

    private myViewHolder lastClickedHolder;
    MediaPlayer mediaPlayer;
    String from;
    long totalSize;
  FileSizeListener fileSizeListener;
  TotalFileSizeTask fileSizeTask;
    private SparseBooleanArray mSelectedItemsIds;

    public  AudioAdapter(List<Uri> whatsappStatusModelList, Context mContext,String from) {
        this.whatsappStatusModelList = whatsappStatusModelList;
        this.mContext = mContext;
        mSelectedItemsIds = new SparseBooleanArray();
        this.from = from;
    }

    public AudioAdapter() {

    }
    public void  setFileSizeListener(FileSizeListener listener){
        this.fileSizeListener=listener;
    }


    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.clean_audio_item, parent, false);
        return new myViewHolder(view);
    }

    @SuppressLint("Range")
    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        Uri uri = whatsappStatusModelList.get(position);
        String name = null;
        String[] projection = { DocumentsContract.Document.COLUMN_DISPLAY_NAME };
        Cursor cursor = mContext.getContentResolver().query(uri, projection, null, null, null);
        try {
            if (cursor != null && cursor.moveToFirst()) {
                name = cursor.getString(cursor.getColumnIndex(DocumentsContract.Document.COLUMN_DISPLAY_NAME));
            }
        } finally {
            if (cursor != null) {
                    holder.textView.setText(name);
                    cursor.close();
            }
        }

        holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                        mediaPlayer.stop();
                        if (lastClickedHolder != null) {
                            lastClickedHolder.pause.setVisibility(View.GONE);
                            lastClickedHolder.view.setVisibility(View.VISIBLE);
                        }
                    }

                    mediaPlayer = new MediaPlayer();
                    try {
                        mediaPlayer.setDataSource(mContext, uri);
                        mediaPlayer.prepare();
                        mediaPlayer.start();
                        holder.pause.setVisibility(View.VISIBLE);
                        holder.view.setVisibility(View.GONE);
                        lastClickedHolder = holder;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            holder.pause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                        mediaPlayer.stop();
                        holder.pause.setVisibility(View.GONE);
                        holder.view.setVisibility(View.VISIBLE);
                    }
                }
            });

            if (lastClickedHolder == holder) {
                holder.pause.setVisibility(View.VISIBLE);
                holder.view.setVisibility(View.GONE);
            } else {
                holder.pause.setVisibility(View.GONE);
                holder.view.setVisibility(View.VISIBLE);
            }

        holder.checkBox.setChecked(mSelectedItemsIds.get(position));

        holder.checkBox.setOnClickListener(view -> {
            int adapterPosition = holder.getAdapterPosition();
            if (mSelectedItemsIds.get(adapterPosition, false)) {
                mSelectedItemsIds.delete(adapterPosition);
                Uri audio =whatsappStatusModelList.get(position);
                long size =getFileSize(audio);

                totalSize-=size;
                if (fileSizeListener!=null){
                    fileSizeListener.selectImageSize("Selected File Size:"+" " + Formatter.formatFileSize(mContext,totalSize));

                }
            } else {
                mSelectedItemsIds.put(adapterPosition, true);
                Uri audio =whatsappStatusModelList.get(position);
                long size =getFileSize(audio);
                totalSize+=size;
                if (fileSizeListener!=null){
                    fileSizeListener.selectImageSize("Selected File Size:"+" " + Formatter.formatFileSize(mContext,totalSize));

                }
            }
        });

        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("audio/*");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                    }else {
                        if (uri.getPath()!=null) {


                            Uri contentUri = FileProvider.getUriForFile(mContext, BuildConfig.APPLICATION_ID +".fileprovider", new File(uri.getPath()));
                            shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
                        }
                        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    }


                    mContext.startActivity(Intent.createChooser(shareIntent, "Share audio"));


                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

    }


    @Override
    public int getItemCount() {
        return whatsappStatusModelList.size();
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

    public void stopMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            if (lastClickedHolder != null) {
                lastClickedHolder.pause.setVisibility(View.GONE);
                lastClickedHolder.view.setVisibility(View.VISIBLE);
                lastClickedHolder = null;
            }

        }
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        ImageView share, view, imageView,pause;
        CheckBox checkBox;

        TextView textView;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            share = itemView.findViewById(R.id.shareImage);
            view = itemView.findViewById(R.id.viewImage);
            checkBox = itemView.findViewById(R.id.checkBox);
            imageView = itemView.findViewById(R.id.image_list);
            textView  =itemView.findViewById(R.id.audioName);
            pause  = itemView.findViewById(R.id.pauseImage);
        }


    }


    public boolean isSelected(int position) {
        return mSelectedItemsIds.get(position);
    }

    public void AudremoveItem(int position) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            Uri ImgUri = Uri.parse((whatsappStatusModelList.get(position).toString()));




            boolean isFileExists = false;
            Cursor cursor = mContext.getContentResolver().query(ImgUri, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                @SuppressLint("Range") int size = cursor.getInt(cursor.getColumnIndex(DocumentsContract.Document.COLUMN_SIZE));
                isFileExists = size > 0;
                cursor.close();
            }
            if (isFileExists) {
                boolean isDeleted = false;
                try {
                    isDeleted = DocumentsContract.deleteDocument(mContext.getContentResolver(),ImgUri);
                } catch (Exception e) {
                    e.printStackTrace();
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
    public Uri getItem ( int position){
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

