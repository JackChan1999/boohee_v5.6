package com.boohee.model.status;

import com.google.gson.Gson;

import org.json.JSONObject;

public class FeedbackNotification extends Notification {
    public Feedback feedback;

    public class Feedback {
        public int        diamond;
        public Post       post;
        public String     type;
        public StatusUser user;
    }

    public static FeedbackNotification parseSelf(JSONObject object) {
        return (FeedbackNotification) new Gson().fromJson(object.toString(), FeedbackNotification.class);
    }
}
