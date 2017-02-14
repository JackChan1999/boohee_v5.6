package com.boohee.model.status;

import com.google.gson.Gson;

import org.json.JSONObject;

public class StoryCommentNotification extends Notification {
    public StoryComment story_comment;

    public static StoryCommentNotification parseSelf(JSONObject object) {
        return (StoryCommentNotification) new Gson().fromJson(object.toString(),
                StoryCommentNotification.class);
    }
}
