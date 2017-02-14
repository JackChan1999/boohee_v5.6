package com.ToxicBakery.viewpager.transforms;

import android.view.View;

public class ScaleInOutTransformer extends ABaseTransformer {
    protected void onTransform(View view, float position) {
        view.setPivotX(position < 0.0f ? 0.0f : (float) view.getWidth());
        view.setPivotY(((float) view.getHeight()) / 2.0f);
        float scale = position < 0.0f ? 1.0f + position : 1.0f - position;
        view.setScaleX(scale);
        view.setScaleY(scale);
    }
}
