package com.boohee.one.bet.model;

import com.boohee.model.CustomSliderImage;
import com.boohee.one.R;

public class BetBannerBottom implements CustomSliderImage {
    public String link;
    public String pic_url;

    public String getTitle() {
        return "";
    }

    public String getPicUrl() {
        return this.pic_url;
    }

    public String getLink() {
        return this.link;
    }

    public String getMobEvent() {
        return "";
    }

    public int getDefaultImage() {
        return R.drawable.sm;
    }
}
