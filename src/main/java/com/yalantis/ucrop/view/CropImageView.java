package com.yalantis.ucrop.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.yalantis.ucrop.R;
import com.yalantis.ucrop.util.RectUtils;

import java.util.Arrays;

public class CropImageView extends TransformImageView {
    public static final float DEFAULT_ASPECT_RATIO                       = 0.0f;
    public static final int   DEFAULT_IMAGE_TO_CROP_BOUNDS_ANIM_DURATION = 500;
    public static final int   DEFAULT_MAX_BITMAP_SIZE                    = 0;
    public static final float DEFAULT_MAX_SCALE_MULTIPLIER               = 10.0f;
    public static final float SOURCE_IMAGE_ASPECT_RATIO                  = 0.0f;
    private       CropBoundsChangeListener mCropBoundsChangeListener;
    private final RectF                    mCropRect;
    private       long                     mImageToWrapCropBoundsAnimDuration;
    private       int                      mMaxResultImageSizeX;
    private       int                      mMaxResultImageSizeY;
    private       float                    mMaxScale;
    private       float                    mMaxScaleMultiplier;
    private       float                    mMinScale;
    private       float                    mTargetAspectRatio;
    private final Matrix                   mTempMatrix;
    private       Runnable                 mWrapCropBoundsRunnable;
    private       Runnable                 mZoomImageToPositionRunnable;

    public CropImageView(Context context) {
        this(context, null);
    }

