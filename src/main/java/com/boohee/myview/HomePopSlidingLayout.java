package com.boohee.myview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import com.boohee.utility.DensityUtil;

public class HomePopSlidingLayout extends RelativeLayout {
    private int  DEFAULT_SLIDING;
    private long deltaTime;
    private int  distance;
    long  downTime;
    float downX;
    float downY;
    private OnSlidingUpListener onSlidingUpListener;
    long  upTime;
    float upX;
    float upY;

    public interface OnSlidingUpListener {
        void onClick();

        void onSlidingUp();
    }

    public HomePopSlidingLayout(Context context) {
        this(context, null);
    }

    public HomePopSlidingLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HomePopSlidingLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.DEFAULT_SLIDING = 50;
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case 0:
                this.downY = motionEvent.getRawY();
                this.downX = motionEvent.getRawX();
                this.downTime = motionEvent.getEventTime();
                break;
            case 1:
                this.upTime = motionEvent.getEventTime();
                this.upX = motionEvent.getRawX();
                this.upY = motionEvent.getRawY();
                this.deltaTime = this.upTime - this.downTime;
                if (this.downY - this.upY >= ((float) DensityUtil.dip2px(getContext(), (float)
                        this.DEFAULT_SLIDING))) {
                    if (this.onSlidingUpListener != null) {
                        this.onSlidingUpListener.onSlidingUp();
                    }
                    return true;
                }
                break;
        }
        return false;
    }

    public void setOnSlidingUpListener(OnSlidingUpListener onSlidingUpListener) {
        this.onSlidingUpListener = onSlidingUpListener;
    }
}
