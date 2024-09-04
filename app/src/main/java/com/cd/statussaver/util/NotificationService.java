package com.cd.statussaver.util;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
import android.provider.ContactsContract;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.text.SpannableString;
import android.util.Log;
import android.widget.TextView;

import androidx.core.app.NotificationCompat;

import com.cd.statussaver.model.ActionModel;
import com.cd.statussaver.model.AutoReply;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;


public class NotificationService extends NotificationListenerService {

    public static final String TAG = "Auto Reply";
    private String autoReplyText;
    private List<String> cntList;
    private String contactType;
    private long count = 0;
    private List<String> getUserList;
    private String headerText;
    private String noMatch = "";
    private SharedPreference preference;
    private int replyTime;
    private String sendMessage;
    private TextView text;


    @Override
    public void onListenerConnected() {
        super.onListenerConnected();
        Log.e(TAG, "Notification Listener Connected");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        preference = new SharedPreference(this);
        count = preference.getFromPref_Long("Counter");
        getUserList = new ArrayList();
        cntList = new ArrayList();
        Const.staticsReplyList = new ArrayList();
        Const.contactList = new ArrayList();
    }

    @Override
    public int onStartCommand(Intent intent, int i, int i2) {
        Log.e("Service Class", "Service is started-----");
        return Service.START_STICKY;
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification statusBarNotification) {
        super.onNotificationRemoved(statusBarNotification);
        Log.e("AUTO REPLY", "Notificationremoved");
    }