    public CropImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CropImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mCropRect = new RectF();
        this.mTempMatrix = new Matrix();
        this.mMaxScaleMultiplier = 10.0f;
        this.mZoomImageToPositionRunnable = null;
        this.mMaxResultImageSizeX = 0;
        this.mMaxResultImageSizeY = 0;
        this.mImageToWrapCropBoundsAnimDuration = 500;
    }

    @Nullable
    public Bitmap cropImage() {
        Bitmap viewBitmap = getViewBitmap();
        if (viewBitmap == null || viewBitmap.isRecycled()) {
            return null;
        }
        cancelAllAnimations();
        setImageToWrapCropBounds(false);
        RectF currentImageRect = RectUtils.trapToRect(this.mCurrentImageCorners);
        if (currentImageRect.isEmpty()) {
            return null;
        }
        float currentScale = getCurrentScale();
        float currentAngle = getCurrentAngle();
        if (this.mMaxResultImageSizeX > 0 && this.mMaxResultImageSizeY > 0) {
            float cropWidth = this.mCropRect.width() / currentScale;
            float cropHeight = this.mCropRect.height() / currentScale;
            if (cropWidth > ((float) this.mMaxResultImageSizeX) || cropHeight > ((float) this
                    .mMaxResultImageSizeY)) {
                float resizeScale = Math.min(((float) this.mMaxResultImageSizeX) / cropWidth, (
                        (float) this.mMaxResultImageSizeY) / cropHeight);
                Bitmap resizedBitmap = Bitmap.createScaledBitmap(viewBitmap, (int) (((float)
                        viewBitmap.getWidth()) * resizeScale), (int) (((float) viewBitmap
                        .getHeight()) * resizeScale), false);
                if (viewBitmap != resizedBitmap) {
                    viewBitmap.recycle();
                }
                viewBitmap = resizedBitmap;
                currentScale /= resizeScale;
            }
        }
        if (currentAngle != 0.0f) {
            this.mTempMatrix.reset();
            this.mTempMatrix.setRotate(currentAngle, (float) (viewBitmap.getWidth() / 2), (float)
                    (viewBitmap.getHeight() / 2));
            Bitmap rotatedBitmap = Bitmap.createBitmap(viewBitmap, 0, 0, viewBitmap.getWidth(),
                    viewBitmap.getHeight(), this.mTempMatrix, true);
            if (viewBitmap != rotatedBitmap) {
                viewBitmap.recycle();
            }
            viewBitmap = rotatedBitmap;
        }
        return Bitmap.createBitmap(viewBitmap, (int) ((this.mCropRect.left - currentImageRect
                .left) / currentScale), (int) ((this.mCropRect.top - currentImageRect.top) /
                currentScale), (int) (this.mCropRect.width() / currentScale), (int) (this
                .mCropRect.height() / currentScale));
    }

    public float getMaxScale() {
        return this.mMaxScale;
    }

    public float getMinScale() {
        return this.mMinScale;
    }

    public float getTargetAspectRatio() {
        return this.mTargetAspectRatio;
    }

    public void setTargetAspectRatio(float targetAspectRatio) {
        Drawable drawable = getDrawable();
        if (drawable == null) {
            this.mTargetAspectRatio = targetAspectRatio;
            return;
        }
        if (targetAspectRatio == 0.0f) {
            this.mTargetAspectRatio = ((float) drawable.getIntrinsicWidth()) / ((float) drawable
                    .getIntrinsicHeight());
        } else {
            this.mTargetAspectRatio = targetAspectRatio;
        }
        setupCropBounds();
        postInvalidate();
    }

    @Nullable
    public CropBoundsChangeListener getCropBoundsChangeListener() {
        return this.mCropBoundsChangeListener;
    }

    public void setCropBoundsChangeListener(@Nullable CropBoundsChangeListener
                                                    cropBoundsChangeListener) {
        this.mCropBoundsChangeListener = cropBoundsChangeListener;
    }

    public void setMaxResultImageSizeX(@IntRange(from = 10) int maxResultImageSizeX) {
        this.mMaxResultImageSizeX = maxResultImageSizeX;
    }

    public void setMaxResultImageSizeY(@IntRange(from = 10) int maxResultImageSizeY) {
        this.mMaxResultImageSizeY = maxResultImageSizeY;
    }

    public void setImageToWrapCropBoundsAnimDuration(@IntRange(from = 100) long
                                                             imageToWrapCropBoundsAnimDuration) {
        if (imageToWrapCropBoundsAnimDuration > 0) {
            this.mImageToWrapCropBoundsAnimDuration = imageToWrapCropBoundsAnimDuration;
            return;
        }
        throw new IllegalArgumentException("Animation duration cannot be negative value.");
    }

    public void setMaxScaleMultiplier(float maxScaleMultiplier) {
        this.mMaxScaleMultiplier = maxScaleMultiplier;
    }

    public void zoomOutImage(float deltaScale) {
        zoomOutImage(deltaScale, this.mCropRect.centerX(), this.mCropRect.centerY());
    }

    public void zoomOutImage(float scale, float centerX, float centerY) {
        if (scale >= getMinScale()) {
            postScale(scale / getCurrentScale(), centerX, centerY);
        }
    }

    public void zoomInImage(float deltaScale) {
        zoomInImage(deltaScale, this.mCropRect.centerX(), this.mCropRect.centerY());
    }

    public void zoomInImage(float scale, float centerX, float centerY) {
        if (scale <= getMaxScale()) {
            postScale(scale / getCurrentScale(), centerX, centerY);
        }
    }

    public void postScale(float deltaScale, float px, float py) {
        if (deltaScale > 1.0f && getCurrentScale() * deltaScale <= getMaxScale()) {
            super.postScale(deltaScale, px, py);
        } else if (deltaScale < 1.0f && getCurrentScale() * deltaScale >= getMinScale()) {
            super.postScale(deltaScale, px, py);
        }
    }

    public void postRotate(float deltaAngle) {
        postRotate(deltaAngle, this.mCropRect.centerX(), this.mCropRect.centerY());
    }

    public void cancelAllAnimations() {
        removeCallbacks(this.mWrapCropBoundsRunnable);
        removeCallbacks(this.mZoomImageToPositionRunnable);
    }

    public void setImageToWrapCropBounds() {
        setImageToWrapCropBounds(true);
    }

    public void setImageToWrapCropBounds(boolean animate) {
        if (!isImageWrapCropBounds()) {
            float currentX = this.mCurrentImageCenter[0];
            float currentY = this.mCurrentImageCenter[1];
            float currentScale = getCurrentScale();
            float deltaX = this.mCropRect.centerX() - currentX;
            float deltaY = this.mCropRect.centerY() - currentY;
            float deltaScale = 0.0f;
            this.mTempMatrix.reset();
            this.mTempMatrix.setTranslate(deltaX, deltaY);
            float[] tempCurrentImageCorners = Arrays.copyOf(this.mCurrentImageCorners, this
                    .mCurrentImageCorners.length);
            this.mTempMatrix.mapPoints(tempCurrentImageCorners);
            boolean willImageWrapCropBoundsAfterTranslate = isImageWrapCropBounds
                    (tempCurrentImageCorners);
            if (willImageWrapCropBoundsAfterTranslate) {
                float[] imageIndents = calculateImageIndents();
                deltaX = -(imageIndents[0] + imageIndents[2]);
                deltaY = -(imageIndents[1] + imageIndents[3]);
            } else {
                RectF tempCropRect = new RectF(this.mCropRect);
                this.mTempMatrix.reset();
                this.mTempMatrix.setRotate(getCurrentAngle());
                this.mTempMatrix.mapRect(tempCropRect);
                float[] currentImageSides = RectUtils.getRectSidesFromCorners(this
                        .mCurrentImageCorners);
                deltaScale = (((float) (((double) Math.max(tempCropRect.width() /
                        currentImageSides[0], tempCropRect.height() / currentImageSides[1])) * 1
                .01d)) * currentScale) - currentScale;
            }
            if (animate) {
                Runnable wrapCropBoundsRunnable = new WrapCropBoundsRunnable(this, this
                        .mImageToWrapCropBoundsAnimDuration, currentX, currentY, deltaX, deltaY,
                        currentScale, deltaScale, willImageWrapCropBoundsAfterTranslate);
                this.mWrapCropBoundsRunnable = wrapCropBoundsRunnable;
                post(wrapCropBoundsRunnable);
                return;
            }
            postTranslate(deltaX, deltaY);
            if (!willImageWrapCropBoundsAfterTranslate) {
                zoomInImage(currentScale + deltaScale, this.mCropRect.centerX(), this.mCropRect
                        .centerY());
            }
        }
    }

    private float[] calculateImageIndents() {
        this.mTempMatrix.reset();
        this.mTempMatrix.setRotate(-getCurrentAngle());
        float[] unrotatedImageCorners = Arrays.copyOf(this.mCurrentImageCorners, this
                .mCurrentImageCorners.length);
        float[] unrotatedCropBoundsCorners = RectUtils.getCornersFromRect(this.mCropRect);
        this.mTempMatrix.mapPoints(unrotatedImageCorners);
        this.mTempMatrix.mapPoints(unrotatedCropBoundsCorners);
        RectF unrotatedImageRect = RectUtils.trapToRect(unrotatedImageCorners);
        RectF unrotatedCropRect = RectUtils.trapToRect(unrotatedCropBoundsCorners);
        float deltaLeft = unrotatedImageRect.left - unrotatedCropRect.left;
        float deltaTop = unrotatedImageRect.top - unrotatedCropRect.top;
        float deltaRight = unrotatedImageRect.right - unrotatedCropRect.right;
        float deltaBottom = unrotatedImageRect.bottom - unrotatedCropRect.bottom;
        float[] indents = new float[4];
        if (deltaLeft <= 0.0f) {
            deltaLeft = 0.0f;
        }
        indents[0] = deltaLeft;
        if (deltaTop <= 0.0f) {
            deltaTop = 0.0f;
        }
        indents[1] = deltaTop;
        if (deltaRight >= 0.0f) {
            deltaRight = 0.0f;
        }
        indents[2] = deltaRight;
        if (deltaBottom >= 0.0f) {
            deltaBottom = 0.0f;
        }
        indents[3] = deltaBottom;
        this.mTempMatrix.reset();
        this.mTempMatrix.setRotate(getCurrentAngle());
        this.mTempMatrix.mapPoints(indents);
        return indents;
    }

    protected void onImageLaidOut() {
        super.onImageLaidOut();
        Drawable drawable = getDrawable();
        if (drawable != null) {
            float drawableWidth = (float) drawable.getIntrinsicWidth();
            float drawableHeight = (float) drawable.getIntrinsicHeight();
            if (this.mTargetAspectRatio == 0.0f) {
                this.mTargetAspectRatio = drawableWidth / drawableHeight;
            }
            setupCropBounds();
            setupInitialImagePosition(drawableWidth, drawableHeight);
            setImageMatrix(this.mCurrentImageMatrix);
            if (this.mTransformImageListener != null) {
                this.mTransformImageListener.onScale(getCurrentScale());
                this.mTransformImageListener.onRotate(getCurrentAngle());
            }
        }
    }

    protected boolean isImageWrapCropBounds() {
        return isImageWrapCropBounds(this.mCurrentImageCorners);
    }

    protected boolean isImageWrapCropBounds(float[] imageCorners) {
        this.mTempMatrix.reset();
        this.mTempMatrix.setRotate(-getCurrentAngle());
        float[] unrotatedImageCorners = Arrays.copyOf(imageCorners, imageCorners.length);
        this.mTempMatrix.mapPoints(unrotatedImageCorners);
        float[] unrotatedCropBoundsCorners = RectUtils.getCornersFromRect(this.mCropRect);
        this.mTempMatrix.mapPoints(unrotatedCropBoundsCorners);
        return RectUtils.trapToRect(unrotatedImageCorners).contains(RectUtils.trapToRect
                (unrotatedCropBoundsCorners));
    }

    protected void zoomImageToPosition(float scale, float centerX, float centerY, long durationMs) {
        if (scale > getMaxScale()) {
            scale = getMaxScale();
        }
        float oldScale = getCurrentScale();
        Runnable zoomImageToPosition = new ZoomImageToPosition(this, durationMs, oldScale, scale
                - oldScale, centerX, centerY);
        this.mZoomImageToPositionRunnable = zoomImageToPosition;
        post(zoomImageToPosition);
    }

    private void setupInitialImagePosition(float drawableWidth, float drawableHeight) {
        float cropRectWidth = this.mCropRect.width();
        float cropRectHeight = this.mCropRect.height();
        this.mMinScale = Math.max(cropRectWidth / drawableWidth, cropRectHeight / drawableHeight);
        this.mMaxScale = this.mMinScale * this.mMaxScaleMultiplier;
        float tw = ((cropRectWidth - (this.mMinScale * drawableWidth)) / 2.0f) + this.mCropRect
                .left;
        float th = ((cropRectHeight - (this.mMinScale * drawableHeight)) / 2.0f) + this.mCropRect
                .top;
        this.mCurrentImageMatrix.reset();
        this.mCurrentImageMatrix.postScale(this.mMinScale, this.mMinScale);
        this.mCurrentImageMatrix.postTranslate(tw, th);
    }

    protected void processStyledAttributes(@NonNull TypedArray a) {
        float targetAspectRatioX = Math.abs(a.getFloat(R.styleable
                .ucrop_UCropView_ucrop_aspect_ratio_x, 0.0f));
        float targetAspectRatioY = Math.abs(a.getFloat(R.styleable
                .ucrop_UCropView_ucrop_aspect_ratio_y, 0.0f));
        if (targetAspectRatioX == 0.0f || targetAspectRatioY == 0.0f) {
            this.mTargetAspectRatio = 0.0f;
        } else {
            this.mTargetAspectRatio = targetAspectRatioX / targetAspectRatioY;
        }
    }

    private void setupCropBounds() {
        int height = (int) (((float) this.mThisWidth) / this.mTargetAspectRatio);
        int halfDiff;
        if (height > this.mThisHeight) {
            int width = (int) (((float) this.mThisHeight) * this.mTargetAspectRatio);
            halfDiff = (this.mThisWidth - width) / 2;
            this.mCropRect.set((float) halfDiff, 0.0f, (float) (width + halfDiff), (float) this
                    .mThisHeight);
        } else {
            halfDiff = (this.mThisHeight - height) / 2;
            this.mCropRect.set(0.0f, (float) halfDiff, (float) this.mThisWidth, (float) (height + halfDiff));
        }
        if (this.mCropBoundsChangeListener != null) {
            this.mCropBoundsChangeListener.onCropBoundsChangedRotate(this.mTargetAspectRatio);
        }
    }
}
