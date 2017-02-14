package com.boohee.myview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class BooheeCircleProgressBar extends View {
    private int maxProgress = 100;
    RectF oval  = new RectF();
    Paint paint = new Paint();
    private int progress            = 0;
    private int progressStrokeWidth = 4;

    public BooheeCircleProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        if (width != height) {
            int min = Math.min(width, height);
            width = min;
            height = min;
        }
        this.paint.setAntiAlias(true);
        this.paint.setColor(-1);
        canvas.drawColor(0);
        this.paint.setStrokeWidth((float) this.progressStrokeWidth);
        this.paint.setStyle(Style.STROKE);
        this.oval.left = (float) (this.progressStrokeWidth / 2);
        this.oval.top = (float) (this.progressStrokeWidth / 2);
        this.oval.right = (float) (width - (this.progressStrokeWidth / 2));
        this.oval.bottom = (float) (height - (this.progressStrokeWidth / 2));
        this.paint.setColor(Color.parseColor("#4cd964"));
        canvas.drawArc(this.oval, -90.0f, 360.0f * (((float) this.progress) / ((float) this
                .maxProgress)), false, this.paint);
    }

    public int getMaxProgress() {
        return this.maxProgress;
    }

    public void setMaxProgress(int maxProgress) {
        this.maxProgress = maxProgress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
        invalidate();
    }

    public void setProgressNotInUiThread(int progress) {
        this.progress = progress;
        postInvalidate();
    }
}
