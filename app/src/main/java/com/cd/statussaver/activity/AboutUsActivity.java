package com.cd.statussaver.activity;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.cd.statussaver.R;


public class AboutUsActivity extends AppCompatActivity {
    RelativeLayout websiteRL, emailRL, privacyPolicyRL;
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();

            return true;
        }
        return super.onOptionsItemSelected(item);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        websiteRL = findViewById(R.id.RLWebsite);
        emailRL = findViewById(R.id.RLEmail);

        privacyPolicyRL = findViewById(R.id.RLPrivacyPolicy);
        privacyPolicyRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(AboutUsActivity.this, PrivacyActivity.class);
//                startActivity(intent);

                Uri uri = Uri.parse("https://www.boxingessential.com/MasterToolkitPrivacyPolicy.html"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    startActivity(intent);
                }catch(Exception e){

                }
            }
        });
        websiteRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("http://www.nextsalution.com"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    startActivity(intent);
                }catch(Exception e){

                }
            }
        });
        emailRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("plain/text");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[] { "nextsalution@gmail.com" });
                intent.putExtra(Intent.EXTRA_SUBJECT, "");
                intent.putExtra(Intent.EXTRA_TEXT, "");
                try {
                    startActivity(Intent.createChooser(intent, ""));
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });

    }
    AlertDialog alertDialog;

    public void backpress_dialog() {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Rate "+getString(R.string.app_name));
        alertDialogBuilder.setMessage("If you enjoy using this app please take a moment to rate it .Thanks for your support!");
        alertDialogBuilder.setPositiveButton("Rate Now", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id="+ getApplicationContext().getPackageName())));
                    finish();
                }catch (Exception e){
                    e.printStackTrace();
                }




            }
        });

        alertDialogBuilder.setNegativeButton("No Thanks", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.cancel();
                finish();


            }
        });
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        backpress_dialog();
        // super.onBackPressed();
    }
}