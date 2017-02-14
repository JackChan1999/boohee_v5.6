package com.boohee.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import org.json.JSONObject;

public class Food extends ModelBase {
    public String              appraise;
    public float               calory;
    public float               calory_star;
    public String              calory_star_tag;
    public float               carbohydrate;
    public String              code;
    public CoreFood            core_food;
    public String              ext;
    public float               fat;
    public float               fiber_dietary;
    public int                 food_group_id;
    public int                 food_id;
    public int                 food_type;
    public int                 good_id;
    public int                 health_light;
    public float               healthy_star;
    public String              healthy_star_tag;
    public boolean             in_store;
    public String              large_image_name;
    public Lights              lights;
    public Materials           materials;
    public String              name;
    public float               protein;
    public int                 reduce_weight_star;
    public float               s_points;
    public float               satiety_star;
    public String              satiety_star_tag;
    public String              thumb_image_name;
    public ArrayList<FoodUnit> units;
    public String              verifier;
    public float               weight;

    public Food(int id, String code, int food_group_id, int food_id, String name, float weight,
                float calory, int food_type, String thumb_image_name, int reduce_weight_star, int
                        health_light, float s_points) {
        this.id = id;
        this.code = code;
        this.food_group_id = food_group_id;
        this.food_id = food_id;
        this.name = name;
        this.weight = weight;
        this.calory = calory;
        this.food_type = food_type;
        this.thumb_image_name = thumb_image_name;
        this.reduce_weight_star = reduce_weight_star;
        this.health_light = health_light;
        this.s_points = s_points;
    }

    public Food(int id, String code, int food_group_id, int food_id, String name, float weight,
                float calory, int food_type, String thumb_image_name, String large_image_name,
                String appraise, float calory_star, float satiety_star, float healthy_star, int
                        reduce_weight_star, String calory_star_tag, String satiety_star_tag,
                String healthy_star_tag, float protein, float fat, float carbohydrate, float
                        fiber_dietary, Materials materials, ArrayList<FoodUnit> units, CoreFood
                        core_food, String ext, int health_light, float s_points) {
        this(id, code, food_group_id, food_id, name, weight, calory, food_type, thumb_image_name,
                reduce_weight_star, health_light, s_points);
        this.large_image_name = large_image_name;
        this.appraise = appraise;
        this.calory_star = calory_star;
        this.satiety_star = satiety_star;
        this.healthy_star = healthy_star;
        this.calory_star_tag = calory_star_tag;
        this.satiety_star_tag = satiety_star_tag;
        this.healthy_star_tag = healthy_star_tag;
        this.protein = protein;
        this.fat = fat;
        this.carbohydrate = carbohydrate;
        this.fiber_dietary = fiber_dietary;
        this.materials = materials;
        this.units = units;
        this.core_food = core_food;
        this.ext = ext;
        this.s_points = s_points;
    }

    public float getCalory() {
        return (this.calory * 100.0f) / this.weight;
    }

    public String getCaloryString() {
        return String.format("%.0f", new Object[]{Float.valueOf(getCalory())});
    }

    public static Food parseFood(JSONObject res) {
        Food food = null;
        try {
            return (Food) new Gson().fromJson(res.toString(), Food.class);
        } catch (Exception e) {
            e.printStackTrace();
            return food;
        }
    }

    public static ArrayList<Food> parseFoods(JSONObject res) {
        ArrayList<Food> foods = null;
        try {
            return (ArrayList) new Gson().fromJson(res.optJSONArray("foods").toString(), new TypeToken<ArrayList<Food>>() {
            }.getType());
        } catch (Exception e) {
            e.printStackTrace();
            return foods;
        }
    }
}
