package com.boohee.model.status;

import com.google.gson.Gson;

import java.util.ArrayList;

import org.json.JSONObject;

public class Conditions {
    public ArrayList<Condition> conditions;
    public boolean              satisfied;

    public static Conditions parseConditionsFromJson(JSONObject object) {
        Conditions conditions = null;
        try {
            return (Conditions) new Gson().fromJson(object.toString(), Conditions.class);
        } catch (Exception e) {
            e.printStackTrace();
            return conditions;
        }
    }
}
