package com.boohee.widgets;

import android.content.Context;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.widget.Button;

public class AutoBgButton extends Button {

    protected class SAutoBgButtonBackgroundDrawable extends LayerDrawable {
        protected int         _disabledAlpha = 100;
        protected ColorFilter _pressedFilter = new LightingColorFilter(-3355444, 1);

        public SAutoBgButtonBackgroundDrawable(Drawable d) {
            super(new Drawable[]{d});
        }

        protected boolean onStateChange(int[] states) {
            boolean enabled = false;
            boolean pressed = false;
            for (int state : states) {
                if (state == 16842910) {
                    enabled = true;
                } else if (state == 16842919) {
                    pressed = true;
                }
            }
            mutate();
            if (enabled && pressed) {
                setColorFilter(this._pressedFilter);
            } else if (enabled) {
                setColorFilter(null);
            } else {
                setColorFilter(null);
                setAlpha(this._disabledAlpha);
            }
            invalidateSelf();
            return super.onStateChange(states);
        }

        public boolean isStateful() {
            return true;
        }
    }

    public AutoBgButton(Context context) {
        super(context);
    }

    public AutoBgButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoBgButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setBackgroundDrawable(Drawable d) {
        super.setBackgroundDrawable(new SAutoBgButtonBackgroundDrawable(d));
    }
}
