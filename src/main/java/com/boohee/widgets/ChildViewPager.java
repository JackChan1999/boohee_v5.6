package com.boohee.widgets;

import android.content.Context;
import android.graphics.PointF;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class ChildViewPager extends ViewPager {
    PointF curP  = new PointF();
    PointF downP = new PointF();
    OnSingleTouchListener onSingleTouchListener;

    public interface OnSingleTouchListener {
        void onSingleTouch();
    }

    public ChildViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ChildViewPager(Context context) {
        super(context);
    }

    public boolean onTouchEvent(MotionEvent arg0) {
        this.curP.x = arg0.getX();
        this.curP.y = arg0.getY();
        if (arg0.getAction() == 0) {
            this.downP.x = arg0.getX();
            this.downP.y = arg0.getY();
            getParent().requestDisallowInterceptTouchEvent(true);
        }
        if (arg0.getAction() == 2) {
            getParent().requestDisallowInterceptTouchEvent(true);
        }
        if (arg0.getAction() == 1 && ((int) this.downP.x) - ((int) this.curP.x) > -10 && ((int)
                this.downP.x) - ((int) this.curP.x) < 10) {
            onSingleTouch();
        }
        return super.onTouchEvent(arg0);
    }

    public void onSingleTouch() {
        if (this.onSingleTouchListener != null) {
            this.onSingleTouchListener.onSingleTouch();
        }
    }

    public void setOnSingleTouchListener(OnSingleTouchListener onSingleTouchListener) {
        this.onSingleTouchListener = onSingleTouchListener;
    }
}
