package com.cd.statussaver.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cd.statussaver.R;
import com.cd.statussaver.databinding.ActivityFullImageViewBinding;

public class FullImageViewActivity extends AppCompatActivity {
    ActivityFullImageViewBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFullImageViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        binding.fullBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Uri imgpath = Uri.parse(getIntent().getStringExtra("FullImage"));
        if (imgpath != null) {
            Glide.with(this).load(imgpath).into(binding.FullimgView);
        } else {
            Toast.makeText(this, "FAILED", Toast.LENGTH_SHORT).show();
        }

    }
}