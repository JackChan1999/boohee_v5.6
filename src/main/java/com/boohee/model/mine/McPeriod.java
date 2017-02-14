package com.boohee.model.mine;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import org.json.JSONObject;

public class McPeriod {
    public int    cycle;
    public int    duration;
    public String end_on;
    public String predict_end_on;
    public String start_on;

    public static McPeriod parseSelf(JSONObject object) {
        if (object == null) {
            return null;
        }
        return (McPeriod) new Gson().fromJson(object.toString(), McPeriod.class);
    }

    public static ArrayList<McPeriod> parseMcList(JSONObject res) {
        ArrayList<McPeriod> mcPeriodlist = null;
        try {
            return (ArrayList) new Gson().fromJson(res.optJSONArray("mc_periods").toString(), new
                    TypeToken<ArrayList<McPeriod>>() {
            }.getType());
        } catch (Exception e) {
            e.printStackTrace();
            return mcPeriodlist;
        }
    }
}
