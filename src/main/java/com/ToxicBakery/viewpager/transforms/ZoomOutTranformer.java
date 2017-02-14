package com.ToxicBakery.viewpager.transforms;

import android.view.View;

public class ZoomOutTranformer extends ABaseTransformer {
    protected void onTransform(View view, float position) {
        float scale = 1.0f + Math.abs(position);
        view.setScaleX(scale);
        view.setScaleY(scale);
        view.setPivotX(((float) view.getWidth()) * 0.5f);
        view.setPivotY(((float) view.getHeight()) * 0.5f);
        float f = (position < -1.0f || position > 1.0f) ? 0.0f : 1.0f - (scale - 1.0f);
        view.setAlpha(f);
        if (position == -1.0f) {
            view.setTranslationX((float) (view.getWidth() * -1));
        }
    }
}
