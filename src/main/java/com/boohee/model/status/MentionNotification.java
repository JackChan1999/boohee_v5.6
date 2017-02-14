package com.boohee.model.status;

import com.google.gson.Gson;

import org.json.JSONObject;

public class MentionNotification extends Notification {
    public Mention mention;

    public static MentionNotification parseSelf(JSONObject object) {
        return (MentionNotification) new Gson().fromJson(object.toString(), MentionNotification
                .class);
    }
}
