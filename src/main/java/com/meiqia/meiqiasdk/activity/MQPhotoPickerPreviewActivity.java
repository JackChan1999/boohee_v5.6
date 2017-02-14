package com.meiqia.meiqiasdk.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager.SimpleOnPageChangeListener;
import android.support.v4.view.ViewPropertyAnimatorListenerAdapter;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.meiqia.meiqiasdk.R;
import com.meiqia.meiqiasdk.util.MQConfig;
import com.meiqia.meiqiasdk.util.MQUtils;
import com.meiqia.meiqiasdk.widget.MQHackyViewPager;
import com.meiqia.meiqiasdk.widget.MQImageView;
import com.meiqia.meiqiasdk.widget.MQImageView$OnDrawableChangedCallback;
import com.umeng.socialize.common.SocializeConstants;

import java.util.ArrayList;

import uk.co.senab.photoview.PhotoViewAttacher;
import uk.co.senab.photoview.PhotoViewAttacher$OnViewTapListener;

public class MQPhotoPickerPreviewActivity extends Activity implements OnClickListener,
        PhotoViewAttacher$OnViewTapListener {
    private static final String EXTRA_CURRENT_POSITION   = "EXTRA_CURRENT_POSITION";
    private static final String EXTRA_IS_FROM_TAKE_PHOTO = "EXTRA_IS_FROM_TAKE_PHOTO";
    private static final String EXTRA_MAX_CHOOSE_COUNT   = "EXTRA_MAX_CHOOSE_COUNT";
    private static final String EXTRA_PREVIEW_IMAGES     = "EXTRA_PREVIEW_IMAGES";
    private static final String EXTRA_SELECTED_IMAGES    = "EXTRA_SELECTED_IMAGES";
    private static final String EXTRA_TOP_RIGHT_BTN_TEXT = "EXTRA_TOP_RIGHT_BTN_TEXT";
    private RelativeLayout   mChooseRl;
    private TextView         mChooseTv;
    private MQHackyViewPager mContentHvp;
    private boolean          mIsFromTakePhoto;
    private boolean mIsHidden = false;
    private long mLastShowHiddenTime;
    private int mMaxChooseCount = 1;
    private ArrayList<String> mPreviewImages;
    private ArrayList<String> mSelectedImages;
    private TextView          mSubmitTv;
    private RelativeLayout    mTitleRl;
    private TextView          mTitleTv;
    private String            mTopRightBtnText;

    private class ImagePageAdapter extends PagerAdapter {
        private ImagePageAdapter() {
        }

        public int getCount() {
            return MQPhotoPickerPreviewActivity.this.mPreviewImages.size();
        }

        public View instantiateItem(ViewGroup container, int position) {
            MQImageView imageView = new MQImageView(container.getContext());
            container.addView(imageView, -1, -1);
            final PhotoViewAttacher photoViewAttacher = new PhotoViewAttacher(imageView);
            photoViewAttacher.setOnViewTapListener(MQPhotoPickerPreviewActivity.this);
            imageView.setDrawableChangedCallback(new MQImageView$OnDrawableChangedCallback() {
                public void onDrawableChanged() {
                    photoViewAttacher.update();
                }
            });
            MQConfig.getImageLoader(MQPhotoPickerPreviewActivity.this).displayImage(imageView,
                    (String) MQPhotoPickerPreviewActivity.this.mPreviewImages.get(position), R
                            .drawable.mq_ic_holder_dark, R.drawable.mq_ic_holder_dark, MQUtils
                            .getScreenWidth(MQPhotoPickerPreviewActivity.this), MQUtils
                            .getScreenHeight(MQPhotoPickerPreviewActivity.this), null);
            return imageView;
        }

        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

    public static Intent newIntent(Context context, int maxChooseCount, ArrayList<String>
            selectedImages, ArrayList<String> previewImages, int currentPosition, String
            topRightBtnText, boolean isFromTakePhoto) {
        Intent intent = new Intent(context, MQPhotoPickerPreviewActivity.class);
        intent.putStringArrayListExtra(EXTRA_SELECTED_IMAGES, selectedImages);
        intent.putStringArrayListExtra(EXTRA_PREVIEW_IMAGES, previewImages);
        intent.putExtra(EXTRA_MAX_CHOOSE_COUNT, maxChooseCount);
        intent.putExtra(EXTRA_CURRENT_POSITION, currentPosition);
        intent.putExtra(EXTRA_TOP_RIGHT_BTN_TEXT, topRightBtnText);
        intent.putExtra(EXTRA_IS_FROM_TAKE_PHOTO, isFromTakePhoto);
        return intent;
    }

    public static ArrayList<String> getSelectedImages(Intent intent) {
        return intent.getStringArrayListExtra(EXTRA_SELECTED_IMAGES);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initListener();
        processLogic(savedInstanceState);
    }

    private void initView() {
        setContentView(R.layout.mq_activity_photo_picker_preview);
        this.mTitleRl = (RelativeLayout) findViewById(R.id.title_rl);
        this.mTitleTv = (TextView) findViewById(R.id.title_tv);
        this.mSubmitTv = (TextView) findViewById(R.id.submit_tv);
        this.mContentHvp = (MQHackyViewPager) findViewById(R.id.content_hvp);
        this.mChooseRl = (RelativeLayout) findViewById(R.id.choose_rl);
        this.mChooseTv = (TextView) findViewById(R.id.choose_tv);
    }

    private void initListener() {
        findViewById(R.id.back_iv).setOnClickListener(this);
        this.mSubmitTv.setOnClickListener(this);
        this.mChooseTv.setOnClickListener(this);
        this.mContentHvp.addOnPageChangeListener(new SimpleOnPageChangeListener() {
            public void onPageSelected(int position) {
                MQPhotoPickerPreviewActivity.this.handlePageSelectedStatus();
            }
        });
    }

    private void processLogic(Bundle savedInstanceState) {
        this.mMaxChooseCount = getIntent().getIntExtra(EXTRA_MAX_CHOOSE_COUNT, 1);
        if (this.mMaxChooseCount < 1) {
            this.mMaxChooseCount = 1;
        }
        this.mSelectedImages = getIntent().getStringArrayListExtra(EXTRA_SELECTED_IMAGES);
        this.mPreviewImages = getIntent().getStringArrayListExtra(EXTRA_PREVIEW_IMAGES);
        if (TextUtils.isEmpty((CharSequence) this.mPreviewImages.get(0))) {
            this.mPreviewImages.remove(0);
        }
        this.mIsFromTakePhoto = getIntent().getBooleanExtra(EXTRA_IS_FROM_TAKE_PHOTO, false);
        if (this.mIsFromTakePhoto) {
            String photoPath = (String) this.mPreviewImages.get(0);
            this.mPreviewImages.clear();
            this.mSelectedImages.clear();
            this.mPreviewImages.add(photoPath);
            this.mSelectedImages.add(photoPath);
            this.mChooseRl.setVisibility(4);
        }
        this.mTopRightBtnText = getIntent().getStringExtra(EXTRA_TOP_RIGHT_BTN_TEXT);
        int currentPosition = getIntent().getIntExtra(EXTRA_CURRENT_POSITION, 0);
        this.mContentHvp.setAdapter(new ImagePageAdapter());
        this.mContentHvp.setCurrentItem(currentPosition);
        handlePageSelectedStatus();
        renderTopRightBtn();
        this.mTitleRl.postDelayed(new Runnable() {
            public void run() {
                MQPhotoPickerPreviewActivity.this.hiddenTitlebarAndChoosebar();
            }
        }, 2000);
    }

    public void onClick(View v) {
        if (v.getId() == R.id.back_iv) {
            onBackPressed();
        } else if (v.getId() == R.id.submit_tv) {
            Intent intent = new Intent();
            intent.putStringArrayListExtra(EXTRA_SELECTED_IMAGES, this.mSelectedImages);
            setResult(-1, intent);
            finish();
        } else if (v.getId() == R.id.choose_tv) {
            String currentImage = (String) this.mPreviewImages.get(this.mContentHvp
                    .getCurrentItem());
            if (this.mSelectedImages.contains(currentImage)) {
                this.mSelectedImages.remove(currentImage);
                this.mChooseTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable
                        .mq_ic_cb_normal, 0, 0, 0);
                renderTopRightBtn();
            } else if (this.mMaxChooseCount == this.mSelectedImages.size()) {
                MQUtils.show((Context) this, getString(R.string.mq_toast_photo_picker_max, new
                        Object[]{Integer.valueOf(this.mMaxChooseCount)}));
            } else {
                this.mSelectedImages.add(currentImage);
                this.mChooseTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable
                        .mq_ic_cb_checked, 0, 0, 0);
                renderTopRightBtn();
            }
        }
    }

    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putStringArrayListExtra(EXTRA_SELECTED_IMAGES, this.mSelectedImages);
        setResult(0, intent);
        finish();
    }

    private void handlePageSelectedStatus() {
        this.mTitleTv.setText((this.mContentHvp.getCurrentItem() + 1) + "/" + this.mPreviewImages
                .size());
        if (this.mSelectedImages.contains(this.mPreviewImages.get(this.mContentHvp.getCurrentItem
                ()))) {
            this.mChooseTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.mq_ic_cb_checked,
                    0, 0, 0);
        } else {
            this.mChooseTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.mq_ic_cb_normal, 0,
                    0, 0);
        }
    }

    private void renderTopRightBtn() {
        if (this.mIsFromTakePhoto) {
            this.mSubmitTv.setEnabled(true);
            this.mSubmitTv.setText(this.mTopRightBtnText);
        } else if (this.mSelectedImages.size() == 0) {
            this.mSubmitTv.setEnabled(false);
            this.mSubmitTv.setText(this.mTopRightBtnText);
        } else {
            this.mSubmitTv.setEnabled(true);
            this.mSubmitTv.setText(this.mTopRightBtnText + SocializeConstants.OP_OPEN_PAREN +
                    this.mSelectedImages.size() + "/" + this.mMaxChooseCount + SocializeConstants
                    .OP_CLOSE_PAREN);
        }
    }

    public void onViewTap(View view, float x, float y) {
        if (System.currentTimeMillis() - this.mLastShowHiddenTime > 500) {
            this.mLastShowHiddenTime = System.currentTimeMillis();
            if (this.mIsHidden) {
                showTitlebarAndChoosebar();
            } else {
                hiddenTitlebarAndChoosebar();
            }
        }
    }

    private void showTitlebarAndChoosebar() {
        ViewCompat.animate(this.mTitleRl).translationY(0.0f).setInterpolator(new
                DecelerateInterpolator(2.0f)).setListener(new ViewPropertyAnimatorListenerAdapter
                () {
            public void onAnimationEnd(View view) {
                MQPhotoPickerPreviewActivity.this.mIsHidden = false;
            }
        }).start();
        if (!this.mIsFromTakePhoto) {
            this.mChooseRl.setVisibility(0);
            ViewCompat.setAlpha(this.mChooseRl, 0.0f);
            ViewCompat.animate(this.mChooseRl).alpha(1.0f).setInterpolator(new
                    DecelerateInterpolator(2.0f)).start();
        }
    }

    private void hiddenTitlebarAndChoosebar() {
        ViewCompat.animate(this.mTitleRl).translationY((float) (-this.mTitleRl.getHeight()))
                .setInterpolator(new DecelerateInterpolator(2.0f)).setListener(new ViewPropertyAnimatorListenerAdapter() {
            public void onAnimationEnd(View view) {
                MQPhotoPickerPreviewActivity.this.mIsHidden = true;
                MQPhotoPickerPreviewActivity.this.mChooseRl.setVisibility(4);
            }
        }).start();
        if (!this.mIsFromTakePhoto) {
            ViewCompat.animate(this.mChooseRl).alpha(0.0f).setInterpolator(new DecelerateInterpolator(2.0f)).start();
        }
    }
}
