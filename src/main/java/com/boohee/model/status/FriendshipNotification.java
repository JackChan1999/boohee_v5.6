package com.boohee.model.status;

import com.google.gson.Gson;

import org.json.JSONObject;

public class FriendshipNotification extends Notification {
    public StatusUser follower;

    public static FriendshipNotification parseSelf(JSONObject object) {
        return (FriendshipNotification) new Gson().fromJson(object.toString(),
                FriendshipNotification.class);
    }
}
