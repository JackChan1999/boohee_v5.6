package com.boohee.model.mine;

import com.google.gson.Gson;

import org.json.JSONObject;

public class McLatest {
    public static String ACTION_END   = "end";
    public static String ACTION_START = "start";
    public String action;
    public int    mc_distance;
    public int    mc_index;
    public String next_start_on;
    public String oviposit_day;

    public static McLatest parseSelf(JSONObject object) {
        if (object == null) {
            return null;
        }
        return (McLatest) new Gson().fromJson(object.toString(), McLatest.class);
    }
}
