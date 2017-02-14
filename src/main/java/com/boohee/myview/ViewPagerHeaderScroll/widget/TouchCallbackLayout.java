package com.boohee.myview.ViewPagerHeaderScroll.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

public class TouchCallbackLayout extends FrameLayout {
    private TouchEventListener mTouchEventListener;

    public interface TouchEventListener {
        boolean onLayoutInterceptTouchEvent(MotionEvent motionEvent);

        boolean onLayoutTouchEvent(MotionEvent motionEvent);
    }

    public void setTouchEventListener(TouchEventListener touchEventListener) {
        this.mTouchEventListener = touchEventListener;
    }

    public TouchCallbackLayout(Context context) {
        super(context);
    }

    public TouchCallbackLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TouchCallbackLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (this.mTouchEventListener != null) {
            return this.mTouchEventListener.onLayoutInterceptTouchEvent(ev);
        }
        return super.onInterceptTouchEvent(ev);
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (this.mTouchEventListener != null) {
            return this.mTouchEventListener.onLayoutTouchEvent(event);
        }
        return super.onTouchEvent(event);
    }
}
