package com.boohee.one.event;

import com.boohee.model.CustomCookItem;

public class CustomCookItemEvent {
    private CustomCookItem customCookItem;

    public CustomCookItem getCustomCookItem() {
        return this.customCookItem;
    }

    public CustomCookItemEvent setCustomCookItem(CustomCookItem customCookItem) {
        this.customCookItem = customCookItem;
        return this;
    }
}
