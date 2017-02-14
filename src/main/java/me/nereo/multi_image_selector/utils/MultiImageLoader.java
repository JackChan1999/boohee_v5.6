package me.nereo.multi_image_selector.utils;

import android.widget.ImageView;

public interface MultiImageLoader {

    public static abstract class LoadCallBack {
        public void onSuccess() {
        }

        public void onFaild() {
        }
    }

    void loadImage(String str, ImageView imageView, int i);

    void loadImage(String str, ImageView imageView, int i, int i2, int i3);

    void loadImage(String str, ImageView imageView, int i, LoadCallBack loadCallBack);

    void pause(Object obj);

    void resume(Object obj);
}
