package cn.dreamtobe.kpswitch;

public interface IPanelConflictLayout {
    void handleHide();

    void handleShow();

    boolean isKeyboardShowing();

    boolean isVisible();

    void setIgnoreRecommendHeight(boolean z);
}
