package com.yalantis.ucrop.view;

import com.yalantis.ucrop.util.CubicEasing;

import java.lang.ref.WeakReference;

class CropImageView$WrapCropBoundsRunnable implements Runnable {
    private final float                        mCenterDiffX;
    private final float                        mCenterDiffY;
    private final WeakReference<CropImageView> mCropImageView;
    private final float                        mDeltaScale;
    private final long                         mDurationMs;
    private final float                        mOldScale;
    private final float                        mOldX;
    private final float                        mOldY;
    private final long mStartTime = System.currentTimeMillis();
    private final boolean mWillBeImageInBoundsAfterTranslate;

    public CropImageView$WrapCropBoundsRunnable(CropImageView cropImageView, long durationMs,
                                                float oldX, float oldY, float centerDiffX, float
                                                        centerDiffY, float oldScale, float
                                                        deltaScale, boolean
                                                        willBeImageInBoundsAfterTranslate) {
        this.mCropImageView = new WeakReference(cropImageView);
        this.mDurationMs = durationMs;
        this.mOldX = oldX;
        this.mOldY = oldY;
        this.mCenterDiffX = centerDiffX;
        this.mCenterDiffY = centerDiffY;
        this.mOldScale = oldScale;
        this.mDeltaScale = deltaScale;
        this.mWillBeImageInBoundsAfterTranslate = willBeImageInBoundsAfterTranslate;
    }

    public void run() {
        CropImageView cropImageView = (CropImageView) this.mCropImageView.get();
        if (cropImageView != null) {
            float currentMs = (float) Math.min(this.mDurationMs, System.currentTimeMillis() -
                    this.mStartTime);
            float newX = CubicEasing.easeOut(currentMs, 0.0f, this.mCenterDiffX, (float) this
                    .mDurationMs);
            float newY = CubicEasing.easeOut(currentMs, 0.0f, this.mCenterDiffY, (float) this
                    .mDurationMs);
            float newScale = CubicEasing.easeInOut(currentMs, 0.0f, this.mDeltaScale, (float)
                    this.mDurationMs);
            if (currentMs < ((float) this.mDurationMs)) {
                cropImageView.postTranslate(newX - (cropImageView.mCurrentImageCenter[0] - this
                        .mOldX), newY - (cropImageView.mCurrentImageCenter[1] - this.mOldY));
                if (!this.mWillBeImageInBoundsAfterTranslate) {
                    cropImageView.zoomInImage(this.mOldScale + newScale, CropImageView.access$000
                            (cropImageView).centerX(), CropImageView.access$000(cropImageView)
                            .centerY());
                }
                if (!cropImageView.isImageWrapCropBounds()) {
                    cropImageView.post(this);
                }
            }
        }
    }
}