    @Override
    public void onNotificationPosted(StatusBarNotification statusBarNotification) {
        if (preference.getFromPref_Boolean("CheckedState")) {
            boolean fromPref_Boolean = preference.getFromPref_Boolean("ScheduleTime");
            Log.e("scheduleTime", fromPref_Boolean + ":::");
            contactType = preference.getFromPref_String("ContactType");
            Const.contactList = preference.getContactList("ContactList");
            try {
                if (Const.contactList.isEmpty()) {
                    Const.contactList = new ArrayList();
                }
            } catch (Exception unused) {
                Const.contactList = new ArrayList();
            }
            Const.replyList = preference.getList("MessageList");
            try {
                if (Const.replyList.isEmpty()) {
                    Const.replyList = new ArrayList();
                }
            } catch (Exception unused2) {
                Const.replyList = new ArrayList();
            }
            Const.staticsReplyList = preference.getReplyList("StaticsReplyList");
            try {
                if (Const.staticsReplyList.isEmpty()) {
                    Const.staticsReplyList = new ArrayList();
                }
            } catch (Exception e) {
                Const.staticsReplyList = new ArrayList();
                Log.e("StaticsList", e.getMessage());
            }
            String fromPref_String = preference.getFromPref_String("BoldHeaderText");
            if (fromPref_String.isEmpty()) {
                headerText = "*Auto Reply*\n\n";
            } else if (fromPref_String.equals(" ")) {
                headerText = fromPref_String;
            } else {
                headerText = "*" + fromPref_String + "*\n\n";
            }
            autoReplyText = headerText + preference.getFromPref_String("autoReplyText");
            if (isScheduleTime(fromPref_Boolean)) {
                Log.e("IS SCHEDULE TIME", isScheduleTime(fromPref_Boolean) + "::::");
//             cancelNotification(statusBarNotification.getKey());
                final ActionModel quickReplyAction = NotficationUtils.getQuickReplyAction(statusBarNotification.getNotification(), getPackageName());
                Log.e("Action", quickReplyAction + "::Action:");
                if (quickReplyAction == null) {
                    return;
                }
                if (statusBarNotification.getPackageName().equalsIgnoreCase("com.whatsapp")||statusBarNotification.getPackageName().equalsIgnoreCase("com.whatsapp.w4b"))
                {
                    if (statusBarNotification.getPackageName().equalsIgnoreCase("com.whatsapp")&&preference.getFromPref_Boolean("WAState")&&preference.getFromPref_Boolean("CheckedState")){
                        final String string = statusBarNotification.getNotification().extras.getString(NotificationCompat.EXTRA_TITLE);
                        Log.e("USERNAME", string + ":::");
                        final String string2 = statusBarNotification.getNotification().extras.getString(NotificationCompat.EXTRA_TEXT);
                        if (EveryOne(contactType) || ContactList(contactType, string) || ExceptContactList(contactType, string) || ExceptPhoneList(contactType, string)) {
                            Log.e("CONTACT TYPE", contactType + "::::");
                            if (string2 != null && !string2.equalsIgnoreCase("📷 Photo") && isGroupMessageAndReplyAllowed(statusBarNotification) && NotficationUtils.isNewNotification(statusBarNotification) && selfName(statusBarNotification)) {
                                if (preference.getFromPref_Boolean("Immediately")) {
                                    Log.e("Immediately", "True");
                                    sendMsg(quickReplyAction, string2, string);
                                } else if (preference.getFromPref_Boolean("Time")) {
                                    Log.e("Time", "True");
                                    String fromPref_String2 = preference.getFromPref_String("TimeofMsg");
                                    String fromPref_String3 = preference.getFromPref_String("SpinTime");
                                    if (fromPref_String3.equals("Minutes")) {
                                        replyTime = Integer.valueOf(fromPref_String2).intValue() * 1000 * 60;
                                    } else if (fromPref_String3.equals("Seconds")) {
                                        replyTime = Integer.valueOf(fromPref_String2).intValue() * 1000;
                                    }
                                    Log.e("REPLY TIME", replyTime + ":::::");
                                    new Handler().postDelayed(new Runnable() {
                                        public void run() {
                                            sendMsg(quickReplyAction, string2, string);
                                        }
                                    }, (long) replyTime);
                                } else if (preference.getFromPref_Boolean("Once")) {
                                    Log.e("Once", "True");
                                    List<String> userList = preference.getUserList("UserList");
                                    getUserList = userList;
                                    try {
                                        if (userList.isEmpty()) {
                                            getUserList = new ArrayList();
                                        }
                                    } catch (Exception e2) {
                                        Log.e("ONCE", e2.getMessage());
                                        getUserList = new ArrayList();
                                    }
                                    if (getUserList.contains(string)) {
                                        stopService(new Intent(getApplicationContext(), getClass()));
                                    } else {
                                        sendMsg(quickReplyAction, string2, string);
                                    }
                                } else {
                                    Log.e("ELse", "True");
                                    sendMsg(quickReplyAction, string2, string);
                                }
                            }
                        }
                    }
                    if (statusBarNotification.getPackageName().equalsIgnoreCase("com.whatsapp.w4b")&&preference.getFromPref_Boolean("WBState")&&preference.getFromPref_Boolean("CheckedState")){
                        final String string = statusBarNotification.getNotification().extras.getString(NotificationCompat.EXTRA_TITLE);
                        Log.e("USERNAME", string + ":::");
                        final String string2 = statusBarNotification.getNotification().extras.getString(NotificationCompat.EXTRA_TEXT);
                        if (EveryOne(contactType) || ContactList(contactType, string) || ExceptContactList(contactType, string) || ExceptPhoneList(contactType, string)) {
                            Log.e("CONTACT TYPE", contactType + "::::");
                            if (string2 != null && !string2.equalsIgnoreCase("📷 Photo") && isGroupMessageAndReplyAllowed(statusBarNotification) && NotficationUtils.isNewNotification(statusBarNotification) && selfName(statusBarNotification)) {
                                if (preference.getFromPref_Boolean("Immediately")) {
                                    Log.e("Immediately", "True");
                                    sendMsg(quickReplyAction, string2, string);
                                } else if (preference.getFromPref_Boolean("Time")) {
                                    Log.e("Time", "True");
                                    String fromPref_String2 = preference.getFromPref_String("TimeofMsg");
                                    String fromPref_String3 = preference.getFromPref_String("SpinTime");
                                    if (fromPref_String3.equals("Minutes")) {
                                        replyTime = Integer.valueOf(fromPref_String2).intValue() * 1000 * 60;
                                    } else if (fromPref_String3.equals("Seconds")) {
                                        replyTime = Integer.valueOf(fromPref_String2).intValue() * 1000;
                                    }
                                    Log.e("REPLY TIME", replyTime + ":::::");
                                    new Handler().postDelayed(new Runnable() {
                                        public void run() {
                                            sendMsg(quickReplyAction, string2, string);
                                        }
                                    }, (long) replyTime);
                                } else if (preference.getFromPref_Boolean("Once")) {
                                    Log.e("Once", "True");
                                    List<String> userList = preference.getUserList("UserList");
                                    getUserList = userList;
                                    try {
                                        if (userList.isEmpty()) {
                                            getUserList = new ArrayList();
                                        }
                                    } catch (Exception e2) {
                                        Log.e("ONCE", e2.getMessage());
                                        getUserList = new ArrayList();
                                    }
                                    if (getUserList.contains(string)) {
                                        stopService(new Intent(getApplicationContext(), getClass()));
                                    } else {
                                        sendMsg(quickReplyAction, string2, string);
                                    }
                                } else {
                                    Log.e("ELse", "True");
                                    sendMsg(quickReplyAction, string2, string);
                                }
                            }
                        }
                    }

                }

                else {
                    Log.e(TAG, "not success");
                }
            }
        }
    }

