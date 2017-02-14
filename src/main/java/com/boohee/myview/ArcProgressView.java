package com.boohee.myview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.boohee.model.scale.ScaleIndex;
import com.boohee.utility.DensityUtil;

public class ArcProgressView extends View {
    private       int ARC_WIDTH          = 6;
    private final int BACKGROUND_COLOR   = 2113929215;
    private final int GREEN              = ScaleIndex.COLOR_STANDARD;
    private final int RED                = -94880;
    private       int SMALL_CIRCLE_WIDTH = 3;
    private final int WHITE              = -1;
    private int     height;
    private Paint   mBackgroundPaint;
    private Paint   mCirclePaint;
    private Context mContext;
    private RectF   mRect;
    private float percent = 0.0f;
    private float radius;
    private int   width;

    public ArcProgressView(Context context) {
        super(context);
        init(context);
    }

    public ArcProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ArcProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setLayerType(1, null);
        this.ARC_WIDTH = DensityUtil.dip2px(context, (float) this.ARC_WIDTH);
        this.SMALL_CIRCLE_WIDTH = DensityUtil.dip2px(context, (float) this.SMALL_CIRCLE_WIDTH);
        this.mBackgroundPaint = new Paint();
        this.mBackgroundPaint.setAntiAlias(true);
        this.mBackgroundPaint.setStrokeWidth((float) this.ARC_WIDTH);
        this.mBackgroundPaint.setStyle(Style.STROKE);
        this.mBackgroundPaint.setStrokeCap(Cap.ROUND);
        this.mCirclePaint = new Paint();
        this.mCirclePaint.setAntiAlias(true);
        this.mCirclePaint.setColor(-1);
        this.mCirclePaint.setStrokeWidth((float) this.SMALL_CIRCLE_WIDTH);
        this.mCirclePaint.setStyle(Style.STROKE);
        this.mCirclePaint.setStrokeCap(Cap.ROUND);
    }

    public void setProgress(float percent) {
        this.percent = percent;
        invalidate();
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.width = w;
        this.height = h;
        this.radius = ((float) (Math.min(w, h) - this.ARC_WIDTH)) / 2.0f;
        this.mRect = new RectF(-this.radius, -this.radius, this.radius, this.radius);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(((float) this.width) / 2.0f, ((float) this.height) / 2.0f);
        this.mBackgroundPaint.setColor(2113929215);
        canvas.drawArc(this.mRect, 120.0f, 300.0f, false, this.mBackgroundPaint);
        if (this.percent < -0.001f) {
            this.mBackgroundPaint.setColor(-94880);
            canvas.drawArc(this.mRect, 120.0f, 300.0f, false, this.mBackgroundPaint);
            return;
        }
        this.mBackgroundPaint.setColor(Color.GREEN);
        float sweep = Math.min(Math.max(this.percent * 300.0f, 1.0f), 300.0f);
        canvas.drawArc(this.mRect, 120.0f, sweep, false, this.mBackgroundPaint);
        double radians = Math.toRadians((double) (120.0f + sweep));
        canvas.drawPoint((float) (Math.cos(radians) * ((double) this.radius)), (float) (Math.sin
                (radians) * ((double) this.radius)), this.mCirclePaint);
    }
}
