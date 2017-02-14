package com.boohee.myview.homeMenu;

import android.graphics.drawable.Drawable;

public class PopMenuItem {
    private Drawable drawable;
    private int      index;
    private String   title;

    public int getIndex() {
        return this.index;
    }

    public PopMenuItem(int index, String title, Drawable drawable) {
        this.index = index;
        this.title = title;
        this.drawable = drawable;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Drawable getDrawable() {
        return this.drawable;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }
}
