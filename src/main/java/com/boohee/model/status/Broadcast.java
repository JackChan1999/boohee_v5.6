package com.boohee.model.status;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import org.json.JSONArray;

public class Broadcast {
    public String  created_at;
    public String  from;
    public int     id;
    public String  preview_body;
    public boolean read;
    public String  title;

    public static ArrayList<Broadcast> parseBroadacasts(JSONArray arrays) {
        if (arrays == null || arrays.length() <= 0) {
            return null;
        }
        return (ArrayList) new Gson().fromJson(arrays.toString(), new
                TypeToken<ArrayList<Broadcast>>() {
        }.getType());
    }
}
