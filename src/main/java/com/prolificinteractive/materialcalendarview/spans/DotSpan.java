package com.prolificinteractive.materialcalendarview.spans;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.style.LineBackgroundSpan;

public class DotSpan implements LineBackgroundSpan {
    private static final float DEFAULT_RADIUS = 3.0f;
    private final int   color;
    private final float radius;

    public DotSpan() {
        this.radius = 3.0f;
        this.color = 0;
    }

    public DotSpan(int color) {
        this.radius = 3.0f;
        this.color = color;
    }

    public DotSpan(float radius) {
        this.radius = radius;
        this.color = 0;
    }

    public DotSpan(float radius, int color) {
        this.radius = radius;
        this.color = color;
    }

    public void drawBackground(Canvas canvas, Paint paint, int left, int right, int top, int
            baseline, int bottom, CharSequence charSequence, int start, int end, int lineNum) {
        int oldColor = paint.getColor();
        if (this.color != 0) {
            paint.setColor(this.color);
        }
        canvas.drawCircle((float) ((left + right) / 2), ((float) bottom) + this.radius, this
                .radius, paint);
        paint.setColor(oldColor);
    }
}
