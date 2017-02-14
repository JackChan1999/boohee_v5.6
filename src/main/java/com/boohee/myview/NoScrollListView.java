package com.boohee.myview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View.MeasureSpec;

import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class NoScrollListView extends PullToRefreshListView {
    public NoScrollListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(536870911, Integer
                .MIN_VALUE));
    }
}
