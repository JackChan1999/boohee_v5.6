package com.boohee.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.boohee.utility.DensityUtil;
import com.boohee.utils.Helper;

public class FixGridLayout extends ViewGroup {
    static final String TAG    = FixGridLayout.class.getSimpleName();
    private      int    margin = 16;

    public FixGridLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.margin = DensityUtil.dip2px(context, 16.0f);
    }

    public FixGridLayout(Context context) {
        super(context);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Helper.showLog(TAG, "widthMeasureSpec = " + widthMeasureSpec + " heightMeasureSpec = " +
                heightMeasureSpec);
        for (int index = 0; index < getChildCount(); index++) {
            getChildAt(index).measure(0, 0);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    protected void onLayout(boolean arg0, int arg1, int arg2, int arg3, int arg4) {
        Helper.showLog(TAG, "changed = " + arg0 + " left = " + arg1 + " top = " + arg2 + " right " +
                "= " + arg3 + " botom = " + arg4);
        int count = getChildCount();
        int row = 0;
        int lengthX = arg1;
        int lengthY = arg2;
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            int width = child.getMeasuredWidth();
            int height = child.getMeasuredHeight();
            lengthX += this.margin + width;
            lengthY = ((((this.margin + height) * row) + this.margin) + height) + arg2;
            if (lengthX > arg3) {
                lengthX = (this.margin + width) + arg1;
                row++;
                lengthY = ((((this.margin + height) * row) + this.margin) + height) + arg2;
            }
            child.layout(lengthX - width, lengthY - height, lengthX, lengthY);
        }
    }
}
