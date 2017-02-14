package com.boohee.model;

import java.io.Serializable;

public interface CustomSliderImage extends Serializable {
    int getDefaultImage();

    String getLink();

    String getMobEvent();

    String getPicUrl();

    String getTitle();
}
