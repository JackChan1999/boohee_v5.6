package com.boohee.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.balysv.materialripple.MaterialRippleLayout;
import com.boohee.utils.ViewUtils;

public class BooheeRippleLayout extends MaterialRippleLayout {
    private boolean isFastClick = false;

    public BooheeRippleLayout(Context context) {
        super(context);
    }

    public BooheeRippleLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BooheeRippleLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case 0:
                this.isFastClick = false;
                if (ViewUtils.isFastDoubleClick()) {
                    this.isFastClick = true;
                    return true;
                }
                break;
            case 1:
                break;
        }
        if (this.isFastClick) {
            return true;
        }
        return super.dispatchTouchEvent(ev);
    }
}
