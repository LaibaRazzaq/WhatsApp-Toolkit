package com.cd.statussaver.model;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.RemoteInput;

import com.cd.statussaver.util.NotificationService;

import java.util.ArrayList;
import java.util.Iterator;

public class ActionModel implements Parcelable {
    public static final Creator CREATOR = new Creator() {
        public ActionModel createFromParcel(Parcel parcel) {
            return new ActionModel(parcel);
        }

        public ActionModel[] newArray(int i) {
            return new ActionModel[i];
        }
    };
    private final boolean isQuickReply;
    private final PendingIntent p;
    private final String packageName;
    private final ArrayList<RemoteInputParcel> remoteInputs;
    private final String text;

    public int describeContents() {
        return 0;
    }

    public ActionModel(Parcel parcel) {
        ArrayList<RemoteInputParcel> arrayList = new ArrayList<>();
        remoteInputs = arrayList;
        text = parcel.readString();
        packageName = parcel.readString();
        p = (PendingIntent) parcel.readParcelable(PendingIntent.class.getClassLoader());
        isQuickReply = parcel.readByte() != 0;
        parcel.readTypedList(arrayList, RemoteInputParcel.CREATOR);
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(text);
        parcel.writeString(packageName);
        parcel.writeParcelable(p, i);
        parcel.writeByte(isQuickReply ? (byte) 1 : 0);
        parcel.writeTypedList(remoteInputs);
    }

    public ActionModel(String str, String str2, PendingIntent pendingIntent, RemoteInput remoteInput, boolean z) {
        ArrayList<RemoteInputParcel> arrayList = new ArrayList<>();
        remoteInputs = arrayList;
        text = str;
        packageName = str2;
        p = pendingIntent;
        isQuickReply = z;
        arrayList.add(new RemoteInputParcel(remoteInput));
    }

    public ActionModel(NotificationCompat.Action action, String str, boolean z) {
        remoteInputs = new ArrayList<>();
        text = action.title.toString();
        packageName = str;
        p = action.actionIntent;
        if (action.getRemoteInputs() != null) {
            for (RemoteInput remoteInputParcel : action.getRemoteInputs()) {
                remoteInputs.add(new RemoteInputParcel(remoteInputParcel));
            }
        }
        isQuickReply = z;
    }

    public void sendReply(Context context, String str) throws PendingIntent.CanceledException {
        Log.e("AutoReply", "inside sendReply");
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        ArrayList arrayList = new ArrayList();
        Iterator<RemoteInputParcel> it = remoteInputs.iterator();
        while (it.hasNext()) {
            RemoteInputParcel next = it.next();
            Log.e("", "RemoteInput: " + next.getLabel());
            bundle.putCharSequence(next.getResultKey(), str);
            RemoteInput.Builder builder = new RemoteInput.Builder(next.getResultKey());
            builder.setLabel(next.getLabel());
            builder.setChoices(next.getChoices());
            builder.setAllowFreeFormInput(next.isAllowFreeFormInput());
            builder.addExtras(next.getExtras());
            arrayList.add(builder.build());
        }
        RemoteInput.addResultsToIntent((RemoteInput[]) arrayList.toArray(new RemoteInput[arrayList.size()]), intent, bundle);
        p.send(context, 0, intent);
        context.stopService(new Intent(context, NotificationService.class));
    }

    public ArrayList<RemoteInputParcel> getRemoteInputs() {
        return remoteInputs;
    }

    public boolean isQuickReply() {
        return isQuickReply;
    }

    public String getText() {
        return text;
    }

    public PendingIntent getQuickReplyIntent() {
        if (isQuickReply) {
            return p;
        }
        return null;
    }

    public String getPackageName() {
        return packageName;
    }
}
