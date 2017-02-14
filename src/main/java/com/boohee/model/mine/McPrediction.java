package com.boohee.model.mine;

import com.google.gson.Gson;

import org.json.JSONObject;

public class McPrediction {
    public Menstruation current_mc;
    public Menstruation next_mc;

    public static McPrediction parseSelf(JSONObject object) {
        if (object == null) {
            return null;
        }
        return (McPrediction) new Gson().fromJson(object.toString(), McPrediction.class);
    }
}
