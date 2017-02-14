package android.support.v7.widget;

import android.graphics.drawable.Drawable;

interface CardViewDelegate {
    Drawable getBackground();

    boolean getPreventCornerOverlap();

    float getRadius();

    boolean getUseCompatPadding();

    void setBackgroundDrawable(Drawable drawable);

    void setMinWidthHeightInternal(int i, int i2);

    void setShadowPadding(int i, int i2, int i3, int i4);
}
