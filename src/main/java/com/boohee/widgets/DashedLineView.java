package com.boohee.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

import com.boohee.one.R;

public class DashedLineView extends View {
    private Paint mPaint;
    private Path  mPath;

    public DashedLineView(Context context) {
        super(context);
        init(context);
    }

    public DashedLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DashedLineView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float dashGap = TypedValue.applyDimension(1, 2.0f, metrics);
        float dashWidth = TypedValue.applyDimension(1, 2.0f, metrics);
        this.mPaint = new Paint();
        this.mPaint.setStyle(Style.STROKE);
        this.mPaint.setAntiAlias(true);
        this.mPaint.setColor(getResources().getColor(R.color.j8));
        this.mPaint.setPathEffect(new DashPathEffect(new float[]{dashWidth, dashGap}, 0.0f));
        this.mPath = new Path();
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int measuredHeight = getMeasuredHeight();
        int measuredWidth = getMeasuredWidth();
        if (measuredHeight <= measuredWidth) {
            this.mPaint.setStrokeWidth((float) measuredHeight);
            this.mPath.moveTo(0.0f, ((float) measuredHeight) / 2.0f);
            this.mPath.lineTo((float) measuredWidth, ((float) measuredHeight) / 2.0f);
            canvas.drawPath(this.mPath, this.mPaint);
            return;
        }
        this.mPaint.setStrokeWidth((float) measuredWidth);
        this.mPath.moveTo(((float) measuredWidth) / 2.0f, 0.0f);
        this.mPath.lineTo(((float) measuredWidth) / 2.0f, (float) measuredHeight);
        canvas.drawPath(this.mPath, this.mPaint);
    }
}
