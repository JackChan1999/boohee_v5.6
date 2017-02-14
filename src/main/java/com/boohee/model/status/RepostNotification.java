package com.boohee.model.status;

import com.google.gson.Gson;

import org.json.JSONObject;

public class RepostNotification extends Notification {
    public Repost repost;

    public class Repost {
        public Post       post;
        public StatusUser user;
    }

    public static RepostNotification parseSelf(JSONObject object) {
        return (RepostNotification) new Gson().fromJson(object.toString(), RepostNotification
                .class);
    }
}
