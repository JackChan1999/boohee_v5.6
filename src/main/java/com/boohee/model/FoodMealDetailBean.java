package com.boohee.model;

import com.google.gson.Gson;

import java.util.List;

public class FoodMealDetailBean {
    private int            current_page;
    private List<MealBean> meals;
    private int            total_page;

    public List<MealBean> getMeals() {
        return this.meals;
    }

    public void setMeals(List<MealBean> meals) {
        this.meals = meals;
    }

    public int getTotal_page() {
        return this.total_page;
    }

    public void setTotal_page(int total_page) {
        this.total_page = total_page;
    }

    public int getCurrent_page() {
        return this.current_page;
    }

    public void setCurrent_page(int current_page) {
        this.current_page = current_page;
    }

    public static FoodMealDetailBean parse(String json) {
        return (FoodMealDetailBean) new Gson().fromJson(json, FoodMealDetailBean.class);
    }
}
