package com.boohee.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class FoodSliderBean {
    private String link_type;
    private String pic_url;
    private String sub_title;
    private String title;
    private String url;

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPic_url() {
        return this.pic_url;
    }

    public void setPic_url(String pic_url) {
        this.pic_url = pic_url;
    }

    public String getLink_type() {
        return this.link_type;
    }

    public void setLink_type(String link_type) {
        this.link_type = link_type;
    }

    public List<FoodSliderBean> parseList(String json) {
        return (List) new Gson().fromJson(json, new TypeToken<List<Category>>() {
        }.getType());
    }

    public String getSub_title() {
        return this.sub_title;
    }

    public void setSub_title(String sub_title) {
        this.sub_title = sub_title;
    }
}
