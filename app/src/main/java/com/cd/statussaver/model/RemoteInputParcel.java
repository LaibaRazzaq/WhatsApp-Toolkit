package com.cd.statussaver.model;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.core.app.RemoteInput;

public class RemoteInputParcel implements Parcelable {
    public static final Creator CREATOR = new Creator() {
        public RemoteInputParcel createFromParcel(Parcel parcel) {
            return new RemoteInputParcel(parcel);
        }

        public RemoteInputParcel[] newArray(int i) {
            return new RemoteInputParcel[i];
        }
    };
    private boolean allowFreeFormInput;
    private String[] choices = new String[0];
    private Bundle extras;
    private String label;
    private String resultKey;

    public int describeContents() {
        return 0;
    }

    public RemoteInputParcel(RemoteInput remoteInput) {
        label = remoteInput.getLabel().toString();
        resultKey = remoteInput.getResultKey();
        charSequenceToStringArray(remoteInput.getChoices());
        allowFreeFormInput = remoteInput.getAllowFreeFormInput();
        extras = remoteInput.getExtras();
    }

    public RemoteInputParcel(Parcel parcel) {
        boolean z = false;
        label = parcel.readString();
        resultKey = parcel.readString();
        choices = parcel.createStringArray();
        allowFreeFormInput = parcel.readByte() != 0 ? true : z;
        extras = (Bundle) parcel.readParcelable(Bundle.class.getClassLoader());
    }

    public void charSequenceToStringArray(CharSequence[] charSequenceArr) {
        if (charSequenceArr != null) {
            int length = charSequenceArr.length;
            choices = new String[charSequenceArr.length];
            for (int i = 0; i < length; i++) {
                choices[i] = charSequenceArr[i].toString();
            }
        }
    }

    public String getResultKey() {
        return resultKey;
    }

    public String getLabel() {
        return label;
    }

    public CharSequence[] getChoices() {
        return choices;
    }

    public boolean isAllowFreeFormInput() {
        return allowFreeFormInput;
    }

    public Bundle getExtras() {
        return extras;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(label);
        parcel.writeString(resultKey);
        parcel.writeStringArray(choices);
        parcel.writeByte(allowFreeFormInput ? (byte) 1 : 0);
        parcel.writeParcelable(extras, i);
    }
}
