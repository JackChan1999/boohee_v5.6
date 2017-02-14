package com.boohee.model;

public class HomeWallpager implements CustomSliderImage {
    public String back_img;
    public String date;

    public String getTitle() {
        return "";
    }

    public String getPicUrl() {
        return this.back_img;
    }

    public String getLink() {
        return "";
    }

    public String getMobEvent() {
        return "";
    }

    public int getDefaultImage() {
        return 0;
    }
}
