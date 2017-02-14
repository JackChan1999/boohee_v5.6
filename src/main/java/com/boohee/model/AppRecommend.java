package com.boohee.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class AppRecommend {
    public String icon;
    public String name;
    public String url;

    public AppRecommend(String name, String icon, String url) {
        this.name = name;
        this.icon = icon;
        this.url = url;
    }

    public static ArrayList<AppRecommend> parseAppRecommends(String result) {
        try {
            return (ArrayList) new Gson().fromJson(result, new TypeToken<ArrayList<AppRecommend>>
                    () {
            }.getType());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
