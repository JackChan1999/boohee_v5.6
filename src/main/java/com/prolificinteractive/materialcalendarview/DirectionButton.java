package com.prolificinteractive.materialcalendarview;

import android.content.Context;
import android.graphics.PorterDuff.Mode;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ImageView;

class DirectionButton extends ImageView {
    public DirectionButton(Context context) {
        this(context, null);
    }

    public DirectionButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setBackgroundResource(getThemeSelectableBackgroundId(context));
    }

    public void setColor(int color) {
        setColorFilter(color, Mode.SRC_ATOP);
    }

    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        setAlpha(enabled ? 1.0f : 0.1f);
    }

    private static int getThemeSelectableBackgroundId(Context context) {
        int colorAttr = context.getResources().getIdentifier
                ("selectableItemBackgroundBorderless", "attr", context.getPackageName());
        if (colorAttr == 0) {
            if (VERSION.SDK_INT >= 21) {
                colorAttr = 16843868;
            } else {
                colorAttr = 16843534;
            }
        }
        TypedValue outValue = new TypedValue();
        context.getTheme().resolveAttribute(colorAttr, outValue, true);
        return outValue.resourceId;
    }
}
