package com.yalantis.ucrop.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.ParcelFileDescriptor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;

class BitmapLoadUtils$BitmapWorkerTask extends AsyncTask<Void, Void,
        BitmapLoadUtils$BitmapWorkerResult> {
    private final BitmapLoadUtils$BitmapLoadCallback mBitmapLoadCallback;
    private final Context                            mContext;
    private final int                                mRequiredHeight;
    private final int                                mRequiredWidth;
    private final Uri                                mUri;

    public BitmapLoadUtils$BitmapWorkerTask(@NonNull Context context, @Nullable Uri uri, int
            requiredWidth, int requiredHeight, BitmapLoadUtils$BitmapLoadCallback loadCallback) {
        this.mContext = context;
        this.mUri = uri;
        this.mRequiredWidth = requiredWidth;
        this.mRequiredHeight = requiredHeight;
        this.mBitmapLoadCallback = loadCallback;
    }

    @NonNull
    protected BitmapLoadUtils$BitmapWorkerResult doInBackground(Void... params) {
        if (this.mUri == null) {
            return new BitmapLoadUtils$BitmapWorkerResult(null, new NullPointerException("Uri " +
                    "cannot be null"));
        }
        try {
            ParcelFileDescriptor parcelFileDescriptor = this.mContext.getContentResolver()
                    .openFileDescriptor(this.mUri, "r");
            if (parcelFileDescriptor == null) {
                return new BitmapLoadUtils$BitmapWorkerResult(null, new NullPointerException
                        ("ParcelFileDescriptor was null for given Uri"));
            }
            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
            Options options = new Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);
            options.inSampleSize = BitmapLoadUtils.calculateInSampleSize(options, this
                    .mRequiredWidth, this.mRequiredHeight);
            options.inJustDecodeBounds = false;
            Bitmap decodeSampledBitmap = null;
            boolean success = false;
            while (!success) {
                try {
                    decodeSampledBitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor,
                            null, options);
                    success = true;
                } catch (OutOfMemoryError error) {
                    Log.e("BitmapLoadUtils", "doInBackground: BitmapFactory.decodeFileDescriptor:" +
                            " ", error);
                    options.inSampleSize++;
                }
            }
            if (VERSION.SDK_INT >= 16) {
                BitmapLoadUtils.close(parcelFileDescriptor);
            }
            int exifOrientation = BitmapLoadUtils.access$000(this.mContext, this.mUri);
            int exifDegrees = BitmapLoadUtils.access$100(exifOrientation);
            int exifTranslation = BitmapLoadUtils.access$200(exifOrientation);
            Matrix matrix = new Matrix();
            if (exifDegrees != 0) {
                matrix.preRotate((float) exifDegrees);
            }
            if (exifTranslation != 1) {
                matrix.postScale((float) exifTranslation, 1.0f);
            }
            if (matrix.isIdentity()) {
                return new BitmapLoadUtils$BitmapWorkerResult(decodeSampledBitmap, null);
            }
            return new BitmapLoadUtils$BitmapWorkerResult(BitmapLoadUtils.transformBitmap
                    (decodeSampledBitmap, matrix), null);
        } catch (FileNotFoundException e) {
            return new BitmapLoadUtils$BitmapWorkerResult(null, e);
        }
    }

    protected void onPostExecute(@NonNull BitmapLoadUtils$BitmapWorkerResult result) {
        if (result.mBitmapWorkerException == null) {
            this.mBitmapLoadCallback.onBitmapLoaded(result.mBitmapResult);
        } else {
            this.mBitmapLoadCallback.onFailure(result.mBitmapWorkerException);
        }
    }
}
