package com.yalantis.ucrop.view;

import com.yalantis.ucrop.util.CubicEasing;

import java.lang.ref.WeakReference;

class CropImageView$ZoomImageToPosition implements Runnable {
    private final WeakReference<CropImageView> mCropImageView;
    private final float                        mDeltaScale;
    private final float                        mDestX;
    private final float                        mDestY;
    private final long                         mDurationMs;
    private final float                        mOldScale;
    private final long mStartTime = System.currentTimeMillis();

    public CropImageView$ZoomImageToPosition(CropImageView cropImageView, long durationMs, float
            oldScale, float deltaScale, float destX, float destY) {
        this.mCropImageView = new WeakReference(cropImageView);
        this.mDurationMs = durationMs;
        this.mOldScale = oldScale;
        this.mDeltaScale = deltaScale;
        this.mDestX = destX;
        this.mDestY = destY;
    }

    public void run() {
        CropImageView cropImageView = (CropImageView) this.mCropImageView.get();
        if (cropImageView != null) {
            float currentMs = (float) Math.min(this.mDurationMs, System.currentTimeMillis() -
                    this.mStartTime);
            float newScale = CubicEasing.easeInOut(currentMs, 0.0f, this.mDeltaScale, (float)
                    this.mDurationMs);
            if (currentMs < ((float) this.mDurationMs)) {
                cropImageView.zoomInImage(this.mOldScale + newScale, this.mDestX, this.mDestY);
                cropImageView.post(this);
                return;
            }
            cropImageView.setImageToWrapCropBounds();
        }
    }
}
