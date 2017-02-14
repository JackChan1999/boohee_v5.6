package com.yalantis.ucrop.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.RectF;
import android.graphics.Region.Op;
import android.os.Build.VERSION;
import android.support.annotation.ColorInt;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;

import com.yalantis.ucrop.R;

public class OverlayView extends View {
    public static final int     DEFAULT_CROP_GRID_COLUMN_COUNT = 2;
    public static final int     DEFAULT_CROP_GRID_ROW_COUNT    = 2;
    public static final boolean DEFAULT_OVAL_DIMMED_LAYER      = false;
    public static final boolean DEFAULT_SHOW_CROP_FRAME        = true;
    public static final boolean DEFAULT_SHOW_CROP_GRID         = true;
    private       Path    mCircularPath;
    private       Paint   mCropFramePaint;
    private       int     mCropGridColumnCount;
    private       Paint   mCropGridPaint;
    private       int     mCropGridRowCount;
    private final RectF   mCropViewRect;
    private       int     mDimmedColor;
    private       Paint   mDimmedStrokePaint;
    private       float[] mGridPoints;
    private       boolean mOvalDimmedLayer;
    private       boolean mShowCropFrame;
    private       boolean mShowCropGrid;
    private       float   mTargetAspectRatio;
    protected     int     mThisHeight;
    protected     int     mThisWidth;

    public OverlayView(Context context) {
        this(context, null);
    }

