package com.boohee.model;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;

import java.util.List;

public class Recipe {
    public String                code;
    public List<RecipeCondiment> condiments;
    public int                   id;
    public String                image_url;
    public String                name;
    public List<RecipeStep>      steps;
    public String                tips;

    public static Recipe parse(String json) {
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        Recipe result = null;
        try {
            return (Recipe) JSON.parseObject(json, Recipe.class);
        } catch (Exception e) {
            e.printStackTrace();
            return result;
        }
    }
}
