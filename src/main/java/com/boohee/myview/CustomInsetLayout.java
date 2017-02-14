package com.boohee.myview;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class CustomInsetLayout extends LinearLayout {
    private int[] mInsets;

    public CustomInsetLayout(Context context) {
        super(context);
        this.mInsets = new int[4];
    }

    public CustomInsetLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mInsets = new int[4];
    }

    public CustomInsetLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mInsets = new int[4];
    }

    public final int[] getInsets() {
        return this.mInsets;
    }

    protected final boolean fitSystemWindows(Rect insets) {
        if (VERSION.SDK_INT >= 19) {
            this.mInsets[0] = insets.left;
            this.mInsets[1] = insets.top;
            this.mInsets[2] = insets.right;
            insets.left = 0;
            insets.top = 0;
            insets.right = 0;
        }
        return super.fitSystemWindows(insets);
    }
}
