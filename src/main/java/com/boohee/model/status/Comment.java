package com.boohee.model.status;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import org.json.JSONArray;

public class Comment {
    public String     body;
    public String     created_at;
    public int        id;
    public Post       post;
    public StatusUser user;

    public static ArrayList<Comment> parseComments(JSONArray array) {
        if (array == null) {
            return null;
        }
        return (ArrayList) new Gson().fromJson(array.toString(), new
                TypeToken<ArrayList<Comment>>() {
        }.getType());
    }
}
