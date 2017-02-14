package com.meiqia.meiqiasdk.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.DrawableRes;
import android.view.View;

import com.meiqia.meiqiasdk.widget.MQImageView;

public interface MQImageLoader {

    public interface MQDisplayImageListener {
        void onSuccess(View view, String str);
    }

    public interface MQDownloadImageListener {
        void onFailed(String str);

        void onSuccess(String str, Bitmap bitmap);
    }

    void displayImage(MQImageView mQImageView, String str, @DrawableRes int i, @DrawableRes int
            i2, int i3, int i4, MQDisplayImageListener mQDisplayImageListener);

    void downloadImage(Context context, String str, MQDownloadImageListener
            mQDownloadImageListener);
}
