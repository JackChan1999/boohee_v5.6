package com.boohee.utils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.DisplayImageOptions.Builder;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import me.nereo.multi_image_selector.utils.MultiImageLoader;
import me.nereo.multi_image_selector.utils.MultiImageLoader.LoadCallBack;
import me.nereo.multi_image_selector.utils.MultiImageSelector;

public class MultiImageHelper {
    private static Builder builder = new Builder().cacheInMemory(true).cacheOnDisk(true)
            .imageScaleType(ImageScaleType.EXACTLY).considerExifParams(true).bitmapConfig(Config
                    .RGB_565);

    public static void initMultiSelctor() {
        MultiImageSelector.init(new MultiImageLoader() {
            public void loadImage(String uri, ImageView imageView, int imageWidth, int
                    imageHeight, int placeHolderResId) {
                if (imageView != null && !TextUtils.isEmpty(uri) && !uri.equals(imageView.getTag
                        ())) {
                    imageView.setTag(uri);
                    DisplayImageOptions options = MultiImageHelper.builder.showImageForEmptyUri
                            (placeHolderResId).showImageOnFail(placeHolderResId)
                            .showImageOnLoading(placeHolderResId).build();
                    imageView.getLayoutParams().height = imageHeight;
                    imageView.getLayoutParams().width = imageWidth;
                    imageView.setScaleType(ScaleType.CENTER_CROP);
                    ImageLoader.getInstance().displayImage(Uri.decode(uri), imageView, options,
                            null);
                }
            }

            public void pause(Object o) {
                ImageLoader.getInstance().pause();
            }

            public void resume(Object o) {
                ImageLoader.getInstance().resume();
            }

            public void loadImage(String uri, ImageView imageView, int placeHolderResId) {
                if (imageView != null && !TextUtils.isEmpty(uri) && !uri.equals(imageView.getTag
                        ())) {
                    imageView.setTag(uri);
                    ImageLoader.getInstance().displayImage(Uri.decode(uri), imageView,
                            MultiImageHelper.builder.showImageForEmptyUri(placeHolderResId)
                                    .showImageOnFail(placeHolderResId).showImageOnLoading
                                    (placeHolderResId).build(), null);
                }
            }

            public void loadImage(String uri, ImageView imageView, int placeHolderResId, final
            LoadCallBack loadCallBack) {
                if (imageView != null && !TextUtils.isEmpty(uri) && !uri.equals(imageView.getTag
                        ())) {
                    imageView.setTag(uri);
                    ImageLoader.getInstance().displayImage(Uri.decode(uri), imageView,
                            MultiImageHelper.builder.showImageForEmptyUri(placeHolderResId)
                                    .showImageOnFail(placeHolderResId).showImageOnLoading
                                    (placeHolderResId).build(), new SimpleImageLoadingListener() {
                        public void onLoadingComplete(String imageUri, View view, Bitmap
                                loadedImage) {
                            super.onLoadingComplete(imageUri, view, loadedImage);
                            if (loadCallBack != null) {
                                loadCallBack.onSuccess();
                            }
                        }
                    });
                }
            }
        });
    }
}
