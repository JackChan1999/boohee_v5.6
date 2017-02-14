package com.meiqia.meiqiasdk.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager.SimpleOnPageChangeListener;
import android.support.v4.view.ViewPropertyAnimatorListenerAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.meiqia.meiqiasdk.R;
import com.meiqia.meiqiasdk.util.MQConfig;
import com.meiqia.meiqiasdk.util.MQImageLoader.MQDownloadImageListener;
import com.meiqia.meiqiasdk.util.MQUtils;
import com.meiqia.meiqiasdk.widget.MQHackyViewPager;
import com.meiqia.meiqiasdk.widget.MQImageView;
import com.meiqia.meiqiasdk.widget.MQImageView$OnDrawableChangedCallback;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import uk.co.senab.photoview.PhotoViewAttacher;
import uk.co.senab.photoview.PhotoViewAttacher$OnViewTapListener;

public class MQPhotoPreviewActivity extends Activity implements
        PhotoViewAttacher$OnViewTapListener, OnClickListener {
    private static final String EXTRA_CURRENT_POSITION  = "EXTRA_CURRENT_POSITION";
    private static final String EXTRA_IS_SINGLE_PREVIEW = "EXTRA_IS_SINGLE_PREVIEW";
    private static final String EXTRA_PHOTO_PATH        = "EXTRA_PHOTO_PATH";
    private static final String EXTRA_PREVIEW_IMAGES    = "EXTRA_PREVIEW_IMAGES";
    private static final String EXTRA_SAVE_IMG_DIR      = "EXTRA_SAVE_IMG_DIR";
    private MQHackyViewPager mContentHvp;
    private ImageView        mDownloadIv;
    private boolean mIsHidden = false;
    private boolean           mIsSinglePreview;
    private long              mLastShowHiddenTime;
    private ArrayList<String> mPreviewImages;
    private File              mSaveImgDir;
    private Semaphore         mSemaphore;
    private RelativeLayout    mTitleRl;
    private TextView          mTitleTv;

    private class ImagePageAdapter extends PagerAdapter {
        private ImagePageAdapter() {
        }

        public int getCount() {
            return MQPhotoPreviewActivity.this.mPreviewImages.size();
        }

        public View instantiateItem(ViewGroup container, int position) {
            MQImageView imageView = new MQImageView(container.getContext());
            container.addView(imageView, -1, -1);
            final PhotoViewAttacher photoViewAttacher = new PhotoViewAttacher(imageView);
            photoViewAttacher.setOnViewTapListener(MQPhotoPreviewActivity.this);
            imageView.setDrawableChangedCallback(new MQImageView$OnDrawableChangedCallback() {
                public void onDrawableChanged() {
                    photoViewAttacher.update();
                }
            });
            MQConfig.getImageLoader(MQPhotoPreviewActivity.this).displayImage(imageView, (String)
                    MQPhotoPreviewActivity.this.mPreviewImages.get(position), R.drawable
                    .mq_ic_holder_dark, R.drawable.mq_ic_holder_dark, MQUtils.getScreenWidth
                    (MQPhotoPreviewActivity.this), MQUtils.getScreenHeight(MQPhotoPreviewActivity
                    .this), null);
            return imageView;
        }

        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

    public static Intent newIntent(Context context, File saveImgDir, ArrayList<String>
            previewImages, int currentPosition) {
        Intent intent = new Intent(context, MQPhotoPreviewActivity.class);
        intent.putExtra(EXTRA_SAVE_IMG_DIR, saveImgDir);
        intent.putStringArrayListExtra(EXTRA_PREVIEW_IMAGES, previewImages);
        intent.putExtra(EXTRA_CURRENT_POSITION, currentPosition);
        intent.putExtra(EXTRA_IS_SINGLE_PREVIEW, false);
        return intent;
    }

    public static Intent newIntent(Context context, File saveImgDir, String photoPath) {
        Intent intent = new Intent(context, MQPhotoPreviewActivity.class);
        intent.putExtra(EXTRA_SAVE_IMG_DIR, saveImgDir);
        intent.putExtra(EXTRA_PHOTO_PATH, photoPath);
        intent.putExtra(EXTRA_CURRENT_POSITION, 0);
        intent.putExtra(EXTRA_IS_SINGLE_PREVIEW, true);
        return intent;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initListener();
        processLogic(savedInstanceState);
    }

    private void initView() {
        setContentView(R.layout.mq_activity_photo_preview);
        this.mTitleRl = (RelativeLayout) findViewById(R.id.title_rl);
        this.mTitleTv = (TextView) findViewById(R.id.title_tv);
        this.mDownloadIv = (ImageView) findViewById(R.id.download_iv);
        this.mContentHvp = (MQHackyViewPager) findViewById(R.id.content_hvp);
    }

    private void initListener() {
        findViewById(R.id.back_iv).setOnClickListener(this);
        this.mDownloadIv.setOnClickListener(this);
        this.mContentHvp.addOnPageChangeListener(new SimpleOnPageChangeListener() {
            public void onPageSelected(int position) {
                MQPhotoPreviewActivity.this.renderTitleTv();
            }
        });
    }

    private void processLogic(Bundle savedInstanceState) {
        this.mSaveImgDir = (File) getIntent().getSerializableExtra(EXTRA_SAVE_IMG_DIR);
        if (this.mSaveImgDir == null) {
            this.mDownloadIv.setVisibility(4);
        }
        this.mPreviewImages = getIntent().getStringArrayListExtra(EXTRA_PREVIEW_IMAGES);
        this.mIsSinglePreview = getIntent().getBooleanExtra(EXTRA_IS_SINGLE_PREVIEW, false);
        if (this.mIsSinglePreview) {
            this.mPreviewImages = new ArrayList();
            this.mPreviewImages.add(getIntent().getStringExtra(EXTRA_PHOTO_PATH));
        }
        int currentPosition = getIntent().getIntExtra(EXTRA_CURRENT_POSITION, 0);
        this.mContentHvp.setAdapter(new ImagePageAdapter());
        this.mContentHvp.setCurrentItem(currentPosition);
        renderTitleTv();
        this.mSemaphore = new Semaphore(1);
        this.mTitleRl.postDelayed(new Runnable() {
            public void run() {
                MQPhotoPreviewActivity.this.hiddenTitlebar();
            }
        }, 2000);
    }

    private void renderTitleTv() {
        if (this.mIsSinglePreview) {
            this.mTitleTv.setText(R.string.mq_view_photo);
        } else {
            this.mTitleTv.setText((this.mContentHvp.getCurrentItem() + 1) + "/" + this
                    .mPreviewImages.size());
        }
    }

    public void onViewTap(View view, float x, float y) {
        if (System.currentTimeMillis() - this.mLastShowHiddenTime > 500) {
            this.mLastShowHiddenTime = System.currentTimeMillis();
            if (this.mIsHidden) {
                showTitlebar();
            } else {
                hiddenTitlebar();
            }
        }
    }

    private void showTitlebar() {
        ViewCompat.animate(this.mTitleRl).translationY(0.0f).setInterpolator(new
                DecelerateInterpolator(2.0f)).setListener(new ViewPropertyAnimatorListenerAdapter
                () {
            public void onAnimationEnd(View view) {
                MQPhotoPreviewActivity.this.mIsHidden = false;
            }
        }).start();
    }

    private void hiddenTitlebar() {
        ViewCompat.animate(this.mTitleRl).translationY((float) (-this.mTitleRl.getHeight()))
                .setInterpolator(new DecelerateInterpolator(2.0f)).setListener(new ViewPropertyAnimatorListenerAdapter() {
            public void onAnimationEnd(View view) {
                MQPhotoPreviewActivity.this.mIsHidden = true;
            }
        }).start();
    }

    public void onClick(View v) {
        if (v.getId() == R.id.back_iv) {
            onBackPressed();
        } else if (v.getId() == R.id.download_iv) {
            try {
                this.mSemaphore.acquire();
                savePic();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void savePic() {
        String url = (String) this.mPreviewImages.get(this.mContentHvp.getCurrentItem());
        if (url.startsWith("file")) {
            if (new File(url.replace("file://", "")).exists()) {
                MQUtils.showSafe((Context) this, getString(R.string.mq_save_img_success_folder,
                        new Object[]{file.getParentFile().getAbsolutePath()}));
                this.mSemaphore.release();
                return;
            }
        }
        if (new File(this.mSaveImgDir, MQUtils.stringToMD5(url) + ".png").exists()) {
            MQUtils.showSafe((Context) this, getString(R.string.mq_save_img_success_folder, new
                    Object[]{this.mSaveImgDir.getAbsolutePath()}));
            this.mSemaphore.release();
            return;
        }
        MQConfig.getImageLoader(this).downloadImage(this, url, new MQDownloadImageListener() {
            public void onSuccess(String url, Bitmap bitmap) {
                Throwable th;
                FileOutputStream fos = null;
                try {
                    File newFile = new File(MQPhotoPreviewActivity.this.mSaveImgDir, MQUtils
                            .stringToMD5(url) + ".png");
                    if (!newFile.exists()) {
                        FileOutputStream fos2 = new FileOutputStream(newFile);
                        try {
                            bitmap.compress(CompressFormat.PNG, 100, fos2);
                            fos2.flush();
                            MQPhotoPreviewActivity.this.sendBroadcast(new Intent("android.intent" +
                                    ".action.MEDIA_SCANNER_SCAN_FILE", Uri.fromFile(newFile)));
                            fos = fos2;
                        } catch (IOException e) {
                            fos = fos2;
                            try {
                                MQUtils.showSafe(MQPhotoPreviewActivity.this, R.string
                                        .mq_save_img_failure);
                                if (fos != null) {
                                    try {
                                        fos.close();
                                    } catch (IOException e2) {
                                        MQUtils.showSafe(MQPhotoPreviewActivity.this, R.string
                                                .mq_save_img_failure);
                                    }
                                }
                                MQPhotoPreviewActivity.this.mSemaphore.release();
                            } catch (Throwable th2) {
                                th = th2;
                                if (fos != null) {
                                    try {
                                        fos.close();
                                    } catch (IOException e3) {
                                        MQUtils.showSafe(MQPhotoPreviewActivity.this, R.string
                                                .mq_save_img_failure);
                                    }
                                }
                                MQPhotoPreviewActivity.this.mSemaphore.release();
                                throw th;
                            }
                        } catch (Throwable th3) {
                            th = th3;
                            fos = fos2;
                            if (fos != null) {
                                fos.close();
                            }
                            MQPhotoPreviewActivity.this.mSemaphore.release();
                            throw th;
                        }
                    }
                    MQUtils.showSafe(MQPhotoPreviewActivity.this, MQPhotoPreviewActivity.this
                            .getString(R.string.mq_save_img_success_folder, new
                                    Object[]{MQPhotoPreviewActivity.this.mSaveImgDir
                                    .getAbsolutePath()}));
                    if (fos != null) {
                        try {
                            fos.close();
                        } catch (IOException e4) {
                            MQUtils.showSafe(MQPhotoPreviewActivity.this, R.string
                                    .mq_save_img_failure);
                        }
                    }
                    MQPhotoPreviewActivity.this.mSemaphore.release();
                } catch (IOException e5) {
                    MQUtils.showSafe(MQPhotoPreviewActivity.this, R.string.mq_save_img_failure);
                    if (fos != null) {
                        fos.close();
                    }
                    MQPhotoPreviewActivity.this.mSemaphore.release();
                }
            }

            public void onFailed(String url) {
                MQUtils.showSafe(MQPhotoPreviewActivity.this, R.string.mq_save_img_failure);
                MQPhotoPreviewActivity.this.mSemaphore.release();
            }
        });
    }
}
