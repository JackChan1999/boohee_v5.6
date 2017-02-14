package com.boohee.model;

public class CustomCook extends ModelBase {
    public float  amount;
    public float  calory;
    public String code;
    public String food_name;
    public int    food_unit_id;
    public String unit_name;

    public CustomCook(String food_name, String code, float amount, float calory, int
            food_unit_id, String unit_name, int id) {
        this.food_name = food_name;
        this.code = code;
        this.amount = amount;
        this.calory = calory;
        this.food_unit_id = food_unit_id;
        this.unit_name = unit_name;
        this.id = id;
    }
}
