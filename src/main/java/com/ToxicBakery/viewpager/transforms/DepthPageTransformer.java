package com.ToxicBakery.viewpager.transforms;

import android.view.View;

import lecho.lib.hellocharts.gesture.ChartZoomer;

public class DepthPageTransformer extends ABaseTransformer {
    private static final float MIN_SCALE = 0.75f;

    protected void onTransform(View view, float position) {
        if (position <= 0.0f) {
            view.setTranslationX(0.0f);
            view.setScaleX(1.0f);
            view.setScaleY(1.0f);
        } else if (position <= 1.0f) {
            float scaleFactor = 0.75f + (ChartZoomer.ZOOM_AMOUNT * (1.0f - Math.abs(position)));
            view.setAlpha(1.0f - position);
            view.setPivotY(0.5f * ((float) view.getHeight()));
            view.setTranslationX(((float) view.getWidth()) * (-position));
            view.setScaleX(scaleFactor);
            view.setScaleY(scaleFactor);
        }
    }

    protected boolean isPagingEnabled() {
        return true;
    }
}
