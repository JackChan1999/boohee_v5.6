package com.boohee.model;

import com.boohee.utility.Event;
import com.boohee.utility.ImageLoaderOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class HomeSlider implements CustomSliderImage {
    String link_type;
    String pic_url;
    String title;
    String url;

    public static List<HomeSlider> parseSliders(String str) {
        return (List) new Gson().fromJson(str, new TypeToken<List<HomeSlider>>() {
        }.getType());
    }

    public String getTitle() {
        return this.title;
    }

    public String getPicUrl() {
        return this.pic_url;
    }

    public String getLink() {
        return this.url;
    }

    public String getMobEvent() {
        return Event.status_top_pic;
    }

    public int getDefaultImage() {
        return ImageLoaderOptions.getRandomColor();
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
}
