package com.boohee.one.ui.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import butterknife.ButterKnife;
import butterknife.InjectView;

import com.boohee.model.status.Photo;
import com.boohee.one.R;
import com.boohee.utility.Event;
import com.boohee.utility.ImageLoaderOptions;
import com.boohee.utility.TimeLinePatterns;
import com.boohee.utils.FileUtil;
import com.boohee.utils.Helper;
import com.boohee.widgets.ProgressWheel;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.umeng.analytics.MobclickAgent;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;
import uk.co.senab.photoview.PhotoViewAttacher.OnPhotoTapListener;

public class PhotoImageFragment extends BaseFragment {
    public static final int TYPE_PHOTO = 0;
    public static final int TYPE_URL   = 1;
    @InjectView(2131428591)
    Button btnSave;
    private Context     context;
    private ImageLoader imageLoader;
    private String      imageUrl;
    @InjectView(2131427433)
    PhotoView ivPhoto;
    private PhotoViewAttacher mAttacher;
    private Photo             photo;
    @InjectView(2131428308)
    ProgressWheel progressWheel;
    private int type = -1;

    private class SaveImageTask extends AsyncTask<Bitmap, String, String> {
        private SaveImageTask() {
        }

        protected String doInBackground(Bitmap... params) {
            if (PhotoImageFragment.this.getActivity() == null || params[0] == null || TextUtils
                    .isEmpty(PhotoImageFragment.this.imageUrl)) {
                return null;
            }
            return FileUtil.downloadImage2Gallery(PhotoImageFragment.this.getActivity(),
                    params[0], new Md5FileNameGenerator().generate(PhotoImageFragment.this
                            .imageUrl));
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (!PhotoImageFragment.this.isDetached() && PhotoImageFragment.this.getActivity() !=
                    null) {
                if (PhotoImageFragment.this.btnSave != null) {
                    PhotoImageFragment.this.btnSave.setEnabled(true);
                }
                if (TextUtils.isEmpty(result)) {
                    Helper.showToast((CharSequence) "保存图片失败，请重新保存~~");
                } else {
                    Helper.showToast("图片已保存到" + result);
                }
            }
        }
    }

    public static PhotoImageFragment newInstance(Photo photo) {
        PhotoImageFragment fragment = new PhotoImageFragment();
        fragment.photo = photo;
        fragment.type = 0;
        return fragment;
    }

    public static PhotoImageFragment newInstance(String url) {
        PhotoImageFragment fragment = new PhotoImageFragment();
        fragment.imageUrl = url;
        fragment.type = 1;
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.jo, null);
        ButterKnife.inject((Object) this, view);
        return view;
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.imageLoader = ImageLoader.getInstance();
        this.context = getActivity();
        ButterKnife.inject((Object) this, view);
        init(this.type);
    }

    private void init(int type) {
        getImageUrl(type);
        if (!TextUtils.isEmpty(this.imageUrl)) {
            this.mAttacher = new PhotoViewAttacher(this.ivPhoto);
            this.mAttacher.setOnPhotoTapListener(new OnPhotoTapListener() {
                public void onPhotoTap(View view, float v, float v2) {
                    PhotoImageFragment.this.getActivity().finish();
                }
            });
            if (this.imageUrl.contains(TimeLinePatterns.WEB_SCHEME)) {
                this.imageLoader.displayImage(this.imageUrl, this.ivPhoto, ImageLoaderOptions
                        .noImage(), new SimpleImageLoadingListener() {
                    public void onLoadingComplete(String imageUri, View view, final Bitmap
                            loadedImage) {
                        if (loadedImage != null && PhotoImageFragment.this.getActivity() != null) {
                            PhotoImageFragment.this.progressWheel.setVisibility(8);
                            PhotoImageFragment.this.mAttacher.update();
                            PhotoImageFragment.this.btnSave.setVisibility(0);
                            PhotoImageFragment.this.btnSave.setOnClickListener(new OnClickListener() {
                                public void onClick(View v) {
                                    if (PhotoImageFragment.this.getActivity() != null) {
                                        PhotoImageFragment.this.btnSave.setEnabled(false);
                                        MobclickAgent.onEvent(PhotoImageFragment.this.context,
                                                Event.STATUS_SAVE_IMAGE_OK);
                                        new SaveImageTask().execute(new Bitmap[]{loadedImage});
                                    }
                                }
                            });
                        }
                    }
                }, new ImageLoadingProgressListener() {
                    public void onProgressUpdate(String imageUri, View view, int current, int
                            total) {
                        if (PhotoImageFragment.this.isAdded() && PhotoImageFragment.this
                                .progressWheel != null) {
                            PhotoImageFragment.this.progressWheel.setProgress((current * 360) /
                                    total);
                        }
                    }
                });
                return;
            }
            this.imageLoader.displayImage("file://" + this.imageUrl, this.ivPhoto,
                    ImageLoaderOptions.noImage());
            this.btnSave.setVisibility(8);
        }
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
    }

    private void getImageUrl(int type) {
        if (type == 0 && this.photo != null) {
            if (!TextUtils.isEmpty(this.photo.big_url)) {
                this.imageUrl = this.photo.big_url;
            } else if (!TextUtils.isEmpty(this.photo.middle_url)) {
                this.imageUrl = this.photo.middle_url;
            } else if (TextUtils.isEmpty(this.photo.small_url)) {
                this.imageUrl = this.photo.original_url;
            } else {
                this.imageUrl = this.photo.small_url;
            }
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
}
