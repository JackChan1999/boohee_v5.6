package com.ToxicBakery.viewpager.transforms;

import android.view.View;

public class RotateDownTransformer extends ABaseTransformer {
    private static final float ROT_MOD = -15.0f;

    protected void onTransform(View view, float position) {
        float height = (float) view.getHeight();
        float rotation = (ROT_MOD * position) * -1.25f;
        view.setPivotX(0.5f * ((float) view.getWidth()));
        view.setPivotY(height);
        view.setRotation(rotation);
    }

    protected boolean isPagingEnabled() {
        return true;
    }
}
