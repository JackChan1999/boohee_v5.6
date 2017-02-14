package com.yalantis.ucrop.util;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

public interface BitmapLoadUtils$BitmapLoadCallback {
    void onBitmapLoaded(@NonNull Bitmap bitmap);

    void onFailure(@NonNull Exception exception);
}
