package com.boohee.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;

import com.boohee.one.R;

public class RoundedCornersImage extends MaskedImage {
    private static final int DEFAULT_CORNER_RADIUS = 6;
    private              int cornerRadius          = 6;

    public RoundedCornersImage(Context paramContext) {
        super(paramContext);
    }

    public RoundedCornersImage(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
        TypedArray a = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable
                .RoundedCornersImage);
        this.cornerRadius = a.getDimensionPixelSize(0, 6);
        a.recycle();
    }

    public Bitmap createMask() {
        Bitmap localBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Config.ARGB_8888);
        Canvas localCanvas = new Canvas(localBitmap);
        Paint localPaint = new Paint(1);
        localPaint.setColor(-16777216);
        localCanvas.drawRoundRect(new RectF(0.0f, 0.0f, (float) getWidth(), (float) getHeight()),
                (float) this.cornerRadius, (float) this.cornerRadius, localPaint);
        return localBitmap;
    }
}
