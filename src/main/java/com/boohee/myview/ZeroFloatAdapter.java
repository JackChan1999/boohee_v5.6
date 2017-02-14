package com.boohee.myview;

import android.content.Context;

import kankan.wheel.widget.adapters.AbstractWheelTextAdapter;

public class ZeroFloatAdapter extends AbstractWheelTextAdapter {
    private int maxValue = 9;
    private int minValue = 0;

    public ZeroFloatAdapter(Context context) {
        super(context);
    }

    public int getItemsCount() {
        return (this.maxValue - this.minValue) + 1;
    }

    protected CharSequence getItemText(int index) {
        if (index < 0 || index >= getItemsCount()) {
            return null;
        }
        return "." + index;
    }
}
