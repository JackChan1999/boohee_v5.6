package com.ToxicBakery.viewpager.transforms;

import android.view.View;

public class FlipVerticalTransformer extends ABaseTransformer {
    protected void onTransform(View view, float position) {
        float rotation = -180.0f * position;
        float f = (rotation > 90.0f || rotation < -90.0f) ? 0.0f : 1.0f;
        view.setAlpha(f);
        view.setPivotX(((float) view.getWidth()) * 0.5f);
        view.setPivotY(((float) view.getHeight()) * 0.5f);
        view.setRotationX(rotation);
    }
}
