package com.yalantis.ucrop.view;

import com.yalantis.ucrop.util.RotationGestureDetector;
import com.yalantis.ucrop.util.RotationGestureDetector.SimpleOnRotationGestureListener;

class GestureCropImageView$RotateListener extends SimpleOnRotationGestureListener {
    final /* synthetic */ GestureCropImageView this$0;

    private GestureCropImageView$RotateListener(GestureCropImageView gestureCropImageView) {
        this.this$0 = gestureCropImageView;
    }

    public boolean onRotation(RotationGestureDetector rotationDetector) {
        this.this$0.postRotate(rotationDetector.getAngle(), GestureCropImageView.access$300(this
                .this$0), GestureCropImageView.access$400(this.this$0));
        return true;
    }
}
