package com.ToxicBakery.viewpager.transforms;

import android.view.View;

import lecho.lib.hellocharts.gesture.ChartZoomer;

public class BackgroundToForegroundTransformer extends ABaseTransformer {
    protected void onTransform(View view, float position) {
        float f = 1.0f;
        float height = (float) view.getHeight();
        float width = (float) view.getWidth();
        if (position >= 0.0f) {
            f = Math.abs(1.0f - position);
        }
        float scale = ABaseTransformer.min(f, 0.5f);
        view.setScaleX(scale);
        view.setScaleY(scale);
        view.setPivotX(width * 0.5f);
        view.setPivotY(height * 0.5f);
        view.setTranslationX(position < 0.0f ? width * position : ((-width) * position) *
                ChartZoomer.ZOOM_AMOUNT);
    }
}
