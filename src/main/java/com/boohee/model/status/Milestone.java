package com.boohee.model.status;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import org.json.JSONObject;

public class Milestone {
    public boolean achieved;
    public String  key_note;
    public int     order;
    public String  title;

    public static ArrayList<Milestone> parseLists(JSONObject object) {
        if (!object.has("achieved_milestones")) {
            return null;
        }
        return (ArrayList) new Gson().fromJson(object.optString("achieved_milestones"), new
                TypeToken<ArrayList<Milestone>>() {
        }.getType());
    }
}
