package com.boohee.one.event;

import com.boohee.model.CustomCook;

public class CustomCookEvent {
    private CustomCook customCook;

    public CustomCook getCustomCook() {
        return this.customCook;
    }

    public CustomCookEvent setCustomCook(CustomCook customCook) {
        this.customCook = customCook;
        return this;
    }
}
