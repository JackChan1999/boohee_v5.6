package com.boohee.model;

public class FoodGroup extends ModelBase {
    public static final int IS_MENU  = 1;
    public static final int NOT_MENU = 0;
    public String appraise;
    public int    count_lock;
    public int    count_unlock;
    public int    is_menu;
    public String name;
    public String photo_url;

    public FoodGroup(int id, String name, String photo_url, int is_menu, String appraise, int
            count_lock, int count_unlock) {
        this.id = id;
        this.name = name;
        this.photo_url = photo_url;
        this.is_menu = is_menu;
        this.appraise = appraise;
        this.count_lock = count_lock;
        this.count_unlock = count_unlock;
    }
}
