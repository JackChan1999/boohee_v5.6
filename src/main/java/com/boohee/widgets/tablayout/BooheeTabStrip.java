package com.boohee.widgets.tablayout;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class BooheeTabStrip extends LinearLayout {
    public BooheeTabStrip(Context context) {
        this(context, null);
    }

    public BooheeTabStrip(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BooheeTabStrip(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