    @Override
    public void onTaskRemoved(Intent intent) {
        super.onTaskRemoved(intent);
        Intent intent2 = new Intent(getApplicationContext(), getClass());
        intent2.setPackage(getPackageName());
        startService(intent2);
    }


    public void sendMsg(ActionModel action, String str, String str2) {
        String str3;
        String str4;
        boolean z;
        try {
            Log.e("RECEIVE MSG", str);
            Const.replyList.isEmpty();
            boolean z2 = false;
            boolean isContain=preference.getFromPref_Boolean("contain");
            boolean isExtra=preference.getFromPref_Boolean("exact");
            Log.e("rules","contain=="+String.valueOf(isContain)+"    "+"exact==="+String.valueOf(isExtra));
            boolean fromPref_Boolean = preference.getFromPref_Boolean("customAutoReplySwitch");
            Log.e("CustomReply", fromPref_Boolean + ":::");
            Iterator<AutoReply> it = Const.replyList.iterator();
            while (true) {
                if (!it.hasNext()) {
                    str3 = "autoReplyText";
                    str4 = "Counter";
                    z = true;
                    break;
                }
                AutoReply next = it.next();
                if (isContain?next.getReceiveMsg().equalsIgnoreCase(str)||str.contains(next.getReceiveMsg()):next.getReceiveMsg().equalsIgnoreCase(str)) {
                    if (fromPref_Boolean) {
                        long currentTimeMillis = System.currentTimeMillis();
                        Context applicationContext = getApplicationContext();
                        action.sendReply(applicationContext, headerText + next.getSendMsg());
                        stopService(new Intent(getApplicationContext(), getClass()));
                        long j = count + 1;
                        count = j;
                        preference .addToPref_Long("Counter", j);
                        sendMessage = next.getSendMsg();
                        List<StatisticsReplyMsgListModel> list = Const.staticsReplyList;

                        str3 = "autoReplyText";
                        str4 = "Counter";
                        z = true;
                        StatisticsReplyMsgListModel statisticsReplyMsgListModel = new StatisticsReplyMsgListModel((int) count, next.getSendMsg(), str2, currentTimeMillis);
                        list.add(statisticsReplyMsgListModel);
                        preference.setReplyList("StaticsReplyList", Const.staticsReplyList);
                        if (preference.getFromPref_Boolean("Once") && !Const.userList.contains(str2)) {
                            Const.userList.add(str2);
                            preference.setUserList("UserList", Const.userList);
                        }
                    } else {
                        str3 = "autoReplyText";
                        str4 = "Counter";
                        z = true;
                        stopService(new Intent(getApplicationContext(), getClass()));
                    }
                    z2 = true;
                }
            }
            if (!z2 && preference.getFromPref_Boolean("autoReplyTextSwitch") == z) {
                Log.e(TAG, "Else");
                if (autoReplyText.equals(str)) {
                    Log.e("Equal", "Message");
                    stopService(new Intent(getApplicationContext(), getClass()));
                }
                long currentTimeMillis2 = System.currentTimeMillis();
                action.sendReply(getApplicationContext(), autoReplyText);
                stopService(new Intent(getApplicationContext(), getClass()));
                String str7 = str3;
                sendMessage = preference.getFromPref_String(str7);
                long j2 = count + 1;
                count = j2;
                preference.addToPref_Long(str4, j2);
                Const.staticsReplyList.add(new StatisticsReplyMsgListModel((int) count, preference.getFromPref_String(str7), str2, currentTimeMillis2));
                preference.setReplyList("StaticsReplyList", Const.staticsReplyList);
                if (preference.getFromPref_Boolean("Once") == z && !Const.userList.contains(str2)) {
                    Const.userList.add(str2);
                    preference.setUserList("UserList", Const.userList);
                }
            }
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
            Log.e("Replied Message Error", e.getMessage());
        }
    }

