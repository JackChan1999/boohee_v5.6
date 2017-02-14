package com.yalantis.ucrop.view;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.util.Log;

import com.yalantis.ucrop.util.BitmapLoadUtils$BitmapLoadCallback;

class TransformImageView$1 implements BitmapLoadUtils$BitmapLoadCallback {
    final /* synthetic */ TransformImageView this$0;

    TransformImageView$1(TransformImageView transformImageView) {
        this.this$0 = transformImageView;
    }

    public void onBitmapLoaded(@NonNull Bitmap bitmap) {
        TransformImageView.access$002(this.this$0, true);
        this.this$0.setImageBitmap(bitmap);
        this.this$0.invalidate();
    }

    public void onFailure(@NonNull Exception bitmapWorkerException) {
        Log.e("TransformImageView", "onFailure: setImageUri", bitmapWorkerException);
        if (this.this$0.mTransformImageListener != null) {
            this.this$0.mTransformImageListener.onLoadFailure(bitmapWorkerException);
        }
    }
}
