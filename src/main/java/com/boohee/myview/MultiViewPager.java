package com.boohee.myview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;

import com.boohee.one.R;

public class MultiViewPager extends ViewPager {
    private       int     mMatchWidthChildResId;
    private       int     mMaxHeight;
    private       int     mMaxWidth;
    private       boolean mNeedsMeasurePage;
    private final Point   maxSize;
    private final Point   size;

    private static void constrainTo(Point size, Point maxSize) {
        if (maxSize.x >= 0 && size.x > maxSize.x) {
            size.x = maxSize.x;
        }
        if (maxSize.y >= 0 && size.y > maxSize.y) {
            size.y = maxSize.y;
        }
    }

    public MultiViewPager(Context context) {
        super(context);
        this.mMaxWidth = -1;
        this.mMaxHeight = -1;
        this.size = new Point();
        this.maxSize = new Point();
    }

    public MultiViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mMaxWidth = -1;
        this.mMaxHeight = -1;
        init(context, attrs);
        this.size = new Point();
        this.maxSize = new Point();
    }

    private void init(Context context, AttributeSet attrs) {
        setClipChildren(false);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MultiViewPager);
        setMaxWidth(ta.getDimensionPixelSize(0, -1));
        setMaxHeight(ta.getDimensionPixelSize(1, -1));
        setMatchChildWidth(ta.getResourceId(2, 0));
        ta.recycle();
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        this.size.set(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize
                (heightMeasureSpec));
        if (this.mMaxWidth >= 0 || this.mMaxHeight >= 0) {
            this.maxSize.set(this.mMaxWidth, this.mMaxHeight);
            constrainTo(this.size, this.maxSize);
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(this.size.x, 1073741824);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(this.size.y, 1073741824);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        onMeasurePage(widthMeasureSpec, heightMeasureSpec);
    }

    protected void onMeasurePage(int widthMeasureSpec, int heightMeasureSpec) {
        if (!this.mNeedsMeasurePage) {
            return;
        }
        if (this.mMatchWidthChildResId == 0) {
            this.mNeedsMeasurePage = false;
        } else if (getChildCount() > 0) {
            View child = getChildAt(0);
            child.measure(widthMeasureSpec, heightMeasureSpec);
            int pageWidth = child.getMeasuredWidth();
            View match = child.findViewById(this.mMatchWidthChildResId);
            if (match == null) {
                throw new NullPointerException("MatchWithChildResId did not find that ID in the " +
                        "first fragment of the ViewPager; is that view defined in the child " +
                        "view's layout? Note that MultiViewPager only measures the child for " +
                        "index 0.");
            }
            int childWidth = match.getMeasuredWidth();
            if (childWidth > 0) {
                this.mNeedsMeasurePage = false;
                setPageMargin(-(pageWidth - childWidth));
                setOffscreenPageLimit(((int) Math.ceil((double) (((float) pageWidth) / ((float)
                        childWidth)))) + 1);
                requestLayout();
            }
        }
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.mNeedsMeasurePage = true;
    }

    public void setMatchChildWidth(int matchChildWidthResId) {
        if (this.mMatchWidthChildResId != matchChildWidthResId) {
            this.mMatchWidthChildResId = matchChildWidthResId;
            this.mNeedsMeasurePage = true;
        }
    }

    public void setMaxWidth(int width) {
        this.mMaxWidth = width;
    }

    public void setMaxHeight(int height) {
        this.mMaxHeight = height;
    }
}
