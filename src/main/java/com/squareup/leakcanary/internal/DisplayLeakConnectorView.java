package com.squareup.leakcanary.internal;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.baidu.location.aj;

import uk.co.senab.photoview.IPhotoView;

public final class DisplayLeakConnectorView extends View {
    private static final Paint clearPaint = new Paint(1);
    private static final Paint iconPaint  = new Paint(1);
    private static final Paint leakPaint  = new Paint(1);
    private static final Paint rootPaint  = new Paint(1);
    private Bitmap cache;
    private Type type = Type.NODE;

    public enum Type {
        START,
        NODE,
        END
    }

    static {
        iconPaint.setColor(-4539718);
        rootPaint.setColor(-8083771);
        leakPaint.setColor(-5155506);
        clearPaint.setColor(0);
        clearPaint.setXfermode(LeakCanaryUi.CLEAR_XFER_MODE);
    }

    public DisplayLeakConnectorView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected void onDraw(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();
        if (!(this.cache == null || (this.cache.getWidth() == width && this.cache.getHeight() ==
                height))) {
            this.cache.recycle();
            this.cache = null;
        }
        if (this.cache == null) {
            this.cache = Bitmap.createBitmap(width, height, Config.ARGB_8888);
            Canvas cacheCanvas = new Canvas(this.cache);
            float halfWidth = ((float) width) / 2.0f;
            float halfHeight = ((float) height) / 2.0f;
            float thirdWidth = ((float) width) / IPhotoView.DEFAULT_MAX_SCALE;
            float strokeSize = LeakCanaryUi.dpToPixel(aj.hA, getResources());
            iconPaint.setStrokeWidth(strokeSize);
            rootPaint.setStrokeWidth(strokeSize);
            switch (this.type) {
                case NODE:
                    cacheCanvas.drawLine(halfWidth, 0.0f, halfWidth, (float) height, iconPaint);
                    cacheCanvas.drawCircle(halfWidth, halfHeight, halfWidth, iconPaint);
                    cacheCanvas.drawCircle(halfWidth, halfHeight, thirdWidth, clearPaint);
                    break;
                case START:
                    float radiusClear = halfWidth - (strokeSize / 2.0f);
                    cacheCanvas.drawRect(0.0f, 0.0f, (float) width, radiusClear, rootPaint);
                    cacheCanvas.drawCircle(0.0f, radiusClear, radiusClear, clearPaint);
                    cacheCanvas.drawCircle((float) width, radiusClear, radiusClear, clearPaint);
                    cacheCanvas.drawLine(halfWidth, 0.0f, halfWidth, halfHeight, rootPaint);
                    cacheCanvas.drawLine(halfWidth, halfHeight, halfWidth, (float) height,
                            iconPaint);
                    cacheCanvas.drawCircle(halfWidth, halfHeight, halfWidth, iconPaint);
                    cacheCanvas.drawCircle(halfWidth, halfHeight, thirdWidth, clearPaint);
                    break;
                default:
                    cacheCanvas.drawLine(halfWidth, 0.0f, halfWidth, halfHeight, iconPaint);
                    cacheCanvas.drawCircle(halfWidth, halfHeight, thirdWidth, leakPaint);
                    break;
            }
        }
        canvas.drawBitmap(this.cache, 0.0f, 0.0f, null);
    }

    public void setType(Type type) {
        if (type != this.type) {
            this.type = type;
            if (this.cache != null) {
                this.cache.recycle();
                this.cache = null;
            }
            invalidate();
        }
    }
}
