package com.ToxicBakery.viewpager.transforms;

import android.view.View;

public class CubeInTransformer extends ABaseTransformer {
    protected void onTransform(View view, float position) {
        view.setPivotX(position > 0.0f ? 0.0f : (float) view.getWidth());
        view.setPivotY(0.0f);
        view.setRotationY(-90.0f * position);
    }

    public boolean isPagingEnabled() {
        return true;
    }
}
