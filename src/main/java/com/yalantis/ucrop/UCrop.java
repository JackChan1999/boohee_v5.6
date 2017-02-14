package com.yalantis.ucrop;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class UCrop {
    public static final  String EXTRA_ASPECT_RATIO_SET         = "com.yalantis.ucrop" +
            ".AspectRatioSet";
    public static final  String EXTRA_ASPECT_RATIO_X           = "com.yalantis.ucrop.AspectRatioX";
    public static final  String EXTRA_ASPECT_RATIO_Y           = "com.yalantis.ucrop.AspectRatioY";
    public static final  String EXTRA_ERROR                    = "com.yalantis.ucrop.Error";
    public static final  String EXTRA_INPUT_URI                = "com.yalantis.ucrop.InputUri";
    public static final  String EXTRA_MAX_SIZE_SET             = "com.yalantis.ucrop.MaxSizeSet";
    public static final  String EXTRA_MAX_SIZE_X               = "com.yalantis.ucrop.MaxSizeX";
    public static final  String EXTRA_MAX_SIZE_Y               = "com.yalantis.ucrop.MaxSizeY";
    public static final  String EXTRA_OUTPUT_CROP_ASPECT_RATIO = "com.yalantis.ucrop" +
            ".CropAspectRatio";
    public static final  String EXTRA_OUTPUT_URI               = "com.yalantis.ucrop.OutputUri";
    private static final String EXTRA_PREFIX                   = "com.yalantis.ucrop";
    public static final  int    REQUEST_CROP                   = 69;
    public static final  int    RESULT_ERROR                   = 96;
    private              Intent mCropIntent                    = new Intent();
    private              Bundle mCropOptionsBundle             = new Bundle();

    public static class Options {
        public static final String EXTRA_ALLOWED_GESTURES                   = "com.yalantis.ucrop" +
                ".AllowedGestures";
        public static final String EXTRA_COMPRESSION_FORMAT_NAME            = "com.yalantis.ucrop" +
                ".CompressionFormatName";
        public static final String EXTRA_COMPRESSION_QUALITY                = "com.yalantis.ucrop" +
                ".CompressionQuality";
        public static final String EXTRA_CROP_FRAME_COLOR                   = "com.yalantis.ucrop" +
                ".CropFrameColor";
        public static final String EXTRA_CROP_FRAME_STROKE_WIDTH            = "com.yalantis.ucrop" +
                ".CropFrameStrokeWidth";
        public static final String EXTRA_CROP_GRID_COLOR                    = "com.yalantis.ucrop" +
                ".CropGridColor";
        public static final String EXTRA_CROP_GRID_COLUMN_COUNT             = "com.yalantis.ucrop" +
                ".CropGridColumnCount";
        public static final String EXTRA_CROP_GRID_ROW_COUNT                = "com.yalantis.ucrop" +
                ".CropGridRowCount";
        public static final String EXTRA_CROP_GRID_STROKE_WIDTH             = "com.yalantis.ucrop" +
                ".CropGridStrokeWidth";
        public static final String EXTRA_DIMMED_LAYER_COLOR                 = "com.yalantis.ucrop" +
                ".DimmedLayerColor";
        public static final String EXTRA_IMAGE_TO_CROP_BOUNDS_ANIM_DURATION = "com.yalantis.ucrop" +
                ".ImageToCropBoundsAnimDuration";
        public static final String EXTRA_MAX_BITMAP_SIZE                    = "com.yalantis.ucrop" +
                ".MaxBitmapSize";
        public static final String EXTRA_MAX_SCALE_MULTIPLIER               = "com.yalantis.ucrop" +
                ".MaxScaleMultiplier";
        public static final String EXTRA_OVAL_DIMMED_LAYER                  = "com.yalantis.ucrop" +
                ".OvalDimmedLayer";
        public static final String EXTRA_SHOW_CROP_FRAME                    = "com.yalantis.ucrop" +
                ".ShowCropFrame";
        public static final String EXTRA_SHOW_CROP_GRID                     = "com.yalantis.ucrop" +
                ".ShowCropGrid";
        public static final String EXTRA_STATUS_BAR_COLOR                   = "com.yalantis.ucrop" +
                ".StatusBarColor";
        public static final String EXTRA_TOOL_BAR_COLOR                     = "com.yalantis.ucrop" +
                ".ToolbarColor";
        public static final String EXTRA_UCROP_COLOR_WIDGET_ACTIVE          = "com.yalantis.ucrop" +
                ".UcropColorWidgetActive";
        public static final String EXTRA_UCROP_LOGO_COLOR                   = "com.yalantis.ucrop" +
                ".UcropLogoColor";
        public static final String EXTRA_UCROP_TITLE_COLOR_TOOLBAR          = "com.yalantis.ucrop" +
                ".UcropToolbarTitleColor";
        public static final String EXTRA_UCROP_TITLE_TEXT_TOOLBAR           = "com.yalantis.ucrop" +
                ".UcropToolbarTitleText";
        private final       Bundle mOptionBundle                            = new Bundle();

        @NonNull
        public Bundle getOptionBundle() {
            return this.mOptionBundle;
        }

        public void setCompressionFormat(@NonNull CompressFormat format) {
            this.mOptionBundle.putString(EXTRA_COMPRESSION_FORMAT_NAME, format.name());
        }

        public void setCompressionQuality(@IntRange(from = 0) int compressQuality) {
            this.mOptionBundle.putInt(EXTRA_COMPRESSION_QUALITY, compressQuality);
        }

        public void setAllowedGestures(int tabScale, int tabRotate, int tabAspectRatio) {
            this.mOptionBundle.putIntArray(EXTRA_ALLOWED_GESTURES, new int[]{tabScale, tabRotate,
                    tabAspectRatio});
        }

        public void setMaxScaleMultiplier(@FloatRange(from = 1.0d, fromInclusive = false) float
                                                  maxScaleMultiplier) {
            this.mOptionBundle.putFloat(EXTRA_MAX_SCALE_MULTIPLIER, maxScaleMultiplier);
        }

        public void setImageToCropBoundsAnimDuration(@IntRange(from = 100) int durationMillis) {
            this.mOptionBundle.putInt(EXTRA_IMAGE_TO_CROP_BOUNDS_ANIM_DURATION, durationMillis);
        }

        public void setMaxBitmapSize(@IntRange(from = 100) int maxBitmapSize) {
            this.mOptionBundle.putInt(EXTRA_MAX_BITMAP_SIZE, maxBitmapSize);
        }

        public void setDimmedLayerColor(@ColorInt int color) {
            this.mOptionBundle.putInt(EXTRA_DIMMED_LAYER_COLOR, color);
        }

        public void setOvalDimmedLayer(boolean isOval) {
            this.mOptionBundle.putBoolean(EXTRA_OVAL_DIMMED_LAYER, isOval);
        }

        public void setShowCropFrame(boolean show) {
            this.mOptionBundle.putBoolean(EXTRA_SHOW_CROP_FRAME, show);
        }

        public void setCropFrameColor(@ColorInt int color) {
            this.mOptionBundle.putInt(EXTRA_CROP_FRAME_COLOR, color);
        }

        public void setCropFrameStrokeWidth(@IntRange(from = 0) int width) {
            this.mOptionBundle.putInt(EXTRA_CROP_FRAME_STROKE_WIDTH, width);
        }

        public void setShowCropGrid(boolean show) {
            this.mOptionBundle.putBoolean(EXTRA_SHOW_CROP_GRID, show);
        }

        public void setCropGridRowCount(@IntRange(from = 0) int count) {
            this.mOptionBundle.putInt(EXTRA_CROP_GRID_ROW_COUNT, count);
        }

        public void setCropGridColumnCount(@IntRange(from = 0) int count) {
            this.mOptionBundle.putInt(EXTRA_CROP_GRID_COLUMN_COUNT, count);
        }

        public void setCropGridColor(@ColorInt int color) {
            this.mOptionBundle.putInt(EXTRA_CROP_GRID_COLOR, color);
        }

        public void setCropGridStrokeWidth(@IntRange(from = 0) int width) {
            this.mOptionBundle.putInt(EXTRA_CROP_GRID_STROKE_WIDTH, width);
        }

        public void setToolbarColor(@ColorInt int color) {
            this.mOptionBundle.putInt(EXTRA_TOOL_BAR_COLOR, color);
        }

        public void setStatusBarColor(@ColorInt int color) {
            this.mOptionBundle.putInt(EXTRA_STATUS_BAR_COLOR, color);
        }

        public void setActiveWidgetColor(@ColorInt int color) {
            this.mOptionBundle.putInt(EXTRA_UCROP_COLOR_WIDGET_ACTIVE, color);
        }

        public void setToolbarTitleTextColor(@ColorInt int color) {
            this.mOptionBundle.putInt(EXTRA_UCROP_TITLE_COLOR_TOOLBAR, color);
        }

        public void setToolbarTitle(@Nullable String text) {
            this.mOptionBundle.putString(EXTRA_UCROP_TITLE_TEXT_TOOLBAR, text);
        }

        public void setLogoColor(@ColorInt int color) {
            this.mOptionBundle.putInt(EXTRA_UCROP_LOGO_COLOR, color);
        }
    }

    public static UCrop of(@NonNull Uri source, @NonNull Uri destination) {
        return new UCrop(source, destination);
    }

    private UCrop(@NonNull Uri source, @NonNull Uri destination) {
        this.mCropOptionsBundle.putParcelable(EXTRA_INPUT_URI, source);
        this.mCropOptionsBundle.putParcelable(EXTRA_OUTPUT_URI, destination);
    }

    public UCrop withAspectRatio(float x, float y) {
        this.mCropOptionsBundle.putBoolean(EXTRA_ASPECT_RATIO_SET, true);
        this.mCropOptionsBundle.putFloat(EXTRA_ASPECT_RATIO_X, x);
        this.mCropOptionsBundle.putFloat(EXTRA_ASPECT_RATIO_Y, y);
        return this;
    }

    public UCrop useSourceImageAspectRatio() {
        this.mCropOptionsBundle.putBoolean(EXTRA_ASPECT_RATIO_SET, true);
        this.mCropOptionsBundle.putInt(EXTRA_ASPECT_RATIO_X, 0);
        this.mCropOptionsBundle.putInt(EXTRA_ASPECT_RATIO_Y, 0);
        return this;
    }

    public UCrop withMaxResultSize(@IntRange(from = 100) int width, @IntRange(from = 100) int
            height) {
        this.mCropOptionsBundle.putBoolean(EXTRA_MAX_SIZE_SET, true);
        this.mCropOptionsBundle.putInt(EXTRA_MAX_SIZE_X, width);
        this.mCropOptionsBundle.putInt(EXTRA_MAX_SIZE_Y, height);
        return this;
    }

    public UCrop withOptions(@NonNull Options options) {
        this.mCropOptionsBundle.putAll(options.getOptionBundle());
        return this;
    }

    public void start(@NonNull Activity activity) {
        start(activity, 69);
    }

    public void start(@NonNull Activity activity, int requestCode) {
        activity.startActivityForResult(getIntent(activity), requestCode);
    }

    public void start(@NonNull Context context, @NonNull Fragment fragment) {
        start(context, fragment, 69);
    }

    public void start(@NonNull Context context, @NonNull android.support.v4.app.Fragment fragment) {
        start(context, fragment, 69);
    }

    @TargetApi(11)
    public void start(@NonNull Context context, @NonNull Fragment fragment, int requestCode) {
        fragment.startActivityForResult(getIntent(context), requestCode);
    }

    public void start(@NonNull Context context, @NonNull android.support.v4.app.Fragment
            fragment, int requestCode) {
        fragment.startActivityForResult(getIntent(context), requestCode);
    }

    public Intent getIntent(@NonNull Context context) {
        this.mCropIntent.setClass(context, UCropActivity.class);
        this.mCropIntent.putExtras(this.mCropOptionsBundle);
        return this.mCropIntent;
    }

    @Nullable
    public static Uri getOutput(@NonNull Intent intent) {
        return (Uri) intent.getParcelableExtra(EXTRA_OUTPUT_URI);
    }

    public static float getOutputCropAspectRatio(@NonNull Intent intent) {
        return ((Float) intent.getParcelableExtra(EXTRA_OUTPUT_CROP_ASPECT_RATIO)).floatValue();
    }

    @Nullable
    public static Throwable getError(@NonNull Intent result) {
        return (Throwable) result.getSerializableExtra(EXTRA_ERROR);
    }
}
