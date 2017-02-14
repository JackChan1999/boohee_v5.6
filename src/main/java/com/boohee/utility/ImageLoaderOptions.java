package com.boohee.utility;

import android.graphics.Bitmap.Config;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;

import com.boohee.one.MyApplication;
import com.boohee.one.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.DisplayImageOptions.Builder;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.Random;

public class ImageLoaderOptions {
    protected static final int[] PREVIEW_COLORS = new int[]{R.color.dg, R.color.dh, R.color.di, R
            .color.dj, R.color.dk};

    public static DisplayImageOptions avatar() {
        return new Builder().cacheOnDisc(true).showImageOnLoading((int) R.drawable.aa0)
                .showImageOnFail((int) R.drawable.aa0).cacheInMemory(true).considerExifParams
                        (true).showImageForEmptyUri((int) R.drawable.aa0).bitmapConfig(Config
                        .RGB_565).build();
    }

    public static DisplayImageOptions uchoice() {
        return uchoiceOption();
    }

    public static DisplayImageOptions global() {
        return global((int) R.drawable.l_);
    }

    public static DisplayImageOptions food() {
        return global((int) R.drawable.m2);
    }

    public static DisplayImageOptions statusMiddle() {
        return global((int) R.drawable.l_);
    }

    public static DisplayImageOptions custom(int drawableId) {
        return global(drawableId);
    }

    public static DisplayImageOptions global(int drawableId) {
        return new Builder().cacheInMemory(true).cacheOnDisc(true).considerExifParams(true)
                .showImageOnLoading(drawableId).showImageOnFail(drawableId).showImageForEmptyUri
                        (drawableId).imageScaleType(ImageScaleType.IN_SAMPLE_INT).bitmapConfig
                        (Config.ARGB_8888).build();
    }

    public static DisplayImageOptions global(Drawable drawable) {
        return new Builder().cacheInMemory(true).cacheOnDisc(true).considerExifParams(true)
                .showImageOnLoading(drawable).showImageOnFail(drawable).showImageForEmptyUri
                        (drawable).imageScaleType(ImageScaleType.EXACTLY).bitmapConfig(Config
                        .ARGB_8888).build();
    }

    public static DisplayImageOptions color(int colorId) {
        return global(new ColorDrawable(MyApplication.getContext().getResources().getColor
                (colorId)));
    }

    public static DisplayImageOptions uchoiceOption() {
        return new Builder().showImageOnLoading((int) R.drawable.l_).showImageOnFail((int) R
                .drawable.mh).cacheInMemory(false).cacheOnDisc(true).showImageForEmptyUri((int) R
                .drawable.mh).imageScaleType(ImageScaleType.IN_SAMPLE_INT).bitmapConfig(Config
                .ARGB_8888).build();
    }

    public static DisplayImageOptions noImage() {
        return new Builder().cacheOnDisc(true).build();
    }

    public static DisplayImageOptions randomColor() {
        return color(getRandomColor());
    }

    public static int getRandomColor() {
        return PREVIEW_COLORS[new Random().nextInt(PREVIEW_COLORS.length)];
    }

    public static DisplayImageOptions randomColorWithOrder(int position) {
        return color(PREVIEW_COLORS[position % PREVIEW_COLORS.length]);
    }
}
