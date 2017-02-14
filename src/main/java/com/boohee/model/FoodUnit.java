package com.boohee.model;

public class FoodUnit extends ModelBase {
    public float  eating_weight;
    public int    food_id;
    public int    food_type;
    public int    food_unit_id;
    public String unit_name;
    public float  weight;

    public float getCalory(Food food) {
        return (food.getCalory() * this.eating_weight) / 100.0f;
    }

    public float getCalory(float calory) {
        return (this.eating_weight * calory) / 100.0f;
    }
}