    private boolean selfName(StatusBarNotification statusBarNotification) {
        String title = NotficationUtils.getTitle(statusBarNotification);
        return title == null || !title.equalsIgnoreCase(statusBarNotification.getNotification().extras.getString(NotificationCompat.EXTRA_SELF_DISPLAY_NAME));
    }

    private boolean isGroupMessageAndReplyAllowed(StatusBarNotification statusBarNotification) {
        String titleRaw = NotficationUtils.getTitleRaw(statusBarNotification);
        SpannableString valueOf = SpannableString.valueOf("" + statusBarNotification.getNotification().extras.get(NotificationCompat.EXTRA_TEXT));
        boolean z = titleRaw != null && (": ".contains(titleRaw) || "@ ".contains(titleRaw)) && valueOf != null && valueOf.toString().contains("📷");
        if (!statusBarNotification.getNotification().extras.getBoolean(NotificationCompat.EXTRA_IS_GROUP_CONVERSATION)) {
            return !z;
        }
        boolean fromPref_Boolean = preference.getFromPref_Boolean("GroupEnable");
        Log.e("Group Message", "Return" + fromPref_Boolean);
        return fromPref_Boolean;
    }

    private boolean EveryOne(String str) {
        return str.equals("Everyone") || str.equals("");
    }

    private boolean ContactList(String str, String str2) {
        return str.equals("ContactList") && Const.contactList.contains(str2);
    }

    private boolean ExceptContactList(String str, String str2) {
        return str.equals("ExceptContList") && !Const.contactList.contains(str2);
    }

    @SuppressLint("Range")
    private boolean ExceptPhoneList(String str, String str2) {
        if (!str.equals("ExceptPhoneList")) {
            return false;
        }
        Cursor query = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, (String[]) null, (String) null, (String[]) null, "display_name ASC");
        while (query.moveToNext()) {
            cntList.add(query.getString(query.getColumnIndex("display_name")));
        }
        query.close();
        return !cntList.contains(str2);
    }



    private boolean isScheduleTime(boolean z) {
        if (z) {
            try {
                Calendar instance = Calendar.getInstance();
                String fromPref_String = preference.getFromPref_String("StartTime");
                String fromPref_String2 = preference.getFromPref_String("EndTime");
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a");
                Date parse = simpleDateFormat.parse(simpleDateFormat.format(instance.getTime()));
                Date parse2 = simpleDateFormat.parse(fromPref_String);
                Date parse3 = simpleDateFormat.parse(fromPref_String2);
                if (!parse.after(parse2) || !parse.before(parse3)) {
                    return false;
                }
                return true;
            } catch (ParseException e) {
                e.printStackTrace();
                Log.e("CURRENTTIME", e.getMessage());
            }
        }
        return true;
    }
}
