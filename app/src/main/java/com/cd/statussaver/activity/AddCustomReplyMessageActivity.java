package com.cd.statussaver.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.cd.statussaver.R;
import com.cd.statussaver.adapter.AutoReplyAdapter;
import com.cd.statussaver.databinding.ActivityAddCustomReplyMessageBinding;
import com.cd.statussaver.model.AutoReply;
import com.cd.statussaver.util.Admenager;
import com.cd.statussaver.util.Const;
import com.cd.statussaver.util.SharedPreference;
import com.cd.statussaver.util.TMAdsUtils;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;

import java.util.ArrayList;

public class AddCustomReplyMessageActivity extends AppCompatActivity implements View.OnClickListener {

    private AutoReplyAdapter autoReplyAdapter;
    private String edIncomingMsg = "";
    private String edReplyMsg = "";
    private String edValue = "";
    private SharedPreference preference;
    ActivityAddCustomReplyMessageBinding thisb;
    Admenager admenager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        thisb = (ActivityAddCustomReplyMessageBinding) DataBindingUtil.setContentView(this, R.layout.activity_add_custom_reply_message);
       ActionBar actionBar= getSupportActionBar();
       if (actionBar!=null){
           actionBar.hide();
       }
        admenager=new Admenager(this,this);
        admenager.load_InterstitialAd(getString(R.string.admobe_intertesial_add_custom_reply_message));
        Const.replyList = new ArrayList<>();
        preference = new SharedPreference(this);
        Const.replyList = preference.getList("MessageList");
        edValue = getIntent().getStringExtra("ActivityValue");
        if (edValue == null || !edValue.equals("Update")) {
            if (edValue != null && edValue.equals("Add")) {
                edValue = "";
                thisb.btnAddMessage.setText("Add Message");
            }
        } else {
            edIncomingMsg = getIntent().getStringExtra("ReceiveMessage");
            edReplyMsg = getIntent().getStringExtra("SendMessage");
            thisb.edIncomingMsg.setText(edIncomingMsg);
            thisb.edReplyMsg.setText(edReplyMsg);
            thisb.txtReply.setText(edReplyMsg);
            thisb.txtIncoming.setText(edIncomingMsg);
            thisb.btnAddMessage.setText("Update Message");
        }
        thisb.btnAddMessage.setOnClickListener(this);
        AdRequest adRequest = new AdRequest.Builder().build();
        thisb.adView.loadAd(adRequest);

        thisb.adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                thisb.adLayout.setVisibility(View.VISIBLE);
                thisb.adView.setVisibility(View.VISIBLE);



            }
        });
        thisb.imgBack.setOnClickListener(this);

        thisb.edIncomingMsg.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if (thisb.edIncomingMsg.getText()!=null){
                    thisb.txtIncoming.setText(thisb.edIncomingMsg.getText().toString().trim());

                }
            }


            @Override
            public void afterTextChanged(Editable editable) {
                if (thisb.edIncomingMsg.getText()!=null){
                    if (thisb.edIncomingMsg.getText().toString().trim().isEmpty()) {
                        thisb.txtIncoming.setText("Type incoming message below");
                    } else {
                        thisb.txtIncoming.setText(thisb.edIncomingMsg.getText().toString().trim());
                    }
                }

            }
        });
        thisb.edReplyMsg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }


            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if (thisb.edReplyMsg.getText()!=null){
                    thisb.txtReply.setText(thisb.edReplyMsg.getText().toString().trim());

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (thisb.edReplyMsg.getText()!=null){
                    if (thisb.edReplyMsg.getText().toString().trim().isEmpty()) {
                        thisb.txtReply.setText("Type your reply message");
                    } else {
                        thisb.txtReply.setText(thisb.edReplyMsg.getText().toString().trim());
                    }
                }

            }
        });
    }
    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btnAddMessage) {
            try {
                if (Const.replyList.isEmpty()) {
                    Const.replyList = new ArrayList<>();
                }

                if (thisb.edIncomingMsg.getText()!=null&&thisb.edIncomingMsg.getText().toString().trim().isEmpty()) {
                    thisb.edIncomingMsg.requestFocus();
                    thisb.edIncomingMsg.setError("Please fill Incoming message");
                } else if (thisb.edReplyMsg.getText()!=null&&thisb.edReplyMsg.getText().toString().trim().isEmpty()) {
                    thisb.edReplyMsg.requestFocus();
                    thisb.edReplyMsg.setError("Please fill Reply message");
                } else {

                    if (edValue != null) {
                        if (edValue.equals("Update")) {
                            for (int i = 0; i < Const.replyList.size(); i++) {
                                if (edIncomingMsg.equalsIgnoreCase(Const.replyList.get(i).getReceiveMsg()) && edReplyMsg.equalsIgnoreCase(Const.replyList.get(i).getSendMsg())) {
                                    Const.replyList.set(i, new AutoReply(thisb.edIncomingMsg.getText().toString().trim(), thisb.edReplyMsg.getText().toString().trim()));
                                    Log.e("I Value", i + ":::");
                                    preference.setList("MessageList", Const.replyList);
                                    finish();
                                }
                                Log.e("List Size", Const.replyList.size() + "::::");
                            }
                            return;
                        }
                    }
                    if (Const.replyList == null || Const.replyList.isEmpty()) {
                        Const.replyList = new ArrayList<>();
                    }
                    Const.replyList.add(new AutoReply(thisb.edIncomingMsg.getText().toString().trim(), thisb.edReplyMsg.getText().toString().trim()));
                    autoReplyAdapter = new AutoReplyAdapter(this, Const.replyList);

                    Toast.makeText(this, "Message added sucessfully", Toast.LENGTH_SHORT).show();
                    preference.setList("MessageList", Const.replyList);
                    finish();
                }
            } catch (Exception e) {
                if (e.getMessage()!=null){
                    Log.e("ADD MSG", e.getMessage());
                }

                Const.replyList = new ArrayList<>();
            }
        } else if (id == R.id.imgBack) {
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        admenager.showadmobeInterstitialAd();
    }
}