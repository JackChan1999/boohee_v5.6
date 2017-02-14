package com.boohee.myview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View;

import com.boohee.one.R;

public class ProgressLine extends View {
    private static final int HORIZONTAL       = 1;
    private static final int VERTICAL         = 0;
    private              int BACKGROUND_COLOR = -1118482;
    private int   height;
    private Paint mPaint;
    private int   orientation;
    private float percent;
    private int progressColor = -11480382;
    private int strokeWidth;
    private int width;

    public ProgressLine(Context context) {
        super(context);
        init(context, null);
    }

    public ProgressLine(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ProgressLine(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable
                .ProgressLine);
        if (typedArray != null) {
            this.orientation = typedArray.getInt(0, 0);
            this.percent = typedArray.getFloat(1, 0.0f);
            typedArray.recycle();
        }
        setLayerType(1, null);
        this.mPaint = new Paint();
        this.mPaint.setStyle(Style.STROKE);
        this.mPaint.setStrokeCap(Cap.ROUND);
        this.mPaint.setAntiAlias(true);
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.height = h;
        this.width = w;
        if (this.orientation == 0) {
            this.strokeWidth = w;
        } else {
            this.strokeWidth = h;
        }
        this.mPaint.setStrokeWidth((float) this.strokeWidth);
    }

    public void setPercent(float percent) {
        if (percent > 1.0f) {
            percent = 1.0f;
        }
        if (percent < 0.0f) {
            percent = 0.0f;
        }
        this.percent = percent;
        invalidate();
    }

    public void setProgressColor(int color) {
        this.progressColor = color;
        invalidate();
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.mPaint.setColor(this.BACKGROUND_COLOR);
        if (this.orientation == 0) {
            canvas.drawLine(((float) this.width) / 2.0f, ((float) this.strokeWidth) / 2.0f, (
                    (float) this.width) / 2.0f, ((float) this.height) - (((float) this
                    .strokeWidth) / 2.0f), this.mPaint);
            if (((double) this.percent) > 1.0E-5d) {
                this.mPaint.setColor(this.progressColor);
                Canvas canvas2 = canvas;
                canvas2.drawLine(((float) this.width) / 2.0f, (((float) this.strokeWidth) / 2.0f)
                        + (((float) (this.height - this.strokeWidth)) * (1.0f - this.percent)), (
                        (float) this.width) / 2.0f, ((float) this.height) - (((float) this
                        .strokeWidth) / 2.0f), this.mPaint);
                return;
            }
            return;
        }
        canvas.drawLine(((float) this.strokeWidth) / 2.0f, ((float) this.height) / 2.0f, ((float)
                this.width) - (((float) this.strokeWidth) / 2.0f), ((float) this.height) / 2.0f,
                this.mPaint);
        if (((double) this.percent) > 1.0E-5d) {
            this.mPaint.setColor(this.progressColor);
            canvas2 = canvas;
            canvas2.drawLine(((float) this.strokeWidth) / 2.0f, ((float) this.height) / 2.0f, ((
                    (float) (this.width - this.strokeWidth)) * this.percent) + (((float) this
                    .strokeWidth) / 2.0f), ((float) this.height) / 2.0f, this.mPaint);
        }
    }
}
