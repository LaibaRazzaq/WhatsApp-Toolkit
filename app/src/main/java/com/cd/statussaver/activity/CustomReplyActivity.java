package com.cd.statussaver.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.core.view.InputDeviceCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.cd.statussaver.R;
import com.cd.statussaver.SwipeToDeleteCallBack;
import com.cd.statussaver.adapter.AutoReplyAdapter;
import com.cd.statussaver.databinding.ActivityCustomReplyBinding;
import com.cd.statussaver.model.AutoReply;
import com.cd.statussaver.util.Admenager;
import com.cd.statussaver.util.Const;
import com.cd.statussaver.util.SharedPreference;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class CustomReplyActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityCustomReplyBinding thisb;
    public AutoReplyAdapter adapter;
    private SharedPreference preference;
    Admenager admenager;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        thisb = ActivityCustomReplyBinding.inflate(getLayoutInflater());
        setContentView(thisb.getRoot());
        admenager=new Admenager(this,this);
        admenager.load_InterstitialAd(getString(R.string.admobe_intertesial_add_custom_reply));
        getSupportActionBar().setTitle("Custom Replay");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);

        preference = new SharedPreference(getApplicationContext());
        thisb.btnAdd.setOnClickListener(this);
        thisb.msgCustomSwitch.setChecked(preference.getFromPref_Boolean("customAutoReplySwitch"));
        Log.e("msgCustomSwitch Value", preference.getFromPref_Boolean("customAutoReplySwitch") + ":::");
        thisb.msgCustomSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!b) {
                    preference.addToPref_Boolean("customAutoReplySwitch", false);
                } else if (!preference.getFromPref_Boolean("CheckedState")) {
                    Toast.makeText(getApplicationContext(), "Please On Auto Reply", Toast.LENGTH_SHORT).show();
                    Log.e("Custom Reply", "Text");
                    thisb.msgCustomSwitch.setChecked(false);
                } else {
                    preference.addToPref_Boolean("customAutoReplySwitch", true);
                }
            }
        });

        thisb.btnAdd.setImageResource(R.drawable.ic_baseline_add_24);
        thisb.btnAdd.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.white)));


        bannerAd();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnAdd) {
            Intent intent = new Intent(getApplicationContext(), AddCustomReplyMessageActivity.class);
            intent.putExtra("ActivityValue", "Add");
            startActivity(intent);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Const.replyList = preference.getList("MessageList");
        try {
            if (Const.replyList != null) {
                if (!Const.replyList.isEmpty()) {
                    thisb.txtEmpty.setVisibility(View.GONE);
                    thisb.linearList.setVisibility(View.VISIBLE);

                    adapter = new AutoReplyAdapter(getApplicationContext(), Const.replyList);
                    thisb.customRecycleview.setAdapter(adapter);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                    linearLayoutManager.setReverseLayout(true);
                    linearLayoutManager.setStackFromEnd(true);
                    thisb.customRecycleview.setLayoutManager(linearLayoutManager);
                    enableSwipeToDeleteAndUndo();
                }
            }
            else{
                Const.replyList = new ArrayList();
                thisb.txtEmpty.setVisibility(View.VISIBLE);
                thisb.linearList.setVisibility(View.GONE);

            }
        } catch (Exception e) {
            Log.e("LISTEMPTY", e.getMessage());
            Const.replyList = new ArrayList();
        }
        enableSwipeToDeleteAndUndo();
    }

    private void bannerAd() {
        MobileAds.initialize((Context) getApplicationContext(), (OnInitializationCompleteListener) new OnInitializationCompleteListener() {
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                Log.e("Adssss", initializationStatus.toString());
            }
        });

    }

    private void enableSwipeToDeleteAndUndo() {
        new ItemTouchHelper(new SwipeToDeleteCallBack(getApplicationContext()) {
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int i) {
                super.onSwiped(viewHolder, i);
                final int adapterPosition = viewHolder.getAdapterPosition();
                final AutoReply autoReply =adapter.getData().get(adapterPosition);
                adapter.removeItem(adapterPosition);
                adapter.notifyDataSetChanged();
                Snackbar make = Snackbar.make(thisb.getRoot(), (CharSequence) "Item was removed from the list.", 0);
                make.setAction((CharSequence) "UNDO", (View.OnClickListener) new View.OnClickListener() {
                    public void onClick(View view) {
                        adapter.restoreItem(autoReply, adapterPosition);
                        thisb.customRecycleview.scrollToPosition(adapterPosition);
                        adapter.notifyDataSetChanged();
                    }
                });
                make.setActionTextColor((int) InputDeviceCompat.SOURCE_ANY);
                make.show();
            }
        }).attachToRecyclerView(thisb.customRecycleview);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        admenager.showadmobeInterstitialAd();
    }
}