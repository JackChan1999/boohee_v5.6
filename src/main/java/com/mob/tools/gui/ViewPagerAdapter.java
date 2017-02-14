package com.mob.tools.gui;

import android.view.View;
import android.view.ViewGroup;

public abstract class ViewPagerAdapter {
    public abstract int getCount();

    public abstract View getView(int i, ViewGroup viewGroup);

    public void onScreenChange(int i, int i2) {
    }
}
