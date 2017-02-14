package com.boohee.one.event;

public class MyFoodEvent {
    public static final int CUSTOM_COOK_EVENT   = 3;
    public static final int CUSTOM_FOOD_EVENT   = 1;
    public static final int FOOD_FAVORITE_EVENT = 0;
    public static final int UPLOAD_FOOD_EVENT   = 2;
    private int flag;

    public int getFlag() {
        return this.flag;
    }

    public MyFoodEvent setFlag(int flag) {
        this.flag = flag;
        return this;
    }
}
