package com.ToxicBakery.viewpager.transforms;

import android.view.View;

public class StackTransformer extends ABaseTransformer {
    protected void onTransform(View view, float position) {
        float f = 0.0f;
        if (position >= 0.0f) {
            f = ((float) (-view.getWidth())) * position;
        }
        view.setTranslationX(f);
    }
}
