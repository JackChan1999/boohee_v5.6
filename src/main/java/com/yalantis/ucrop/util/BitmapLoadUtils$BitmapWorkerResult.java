package com.yalantis.ucrop.util;

import android.graphics.Bitmap;
import android.support.annotation.Nullable;

class BitmapLoadUtils$BitmapWorkerResult {
    Bitmap    mBitmapResult;
    Exception mBitmapWorkerException;

    public BitmapLoadUtils$BitmapWorkerResult(@Nullable Bitmap bitmapResult, @Nullable Exception
            bitmapWorkerException) {
        this.mBitmapResult = bitmapResult;
        this.mBitmapWorkerException = bitmapWorkerException;
    }
}
