package com.yalantis.ucrop.view;

class UCropView$1 implements CropImageView$CropBoundsChangeListener {
    final /* synthetic */ UCropView this$0;

    UCropView$1(UCropView uCropView) {
        this.this$0 = uCropView;
    }

    public void onCropBoundsChangedRotate(float cropRatio) {
        if (UCropView.access$000(this.this$0) != null) {
            UCropView.access$000(this.this$0).setTargetAspectRatio(cropRatio);
            UCropView.access$000(this.this$0).postInvalidate();
        }
    }
}
