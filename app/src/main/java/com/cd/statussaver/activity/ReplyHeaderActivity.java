package com.cd.statussaver.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.cd.statussaver.R;
import com.cd.statussaver.databinding.ActivityReplyHeaderBinding;
import com.cd.statussaver.util.NotificationService;
import com.cd.statussaver.util.SharedPreference;

public class ReplyHeaderActivity extends AppCompatActivity implements View.OnClickListener {
    public String headerMsg;

    public SharedPreference preference;

    public String replyMsg;
    ActivityReplyHeaderBinding thisb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        thisb = (ActivityReplyHeaderBinding) DataBindingUtil.setContentView(this, R.layout.activity_reply_header);
        ActionBar actionBar=getSupportActionBar();
        if (actionBar!=null){
            actionBar.hide();
        }


        preference = new SharedPreference(this);
        if (preference.getFromPref_String("ReplyHeader").equals("")) {
            thisb.switchReplyHeader.setChecked(true);
        }
        replyMsg = preference.getFromPref_String("autoReplyText");
        headerMsg = preference.getFromPref_String("HeaderText");

        if (headerMsg.isEmpty()) {
            headerMsg = NotificationService.TAG;
            thisb.txtHeaderText.setText(NotificationService.TAG);
            thisb.txtReplyMessage.setText(Html.fromHtml("<b>" + headerMsg + "</b><br>" + replyMsg));
        } else {
            thisb.txtHeaderText.setText(preference.getFromPref_String("HeaderText"));
            thisb.txtReplyMessage.setText(Html.fromHtml("<b>" + headerMsg + "</b><br>" + replyMsg));
        }
        headerSwitch();
        Log.e("Header Switch", preference.getFromPref_String("ReplyHeader") + ":::");
        thisb.linearHeaderMessage.setOnClickListener(this);
        thisb.editHeader.setOnClickListener(this);
        thisb.imgHeaderBack.setOnClickListener(this);
    }

    private void headerSwitch() {
        thisb.switchReplyHeader.setText(Html.fromHtml("<font color='black'>Enable</font><br><font color='#808080'>send the reply header along with the reply message</font>"));
        thisb.switchReplyHeader.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    preference.addToPref_String("ReplyHeader", "Checked");
                    thisb.txtReplyMessage.setText(Html.fromHtml("<b>" + headerMsg + "</b><br>" + replyMsg));
                    thisb.txtBoldText.setText(headerMsg);
                    thisb.txtBoldText.setTypeface((Typeface) null, Typeface.BOLD);
                    preference.addToPref_String("BoldHeaderText", thisb.txtBoldText.getText().toString());
                    return;
                }
                preference.addToPref_String("ReplyHeader", "Unchecked");
                thisb.txtReplyMessage.setText(replyMsg);
                preference.addToPref_String("BoldHeaderText", " ");
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (preference.getFromPref_String("ReplyHeader").equals("Checked")) {
            thisb.switchReplyHeader.setChecked(true);
            thisb.txtReplyMessage.setText(Html.fromHtml("<b>" + headerMsg + "</b><br>" + replyMsg));
            thisb.txtBoldText.setText(headerMsg);
            thisb.txtBoldText.setTypeface((Typeface) null, Typeface.BOLD);
            preference.addToPref_String("BoldHeaderText", thisb.txtBoldText.getText().toString());
        } else if (preference.getFromPref_String("ReplyHeader").equals("Unchecked")) {
            thisb.switchReplyHeader.setChecked(false);
            thisb.txtReplyMessage.setText(replyMsg);
            preference.addToPref_String("BoldHeaderText", " ");
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.imgHeaderBack) {
            finish();
        }
        else if (id == R.id.linearHeaderMessage) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Header Text");
            View inflate = getLayoutInflater().inflate(R.layout.reply_header_dialog_layout, (ViewGroup) null);
            builder.setView(inflate);
            @SuppressLint({"MissingInflatedId", "LocalSuppress"}) final EditText editText = (EditText) inflate.findViewById(R.id.edReplyHeaderMsg);
            editText.setText(thisb.txtHeaderText.getText().toString());
            editText.setSelection(editText.getText().length());
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    thisb.txtHeaderText.setText(editText.getText().toString().trim());
                    headerMsg = editText.getText().toString().trim();
                    thisb.txtReplyMessage.setText(Html.fromHtml("<b>" + headerMsg + "</b><br>" + replyMsg));
                    preference.addToPref_String("HeaderText", editText.getText().toString().trim());
                    if (preference.getFromPref_String("ReplyHeader").equals("Checked")) {
                        thisb.txtBoldText.setText(headerMsg);
                        thisb.txtBoldText.setTypeface((Typeface) null, Typeface.BOLD);
                        preference.addToPref_String("BoldHeaderText", thisb.txtBoldText.getText().toString());
                    } else {
                        preference.addToPref_String("BoldHeaderText", " ");
                    }
                    dialogInterface.dismiss();
                }
            });
            builder.show();
        }
        else if (id == R.id.editHeader) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Header Text");
            View inflate = getLayoutInflater().inflate(R.layout.reply_header_dialog_layout, (ViewGroup) null);
            builder.setView(inflate);
            @SuppressLint({"MissingInflatedId", "LocalSuppress"}) final EditText editText = (EditText) inflate.findViewById(R.id.edReplyHeaderMsg);
            editText.setText(thisb.txtHeaderText.getText().toString());
            editText.setSelection(editText.getText().length());
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    thisb.txtHeaderText.setText(editText.getText().toString().trim());
                    headerMsg = editText.getText().toString().trim();
                    thisb.txtReplyMessage.setText(Html.fromHtml("<b>" + headerMsg + "</b><br>" + replyMsg));
                    preference.addToPref_String("HeaderText", editText.getText().toString().trim());
                    if (preference.getFromPref_String("ReplyHeader").equals("Checked")) {
                        thisb.txtBoldText.setText(headerMsg);
                        thisb.txtBoldText.setTypeface((Typeface) null, Typeface.BOLD);
                        preference.addToPref_String("BoldHeaderText", thisb.txtBoldText.getText().toString());
                    } else {
                        preference.addToPref_String("BoldHeaderText", " ");
                    }
                    dialogInterface.dismiss();
                }
            });
            builder.show();
        }
    }
}

