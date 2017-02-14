package com.yalantis.ucrop;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.yalantis.ucrop.UCrop.Options;
import com.yalantis.ucrop.util.SelectedStateListDrawable;
import com.yalantis.ucrop.view.GestureCropImageView;
import com.yalantis.ucrop.view.OverlayView;
import com.yalantis.ucrop.view.TransformImageView.TransformImageListener;
import com.yalantis.ucrop.view.UCropView;
import com.yalantis.ucrop.view.widget.AspectRatioTextView;
import com.yalantis.ucrop.view.widget.HorizontalProgressWheelView;
import com.yalantis.ucrop.view.widget.HorizontalProgressWheelView.ScrollingListener;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

public class UCropActivity extends AppCompatActivity {
    public static final  int            ALL                                   = 3;
    public static final  CompressFormat DEFAULT_COMPRESS_FORMAT               = CompressFormat.JPEG;
    public static final  int            DEFAULT_COMPRESS_QUALITY              = 90;
    public static final  int            NONE                                  = 0;
    public static final  int            ROTATE                                = 2;
    private static final int            ROTATE_WIDGET_SENSITIVITY_COEFFICIENT = 42;
    public static final  int            SCALE                                 = 1;
    private static final int            SCALE_WIDGET_SENSITIVITY_COEFFICIENT  = 15000;
    private static final int            TABS_COUNT                            = 3;
    private static final String         TAG                                   = "UCropActivity";
    private int mActiveWidgetColor;
    private int[]           mAllowedGestures      = new int[]{1, 2, 3};
    private CompressFormat  mCompressFormat       = DEFAULT_COMPRESS_FORMAT;
    private int             mCompressQuality      = 90;
    private List<ViewGroup> mCropAspectRatioViews = new ArrayList();
    private GestureCropImageView mGestureCropImageView;
    private TransformImageListener mImageListener = new TransformImageListener() {
        public void onRotate(float currentAngle) {
            UCropActivity.this.setAngleText(currentAngle);
        }

        public void onScale(float currentScale) {
            UCropActivity.this.setScaleText(currentScale);
        }

        public void onLoadComplete() {
            View ucropView = UCropActivity.this.findViewById(R.id.ucrop);
            Animation fadeInAnimation = AnimationUtils.loadAnimation(UCropActivity.this
                    .getApplicationContext(), R.anim.ucrop_fade_in);
            fadeInAnimation.setAnimationListener(new 1 (this));
            ucropView.startAnimation(fadeInAnimation);
        }

        public void onLoadFailure(@NonNull Exception e) {
            UCropActivity.this.setResultException(e);
            UCropActivity.this.finish();
        }
    };
    private ViewGroup   mLayoutAspectRatio;
    private ViewGroup   mLayoutRotate;
    private ViewGroup   mLayoutScale;
    private int         mLogoColor;
    private Uri         mOutputUri;
    private OverlayView mOverlayView;
    private final OnClickListener mStateClickListener = new OnClickListener() {
        public void onClick(View v) {
            if (!v.isSelected()) {
                UCropActivity.this.setWidgetState(v.getId());
            }
        }
    };
    private int       mStatusBarColor;
    private TextView  mTextViewRotateAngle;
    private TextView  mTextViewScalePercent;
    private int       mToolbarColor;
    private int       mToolbarTextColor;
    private String    mToolbarTitle;
    private UCropView mUCropView;
    private ViewGroup mWrapperStateAspectRatio;
    private ViewGroup mWrapperStateRotate;
    private ViewGroup mWrapperStateScale;

