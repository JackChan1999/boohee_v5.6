package com.boohee.widgets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.support.v4.internal.view.SupportMenu;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

public class TextProgressBar extends View {
    private int    backgroundColor = Color.parseColor("#DFDFDF");
    private Bitmap bgBitmap        = null;
    private int    progressColor   = Color.parseColor("#FF00AEF0");
    private float  progressRate    = 0.0f;
    private int    strokeWidth     = 4;
    private String text            = "";
    private int    textColor       = SupportMenu.CATEGORY_MASK;

    public TextProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public TextProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TextProgressBar(Context context) {
        super(context);
    }

    @SuppressLint({"DrawAllocation"})
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth((float) this.strokeWidth);
        paint.setColor(this.backgroundColor);
        drawLine(canvas, paint, 1.0f);
        paint.setColor(this.progressColor);
        drawLine(canvas, paint, this.progressRate);
        drawBg(canvas, paint, this.progressRate);
        drawText(canvas, paint, this.progressRate);
    }

    private int getBgHeight() {
        return this.bgBitmap != null ? this.bgBitmap.getHeight() : 0;
    }

    private float getBgWidth() {
        return this.bgBitmap != null ? (float) this.bgBitmap.getWidth() : 0.0f;
    }

    private void drawLine(Canvas canvas, Paint paint, float progressRate) {
        paint.setStyle(Style.FILL);
        canvas.drawRect(0.0f, (float) ((getHeight() / 2) - (this.strokeWidth / 2)), ((float)
                getWidth()) * progressRate, (float) ((getHeight() / 2) + (this.strokeWidth / 2)),
                paint);
    }

    private void drawBg(Canvas canvas, Paint paint, float progressRate) {
        if (this.bgBitmap != null) {
            float left = (((float) getWidth()) * progressRate) - (getBgWidth() / 2.0f);
            float top = (float) ((getHeight() / 2) - (getBgHeight() / 2));
            if (left < 0.0f) {
                left = 0.0f;
            }
            if (left > ((float) getWidth()) - getBgWidth()) {
                left = ((float) getWidth()) - getBgWidth();
            }
            canvas.drawBitmap(this.bgBitmap, left, top, paint);
        }
    }

    private void drawText(Canvas canvas, Paint paint, float progressRate) {
        if (!TextUtils.isEmpty(this.text)) {
            float textWidth = paint.measureText(this.text);
            float textHeight = getTextHeight();
            float left = (((float) getWidth()) * progressRate) - (getBgWidth() / 2.0f);
            if (left < 0.0f) {
                left = 0.0f;
            }
            if (left > ((float) getWidth()) - getBgWidth()) {
                left = ((float) getWidth()) - getBgWidth();
            }
            float x = (textWidth / 2.0f) + left;
            float y = ((float) getHeight()) - (textHeight / 2.0f);
            if (x < 0.0f) {
                x = (getBgWidth() / 2.0f) - (textWidth / 2.0f);
            }
            if (x > (((float) getWidth()) - (getBgWidth() / 2.0f)) - (textWidth / 2.0f)) {
                x = (((float) getWidth()) - (getBgWidth() / 2.0f)) - (textWidth / 2.0f);
            }
            paint.setStyle(Style.FILL);
            paint.setStrokeWidth(1.0f);
            paint.setTextSize((float) (getHeight() / 2));
            paint.setColor(this.textColor);
            canvas.drawText(this.text, x, y, paint);
        }
    }

    private float getTextHeight() {
        Paint paint = new Paint();
        paint.setTextSize((float) (getHeight() / 2));
        FontMetrics fm = paint.getFontMetrics();
        return ((float) Math.ceil((double) (fm.descent - fm.top))) + 2.0f;
    }

    public String getText() {
        return this.text;
    }

    public synchronized void setText(String text) {
        this.text = text;
        postInvalidate();
    }

    public int getProgressColor() {
        return this.progressColor;
    }

    public synchronized void setProgressColor(int progressColor) {
        this.progressColor = progressColor;
        postInvalidate();
    }

    public float getProgressReate() {
        return this.progressRate;
    }

    public synchronized void setProgressRate(float progressRate) {
        this.progressRate = progressRate;
        postInvalidate();
    }

    public int getBackgroundColor() {
        return this.backgroundColor;
    }

    public synchronized void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
        postInvalidate();
    }

    public Bitmap getBgBitmap() {
        return this.bgBitmap;
    }

    public synchronized void setBgBitmap(Bitmap bgBitmap) {
        this.bgBitmap = bgBitmap;
        postInvalidate();
    }

    public int getTextColor() {
        return this.textColor;
    }

    public synchronized void setTextColor(int textColor) {
        this.textColor = textColor;
        postInvalidate();
    }
}
