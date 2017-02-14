package com.boohee.model;

import com.boohee.utils.FastJsonUtils;

import java.util.List;

import org.json.JSONObject;

public class MonthMc {
    public String        oviposit_day;
    public List<Section> sections;
    public String        year_month;

    public class Section {
        public int    end;
        public int    start;
        public String type;
    }

    public static MonthMc parse(String json) {
        return (MonthMc) FastJsonUtils.fromJson(json, MonthMc.class);
    }

    public static MonthMc parse(JSONObject jsonObj) {
        return (MonthMc) FastJsonUtils.fromJson(jsonObj, MonthMc.class);
    }
}
