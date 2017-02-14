package com.boohee.myview.highlight;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.view.View;

public class ViewUtils {
    public static Rect getLocationInView(View parent, View child) {
        if (child == null || parent == null) {
            throw new IllegalArgumentException("parent and child can not be null .");
        }
        View decorView = null;
        Context context = child.getContext();
        if (context instanceof Activity) {
            decorView = ((Activity) context).getWindow().getDecorView();
        }
        Rect result = new Rect();
        Rect tmpRect = new Rect();
        View tmp = child;
        if (child == parent) {
            child.getHitRect(result);
        } else {
            while (tmp != decorView && tmp != parent) {
                tmp.getHitRect(tmpRect);
                result.left += tmpRect.left;
                result.top += tmpRect.top;
                tmp = (View) tmp.getParent();
            }
            result.right = result.left + child.getMeasuredWidth();
            result.bottom = result.top + child.getMeasuredHeight();
        }
        return result;
    }
}
