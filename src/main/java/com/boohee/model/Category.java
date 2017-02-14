package com.boohee.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class Category {
    public String      banner_link;
    public String      banner_pic_url;
    public String      category_type;
    public List<Event> events;
    public int         id;
    public String      more_url;
    public boolean     show_more;
    public String      title;

    public static List<Category> parseCategories(String str) {
        return (List) new Gson().fromJson(str, new TypeToken<List<Category>>() {
        }.getType());
    }

    public static Category parse(String json) {
        return (Category) new Gson().fromJson(json, Category.class);
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMore_url() {
        return this.more_url;
    }

    public void setMore_url(String more_url) {
        this.more_url = more_url;
    }

    public List<Event> getEvents() {
        return this.events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public String getBanner_link() {
        return this.banner_link;
    }

    public void setBanner_link(String banner_link) {
        this.banner_link = banner_link;
    }

    public String getBanner_pic_url() {
        return this.banner_pic_url;
    }

    public void setBanner_pic_url(String banner_pic_url) {
        this.banner_pic_url = banner_pic_url;
    }
}
