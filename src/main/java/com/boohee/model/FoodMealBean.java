package com.boohee.model;

import com.google.gson.Gson;

import java.util.List;

public class FoodMealBean {
    private List<String> breakfast;
    private List<String> lunch;
    private List<String> supper;

    public List<String> getBreakfast() {
        return this.breakfast;
    }

    public List<String> getLunch() {
        return this.lunch;
    }

    public void setLunch(List<String> lunch) {
        this.lunch = lunch;
    }

    public List<String> getSupper() {
        return this.supper;
    }

    public void setSupper(List<String> supper) {
        this.supper = supper;
    }

    public void setBreakfast(List<String> breakfast) {
        this.breakfast = breakfast;
    }

    public static FoodMealBean parse(String json) {
        return (FoodMealBean) new Gson().fromJson(json, FoodMealBean.class);
    }
}
