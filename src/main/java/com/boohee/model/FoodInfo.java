package com.boohee.model;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;

import java.util.List;

public class FoodInfo {
    public String             appraise;
    public String             calory;
    public String             carbohydrate;
    public String             code;
    public FoodCompare        compare;
    public String             fat;
    public String             fiber_dietary;
    public int                health_light;
    public int                id;
    public FoodIngredient     ingredient;
    public boolean            is_liquid;
    public String             large_image_url;
    public FoodLights         lights;
    public String             name;
    public String             protein;
    public boolean            recipe;
    public String             recipe_type;
    public String             thumb_image_url;
    public List<FoodInfoUnit> units;
    public String             uploader;
    public String             weight;

    public static FoodInfo parse(String json) {
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        FoodInfo result = null;
        try {
            return (FoodInfo) JSON.parseObject(json, FoodInfo.class);
        } catch (Exception e) {
            e.printStackTrace();
            return result;
        }
    }
}
