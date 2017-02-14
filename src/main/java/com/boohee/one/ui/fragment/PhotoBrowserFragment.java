package com.boohee.one.ui.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.InjectView;

import com.boohee.one.R;
import com.boohee.utility.ImageLoaderOptions;
import com.boohee.widgets.ProgressWheel;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;
import uk.co.senab.photoview.PhotoViewAttacher.OnPhotoTapListener;

public class PhotoBrowserFragment extends BaseFragment {
    public static final String KEY_IMAGE_URL = "key_image_url";
    private ImageLoader imageLoader;
    @InjectView(2131427433)
    PhotoView ivPhoto;
    private PhotoViewAttacher mAttacher;
    private Bitmap            mBitmap;
    private String            mImageUrl;
    @InjectView(2131428308)
    ProgressWheel progressWheel;

    public static PhotoBrowserFragment newInstance(String photo) {
        PhotoBrowserFragment instance = new PhotoBrowserFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_IMAGE_URL, photo);
        instance.setArguments(bundle);
        return instance;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.imageLoader = ImageLoader.getInstance();
        if (getArguments() != null) {
            this.mImageUrl = getArguments().getString(KEY_IMAGE_URL);
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.gb, null);
        ButterKnife.inject((Object) this, view);
        return view;
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!TextUtils.isEmpty(this.mImageUrl)) {
            this.mAttacher = new PhotoViewAttacher(this.ivPhoto);
            this.mAttacher.setOnPhotoTapListener(new OnPhotoTapListener() {
                public void onPhotoTap(View view, float v, float v2) {
                    PhotoBrowserFragment.this.getActivity().finish();
                }
            });
            loadImage();
        }
    }

    private void loadImage() {
        if (!TextUtils.isEmpty(this.mImageUrl) && this.ivPhoto != null) {
            this.imageLoader.displayImage(this.mImageUrl, this.ivPhoto, ImageLoaderOptions
                    .noImage(), new SimpleImageLoadingListener() {
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    PhotoBrowserFragment.this.progressWheel.setVisibility(8);
                    PhotoBrowserFragment.this.mAttacher.update();
                    PhotoBrowserFragment.this.mBitmap = loadedImage;
                }
            }, new ImageLoadingProgressListener() {
                public void onProgressUpdate(String imageUri, View view, int current, int total) {
                    if (PhotoBrowserFragment.this.isAdded()) {
                        PhotoBrowserFragment.this.progressWheel.setProgress((current * 360) /
                                total);
                    }
                }
            });
        }
    }

    public void onDestroyView() {
        super.onDestroyView();
        this.imageLoader.cancelDisplayTask(this.ivPhoto);
        if (this.mAttacher != null) {
            this.mAttacher.cleanup();
        }
        ButterKnife.reset(this);
    }

    public void setImageUrl(String imageUrl) {
        this.mImageUrl = imageUrl;
        loadImage();
    }

    public Bitmap getBitmapImage() {
        return this.mBitmap;
    }
}
