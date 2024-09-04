package com.cd.statussaver.util;

import com.cd.statussaver.model.AutoReply;

import java.util.ArrayList;
import java.util.List;

public class Const {

    private static final int DAY_MILLIS = 86400000;
    private static final int HOUR_MILLIS = 3600000;
    private static final int MINUTE_MILLIS = 60000;
    public static final int REQ_NOTIFICATION_LISTENER = 100;
    private static final int SECOND_MILLIS = 1000;
    public static List<String> autoText = new ArrayList();
    public static List<String> contactList = new ArrayList();
    public static List<AutoReply> replyList = new ArrayList();
    public static List<StatisticsReplyMsgListModel> staticsReplyList = new ArrayList();
    public static List<String> userList = new ArrayList();
    public static String getTimeAgo(long j) {
        if (j < 1000000000000L) {
            j *= 1000;
        }
        long currentTimeMillis = System.currentTimeMillis();
        if (j > currentTimeMillis || j <= 0) {
            return null;
        }
        long j2 = currentTimeMillis - j;
        if (j2 < 60000) {
            return "just now";
        }
        if (j2 < 120000) {
            return "a minute ago";
        }
        if (j2 < 3000000) {
            return (j2 / 60000) + " minutes ago";
        } else if (j2 < 5400000) {
            return "an hour ago";
        } else {
            if (j2 < 86400000) {
                return (j2 / 3600000) + " hours ago";
            } else if (j2 < 172800000) {
                return "yesterday";
            } else {
                return (j2 / 86400000) + " days ago";
            }
        }
    }
}
