package com.boohee.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class Region extends ModelBase {
    private static final long serialVersionUID = 1;
    public List<Area> cities;
    public String     name;

    public static List<Region> parseRegions(String jsonString) {
        try {
            return (List) new Gson().fromJson(jsonString, new TypeToken<List<Region>>() {
            }.getType());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String toString() {
        return "Region [name=" + this.name + ", cities=" + this.cities + "]";
    }
}
