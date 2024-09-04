package com.cd.statussaver.util;

import android.app.Notification;
import android.os.Build;
import android.os.Parcelable;
import android.service.notification.StatusBarNotification;
import android.text.TextUtils;

import androidx.core.app.NotificationCompat;
import androidx.core.app.RemoteInput;
import androidx.work.WorkRequest;

import com.cd.statussaver.model.ActionModel;

import java.util.Iterator;


public class NotficationUtils {

    private static final CharSequence INPUT_KEYWORD = "input";
    private static final int MAX_OLD_NOTIFICATION_CAN_BE_REPLIED_TIME_MS = 30000;
    private static final CharSequence REPLY_KEYWORD = "reply";
    private static final String[] REPLY_KEYWORDS = {"reply", "android.intent.extra.text"};

    public static ActionModel getQuickReplyAction(Notification notification, String str) {
        NotificationCompat.Action quickReplyAction = getQuickReplyAction(notification);
        if (quickReplyAction == null) {
            quickReplyAction = getWearReplyAction(notification);
        }
        if (quickReplyAction == null) {
            return null;
        }
        return new ActionModel(quickReplyAction, str, true);
    }

    public static NotificationCompat.Action getQuickReplyAction(Notification notification) {
        for (int i = 0; i < NotificationCompat.getActionCount(notification); i++) {
            NotificationCompat.Action action = NotificationCompat.getAction(notification, i);
            if (action != null && action.getRemoteInputs() != null) {
                for (RemoteInput resultKey : action.getRemoteInputs()) {
                    if (isKnownReplyKey(resultKey.getResultKey())) {
                        return action;
                    }
                }
                continue;
            }
        }
        return null;
    }

    private static NotificationCompat.Action getWearReplyAction(Notification notification) {
        Iterator<NotificationCompat.Action> it = new NotificationCompat.WearableExtender(notification).getActions().iterator();
        while (it.hasNext()) {
            NotificationCompat.Action next = it.next();
            if (next.getRemoteInputs() != null) {
                for (RemoteInput remoteInput : next.getRemoteInputs()) {
                    if (isKnownReplyKey(remoteInput.getResultKey()) || remoteInput.getResultKey().toLowerCase().contains(INPUT_KEYWORD)) {
                        return next;
                    }
                }
                continue;
            }
        }
        return null;
    }

    private static boolean isKnownReplyKey(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        String lowerCase = str.toLowerCase();
        for (String contains : REPLY_KEYWORDS) {
            if (lowerCase.contains(contains)) {
                return true;
            }
        }
        return false;
    }

    public static String getTitle(StatusBarNotification statusBarNotification) {
        int lastIndexOf;
        int indexOf;
        if (statusBarNotification.getNotification().extras.getBoolean(NotificationCompat.EXTRA_IS_GROUP_CONVERSATION)) {
            String string = statusBarNotification.getNotification().extras.getString(NotificationCompat.EXTRA_HIDDEN_CONVERSATION_TITLE);
            if (string == null && (indexOf = (string = statusBarNotification.getNotification().extras.getString(NotificationCompat.EXTRA_TITLE)).indexOf(58)) != -1) {
                string = string.substring(0, indexOf);
            }
            Parcelable[] parcelableArr = (Parcelable[]) statusBarNotification.getNotification().extras.get(NotificationCompat.EXTRA_MESSAGES);
            return (parcelableArr == null || parcelableArr.length <= 1 || (lastIndexOf = string.lastIndexOf(40)) == -1) ? string : string.substring(0, lastIndexOf);
        }
        return statusBarNotification.getNotification().extras.getString(NotificationCompat.EXTRA_TITLE);
    }

    public static boolean isNewNotification(StatusBarNotification statusBarNotification) {
        return statusBarNotification.getNotification().when == 0 || System.currentTimeMillis() - statusBarNotification.getNotification().when < WorkRequest.DEFAULT_BACKOFF_DELAY_MILLIS;
    }

    public static String getTitleRaw(StatusBarNotification statusBarNotification) {
        return statusBarNotification.getNotification().extras.getString(NotificationCompat.EXTRA_TITLE);
    }
}
