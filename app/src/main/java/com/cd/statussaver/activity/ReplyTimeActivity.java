package com.cd.statussaver.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.cd.statussaver.R;
import com.cd.statussaver.databinding.ActivityReplyTimeBinding;
import com.cd.statussaver.util.Admenager;
import com.cd.statussaver.util.SharedPreference;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ReplyTimeActivity extends AppCompatActivity implements View.OnClickListener {
    private AlertDialog.Builder builder;
    private int compareHour;
    private int compareMinute;
    private String eTime = "";
    public String endTime = "";
    public String formate;
    public SharedPreference preference;
    private String sTime = "";
    public String spinTime = "";
    public String startTime = "";
    ActivityReplyTimeBinding thisb;
    public String time = "";
    private TimePickerDialog timePickerDialog;
    Admenager admenager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        thisb = (ActivityReplyTimeBinding) DataBindingUtil.setContentView(this, R.layout.activity_reply_time);
        ActionBar actionBar=getSupportActionBar();
        if (actionBar!=null){
            actionBar.hide();
        }
        admenager=new Admenager(this,this);
        admenager.loadBannerAd(thisb.adView,getString(R.string.admob_banner_id));
        preference = new SharedPreference(this);
        time = preference.getFromPref_String("TimeofMsg");
        if (time.equals("")) {
            time = String.valueOf(5);
        }
        spinTime = preference.getFromPref_String("SpinTime");
        if (spinTime.isEmpty()) {
            spinTime = "Seconds";
        }
        sTime = preference.getFromPref_String("StartTime");
        eTime = preference.getFromPref_String("EndTime");
        if (sTime.equals("")) {
            thisb.txtStartTime.setText("No start time selected");
        } else {
            thisb.txtStartTime.setText(sTime);
        }
        if (eTime.equals("")) {
            thisb.txtEndTime.setText("No start time selected");
        } else {
            thisb.txtEndTime.setText(eTime);
        }
        thisb.chkScheduleTime.setChecked(preference.getFromPref_Boolean("ScheduleTime"));
        thisb.chkImmediately.setOnClickListener(this);
        thisb.chkTime.setOnClickListener(this);
        thisb.chkOnce.setOnClickListener(this);
        thisb.imgTimeback.setOnClickListener(this);
        thisb.linearStartTime.setOnClickListener(this);
        thisb.linearEndTime.setOnClickListener(this);
        thisb.chkScheduleTime.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    preference.addToPref_Boolean("ScheduleTime", true);
                } else {
                    preference.addToPref_Boolean("ScheduleTime", false);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (thisb.chkScheduleTime.isChecked() && sTime.equals("") && eTime.equals("")) {
            Toast.makeText(this, "Please Select the time", Toast.LENGTH_SHORT).show();
        }
    }
    private void checkBoxText() {
        thisb.chkImmediately.setText(Html.fromHtml("<font color='black'>Reply Immediately</font><br><font color='#808080'>Don't delay in sending auto-reply </font>"));
        thisb.chkTime.setText(Html.fromHtml("<font color='black'>Wait Time</font><br><font color='#808080'>Don't send a reply immediately to the same person,wait for " + time + " " + spinTime + " for the next auto-reply </font>"));
        thisb.chkOnce.setText(Html.fromHtml("<font color='black'>Reply Once</font><br><font color='#808080'>Reply only once to the same person/group until you ON auto-reply next time </font>"));
        thisb.chkScheduleTime.setText(Html.fromHtml("<font color='black'>Schedule Reply Time</font><br><font color='#808080'>Reply only scheduled time that you set for auto reply messages</font>"));
    }

    private void bannerAd() {
        MobileAds.initialize((Context) this, (OnInitializationCompleteListener) new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                Log.e("Adssss", initializationStatus.toString());
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        checkBoxText();
        if (preference.getFromPref_Boolean("Once")) {
            thisb.chkOnce.setChecked(true);
            thisb.chkImmediately.setChecked(false);
            thisb.chkTime.setChecked(false);
        } else if (preference.getFromPref_Boolean("Time")) {
            thisb.chkOnce.setChecked(false);
            thisb.chkImmediately.setChecked(false);
            thisb.chkTime.setChecked(true);
        } else if (preference.getFromPref_Boolean("Immediately")) {
            thisb.chkOnce.setChecked(false);
            thisb.chkImmediately.setChecked(true);
            thisb.chkTime.setChecked(false);
        }
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();

        if (viewId == R.id.chkImmediately) {
            thisb.chkImmediately.setChecked(true);
            thisb.chkTime.setChecked(false);
            thisb.chkOnce.setChecked(false);
            preference.addToPref_Boolean("Immediately", true);
            preference.addToPref_Boolean("Once", false);
            preference.addToPref_Boolean("Time", false);
        } else if (viewId == R.id.chkOnce) {
            thisb.chkOnce.setChecked(true);
            thisb.chkTime.setChecked(false);
            thisb.chkImmediately.setChecked(false);
            preference.addToPref_Boolean("Once", true);
            preference.addToPref_Boolean("Time", false);
            preference.addToPref_Boolean("Immediately", false);
        } else if (viewId == R.id.chkTime) {
            thisb.chkTime.setChecked(true);
            thisb.chkImmediately.setChecked(false);
            thisb.chkOnce.setChecked(false);

            builder = new AlertDialog.Builder(this);
            builder.setTitle("Wait Time");
            View inflate = getLayoutInflater().inflate(R.layout.reply_time_design_layout, null);
            builder.setView(inflate);
            final EditText editText = inflate.findViewById(R.id.edTime);
            final Spinner spinner = inflate.findViewById(R.id.spinTime);

            if (preference.getFromPref_String("SpinTime").equals("Minutes")) {
                spinner.setSelection(1);
            } else if (preference.getFromPref_String("SpinTime").equals("Seconds")) {
                spinner.setSelection(0);
            } else {
                spinner.setSelection(0);
            }

            editText.setText(time);
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
                    preference.addToPref_String("TimeofMsg", editText.getText().toString().trim());
                    preference.addToPref_String("SpinTime", spinner.getSelectedItem().toString());
                    Log.e("Spin Value", spinner.getSelectedItem().toString() + "::::");
                    time = editText.getText().toString().trim();
                    spinTime = spinner.getSelectedItem().toString();
                    thisb.chkTime.setText(Html.fromHtml("<font color='black'>Wait Time</font><br><font color='#808080'>Don't send a reply immediately to the same person, wait for " + time + " " + spinTime + " for the next auto-reply </font>"));
                    dialogInterface.dismiss();
                }
            });

            builder.show();

            preference.addToPref_Boolean("Time", true);
            preference.addToPref_Boolean("Once", false);
            preference.addToPref_Boolean("Immediately", false);

        } else if (viewId == R.id.imgTimeback) {
            finish();
            if (thisb.chkScheduleTime.isChecked() && sTime.equals("") && eTime.equals("")) {
                Toast.makeText(this, "Please Select the time", Toast.LENGTH_SHORT).show();
            }

        } else if (viewId == R.id.linearEndTime) {
            Calendar instance = Calendar.getInstance();
            timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int i, int i2) {


                    Calendar selectedTime = Calendar.getInstance();
                    selectedTime.set(Calendar.HOUR_OF_DAY, i);
                    selectedTime.set(Calendar.MINUTE, i2);

                    SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a", Locale.US);
                    eTime = dateFormat.format(selectedTime.getTime());

                    thisb.txtEndTime.setText(eTime);
                    preference.setEndTime(sTime);


                }
            }, instance.get(11), instance.get(12), false);
            timePickerDialog.show();

        } else if (viewId == R.id.linearStartTime) {
            Calendar instance2 = Calendar.getInstance();
            timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int i, int i2) {

                    Calendar selectedTime = Calendar.getInstance();
                    selectedTime.set(Calendar.HOUR_OF_DAY, i);
                    selectedTime.set(Calendar.MINUTE, i2);

                    SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a", Locale.US);
                    sTime = dateFormat.format(selectedTime.getTime());


                    thisb.txtStartTime.setText(sTime);
                    preference.setStartTime(sTime);


                 }
            }, instance2.get(11), instance2.get(12), false);
            timePickerDialog.show();
        }
    }


    public boolean isAfterTime(Date date, Date date2) {
        return !date2.after(date);
    }
}