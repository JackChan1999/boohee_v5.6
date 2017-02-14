package com.boohee.myview.hybrid;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;

public class ChildView extends ScrollView implements IScrollStatus {
    public ChildView(Context context) {
        this(context, null);
    }

    public ChildView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChildView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public boolean isScrollTop() {
        return getScrollY() == 0;
    }

    public boolean isScrollBottom() {
        View childView = getChildAt(0);
        if (childView == null) {
            return false;
        }
        if (((float) (childView.getMeasuredHeight() - (getHeight() + getScrollY()))) < 10.0f) {
            return true;
        }
        return false;
    }
}
