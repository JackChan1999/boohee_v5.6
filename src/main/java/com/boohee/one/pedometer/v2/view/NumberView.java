package com.boohee.one.pedometer.v2.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class NumberView extends TextView {
    public NumberView(Context context) {
        this(context, null);
    }

    public NumberView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NumberView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setCustomTypeface();
    }

    private void setCustomTypeface() {
        setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/futura.ttf"));
    }
}
