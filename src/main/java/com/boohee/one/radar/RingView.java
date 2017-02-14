package com.boohee.one.radar;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.boohee.utility.DensityUtil;

public class RingView extends View {
    private static final int   START_DEGREE     = -90;
    private final        int   DEFAULT_DURATION = 1200;
    private              int   RING_WIDTH       = 30;
    private              int[] TEXTCOLOR        = new int[]{-16686650, -12996924, -1784965};
    private              int   TEXT_SIZE        = 12;
    private int[]         color;
    private boolean       drawText;
    private int           height;
    private Context       mContext;
    private Paint         mPaint;
    private Path          mPath;
    private RectF         mRect;
    private Paint         mTextPaint;
    private Rect          mTextRect;
    private float[]       percent;
    private float         radius;
    private boolean       showDrawText;
    private float[]       target;
    private ValueAnimator valueAnimator;
    private int           width;

    public RingView(Context context) {
        super(context);
        init(context);
    }

    public RingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.RING_WIDTH = DensityUtil.dip2px(context, (float) this.RING_WIDTH);
        this.TEXT_SIZE = DensityUtil.dip2px(context, (float) this.TEXT_SIZE);
        this.mPaint = new Paint();
        this.mPaint.setAntiAlias(true);
        this.mPaint.setStyle(Style.STROKE);
        this.mPaint.setStrokeWidth((float) this.RING_WIDTH);
        this.mTextPaint = new Paint();
        this.mTextPaint.setAntiAlias(true);
        this.mTextPaint.setTextAlign(Align.LEFT);
        this.mTextPaint.setTextSize((float) this.TEXT_SIZE);
        this.mPath = new Path();
        this.mTextRect = new Rect();
    }

    public void setColor(int[] color) {
        this.color = color;
        invalidate();
    }

    public void setPercent(float[] percent) {
        this.percent = percent;
        invalidate();
    }

    public void setTarget(float[] target) {
        this.target = target;
        this.percent = new float[target.length];
        this.showDrawText = this.drawText;
        setDrawText(false);
    }

    public void startAnim() {
        if (this.target != null && this.valueAnimator == null) {
            this.valueAnimator = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f}).setDuration(1200);
            this.valueAnimator.addUpdateListener(new AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    float value = ((Float) animation.getAnimatedValue()).floatValue();
                    for (int i = 0; i < RingView.this.percent.length; i++) {
                        RingView.this.percent[i] = RingView.this.target[i] * value;
                    }
                    RingView.this.invalidate();
                }
            });
            this.valueAnimator.addListener(new AnimatorListener() {
                public void onAnimationStart(Animator animation) {
                    RingView.this.drawText = false;
                }

                public void onAnimationEnd(Animator animation) {
                    RingView.this.drawText = RingView.this.showDrawText;
                    RingView.this.invalidate();
                }

                public void onAnimationCancel(Animator animation) {
                }

                public void onAnimationRepeat(Animator animation) {
                }
            });
            this.valueAnimator.start();
        }
    }

    public void setDrawText(boolean drawText) {
        this.drawText = drawText;
        invalidate();
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.width = w;
        this.height = h;
        this.radius = Math.min(((float) this.width) / 2.0f, ((float) this.height) / 2.0f) - ((
                (float) this.RING_WIDTH) / 2.0f);
        this.mRect = new RectF(-this.radius, -this.radius, this.radius, this.radius);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(((float) this.width) / 2.0f, ((float) this.height) / 2.0f);
        if (this.percent != null && this.color != null && this.percent.length == this.color
                .length) {
            int i;
            float start = -90.0f;
            for (i = this.percent.length - 1; i >= 0; i--) {
                this.mPath.reset();
                float sweep = 360.0f * this.percent[i];
                if (sweep >= 360.0f) {
                    sweep = 360.0f;
                }
                this.mPath.addArc(this.mRect, start, sweep);
                this.mPaint.setColor(this.color[i]);
                start += this.percent[i] * 360.0f;
                canvas.drawPath(this.mPath, this.mPaint);
            }
            if (this.drawText && this.TEXTCOLOR != null && this.TEXTCOLOR.length == this.percent
                    .length) {
                start = -90.0f;
                for (i = this.percent.length - 1; i >= 0; i--) {
                    float end = start + (this.percent[i] * 360.0f);
                    float radians = (float) Math.toRadians((double) ((start + end) / 2.0f));
                    drawText((float) (Math.cos((double) radians) * ((double) this.radius)),
                            (float) (Math.sin((double) radians) * ((double) this.radius)), String
                                    .valueOf(Math.round(this.percent[i] * 100.0f)) + "%", canvas,
                            this.mTextPaint, this.TEXTCOLOR[i]);
                    start = end;
                }
            }
        }
    }

    private void drawText(float centerX, float centerY, String text, Canvas canvas, Paint paint,
                          int color) {
        paint.setColor(color);
        paint.getTextBounds(text, 0, text.length(), this.mTextRect);
        canvas.drawText(text, (centerX - (((float) this.mTextRect.width()) / 2.0f)) - ((float) this.mTextRect.left), ((((float) this.mTextRect.height()) / 2.0f) + centerY) - ((float) this.mTextRect.bottom), paint);
    }
}
