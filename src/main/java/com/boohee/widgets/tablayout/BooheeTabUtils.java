package com.boohee.widgets.tablayout;

import android.support.v4.view.MarginLayoutParamsCompat;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;

final class BooheeTabUtils {
    static int getMeasuredWidth(View v) {
        return v == null ? 0 : v.getMeasuredWidth();
    }

    static int getWidth(View v) {
        return v == null ? 0 : v.getWidth();
    }

    static int getWidthWithMargin(View v) {
        return getWidth(v) + getMarginHorizontally(v);
    }

    static int getStart(View v) {
        return getStart(v, false);
    }

    static int getStart(View v, boolean withoutPadding) {
        if (v == null) {
            return 0;
        }
        return isLayoutRtl(v) ? withoutPadding ? v.getRight() - getPaddingStart(v) : v.getRight()
                : withoutPadding ? v.getLeft() + getPaddingStart(v) : v.getLeft();
    }

    static int getEnd(View v) {
        return getEnd(v, false);
    }

    static int getEnd(View v, boolean withoutPadding) {
        if (v == null) {
            return 0;
        }
        return isLayoutRtl(v) ? withoutPadding ? v.getLeft() + getPaddingEnd(v) : v.getLeft() :
                withoutPadding ? v.getRight() - getPaddingEnd(v) : v.getRight();
    }

    static int getPaddingStart(View v) {
        if (v == null) {
            return 0;
        }
        return ViewCompat.getPaddingStart(v);
    }

    static int getPaddingEnd(View v) {
        if (v == null) {
            return 0;
        }
        return ViewCompat.getPaddingEnd(v);
    }

    static int getPaddingHorizontally(View v) {
        if (v == null) {
            return 0;
        }
        return v.getPaddingLeft() + v.getPaddingRight();
    }

    static int getMarginStart(View v) {
        if (v == null) {
            return 0;
        }
        return MarginLayoutParamsCompat.getMarginStart((MarginLayoutParams) v.getLayoutParams());
    }

    static int getMarginEnd(View v) {
        if (v == null) {
            return 0;
        }
        return MarginLayoutParamsCompat.getMarginEnd((MarginLayoutParams) v.getLayoutParams());
    }

    static int getMarginHorizontally(View v) {
        if (v == null) {
            return 0;
        }
        MarginLayoutParams lp = (MarginLayoutParams) v.getLayoutParams();
        return MarginLayoutParamsCompat.getMarginStart(lp) + MarginLayoutParamsCompat
                .getMarginEnd(lp);
    }

    static boolean isLayoutRtl(View v) {
        return ViewCompat.getLayoutDirection(v) == 1;
    }

    private BooheeTabUtils() {
    }
}
