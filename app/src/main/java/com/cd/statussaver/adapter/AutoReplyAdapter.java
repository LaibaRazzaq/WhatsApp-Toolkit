package com.cd.statussaver.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.cd.statussaver.R;
import com.cd.statussaver.activity.AddCustomReplyMessageActivity;
import com.cd.statussaver.model.AutoReply;
import com.cd.statussaver.util.SharedPreference;

import java.util.List;

public class AutoReplyAdapter  extends RecyclerView.Adapter<AutoReplyAdapter.Viewholder> {
    public Context context;
    public List<AutoReply> listitem;
    private SharedPreference preference;



    public AutoReplyAdapter(Context context2, List<AutoReply> list) {
        context = context2;
        listitem = list;
    }

    @Override
    public Viewholder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.add_message_design_layout, viewGroup, false);
        preference = new SharedPreference(context);
        return new Viewholder(inflate);
    }

    @Override
    public void onBindViewHolder(Viewholder viewholder, int i) {
        viewholder.ReceiveMsg.setText(listitem.get(i).getReceiveMsg());
        viewholder.SendMsg.setText(listitem.get(i).getSendMsg());
        viewholder.messageCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AddCustomReplyMessageActivity.class);
                intent.putExtra("ReceiveMessage", listitem.get(i).getReceiveMsg());
                intent.putExtra("SendMessage", listitem.get(i).getSendMsg());
                intent.putExtra("ActivityValue", "Update");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        viewholder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AddCustomReplyMessageActivity.class);
                intent.putExtra("ReceiveMessage", listitem.get(i).getReceiveMsg());
                intent.putExtra("SendMessage", listitem.get(i).getSendMsg());
                intent.putExtra("ActivityValue", "Update");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        viewholder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeItem(i);
            }
        });
    }

    @Override
    public int getItemCount()



    {
        return listitem.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        public TextView ReceiveMsg;
        public TextView SendMsg;
        public LinearLayout messageCardView;
        public ImageView imgEdit,imgDelete;

        public Viewholder(View view) {
            super(view);
            ReceiveMsg = (TextView) view.findViewById(R.id.txtIncomingMessage);
            SendMsg = (TextView) view.findViewById(R.id.txtReplyMessage);
            imgEdit = (ImageView) view.findViewById(R.id.imgEdit);
            imgDelete = (ImageView) view.findViewById(R.id.imgDelete);
            messageCardView = (LinearLayout) view.findViewById(R.id.linearUpdate);
        }
    }


    public void removeItem(int i) {
        listitem.remove(i);
        preference.setList("MessageList", listitem);
        notifyDataSetChanged();
    }


    public void restoreItem(AutoReply autoReply, int i) {
        listitem.add(i, autoReply);
        preference.setList("MessageList", listitem);
        notifyDataSetChanged();
    }

    public List<AutoReply> getData() {
        return listitem;
    }
}
