package com.zxinsight.common.http;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.widget.ImageView.ScaleType;

import com.zxinsight.common.http.Request.HttpMethod;

public class n extends Request {
    private static final Object i = new Object();
    private final Config    e;
    private final int       f;
    private final int       g;
    private       ScaleType h;

    public n(String str, u<Bitmap> uVar, int i, int i2, ScaleType scaleType, Config config) {
        super(HttpMethod.GET, str, uVar);
        this.d = uVar;
        this.e = config;
        this.f = i;
        this.g = i2;
        this.h = scaleType;
    }

    public Request$Priority a() {
        return Request$Priority.LOW;
    }

    private static int a(int i, int i2, int i3, int i4, ScaleType scaleType) {
        if (i == 0 && i2 == 0) {
            return i3;
        }
        if (scaleType == ScaleType.FIT_XY) {
            if (i == 0) {
                return i3;
            }
            return i;
        } else if (i == 0) {
            return (int) ((((double) i2) / ((double) i4)) * ((double) i3));
        } else {
            if (i2 == 0) {
                return i;
            }
            double d = ((double) i4) / ((double) i3);
            if (scaleType == ScaleType.CENTER_CROP) {
                if (((double) i) * d < ((double) i2)) {
                    return (int) (((double) i2) / d);
                }
                return i;
            } else if (((double) i) * d > ((double) i2)) {
                return (int) (((double) i2) / d);
            } else {
                return i;
            }
        }
    }

    public void a(byte[] bArr) {
        synchronized (i) {
            try {
                c(bArr);
            } catch (OutOfMemoryError e) {
            }
        }
    }

    private void c(byte[] bArr) {
        Object decodeByteArray;
        Options options = new Options();
        if (this.f == 0 && this.g == 0) {
            options.inPreferredConfig = this.e;
            decodeByteArray = BitmapFactory.decodeByteArray(bArr, 0, bArr.length, options);
        } else {
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeByteArray(bArr, 0, bArr.length, options);
            int i = options.outWidth;
            int i2 = options.outHeight;
            int a = a(this.f, this.g, i, i2, this.h);
            int a2 = a(this.g, this.f, i2, i, this.h);
            options.inJustDecodeBounds = false;
            options.inSampleSize = a(i, i2, a, a2);
            Bitmap decodeByteArray2 = BitmapFactory.decodeByteArray(bArr, 0, bArr.length, options);
            if (decodeByteArray2 == null || (decodeByteArray2.getWidth() <= a && decodeByteArray2
                    .getHeight() <= a2)) {
                Bitmap bitmap = decodeByteArray2;
            } else {
                decodeByteArray = Bitmap.createScaledBitmap(decodeByteArray2, a, a2, true);
                decodeByteArray2.recycle();
            }
        }
        if (decodeByteArray == null) {
            this.d.a(new Exception("The response is not an image"));
        } else {
            this.d.a(decodeByteArray);
        }
    }

    static int a(int i, int i2, int i3, int i4) {
        float f = 1.0f;
        while (((double) (f * 2.0f)) <= Math.min(((double) i) / ((double) i3), ((double) i2) / (
                (double) i4))) {
            f *= 2.0f;
        }
        return (int) f;
    }
}
