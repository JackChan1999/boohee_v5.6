package com.boohee.model.status;

import com.google.gson.Gson;

import org.json.JSONObject;

public class StoryMentionNotification extends Notification {
    public StoryMention story_mention;

    public static StoryMentionNotification parseSelf(JSONObject object) {
        return (StoryMentionNotification) new Gson().fromJson(object.toString(),
                StoryMentionNotification.class);
    }
}
