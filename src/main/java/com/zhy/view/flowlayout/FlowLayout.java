package com.zhy.view.flowlayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;

import java.util.ArrayList;
import java.util.List;

public class FlowLayout extends ViewGroup {
    private static final String TAG = "FlowLayout";
    protected List<List<View>> mAllViews;
    protected List<Integer>    mLineHeight;

    public FlowLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mAllViews = new ArrayList();
        this.mLineHeight = new ArrayList();
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowLayout(Context context) {
        this(context, null);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);
        int width = 0;
        int height = 0;
        int lineWidth = 0;
        int lineHeight = 0;
        int cCount = getChildCount();
        for (int i = 0; i < cCount; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != 8) {
                measureChild(child, widthMeasureSpec, heightMeasureSpec);
                MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
                int childWidth = (child.getMeasuredWidth() + lp.leftMargin) + lp.rightMargin;
                int childHeight = (child.getMeasuredHeight() + lp.topMargin) + lp.bottomMargin;
                if (lineWidth + childWidth > (sizeWidth - getPaddingLeft()) - getPaddingRight()) {
                    width = Math.max(width, lineWidth);
                    lineWidth = childWidth;
                    height += lineHeight;
                    lineHeight = childHeight;
                } else {
                    lineWidth += childWidth;
                    lineHeight = Math.max(lineHeight, childHeight);
                }
                if (i == cCount - 1) {
                    width = Math.max(lineWidth, width);
                    height += lineHeight;
                }
            } else if (i == cCount - 1) {
                width = Math.max(lineWidth, width);
                height += lineHeight;
            }
        }
        if (modeWidth != 1073741824) {
            sizeWidth = (getPaddingLeft() + width) + getPaddingRight();
        }
        if (modeHeight != 1073741824) {
            sizeHeight = (getPaddingTop() + height) + getPaddingBottom();
        }
        setMeasuredDimension(sizeWidth, sizeHeight);
    }

    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int i;
        this.mAllViews.clear();
        this.mLineHeight.clear();
        int width = getWidth();
        int lineWidth = 0;
        int lineHeight = 0;
        List<View> lineViews = new ArrayList();
        int cCount = getChildCount();
        for (i = 0; i < cCount; i++) {
            MarginLayoutParams lp;
            View child = getChildAt(i);
            if (child.getVisibility() != 8) {
                lp = (MarginLayoutParams) child.getLayoutParams();
                int childWidth = child.getMeasuredWidth();
                int childHeight = child.getMeasuredHeight();
                if (((childWidth + lineWidth) + lp.leftMargin) + lp.rightMargin > (width -
                        getPaddingLeft()) - getPaddingRight()) {
                    this.mLineHeight.add(Integer.valueOf(lineHeight));
                    this.mAllViews.add(lineViews);
                    lineWidth = 0;
                    lineHeight = (lp.topMargin + childHeight) + lp.bottomMargin;
                    lineViews = new ArrayList();
                }
                lineWidth += (lp.leftMargin + childWidth) + lp.rightMargin;
                lineHeight = Math.max(lineHeight, (lp.topMargin + childHeight) + lp.bottomMargin);
                lineViews.add(child);
            }
        }
        this.mLineHeight.add(Integer.valueOf(lineHeight));
        this.mAllViews.add(lineViews);
        int left = getPaddingLeft();
        int top = getPaddingTop();
        int lineNum = this.mAllViews.size();
        for (i = 0; i < lineNum; i++) {
            lineViews = (List) this.mAllViews.get(i);
            lineHeight = ((Integer) this.mLineHeight.get(i)).intValue();
            for (int j = 0; j < lineViews.size(); j++) {
                child = (View) lineViews.get(j);
                if (child.getVisibility() != 8) {
                    lp = (MarginLayoutParams) child.getLayoutParams();
                    int lc = left + lp.leftMargin;
                    int tc = top + lp.topMargin;
                    child.layout(lc, tc, lc + child.getMeasuredWidth(), tc + child
                            .getMeasuredHeight());
                    left += (child.getMeasuredWidth() + lp.leftMargin) + lp.rightMargin;
                }
            }
            left = getPaddingLeft();
            top += lineHeight;
        }
    }

    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(-2, -2);
    }

    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }
}
