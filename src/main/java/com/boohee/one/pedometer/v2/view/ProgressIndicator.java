package com.boohee.one.pedometer.v2.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.baidu.location.aj;

public class ProgressIndicator extends RelativeLayout {
    public static final int MARGIN = 24;
    private       CircleProgress cpIndicator;
    private       ImageView      ivIcon;
    private final LayoutParams   mParams;
    private       float          mProgress;
    private       TextView       tvContent;

    class CircleProgress extends View {
        private static final int CIRCLE_INDICATOR_WIDTH = 2;
        private static final int CIRCLE_START_ANGLE     = -90;
        private static final int CIRCLE_STROKE_WIDTH    = 8;
        private final int CIRCLE_INDICATOR_COLOR;
        private final int CIRCLE_STROKE_COLOR;
        private final int CIRCLE_STROKE_COLOR_END;
        private       int mProgress;
        private       int mSize;

        public CircleProgress(ProgressIndicator this$0, Context context) {
            this(this$0, context, null);
        }

        public CircleProgress(ProgressIndicator this$0, Context context, AttributeSet attrs) {
            this(context, attrs, 0);
        }

        public CircleProgress(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            this.CIRCLE_INDICATOR_COLOR = Color.parseColor("#FFDFDFDF");
            this.CIRCLE_STROKE_COLOR = Color.parseColor("#FFFFB182");
            this.CIRCLE_STROKE_COLOR_END = Color.parseColor("#FFFF7C4D");
            this.mSize = 0;
            this.mProgress = 0;
        }

        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            int specSize = MeasureSpec.getSize(widthMeasureSpec);
            setMeasuredDimension(specSize, specSize);
        }

        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            this.mSize = w;
        }

        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            RectF oval = new RectF(8.0f, 8.0f, (float) (this.mSize - 8), (float) (this.mSize - 8));
            Paint bgPaint = new Paint();
            bgPaint.setAntiAlias(true);
            bgPaint.setStrokeWidth(2.0f);
            bgPaint.setStyle(Style.STROKE);
            bgPaint.setColor(this.CIRCLE_INDICATOR_COLOR);
            canvas.drawArc(oval, 0.0f, 360.0f, false, bgPaint);
            Paint circlePaint = new Paint();
            circlePaint.setAntiAlias(true);
            circlePaint.setStrokeWidth(8.0f);
            circlePaint.setStyle(Style.STROKE);
            circlePaint.setStrokeCap(Cap.ROUND);
            Matrix matrix = new Matrix();
            matrix.preRotate(-90.0f, (float) (this.mSize / 2), (float) (this.mSize / 2));
            SweepGradient gradient = new SweepGradient((float) (this.mSize / 2), (float) (this
                    .mSize / 2), this.CIRCLE_STROKE_COLOR, this.CIRCLE_STROKE_COLOR_END);
            gradient.setLocalMatrix(matrix);
            circlePaint.setShader(gradient);
            canvas.drawArc(oval, -90.0f, (float) this.mProgress, false, circlePaint);
            Paint fullPaint = new Paint();
            fullPaint.setAntiAlias(true);
            fullPaint.setStyle(Style.FILL);
            fullPaint.setStrokeCap(Cap.ROUND);
            if (this.mProgress == 360) {
                fullPaint.setColor(this.CIRCLE_STROKE_COLOR_END);
            } else {
                fullPaint.setColor(this.CIRCLE_STROKE_COLOR);
            }
            canvas.drawCircle((float) (this.mSize / 2), 8.0f, aj.hA, fullPaint);
        }

        public void setProgress(float progress) {
            if (progress < 0.0f) {
                progress = 0.0f;
            }
            if (progress > 1.0f) {
                progress = 1.0f;
            }
            this.mProgress = (int) (360.0f * progress);
            invalidate();
        }
    }

    public ProgressIndicator(Context context) {
        this(context, null);
    }

    public ProgressIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mParams = new LayoutParams(-2, -2);
        this.cpIndicator = new CircleProgress(this, context);
        addView(this.cpIndicator);
        this.mParams.addRule(13);
        this.mParams.setMargins(24, 24, 24, 24);
    }

    public void setProgress(float progress) {
        if (progress < 0.0f) {
            progress = 0.0f;
        }
        if (progress > 1.0f) {
            progress = 1.0f;
        }
        this.mProgress = progress;
        if (this.tvContent != null) {
            if (progress == 1.0f) {
                this.tvContent.setTextColor(-1);
            } else {
                this.tvContent.setTextColor(-3355444);
            }
        }
        if (this.cpIndicator != null) {
            this.cpIndicator.setProgress(progress);
        }
    }

    public void setImage(int resId) {
        if (resId > 0) {
            if (this.ivIcon == null) {
                this.ivIcon = new ImageView(getContext());
                addView(this.ivIcon, this.mParams);
            }
            this.ivIcon.setImageResource(resId);
        }
    }
}
