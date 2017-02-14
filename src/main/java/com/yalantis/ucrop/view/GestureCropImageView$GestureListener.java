package com.yalantis.ucrop.view;

import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;

class GestureCropImageView$GestureListener extends SimpleOnGestureListener {
    final /* synthetic */ GestureCropImageView this$0;

    private GestureCropImageView$GestureListener(GestureCropImageView gestureCropImageView) {
        this.this$0 = gestureCropImageView;
    }

    public boolean onDoubleTap(MotionEvent e) {
        this.this$0.zoomImageToPosition(this.this$0.getDoubleTapTargetScale(), e.getX(), e.getY()
                , 200);
        return super.onDoubleTap(e);
    }

    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        this.this$0.postTranslate(-distanceX, -distanceY);
        return true;
    }
}
