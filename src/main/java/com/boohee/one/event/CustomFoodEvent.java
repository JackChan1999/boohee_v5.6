package com.boohee.one.event;

import com.boohee.model.CustomFood;

public class CustomFoodEvent {
    private CustomFood customFood;

    public CustomFood getCustomFood() {
        return this.customFood;
    }

    public CustomFoodEvent setCustomFood(CustomFood customFood) {
        this.customFood = customFood;
        return this;
    }
}
