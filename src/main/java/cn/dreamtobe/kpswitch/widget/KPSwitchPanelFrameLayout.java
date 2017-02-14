package cn.dreamtobe.kpswitch.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import cn.dreamtobe.kpswitch.IPanelConflictLayout;
import cn.dreamtobe.kpswitch.IPanelHeightTarget;
import cn.dreamtobe.kpswitch.handler.KPSwitchPanelLayoutHandler;

public class KPSwitchPanelFrameLayout extends FrameLayout implements IPanelHeightTarget, IPanelConflictLayout {
    private KPSwitchPanelLayoutHandler panelLayoutHandler;

    public KPSwitchPanelFrameLayout(Context context) {
        super(context);
        init(null);
    }

    public KPSwitchPanelFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public KPSwitchPanelFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @TargetApi(21)
    public KPSwitchPanelFrameLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        this.panelLayoutHandler = new KPSwitchPanelLayoutHandler(this, attrs);
    }

    public void setVisibility(int visibility) {
        if (!this.panelLayoutHandler.filterSetVisibility(visibility)) {
            super.setVisibility(visibility);
        }
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int[] processedMeasureWHSpec = this.panelLayoutHandler.processOnMeasure(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(processedMeasureWHSpec[0], processedMeasureWHSpec[1]);
    }

    public boolean isKeyboardShowing() {
        return this.panelLayoutHandler.isKeyboardShowing();
    }

    public boolean isVisible() {
        return this.panelLayoutHandler.isVisible();
    }

    public void handleShow() {
        super.setVisibility(0);
    }

    public void handleHide() {
        this.panelLayoutHandler.handleHide();
    }

    public void setIgnoreRecommendHeight(boolean isIgnoreRecommendHeight) {
        this.panelLayoutHandler.setIgnoreRecommendHeight(isIgnoreRecommendHeight);
    }

    public void refreshHeight(int panelHeight) {
        this.panelLayoutHandler.resetToRecommendPanelHeight(panelHeight);
    }

    public void onKeyboardShowing(boolean showing) {
        this.panelLayoutHandler.setIsKeyboardShowing(showing);
    }
}
