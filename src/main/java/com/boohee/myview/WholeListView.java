package com.boohee.myview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View.MeasureSpec;
import android.widget.ListView;

public class WholeListView extends ListView {
    public WholeListView(Context context) {
        super(context);
    }

    public WholeListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WholeListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(536870911, Integer
                .MIN_VALUE));
    }
}
