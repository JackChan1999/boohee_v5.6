package com.boohee.model.mine;

import com.google.gson.Gson;

import java.util.ArrayList;

import org.json.JSONObject;

public class MonthMc {
    public String             oviposit_day;
    public ArrayList<Section> sections;
    public String             year_month;

    public class Section {
        public int    end;
        public int    start;
        public String type;
    }

    public static MonthMc parseSelf(JSONObject object) {
        if (object == null) {
            return null;
        }
        return (MonthMc) new Gson().fromJson(object.toString(), MonthMc.class);
    }
}
