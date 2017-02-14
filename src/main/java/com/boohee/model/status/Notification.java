package com.boohee.model.status;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Notification {
    public static final String COMMENT       = "comment";
    public static final String FEEDBACK      = "feedback";
    public static final String FRIENDSHIP    = "friendship";
    public static final String MENTION       = "mention";
    public static final String REPOST        = "repost";
    public static final String STORY_COMMENT = "story_comment";
    public static final String STORY_MENTION = "story_mention";
    public String  created_at;
    public int     id;
    public boolean read;
    public String  read_at;
    public String  type;

    public static ArrayList<Notification> parseNotifications(JSONObject object) {
        ArrayList<Notification> notifications = new ArrayList();
        try {
            JSONArray arrays = object.getJSONArray("notifications");
            for (int i = 0; i < arrays.length(); i++) {
                JSONObject child = arrays.getJSONObject(i);
                String type = child.getString("type");
                if (FRIENDSHIP.equals(type)) {
                    FriendshipNotification fs = FriendshipNotification.parseSelf(child);
                    if (fs != null) {
                        notifications.add(fs);
                    }
                } else if (FEEDBACK.equals(type)) {
                    FeedbackNotification fn = FeedbackNotification.parseSelf(child);
                    if (fn != null) {
                        notifications.add(fn);
                    }
                } else if ("comment".equals(type)) {
                    CommentNotification cn = CommentNotification.parseSelf(child);
                    if (cn != null) {
                        notifications.add(cn);
                    }
                } else if (MENTION.equals(type)) {
                    MentionNotification mn = MentionNotification.parseSelf(child);
                    if (mn != null) {
                        notifications.add(mn);
                    }
                } else if (REPOST.equals(type)) {
                    RepostNotification rn = RepostNotification.parseSelf(child);
                    if (rn != null) {
                        notifications.add(rn);
                    }
                } else if (STORY_MENTION.equals(type)) {
                    StoryMentionNotification smn = StoryMentionNotification.parseSelf(child);
                    if (smn != null) {
                        notifications.add(smn);
                    }
                } else if (STORY_COMMENT.equals(type)) {
                    StoryCommentNotification scn = StoryCommentNotification.parseSelf(child);
                    if (scn != null) {
                        notifications.add(scn);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return notifications;
    }
}
