package com.ToxicBakery.viewpager.transforms;

import android.support.v4.view.ViewPager.PageTransformer;
import android.view.View;

public abstract class ABaseTransformer implements PageTransformer {
    protected abstract void onTransform(View view, float f);

    public void transformPage(View page, float position) {
        onPreTransform(page, position);
        onTransform(page, position);
        onPostTransform(page, position);
    }

    protected boolean hideOffscreenPages() {
        return true;
    }

    protected boolean isPagingEnabled() {
        return false;
    }

    protected void onPreTransform(View page, float position) {
        float f = 0.0f;
        float width = (float) page.getWidth();
        page.setRotationX(0.0f);
        page.setRotationY(0.0f);
        page.setRotation(0.0f);
        page.setScaleX(1.0f);
        page.setScaleY(1.0f);
        page.setPivotX(0.0f);
        page.setPivotY(0.0f);
        page.setTranslationY(0.0f);
        page.setTranslationX(isPagingEnabled() ? 0.0f : (-width) * position);
        if (hideOffscreenPages()) {
            if (position > -1.0f && position < 1.0f) {
                f = 1.0f;
            }
            page.setAlpha(f);
            return;
        }
        page.setAlpha(1.0f);
    }

    protected void onPostTransform(View page, float position) {
    }

    protected static final float min(float val, float min) {
        return val < min ? min : val;
    }
}
