package com.boohee.model.status;

import com.google.gson.Gson;

import org.json.JSONObject;

public class CommentNotification extends Notification {
    public Comment comment;

    public static CommentNotification parseSelf(JSONObject object) {
        return (CommentNotification) new Gson().fromJson(object.toString(), CommentNotification
                .class);
    }
}
