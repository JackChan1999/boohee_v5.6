package uk.co.senab.photoview;

import android.widget.ImageView;

class PhotoViewAttacher$AnimatedZoomRunnable implements Runnable {
    private final float mFocalX;
    private final float mFocalY;
    private final long mStartTime = System.currentTimeMillis();
    private final float mZoomEnd;
    private final float mZoomStart;
    final /* synthetic */ PhotoViewAttacher this$0;

    public PhotoViewAttacher$AnimatedZoomRunnable(PhotoViewAttacher photoViewAttacher, float currentZoom, float targetZoom, float focalX, float focalY) {
        this.this$0 = photoViewAttacher;
        this.mFocalX = focalX;
        this.mFocalY = focalY;
        this.mZoomStart = currentZoom;
        this.mZoomEnd = targetZoom;
    }

    public void run() {
        ImageView imageView = this.this$0.getImageView();
        if (imageView != null) {
            float t = interpolate();
            float deltaScale = (this.mZoomStart + ((this.mZoomEnd - this.mZoomStart) * t)) / this.this$0.getScale();
            PhotoViewAttacher.access$100(this.this$0).postScale(deltaScale, deltaScale, this.mFocalX, this.mFocalY);
            PhotoViewAttacher.access$200(this.this$0);
            if (t < 1.0f) {
                Compat.postOnAnimation(imageView, this);
            }
        }
    }

    private float interpolate() {
        return PhotoViewAttacher.sInterpolator.getInterpolation(Math.min(1.0f, (((float) (System.currentTimeMillis() - this.mStartTime)) * 1.0f) / ((float) this.this$0.ZOOM_DURATION)));
    }
}
