package com.boohee.model.mine;

import com.google.gson.Gson;

import org.json.JSONObject;

public class McSummary {
    public static String MC_CIRCLE = "mc_circle";
    public static String MC_DAYS   = "mc_days";
    public int    count;
    public int    cycle;
    public int    duration;
    public String record_on;

    public static McSummary parseSelf(JSONObject object) {
        if (object == null) {
            return null;
        }
        return (McSummary) new Gson().fromJson(object.toString(), McSummary.class);
    }
}
