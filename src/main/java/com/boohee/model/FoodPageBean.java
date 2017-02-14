package com.boohee.model;

import com.google.gson.Gson;

import java.util.List;

public class FoodPageBean {
    private FoodMealBean         meal_images;
    private List<FoodSliderBean> sliders;

    public List<FoodSliderBean> getSliders() {
        return this.sliders;
    }

    public void setSliders(List<FoodSliderBean> sliders) {
        this.sliders = sliders;
    }

    public static FoodPageBean parse(String json) {
        return (FoodPageBean) new Gson().fromJson(json, FoodPageBean.class);
    }

    public FoodMealBean getMeal_imagess() {
        return this.meal_images;
    }

    public void setMeal_imagess(FoodMealBean meal_imagess) {
        this.meal_images = meal_imagess;
    }
}
