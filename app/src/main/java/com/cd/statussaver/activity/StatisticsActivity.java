package com.cd.statussaver.activity;

import static java.security.AccessController.getContext;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cd.statussaver.R;
import com.cd.statussaver.databinding.ActivityStatisticsBinding;
import com.cd.statussaver.util.Const;
import com.cd.statussaver.util.SharedPreference;
import com.cd.statussaver.util.StatisticsReplyMsgListModel;
import com.cd.statussaver.util.TMAdsUtils;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.ArrayList;
import java.util.List;

public class StatisticsActivity extends AppCompatActivity implements View.OnClickListener {

    int count = 0;


    private SharedPreference preference;
    private List<StatisticsReplyMsgListModel> staticList;

    ActivityStatisticsBinding thisb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        thisb = ActivityStatisticsBinding.inflate(getLayoutInflater());
        setContentView(thisb.getRoot());
        getSupportActionBar().hide();

        preference = new SharedPreference(getApplicationContext());
        Const.staticsReplyList = new ArrayList();
        staticList = new ArrayList();
        List<StatisticsReplyMsgListModel> staticReplylList = new ArrayList();
        List<String> messageList = new ArrayList();
        List<String> personList = new ArrayList();
        long fromPref_Long = preference.getFromPref_Long("Counter");
        Log.e("Counter Value", fromPref_Long + ":::");
        thisb.txtCounter.setText(String.valueOf(fromPref_Long));
        thisb.imgReset.setOnClickListener(this);
        thisb.imgDirectBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               onBackPressed();
            }
        });

        staticReplylList = preference.getReplyList("StaticsReplyList");
        int i = 0;
        if (staticReplylList ==null){
            staticReplylList =new ArrayList<>();
        }
        while (i < staticReplylList.size()) {
            try {
                if (!messageList.contains(staticReplylList.get(i).getReplyMsg())) {
                    messageList.add(staticReplylList.get(i).getReplyMsg());
                }
                i++;
            } catch (Exception e) {
                e.printStackTrace();

                staticReplylList = new ArrayList<>();
                return;
            }
        }
        for (int i2 = 0; i2 < messageList.size(); i2++) {
            for (int i3 = 0; i3 < staticReplylList.size(); i3++) {
                if (messageList.get(i2).equals(staticReplylList.get(i3).getReplyMsg())) {
                    count++;
                    if (!personList.contains(staticReplylList.get(i3).getPersonName())) {
                        personList.add(staticReplylList.get(i3).getPersonName());
                    }
                }
            }
            staticList.add(new StatisticsReplyMsgListModel(count, messageList.get(i2), String.valueOf(personList.size()), 0));
            count = 0;
            personList.clear();
        }
    }
    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.imgReset) {
            thisb.txtCounter.setText("0");
            preference.addToPref_Long("Counter", 0);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        try {

            if (staticList != null) {
                if (!staticList.isEmpty()) {
                    thisb.txtMessgesEmpty.setVisibility(View.GONE);
                    thisb.staticRecycleview.setVisibility(View.VISIBLE);
                    thisb.staticRecycleview.setAdapter(new StatisticsReplyMsgListAdapter(getApplicationContext(), staticList));
                    return;
                }
            }
            staticList = new ArrayList<>();
            thisb.txtMessgesEmpty.setVisibility(View.VISIBLE);
            thisb.staticRecycleview.setVisibility(View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
            e.printStackTrace();
            staticList = new ArrayList<>();
        }
    }







    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    public class StatisticsReplyMsgListAdapter extends RecyclerView.Adapter<StatisticsReplyMsgListAdapter.ViewHolder> {
        private final Context context;
        private final List<StatisticsReplyMsgListModel> listItem;

        public StatisticsReplyMsgListAdapter(Context context2, List<StatisticsReplyMsgListModel> list) {
            context = context2;
            listItem = list;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.statistics_reply_messages_list_design_layout, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int i) {
            viewHolder.txtMessage.setText(listItem.get(i).getReplyMsg());
            viewHolder.txtMsgCount.setText(String.format("%02d", listItem.get(i).getI()));
            Log.e("message",listItem.get(i).getPersonName());
            viewHolder.txtPersonCount.setText(String.format("%02d", Integer.parseInt(listItem.get(i).getPersonName())));
            /*viewHolder.txtPersonCount.setText(listItem.get(i).getPersonName());*/
            viewHolder.linearDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ReplyMessageList.class);
                    intent.putExtra("Message", listItem.get(i).getReplyMsg());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
        }


        @Override
        public int getItemCount() {
            return listItem.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public LinearLayout linearDetails;

            public TextView txtMessage;

            public TextView txtMsgCount;

            public TextView txtPersonCount;

            public ViewHolder(View view) {
                super(view);
                txtMessage = (TextView) view.findViewById(R.id.txtSendMsg);
                txtMsgCount = (TextView) view.findViewById(R.id.txtmsgCounter);
                txtPersonCount = (TextView) view.findViewById(R.id.txtMsgperson);
                linearDetails = (LinearLayout) view.findViewById(R.id.linearReplyDetail);
            }
        }
    }
}