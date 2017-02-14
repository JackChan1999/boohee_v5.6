package com.meiqia.meiqiasdk.uilimageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.DrawableRes;
import android.view.View;

import com.meiqia.meiqiasdk.util.MQImageLoader;
import com.meiqia.meiqiasdk.util.MQImageLoader.MQDisplayImageListener;
import com.meiqia.meiqiasdk.util.MQImageLoader.MQDownloadImageListener;
import com.meiqia.meiqiasdk.widget.MQImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration.Builder;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class UILImageLoader implements MQImageLoader {
    private void initImageLoader(Context context) {
        if (!ImageLoader.getInstance().isInited()) {
            ImageLoader.getInstance().init(new Builder(context.getApplicationContext())
                    .threadPoolSize(3).defaultDisplayImageOptions(new DisplayImageOptions.Builder
                            ().cacheInMemory(true).cacheOnDisk(true).build()).build());
        }
    }

    public void displayImage(MQImageView imageView, String path, @DrawableRes int loadingResId,
                             @DrawableRes int failResId, int width, int height, final
                             MQDisplayImageListener displayImageListener) {
        initImageLoader(imageView.getContext());
        if (path == null) {
            path = "";
        }
        if (!(path.startsWith("http") || path.startsWith("file"))) {
            path = "file://" + path;
        }
        String str = path;
        ImageLoader.getInstance().displayImage(str, new ImageViewAware(imageView), new
                DisplayImageOptions.Builder().showImageOnLoading(loadingResId).showImageOnFail
                (failResId).cacheInMemory(true).build(), new ImageSize(width, height), new
                SimpleImageLoadingListener() {
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                if (displayImageListener != null) {
                    displayImageListener.onSuccess(view, imageUri);
                }
            }
        }, null);
    }

    public void downloadImage(Context context, String path, final MQDownloadImageListener
            downloadImageListener) {
        if (!(path.startsWith("http") || path.startsWith("file"))) {
            path = "file://" + path;
        }
        ImageLoader.getInstance().loadImage(path, new SimpleImageLoadingListener() {
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                if (downloadImageListener != null) {
                    downloadImageListener.onSuccess(imageUri, loadedImage);
                }
            }

            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                if (downloadImageListener != null) {
                    downloadImageListener.onFailed(imageUri);
                }
            }
        });
    }
}
