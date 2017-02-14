package com.boohee.model.mine;

import com.google.gson.Gson;

import org.json.JSONObject;

public class Menstruation {
    public String   current_day;
    public String[] mc_cycle;
    public String[] mc_duration;
    public String[] oviposit_day;
    public String[] pregnancy_day;

    public static Menstruation parseSelf(JSONObject object) {
        if (object == null) {
            return null;
        }
        return (Menstruation) new Gson().fromJson(object.toString(), Menstruation.class);
    }
}
