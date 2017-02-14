package com.boohee.myview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class ScrollViewWithViewPager extends ScrollView {
    private float xDistance;
    private float xLast;
    private float yDistance;
    private float yLast;

    public ScrollViewWithViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case 0:
                this.yDistance = 0.0f;
                this.xDistance = 0.0f;
                this.xLast = ev.getX();
                this.yLast = ev.getY();
                break;
            case 2:
                float curX = ev.getX();
                float curY = ev.getY();
                this.xDistance += Math.abs(curX - this.xLast);
                this.yDistance += Math.abs(curY - this.yLast);
                this.xLast = curX;
                this.yLast = curY;
                if (this.xDistance > this.yDistance) {
                    return false;
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }
}