    public OverlayView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OverlayView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mCropViewRect = new RectF();
        this.mGridPoints = null;
        this.mCircularPath = new Path();
        this.mDimmedStrokePaint = new Paint(1);
        this.mCropGridPaint = new Paint(1);
        this.mCropFramePaint = new Paint(1);
        init();
    }

    public void setOvalDimmedLayer(boolean ovalDimmedLayer) {
        this.mOvalDimmedLayer = ovalDimmedLayer;
    }

    public void setCropGridRowCount(@IntRange(from = 0) int cropGridRowCount) {
        this.mCropGridRowCount = cropGridRowCount;
        this.mGridPoints = null;
    }

    public void setCropGridColumnCount(@IntRange(from = 0) int cropGridColumnCount) {
        this.mCropGridColumnCount = cropGridColumnCount;
        this.mGridPoints = null;
    }

    public void setShowCropFrame(boolean showCropFrame) {
        this.mShowCropFrame = showCropFrame;
    }

    public void setShowCropGrid(boolean showCropGrid) {
        this.mShowCropGrid = showCropGrid;
    }

    public void setDimmedColor(@ColorInt int dimmedColor) {
        this.mDimmedColor = dimmedColor;
    }

    public void setCropFrameStrokeWidth(@IntRange(from = 0) int width) {
        this.mCropFramePaint.setStrokeWidth((float) width);
    }

    public void setCropGridStrokeWidth(@IntRange(from = 0) int width) {
        this.mCropGridPaint.setStrokeWidth((float) width);
    }

    public void setCropFrameColor(@ColorInt int color) {
        this.mCropFramePaint.setColor(color);
    }

    public void setCropGridColor(@ColorInt int color) {
        this.mCropGridPaint.setColor(color);
    }

    public void setTargetAspectRatio(float targetAspectRatio) {
        this.mTargetAspectRatio = targetAspectRatio;
        setupCropBounds();
    }

    public void setupCropBounds() {
        int height = (int) (((float) this.mThisWidth) / this.mTargetAspectRatio);
        int halfDiff;
        if (height > this.mThisHeight) {
            int width = (int) (((float) this.mThisHeight) * this.mTargetAspectRatio);
            halfDiff = (this.mThisWidth - width) / 2;
            this.mCropViewRect.set((float) (getPaddingLeft() + halfDiff), (float) getPaddingTop()
                    , (float) ((getPaddingLeft() + width) + halfDiff), (float) (getPaddingTop() +
                            this.mThisHeight));
        } else {
            halfDiff = (this.mThisHeight - height) / 2;
            this.mCropViewRect.set((float) getPaddingLeft(), (float) (getPaddingTop() + halfDiff)
                    , (float) (getPaddingLeft() + this.mThisWidth), (float) ((getPaddingTop() +
                            height) + halfDiff));
        }
        this.mGridPoints = null;
        this.mCircularPath.reset();
        this.mCircularPath.addOval(this.mCropViewRect, Direction.CW);
    }

    protected void init() {
        if (VERSION.SDK_INT < 18 && VERSION.SDK_INT >= 11) {
            setLayerType(1, null);
        }
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            left = getPaddingLeft();
            top = getPaddingTop();
            bottom = getHeight() - getPaddingBottom();
            this.mThisWidth = (getWidth() - getPaddingRight()) - left;
            this.mThisHeight = bottom - top;
            setupCropBounds();
        }
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawDimmedLayer(canvas);
        drawCropGrid(canvas);
    }

    protected void drawDimmedLayer(@NonNull Canvas canvas) {
        canvas.save();
        if (this.mOvalDimmedLayer) {
            canvas.clipPath(this.mCircularPath, Op.DIFFERENCE);
        } else {
            canvas.clipRect(this.mCropViewRect, Op.DIFFERENCE);
        }
        canvas.drawColor(this.mDimmedColor);
        canvas.restore();
        if (this.mOvalDimmedLayer) {
            canvas.drawOval(this.mCropViewRect, this.mDimmedStrokePaint);
        }
    }

    protected void drawCropGrid(@NonNull Canvas canvas) {
        if (this.mShowCropGrid) {
            if (this.mGridPoints == null && !this.mCropViewRect.isEmpty()) {
                int i;
                int i2;
                this.mGridPoints = new float[((this.mCropGridRowCount * 4) + (this
                        .mCropGridColumnCount * 4))];
                int index = 0;
                for (i = 0; i < this.mCropGridRowCount; i++) {
                    i2 = index + 1;
                    this.mGridPoints[index] = this.mCropViewRect.left;
                    index = i2 + 1;
                    this.mGridPoints[i2] = (this.mCropViewRect.height() * ((((float) i) + 1.0f) /
                            ((float) (this.mCropGridRowCount + 1)))) + this.mCropViewRect.top;
                    i2 = index + 1;
                    this.mGridPoints[index] = this.mCropViewRect.right;
                    index = i2 + 1;
                    this.mGridPoints[i2] = (this.mCropViewRect.height() * ((((float) i) + 1.0f) /
                            ((float) (this.mCropGridRowCount + 1)))) + this.mCropViewRect.top;
                }
                for (i = 0; i < this.mCropGridColumnCount; i++) {
                    i2 = index + 1;
                    this.mGridPoints[index] = (this.mCropViewRect.width() * ((((float) i) + 1.0f)
                            / ((float) (this.mCropGridColumnCount + 1)))) + this.mCropViewRect.left;
                    index = i2 + 1;
                    this.mGridPoints[i2] = this.mCropViewRect.top;
                    i2 = index + 1;
                    this.mGridPoints[index] = (this.mCropViewRect.width() * ((((float) i) + 1.0f)
                            / ((float) (this.mCropGridColumnCount + 1)))) + this.mCropViewRect.left;
                    index = i2 + 1;
                    this.mGridPoints[i2] = this.mCropViewRect.bottom;
                }
            }
            if (this.mGridPoints != null) {
                canvas.drawLines(this.mGridPoints, this.mCropGridPaint);
            }
        }
        if (this.mShowCropFrame) {
            canvas.drawRect(this.mCropViewRect, this.mCropFramePaint);
        }
    }

    protected void processStyledAttributes(@NonNull TypedArray a) {
        this.mOvalDimmedLayer = a.getBoolean(R.styleable.ucrop_UCropView_ucrop_oval_dimmed_layer,
                false);
        this.mDimmedColor = a.getColor(R.styleable.ucrop_UCropView_ucrop_dimmed_color,
                getResources().getColor(R.color.ucrop_color_default_dimmed));
        this.mDimmedStrokePaint.setColor(this.mDimmedColor);
        this.mDimmedStrokePaint.setStyle(Style.STROKE);
        this.mDimmedStrokePaint.setStrokeWidth(1.0f);
        initCropFrameStyle(a);
        this.mShowCropFrame = a.getBoolean(R.styleable.ucrop_UCropView_ucrop_show_frame, true);
        initCropGridStyle(a);
        this.mShowCropGrid = a.getBoolean(R.styleable.ucrop_UCropView_ucrop_show_grid, true);
    }

    private void initCropFrameStyle(@NonNull TypedArray a) {
        int cropFrameStrokeSize = a.getDimensionPixelSize(R.styleable
                .ucrop_UCropView_ucrop_frame_stroke_size, getResources().getDimensionPixelSize(R
                .dimen.ucrop_default_crop_frame_stoke_width));
        int cropFrameColor = a.getColor(R.styleable.ucrop_UCropView_ucrop_frame_color,
                getResources().getColor(R.color.ucrop_color_default_crop_frame));
        this.mCropFramePaint.setStrokeWidth((float) cropFrameStrokeSize);
        this.mCropFramePaint.setColor(cropFrameColor);
        this.mCropFramePaint.setStyle(Style.STROKE);
    }

    private void initCropGridStyle(@NonNull TypedArray a) {
        int cropGridStrokeSize = a.getDimensionPixelSize(R.styleable
                .ucrop_UCropView_ucrop_grid_stroke_size, getResources().getDimensionPixelSize(R
                .dimen.ucrop_default_crop_grid_stoke_width));
        int cropGridColor = a.getColor(R.styleable.ucrop_UCropView_ucrop_grid_color, getResources
                ().getColor(R.color.ucrop_color_default_crop_grid));
        this.mCropGridPaint.setStrokeWidth((float) cropGridStrokeSize);
        this.mCropGridPaint.setColor(cropGridColor);
        this.mCropGridRowCount = a.getInt(R.styleable.ucrop_UCropView_ucrop_grid_row_count, 2);
        this.mCropGridColumnCount = a.getInt(R.styleable.ucrop_UCropView_ucrop_grid_column_count, 2);
    }
}
