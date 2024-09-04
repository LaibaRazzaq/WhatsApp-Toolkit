

package com.cd.statussaver.activity;

import static com.cd.statussaver.activity.MainActivity.isAppInstalled;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cd.statussaver.R;
import com.cd.statussaver.databinding.ActivityAutoReplyBinding;
import com.cd.statussaver.util.Admenager;
import com.cd.statussaver.util.Const;
import com.cd.statussaver.util.NotificationService;
import com.cd.statussaver.util.SharedPreference;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.formats.UnifiedNativeAd;

import java.util.ArrayList;
import java.util.List;

public class AutoReplyActivity extends AppCompatActivity implements View.OnClickListener{
    private String AutoSwitch;
    private AdLoader adLoader;
    private AutoReplyTextAdapter adapter;
    private List<String> autoPrefList;
    private List<String> autoText;
    private Context context;
    Admenager admenager;
    AlertDialog alertDialog;


    private List<String> prefList;

    ActivityAutoReplyBinding binding;
    public SharedPreference preference;
    private String selectAutoText = null;
    AlertDialog pannel2dialog;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_option,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.settingMore) {
            startActivity(new Intent(this, ReplySettingActivity.class));
        } else if (id == R.id.statsCheck) {
            startActivity(new Intent(this, StatisticsActivity.class));
        } else if (id == R.id.autoRepyTo) {
            startActivity(new Intent(this, ContactActivity.class));
        } else {
            return super.onOptionsItemSelected(item);
        }
        return true;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        admenager.showadmobeInterstitialAd();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAutoReplyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        admenager=new Admenager(this,this);
        admenager.load_InterstitialAd(getString(R.string.admobe_intertesial_auto_reply));

        ActionBar actionBar=getSupportActionBar();
        if (actionBar!=null) {
           actionBar.setTitle("Auto Replay");
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back);

        }
        context = getApplicationContext();

        AdRequest adRequest = new AdRequest.Builder().build();
        binding.adView.loadAd(adRequest);
        binding.adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                binding.adLayout.setVisibility(View.VISIBLE);
               binding.adView.setVisibility(View.VISIBLE);

            }
        });
        preference = new SharedPreference(getApplicationContext());
        Const.replyList = new ArrayList<>();
        Const.autoText = new ArrayList<>();
        autoText = new ArrayList<>();
        autoPrefList = new ArrayList<>();
        prefList = new ArrayList<>();
        autoText = preference.getAutoTextList("AutoText");


        preference.addToPref_Boolean("autoReplyTextSwitch", true);
        try {
            if (autoText.isEmpty()) {
                autoText = new ArrayList<>();
            }
        } catch (Exception e) {
            autoText = new ArrayList<>();
        }
        binding.WASwitch.setChecked(preference.getFromPref_Boolean("WAState"));
        binding.WBSwitch.setChecked(preference.getFromPref_Boolean("WBState"));
        binding.msgSwitch.setChecked(preference.getFromPref_Boolean("CheckedState"));
        binding.msgSwitch.setChecked(preference.getFromPref_Boolean("CheckedState"));
        Log.e("msgSwitch Value", preference.getFromPref_Boolean("CheckedState") + ":::");
        binding.msgSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!b) {
                    preference.addToPref_Boolean("CheckedState", false);
                    AutoSwitch = "unchecked";
                    AutoReplyActivity.this.stopService(new Intent(AutoReplyActivity.this, NotificationService.class));
                } else if (isNotificationServiceEnabled(AutoReplyActivity.this)) {
                    preference.addToPref_Boolean("CheckedState", true);
                    AutoSwitch = "checked";
                    if (preference.getFromPref_Boolean("Once")) {
                        Const.userList.clear();
                        preference.setUserList("UserList", Const.userList);
                        Const.userList = new ArrayList<>();
                    }
                    if (preference.getFromPref_String("ReplyHeader").equals("")) {
                        preference.addToPref_String("ReplyHeader", "Checked");
                    }
                    AutoReplyActivity.this.startService(new Intent(AutoReplyActivity.this, NotificationService.class));
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(AutoReplyActivity.this);
                    View view=LayoutInflater.from(AutoReplyActivity.this).inflate(R.layout.help_panel_1,null);
                    builder.setView(view);
                    Button cancel,ok;
                    cancel=view.findViewById(R.id.cancel);
                    ok=view.findViewById(R.id.ok);
                    TextView needHelp=view.findViewById(R.id.needHelp);
                    ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            launchNotificationAccessSettings();
                            alertDialog.dismiss();
                        }
                    });
                  cancel.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                          alertDialog.dismiss();
                          binding.msgSwitch.setChecked(false);
                          preference.addToPref_Boolean("CheckedState", false);
                      }
                  });


                   alertDialog= builder.create();
                   alertDialog.setCancelable(false);
                   alertDialog.show();
                   if (alertDialog.getWindow()!=null){
                       alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                   }
                   needHelp.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View v) {
                           binding.msgSwitch.setChecked(false);
                           alertDialog.dismiss();
                           showHelpPanel2();
                       }
                   });
                }
            }
        });
        boolean whatsappFound = isAppInstalled(context, "com.whatsapp");
        binding.whatsClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!whatsappFound){
                    Toast.makeText(context, "WhatsApp not Installed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        if(!whatsappFound){
            binding.WASwitch.setClickable(false);
        }
        else{
            binding.WASwitch.setClickable(true);
        }
        binding.WASwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (!b) {
                    preference.addToPref_Boolean("WAState", false);
                } else {
                    preference.addToPref_Boolean("WAState", true);
                }
            }
        });

        boolean whatsappFound1 = isAppInstalled(context, "com.whatsapp.w4b");


        binding.busiClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!whatsappFound1){
                    Toast.makeText(context, "WhatsApp Business not Installed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.WBSwitch.setClickable(whatsappFound1);
        binding.WBSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!b) {
                    preference.addToPref_Boolean("WBState", false);
                } else {
                    preference.addToPref_Boolean("WBState", true);
                }
            }
        });

        if (!preference.getFromPref_String("autoReplyText").equals("")) {
            binding.txtAutoReply.setText(preference.getFromPref_String("autoReplyText"));
        } else {
            binding.txtAutoReply.setText("Can't talk now.");
            preference.addToPref_String("autoReplyText", "Can't talk now.");
        }
        binding.autoReplyTextSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (!b) {
                    preference.addToPref_Boolean("autoReplyTextSwitch", false);
                } else if (!preference.getFromPref_Boolean("CheckedState")) {
                    Toast.makeText(getApplicationContext(), "Please On Auto Reply", Toast.LENGTH_SHORT).show();
                    Log.e("Auto REply", "Text");
                    binding.autoReplyTextSwitch.setChecked(false);
                } else {

                }
            }
        });

        binding.imgEdit.setOnClickListener( this);
        binding.imgMsgClear.setOnClickListener( this);
        AutoReplyText();
        autoPrefList.addAll(autoText);



    }

    private void showHelpPanel2() {
        AlertDialog.Builder builder=new AlertDialog.Builder(AutoReplyActivity.this);
        View view=LayoutInflater.from(AutoReplyActivity.this).inflate(R.layout.help_panel_2,null);
        builder.setView(view);
        ImageView close=view.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.autoReplyTextSwitch.setChecked(false);
                pannel2dialog.dismiss();
            }
        });

        pannel2dialog=builder.create();
        pannel2dialog.show();
        if (pannel2dialog.getWindow()!=null){
            pannel2dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    public void onResume() {
        super.onResume();
        if (preference.getFromPref_Boolean("autoReplyTextSwitch")) {
            binding.autoReplyTextSwitch.setChecked(true);
            SharedPreference sharedPreference = preference;
            sharedPreference.addToPref_String("autoReplyText", sharedPreference.getFromPref_String("autoReplyText"));
        } else {
            binding.autoReplyTextSwitch.setChecked(false);
        }
        Log.e("autoReplyTextSwitch", preference.getFromPref_Boolean("autoReplyTextSwitch") + ":::");
        adapter = new AutoReplyTextAdapter(getApplicationContext(), autoPrefList);
        binding.autoRecycleview.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        binding.autoRecycleview.setLayoutManager(linearLayoutManager);
    }

    public void onStart() {
        super.onStart();
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.imgEdit) {
            Dialog dialog = new Dialog(AutoReplyActivity.this);
            dialog.setContentView(R.layout.add_auto_reply_text_layout);
            if (dialog.getWindow()!=null){
                dialog.getWindow().setLayout(-1, -2);

            }
            EditText editText = (EditText) dialog.findViewById(R.id.edAutoReplyText);
            editText.setText(binding.txtAutoReply.getText().toString().trim());
            editText.setSelection(editText.getText().length());
            ((ImageView) dialog.findViewById(R.id.imgClose)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!editText.getText().toString().trim().isEmpty()) {
                        editText.getText().clear();
                    }
                }
            });
            ((TextView) dialog.findViewById(R.id.btnSetMsg)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (editText.getText().toString().trim().isEmpty()) {
                        editText.requestFocus();
                        editText.setError("Please Write a text");
                        return;
                    }
                    binding.txtAutoReply.setText(editText.getText().toString().trim());
                    preference.addToPref_String("autoReplyText", editText.getText().toString());
                    autoText.add(editText.getText().toString());
                    preference.setAutoTextList("AutoText", autoText);
                    autoPrefList.add(editText.getText().toString());
                    dialog.dismiss();
                }
            });
            dialog.show();
        } else if (id == R.id.imgMsgClear) {
            autoText.clear();
            preference.setReplyList("AutoText", autoText);
            autoPrefList.clear();
            autoPrefList.addAll(Const.autoText);
            adapter.notifyDataSetChanged();
        }
    }

    private void AutoReplyText() {
        Const.autoText.add("In a meeting,text you later.");
        Const.autoText.add("At work, text you later.");
        Const.autoText.add("At the movie, text you later.");
        Const.autoText.add("I am busy, talk to you later.");
        Const.autoText.add("I am driving, text you later.");
        Const.autoText.add("I am sleeping,text you later.");
        Const.autoText.add("Can't talk now.");
        autoPrefList.addAll(Const.autoText);
    }


    public boolean isListenerEnabled(Context context2, Class cls) {
        ComponentName componentName = new ComponentName(context, cls);
        String string = Settings.Secure.getString(context.getContentResolver(), "enabled_notification_listeners");
        return string != null && string.contains(componentName.flattenToString());
    }

    public void launchNotificationAccessSettings() {
        enableService(true);
        int i = Build.VERSION.SDK_INT;
        startActivityForResult(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"), 100);
    }

    private void enableService(boolean z) {
        AutoReplyActivity.this.getPackageManager().setComponentEnabledSetting(new ComponentName(AutoReplyActivity.this, NotificationService.class), z ?
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED : PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
    }

    public static boolean isNotificationServiceEnabled(Context context2) {
        String packageName = context2.getPackageName();
        String string = Settings.Secure.getString(context2.getContentResolver(), "enabled_notification_listeners");
        if (!TextUtils.isEmpty(string)) {
            String[] split = string.split(":");
            for (String unflattenFromString : split) {
                ComponentName unflattenFromString2 = ComponentName.unflattenFromString(unflattenFromString);
                if (unflattenFromString2 != null && TextUtils.equals(packageName, unflattenFromString2.getPackageName())) {
                    return true;
                }
            }
        }
        return false;
    }


    @Override
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i != 100) {
            return;
        }
        if (isListenerEnabled(getApplicationContext(), NotificationService.class)) {
            Toast.makeText(getApplicationContext(), "permission Granted", Toast.LENGTH_SHORT).show();
            preference.addToPref_Boolean("CheckedState", true);
            return;
        }
        Toast.makeText(getApplicationContext(), "permission Denied", Toast.LENGTH_SHORT).show();
        preference.addToPref_Boolean("CheckedState", false);
    }

    class AutoReplyTextAdapter extends RecyclerView.Adapter<AutoReplyTextAdapter.ViewHolder> {
        private final List<String> listItem;

        public AutoReplyTextAdapter(Context context2, List<String> list) {
            listItem = list;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.auto_reply_text_design, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, @SuppressLint("RecyclerView") int i) {
            viewHolder.txtAutoMsg.setText(listItem.get(i));
            viewHolder.linearAutoText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    binding.txtAutoReply.setText(listItem.get(i));
                    preference.addToPref_String("autoReplyText", listItem.get(i));
                }
            });
        }


        @Override
        public int getItemCount() {
            return listItem.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            LinearLayout linearAutoText;
            TextView txtAutoMsg;

            public ViewHolder(View view) {
                super(view);
                txtAutoMsg = (TextView) view.findViewById(R.id.txtAutoText);
                linearAutoText = (LinearLayout) view.findViewById(R.id.linearAutoText);
            }
        }
    }


}
