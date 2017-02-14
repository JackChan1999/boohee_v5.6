package cn.dreamtobe.kpswitch.handler;

import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import cn.dreamtobe.kpswitch.IPanelConflictLayout;
import cn.dreamtobe.kpswitch.R;
import cn.dreamtobe.kpswitch.util.ViewUtil;

public class KPSwitchPanelLayoutHandler implements IPanelConflictLayout {
    private boolean mIgnoreRecommendHeight = false;
    private boolean mIsHide = false;
    private boolean mIsKeyboardShowing = false;
    private final View panelLayout;
    private final int[] processedMeasureWHSpec = new int[2];

    public KPSwitchPanelLayoutHandler(View panelLayout, AttributeSet attrs) {
        this.panelLayout = panelLayout;
        if (attrs != null) {
            TypedArray typedArray = null;
            try {
                typedArray = panelLayout.getContext().obtainStyledAttributes(attrs, R.styleable.KPSwitchPanelLayout);
                this.mIgnoreRecommendHeight = typedArray.getBoolean(R.styleable.KPSwitchPanelLayout_ignore_recommend_height, false);
            } finally {
                if (typedArray != null) {
                    typedArray.recycle();
                }
            }
        }
    }

    public boolean filterSetVisibility(int visibility) {
        if (visibility == 0) {
            this.mIsHide = false;
        }
        if (visibility == this.panelLayout.getVisibility()) {
            return true;
        }
        if (isKeyboardShowing() && visibility == 0) {
            return true;
        }
        return false;
    }

    public int[] processOnMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (this.mIsHide) {
            this.panelLayout.setVisibility(8);
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(0, 1073741824);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(0, 1073741824);
        }
        this.processedMeasureWHSpec[0] = widthMeasureSpec;
        this.processedMeasureWHSpec[1] = heightMeasureSpec;
        return this.processedMeasureWHSpec;
    }

    public void setIsKeyboardShowing(boolean isKeyboardShowing) {
        this.mIsKeyboardShowing = isKeyboardShowing;
    }

    public boolean isKeyboardShowing() {
        return this.mIsKeyboardShowing;
    }

    public boolean isVisible() {
        return !this.mIsHide;
    }

    public void handleShow() {
        throw new IllegalAccessError("You can't invoke handle show in handler, please instead of handling in the panel layout, maybe just need invoke super.setVisibility(View.VISIBLE)");
    }

    public void handleHide() {
        this.mIsHide = true;
    }

    public void resetToRecommendPanelHeight(int recommendPanelHeight) {
        if (!this.mIgnoreRecommendHeight) {
            ViewUtil.refreshHeight(this.panelLayout, recommendPanelHeight);
        }
    }

    public void setIgnoreRecommendHeight(boolean ignoreRecommendHeight) {
        this.mIgnoreRecommendHeight = ignoreRecommendHeight;
    }
}
