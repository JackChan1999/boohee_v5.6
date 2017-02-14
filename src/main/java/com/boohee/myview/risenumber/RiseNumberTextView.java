package com.boohee.myview.risenumber;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener;
import com.qiniu.android.dns.NetworkInfo;

import java.text.DecimalFormat;

public class RiseNumberTextView extends TextView implements RiseNumberBase {
    public static final  int   FloatFormatType = 2;
    public static final  int   IntFormatType   = 1;
    private static final int   RUNNING         = 1;
    private static final int   STOPPED         = 0;
    static final         int[] sizeTable       = new int[]{9, 99, NetworkInfo.ISP_OTHER, 9999,
            99999, 999999, 9999999, 99999999, 999999999, ActivityChooserViewAdapter
            .MAX_ACTIVITY_COUNT_UNLIMITED};
    private              long  duration        = 1000;
    private DecimalFormat fnum;
    private float         fromNumber;
    private EndListener mEndListener  = null;
    private int         mPlayingState = 0;
    private float number;
    private int numberType = 2;

    public interface EndListener {
        void onEndFinish();
    }

    public RiseNumberTextView(Context context) {
        super(context);
    }

    public RiseNumberTextView(Context context, AttributeSet attr) {
        super(context, attr);
    }

    public RiseNumberTextView(Context context, AttributeSet attr, int defStyle) {
        super(context, attr, defStyle);
    }

    public void setTypeface() {
        try {
            super.setTypeface(Typeface.createFromAsset(getContext().getAssets(),
                    "fonts/helvetica_neue_ltpro_lt.ttf"));
        } catch (Exception e) {
        }
    }

    public boolean isRunning() {
        return this.mPlayingState == 1;
    }

    private void runFloat() {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(this.fromNumber, this.number);
        valueAnimator.setDuration(this.duration);
        valueAnimator.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                RiseNumberTextView.this.setText(RiseNumberTextView.this.fnum.format((double)
                        Float.parseFloat(valueAnimator.getAnimatedValue().toString())));
                if (valueAnimator.getAnimatedFraction() >= 1.0f) {
                    RiseNumberTextView.this.mPlayingState = 0;
                    if (RiseNumberTextView.this.mEndListener != null) {
                        RiseNumberTextView.this.mEndListener.onEndFinish();
                    }
                }
            }
        });
        valueAnimator.start();
    }

    private void runInt() {
        ValueAnimator valueAnimator = ValueAnimator.ofInt((int) this.fromNumber, (int) this.number);
        valueAnimator.setDuration(this.duration);
        valueAnimator.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                RiseNumberTextView.this.setText(valueAnimator.getAnimatedValue().toString());
                if (valueAnimator.getAnimatedFraction() >= 1.0f) {
                    RiseNumberTextView.this.mPlayingState = 0;
                    if (RiseNumberTextView.this.mEndListener != null) {
                        RiseNumberTextView.this.mEndListener.onEndFinish();
                    }
                }
            }
        });
        valueAnimator.start();
    }

    static int sizeOfInt(int x) {
        int i = 0;
        while (x > sizeTable[i]) {
            i++;
        }
        return i + 1;
    }

    protected void onFinishInflate() {
        super.onFinishInflate();
        this.fnum = new DecimalFormat("##0.0");
    }

    public void setFloatFormat() {
        this.fnum = new DecimalFormat("##0.0");
    }

    public void setIntFormat() {
        this.fnum = new DecimalFormat("##0");
    }

    public void start() {
        if (!isRunning()) {
            this.mPlayingState = 1;
            if (this.numberType == 1) {
                runInt();
            } else {
                runFloat();
            }
        }
    }

    public RiseNumberTextView withNumber(float number) {
        this.number = number;
        this.numberType = 2;
        if (number > 1000.0f) {
            this.fromNumber = number - ((float) Math.pow(10.0d, (double) (sizeOfInt((int) number)
                    - 2)));
        } else {
            this.fromNumber = number / 2.0f;
        }
        return this;
    }

    public RiseNumberTextView withNumber(int number) {
        this.number = (float) number;
        this.numberType = 1;
        if (number > 1000) {
            this.fromNumber = ((float) number) - ((float) Math.pow(10.0d, (double) (sizeOfInt
                    (number) - 2)));
        } else {
            this.fromNumber = (float) (number / 2);
        }
        return this;
    }

    public RiseNumberTextView setDuration(long duration) {
        this.duration = duration;
        return this;
    }

    public void setOnEnd(EndListener callback) {
        this.mEndListener = callback;
    }
}