    @Retention(RetentionPolicy.SOURCE)
    public @interface GestureTypes {
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ucrop_activity_photobox);
        Intent intent = getIntent();
        setupViews(intent);
        setImageData(intent);
        setInitialState();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ucrop_menu_activity, menu);
        MenuItem next = menu.findItem(R.id.menu_crop);
        Drawable defaultIcon = next.getIcon();
        if (defaultIcon != null) {
            defaultIcon.mutate();
            defaultIcon.setColorFilter(this.mToolbarTextColor, Mode.SRC_ATOP);
            next.setIcon(defaultIcon);
        }
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_crop) {
            cropAndSaveImage();
        } else if (item.getItemId() == 16908332) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    protected void onStop() {
        super.onStop();
        if (this.mGestureCropImageView != null) {
            this.mGestureCropImageView.cancelAllAnimations();
        }
    }

    private void setImageData(@NonNull Intent intent) {
        Uri inputUri = (Uri) intent.getParcelableExtra(UCrop.EXTRA_INPUT_URI);
        this.mOutputUri = (Uri) intent.getParcelableExtra(UCrop.EXTRA_OUTPUT_URI);
        processOptions(intent);
        if (inputUri == null || this.mOutputUri == null) {
            setResultException(new NullPointerException(getString(R.string
                    .ucrop_error_input_data_is_absent)));
            finish();
        } else {
            try {
                this.mGestureCropImageView.setImageUri(inputUri);
            } catch (Exception e) {
                setResultException(e);
                finish();
            }
        }
        if (intent.getBooleanExtra(UCrop.EXTRA_ASPECT_RATIO_SET, false)) {
            this.mWrapperStateAspectRatio.setVisibility(8);
            float aspectRatioX = intent.getFloatExtra(UCrop.EXTRA_ASPECT_RATIO_X, 0.0f);
            float aspectRatioY = intent.getFloatExtra(UCrop.EXTRA_ASPECT_RATIO_Y, 0.0f);
            if (aspectRatioX <= 0.0f || aspectRatioY <= 0.0f) {
                this.mGestureCropImageView.setTargetAspectRatio(0.0f);
            } else {
                this.mGestureCropImageView.setTargetAspectRatio(aspectRatioX / aspectRatioY);
            }
        }
        if (intent.getBooleanExtra(UCrop.EXTRA_MAX_SIZE_SET, false)) {
            int maxSizeX = intent.getIntExtra(UCrop.EXTRA_MAX_SIZE_X, 0);
            int maxSizeY = intent.getIntExtra(UCrop.EXTRA_MAX_SIZE_Y, 0);
            if (maxSizeX <= 0 || maxSizeY <= 0) {
                Log.w(TAG, "EXTRA_MAX_SIZE_X and EXTRA_MAX_SIZE_Y must be greater than 0");
                return;
            }
            this.mGestureCropImageView.setMaxResultImageSizeX(maxSizeX);
            this.mGestureCropImageView.setMaxResultImageSizeY(maxSizeY);
        }
    }

    private void processOptions(@NonNull Intent intent) {
        String compressionFormatName = intent.getStringExtra(Options.EXTRA_COMPRESSION_FORMAT_NAME);
        CompressFormat compressFormat = null;
        if (!TextUtils.isEmpty(compressionFormatName)) {
            compressFormat = CompressFormat.valueOf(compressionFormatName);
        }
        if (compressFormat == null) {
            compressFormat = DEFAULT_COMPRESS_FORMAT;
        }
        this.mCompressFormat = compressFormat;
        this.mCompressQuality = intent.getIntExtra(Options.EXTRA_COMPRESSION_QUALITY, 90);
        int[] allowedGestures = intent.getIntArrayExtra(Options.EXTRA_ALLOWED_GESTURES);
        if (allowedGestures != null && allowedGestures.length == 3) {
            this.mAllowedGestures = allowedGestures;
        }
        this.mGestureCropImageView.setMaxBitmapSize(intent.getIntExtra(Options
                .EXTRA_MAX_BITMAP_SIZE, 0));
        this.mGestureCropImageView.setMaxScaleMultiplier(intent.getFloatExtra(Options
                .EXTRA_MAX_SCALE_MULTIPLIER, 10.0f));
        this.mGestureCropImageView.setImageToWrapCropBoundsAnimDuration((long) intent.getIntExtra
                (Options.EXTRA_IMAGE_TO_CROP_BOUNDS_ANIM_DURATION, 500));
        this.mOverlayView.setDimmedColor(intent.getIntExtra(Options.EXTRA_DIMMED_LAYER_COLOR,
                getResources().getColor(R.color.ucrop_color_default_dimmed)));
        this.mOverlayView.setOvalDimmedLayer(intent.getBooleanExtra(Options
                .EXTRA_OVAL_DIMMED_LAYER, false));
        this.mOverlayView.setShowCropFrame(intent.getBooleanExtra(Options.EXTRA_SHOW_CROP_FRAME,
                true));
        this.mOverlayView.setCropFrameColor(intent.getIntExtra(Options.EXTRA_CROP_FRAME_COLOR,
                getResources().getColor(R.color.ucrop_color_default_crop_frame)));
        this.mOverlayView.setCropFrameStrokeWidth(intent.getIntExtra(Options
                .EXTRA_CROP_FRAME_STROKE_WIDTH, getResources().getDimensionPixelSize(R.dimen
                .ucrop_default_crop_frame_stoke_width)));
        this.mOverlayView.setShowCropGrid(intent.getBooleanExtra(Options.EXTRA_SHOW_CROP_GRID,
                true));
        this.mOverlayView.setCropGridRowCount(intent.getIntExtra(Options
                .EXTRA_CROP_GRID_ROW_COUNT, 2));
        this.mOverlayView.setCropGridColumnCount(intent.getIntExtra(Options
                .EXTRA_CROP_GRID_COLUMN_COUNT, 2));
        this.mOverlayView.setCropGridColor(intent.getIntExtra(Options.EXTRA_CROP_GRID_COLOR,
                getResources().getColor(R.color.ucrop_color_default_crop_grid)));
        this.mOverlayView.setCropGridStrokeWidth(intent.getIntExtra(Options
                .EXTRA_CROP_GRID_STROKE_WIDTH, getResources().getDimensionPixelSize(R.dimen
                .ucrop_default_crop_grid_stoke_width)));
    }

    private void setupViews(@NonNull Intent intent) {
        this.mStatusBarColor = intent.getIntExtra(Options.EXTRA_STATUS_BAR_COLOR, ContextCompat
                .getColor(this, R.color.ucrop_color_statusbar));
        this.mToolbarColor = intent.getIntExtra(Options.EXTRA_TOOL_BAR_COLOR, ContextCompat
                .getColor(this, R.color.ucrop_color_toolbar));
        this.mActiveWidgetColor = intent.getIntExtra(Options.EXTRA_UCROP_COLOR_WIDGET_ACTIVE,
                ContextCompat.getColor(this, R.color.ucrop_color_widget_active));
        this.mToolbarTextColor = intent.getIntExtra(Options.EXTRA_UCROP_TITLE_COLOR_TOOLBAR,
                ContextCompat.getColor(this, R.color.ucrop_color_title));
        this.mToolbarTitle = intent.getStringExtra(Options.EXTRA_UCROP_TITLE_TEXT_TOOLBAR);
        this.mToolbarTitle = !TextUtils.isEmpty(this.mToolbarTitle) ? this.mToolbarTitle :
                getResources().getString(R.string.ucrop_label_edit_photo);
        this.mLogoColor = intent.getIntExtra(Options.EXTRA_UCROP_LOGO_COLOR, ContextCompat
                .getColor(this, R.color.ucrop_color_default_logo));
        setupAppBar();
        initiateRootViews();
        setupAspectRatioWidget();
        setupRotateWidget();
        setupScaleWidget();
        setupStatesWrapper();
    }

    private void setupAppBar() {
        setStatusBarColor(this.mStatusBarColor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(this.mToolbarColor);
        toolbar.setTitleTextColor(this.mToolbarTextColor);
        TextView toolbarTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        toolbarTitle.setTextColor(this.mToolbarTextColor);
        toolbarTitle.setText(this.mToolbarTitle);
        Drawable stateButtonDrawable = ContextCompat.getDrawable(this, R.drawable.ucrop_ic_cross)
                .mutate();
        stateButtonDrawable.setColorFilter(this.mToolbarTextColor, Mode.SRC_ATOP);
        toolbar.setNavigationIcon(stateButtonDrawable);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

    private void initiateRootViews() {
        this.mUCropView = (UCropView) findViewById(R.id.ucrop);
        this.mGestureCropImageView = this.mUCropView.getCropImageView();
        this.mOverlayView = this.mUCropView.getOverlayView();
        this.mGestureCropImageView.setTransformImageListener(this.mImageListener);
        this.mWrapperStateAspectRatio = (ViewGroup) findViewById(R.id.state_aspect_ratio);
        this.mWrapperStateAspectRatio.setOnClickListener(this.mStateClickListener);
        this.mWrapperStateRotate = (ViewGroup) findViewById(R.id.state_rotate);
        this.mWrapperStateRotate.setOnClickListener(this.mStateClickListener);
        this.mWrapperStateScale = (ViewGroup) findViewById(R.id.state_scale);
        this.mWrapperStateScale.setOnClickListener(this.mStateClickListener);
        this.mLayoutAspectRatio = (ViewGroup) findViewById(R.id.layout_aspect_ratio);
        this.mLayoutRotate = (ViewGroup) findViewById(R.id.layout_rotate_wheel);
        this.mLayoutScale = (ViewGroup) findViewById(R.id.layout_scale_wheel);
        ((ImageView) findViewById(R.id.image_view_logo)).setColorFilter(this.mLogoColor, Mode
                .SRC_ATOP);
    }

    private void setupStatesWrapper() {
        ImageView stateScaleImageView = (ImageView) findViewById(R.id.image_view_state_scale);
        ImageView stateRotateImageView = (ImageView) findViewById(R.id.image_view_state_rotate);
        ImageView stateAspectRatioImageView = (ImageView) findViewById(R.id
                .image_view_state_aspect_ratio);
        stateScaleImageView.setImageDrawable(new SelectedStateListDrawable(stateScaleImageView
                .getDrawable(), this.mActiveWidgetColor));
        stateRotateImageView.setImageDrawable(new SelectedStateListDrawable(stateRotateImageView
                .getDrawable(), this.mActiveWidgetColor));
        stateAspectRatioImageView.setImageDrawable(new SelectedStateListDrawable
                (stateAspectRatioImageView.getDrawable(), this.mActiveWidgetColor));
    }

    @TargetApi(21)
    private void setStatusBarColor(@ColorInt int color) {
        if (VERSION.SDK_INT >= 21 && getWindow() != null) {
            getWindow().setStatusBarColor(color);
        }
    }

    private void setupAspectRatioWidget() {
        ((AspectRatioTextView) ((ViewGroup) findViewById(R.id.crop_aspect_ratio_1_1)).getChildAt
                (0)).setActiveColor(this.mActiveWidgetColor);
        ((AspectRatioTextView) ((ViewGroup) findViewById(R.id.crop_aspect_ratio_3_4)).getChildAt
                (0)).setActiveColor(this.mActiveWidgetColor);
        ((AspectRatioTextView) ((ViewGroup) findViewById(R.id.crop_aspect_ratio_original))
                .getChildAt(0)).setActiveColor(this.mActiveWidgetColor);
        ((AspectRatioTextView) ((ViewGroup) findViewById(R.id.crop_aspect_ratio_3_2)).getChildAt
                (0)).setActiveColor(this.mActiveWidgetColor);
        ((AspectRatioTextView) ((ViewGroup) findViewById(R.id.crop_aspect_ratio_16_9)).getChildAt
                (0)).setActiveColor(this.mActiveWidgetColor);
        this.mCropAspectRatioViews.add((ViewGroup) findViewById(R.id.crop_aspect_ratio_1_1));
        this.mCropAspectRatioViews.add((ViewGroup) findViewById(R.id.crop_aspect_ratio_3_4));
        this.mCropAspectRatioViews.add((ViewGroup) findViewById(R.id.crop_aspect_ratio_original));
        this.mCropAspectRatioViews.add((ViewGroup) findViewById(R.id.crop_aspect_ratio_3_2));
        this.mCropAspectRatioViews.add((ViewGroup) findViewById(R.id.crop_aspect_ratio_16_9));
        ((ViewGroup) this.mCropAspectRatioViews.get(2)).setSelected(true);
        for (ViewGroup cropAspectRatioView : this.mCropAspectRatioViews) {
            cropAspectRatioView.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    UCropActivity.this.mGestureCropImageView.setTargetAspectRatio((
                            (AspectRatioTextView) ((ViewGroup) v).getChildAt(0)).getAspectRatio(v
                            .isSelected()));
                    UCropActivity.this.mGestureCropImageView.setImageToWrapCropBounds();
                    if (!v.isSelected()) {
                        for (View cropAspectRatioView : UCropActivity.this.mCropAspectRatioViews) {
                            cropAspectRatioView.setSelected(cropAspectRatioView == v);
                        }
                    }
                }
            });
        }
    }

    private void setupRotateWidget() {
        this.mTextViewRotateAngle = (TextView) findViewById(R.id.text_view_rotate);
        ((HorizontalProgressWheelView) findViewById(R.id.rotate_scroll_wheel))
                .setScrollingListener(new ScrollingListener() {
            public void onScroll(float delta, float totalDistance) {
                UCropActivity.this.mGestureCropImageView.postRotate(delta / 42.0f);
            }

            public void onScrollEnd() {
                UCropActivity.this.mGestureCropImageView.setImageToWrapCropBounds();
            }

            public void onScrollStart() {
                UCropActivity.this.mGestureCropImageView.cancelAllAnimations();
            }
        });
        ((HorizontalProgressWheelView) findViewById(R.id.rotate_scroll_wheel)).setMiddleLineColor
                (this.mActiveWidgetColor);
        findViewById(R.id.wrapper_reset_rotate).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                UCropActivity.this.resetRotation();
            }
        });
        findViewById(R.id.wrapper_rotate_by_angle).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                UCropActivity.this.rotateByAngle(90);
            }
        });
    }

    private void setupScaleWidget() {
        this.mTextViewScalePercent = (TextView) findViewById(R.id.text_view_scale);
        ((HorizontalProgressWheelView) findViewById(R.id.scale_scroll_wheel))
                .setScrollingListener(new ScrollingListener() {
            public void onScroll(float delta, float totalDistance) {
                if (delta > 0.0f) {
                    UCropActivity.this.mGestureCropImageView.zoomInImage(UCropActivity.this
                            .mGestureCropImageView.getCurrentScale() + (((UCropActivity.this
                            .mGestureCropImageView.getMaxScale() - UCropActivity.this
                            .mGestureCropImageView.getMinScale()) / 15000.0f) * delta));
                } else {
                    UCropActivity.this.mGestureCropImageView.zoomOutImage(UCropActivity.this
                            .mGestureCropImageView.getCurrentScale() + (((UCropActivity.this
                            .mGestureCropImageView.getMaxScale() - UCropActivity.this
                            .mGestureCropImageView.getMinScale()) / 15000.0f) * delta));
                }
            }

            public void onScrollEnd() {
                UCropActivity.this.mGestureCropImageView.setImageToWrapCropBounds();
            }

            public void onScrollStart() {
                UCropActivity.this.mGestureCropImageView.cancelAllAnimations();
            }
        });
        ((HorizontalProgressWheelView) findViewById(R.id.scale_scroll_wheel)).setMiddleLineColor
                (this.mActiveWidgetColor);
    }

    private void setAngleText(float angle) {
        if (this.mTextViewRotateAngle != null) {
            this.mTextViewRotateAngle.setText(String.format("%.1f°", new Object[]{Float.valueOf
                    (angle)}));
        }
    }

    private void setScaleText(float scale) {
        if (this.mTextViewScalePercent != null) {
            this.mTextViewScalePercent.setText(String.format("%d%%", new Object[]{Integer.valueOf
                    ((int) (100.0f * scale))}));
        }
    }

    private void resetRotation() {
        this.mGestureCropImageView.postRotate(-this.mGestureCropImageView.getCurrentAngle());
        this.mGestureCropImageView.setImageToWrapCropBounds();
    }

    private void rotateByAngle(int angle) {
        this.mGestureCropImageView.postRotate((float) angle);
        this.mGestureCropImageView.setImageToWrapCropBounds();
    }

    private void setInitialState() {
        if (this.mWrapperStateAspectRatio.getVisibility() == 0) {
            setWidgetState(R.id.state_aspect_ratio);
        } else {
            setWidgetState(R.id.state_scale);
        }
    }

    private void setWidgetState(@IdRes int stateViewId) {
        boolean z;
        int i;
        int i2 = 8;
        this.mWrapperStateAspectRatio.setSelected(stateViewId == R.id.state_aspect_ratio);
        ViewGroup viewGroup = this.mWrapperStateRotate;
        if (stateViewId == R.id.state_rotate) {
            z = true;
        } else {
            z = false;
        }
        viewGroup.setSelected(z);
        viewGroup = this.mWrapperStateScale;
        if (stateViewId == R.id.state_scale) {
            z = true;
        } else {
            z = false;
        }
        viewGroup.setSelected(z);
        viewGroup = this.mLayoutAspectRatio;
        if (stateViewId == R.id.state_aspect_ratio) {
            i = 0;
        } else {
            i = 8;
        }
        viewGroup.setVisibility(i);
        viewGroup = this.mLayoutRotate;
        if (stateViewId == R.id.state_rotate) {
            i = 0;
        } else {
            i = 8;
        }
        viewGroup.setVisibility(i);
        ViewGroup viewGroup2 = this.mLayoutScale;
        if (stateViewId == R.id.state_scale) {
            i2 = 0;
        }
        viewGroup2.setVisibility(i2);
        if (stateViewId == R.id.state_scale) {
            setAllowedGestures(0);
        } else if (stateViewId == R.id.state_rotate) {
            setAllowedGestures(1);
        } else {
            setAllowedGestures(2);
        }
    }

    private void setAllowedGestures(int tab) {
        boolean z;
        boolean z2 = false;
        GestureCropImageView gestureCropImageView = this.mGestureCropImageView;
        if (this.mAllowedGestures[tab] == 3 || this.mAllowedGestures[tab] == 1) {
            z = true;
        } else {
            z = false;
        }
        gestureCropImageView.setScaleEnabled(z);
        GestureCropImageView gestureCropImageView2 = this.mGestureCropImageView;
        if (this.mAllowedGestures[tab] == 3 || this.mAllowedGestures[tab] == 2) {
            z2 = true;
        }
        gestureCropImageView2.setRotateEnabled(z2);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void cropAndSaveImage() {
        /*
        r5 = this;
        r2 = 0;
        r3 = r5.mGestureCropImageView;	 Catch:{ Exception -> 0x003b }
        r0 = r3.cropImage();	 Catch:{ Exception -> 0x003b }
        if (r0 == 0) goto L_0x002f;
    L_0x0009:
        r3 = r5.getContentResolver();	 Catch:{ Exception -> 0x003b }
        r4 = r5.mOutputUri;	 Catch:{ Exception -> 0x003b }
        r2 = r3.openOutputStream(r4);	 Catch:{ Exception -> 0x003b }
        r3 = r5.mCompressFormat;	 Catch:{ Exception -> 0x003b }
        r4 = r5.mCompressQuality;	 Catch:{ Exception -> 0x003b }
        r0.compress(r3, r4, r2);	 Catch:{ Exception -> 0x003b }
        r0.recycle();	 Catch:{ Exception -> 0x003b }
        r3 = r5.mOutputUri;	 Catch:{ Exception -> 0x003b }
        r4 = r5.mGestureCropImageView;	 Catch:{ Exception -> 0x003b }
        r4 = r4.getTargetAspectRatio();	 Catch:{ Exception -> 0x003b }
        r5.setResultUri(r3, r4);	 Catch:{ Exception -> 0x003b }
        r5.finish();	 Catch:{ Exception -> 0x003b }
    L_0x002b:
        com.yalantis.ucrop.util.BitmapLoadUtils.close(r2);
    L_0x002e:
        return;
    L_0x002f:
        r3 = new java.lang.NullPointerException;	 Catch:{ Exception -> 0x003b }
        r4 = "CropImageView.cropImage() returned null.";
        r3.<init>(r4);	 Catch:{ Exception -> 0x003b }
        r5.setResultException(r3);	 Catch:{ Exception -> 0x003b }
        goto L_0x002b;
    L_0x003b:
        r1 = move-exception;
        r5.setResultException(r1);	 Catch:{ all -> 0x0046 }
        r5.finish();	 Catch:{ all -> 0x0046 }
        com.yalantis.ucrop.util.BitmapLoadUtils.close(r2);
        goto L_0x002e;
    L_0x0046:
        r3 = move-exception;
        com.yalantis.ucrop.util.BitmapLoadUtils.close(r2);
        throw r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.yalantis.ucrop.UCropActivity.cropAndSaveImage():void");
    }

    private void setResultUri(Uri uri, float resultAspectRatio) {
        setResult(-1, new Intent().putExtra(UCrop.EXTRA_OUTPUT_URI, uri).putExtra(UCrop.EXTRA_OUTPUT_CROP_ASPECT_RATIO, resultAspectRatio));
    }

    private void setResultException(Throwable throwable) {
        setResult(96, new Intent().putExtra(UCrop.EXTRA_ERROR, throwable));
    }
}
