package com.boohee.model.status;

import com.google.gson.Gson;

import org.json.JSONObject;

public class UnreadCount {
    public int count;
    public int unread_broadcast_notification_count;
    public int unread_comment_notification_count;
    public int unread_feedback_notification_count;
    public int unread_friendship_notification_count;
    public int unread_group_notification_count;
    public int unread_mention_notification_count;
    public int unread_repost_notification_count;

    public static UnreadCount parseSelf(JSONObject object) {
        if (object == null) {
            return null;
        }
        return (UnreadCount) new Gson().fromJson(object.toString(), UnreadCount.class);
    }
}
