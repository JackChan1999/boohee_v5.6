package com.boohee.one.video.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.boohee.one.R;

public class ProgressBarHintView extends View {
    private int   defaultBgColor;
    private int   defaultColor;
    private int   drawCount;
    private Paint paint;
    private int   sideLenth;

    public ProgressBarHintView(Context context) {
        this(context, null);
    }

    public ProgressBarHintView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressBarHintView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.sideLenth = 4;
        this.defaultColor = Color.parseColor("#7dffe1ff");
        initPaint();
    }

    private void initPaint() {
        this.paint = new Paint();
        this.paint.setColor(this.defaultColor);
        this.defaultBgColor = getResources().getColor(R.color.in);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(this.defaultBgColor);
        if (this.drawCount > 0) {
            drawIndicator(canvas);
        }
    }

    private void drawIndicator(Canvas canvas) {
        int margin = getWidth() / this.drawCount;
        for (int i = 0; i <= this.drawCount; i++) {
            int left = margin * i;
            canvas.drawRect(new Rect(left, 0, this.sideLenth + left, getHeight()), this.paint);
        }
    }

    public void setSideLenth(int sideLenth) {
        this.sideLenth = sideLenth;
    }

    public void setDrawCount(int drawCount) {
        this.drawCount = drawCount;
        invalidate();
    }
}
