package lecho.lib.hellocharts.animation;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.SuppressLint;
import lecho.lib.hellocharts.view.PieChartView;

@SuppressLint({"NewApi"})
public class PieChartRotationAnimatorV14 implements PieChartRotationAnimator, AnimatorListener, AnimatorUpdateListener {
    private ChartAnimationListener animationListener;
    private ValueAnimator animator;
    private final PieChartView chart;
    private float startRotation;
    private float targetRotation;

    public PieChartRotationAnimatorV14(PieChartView chart) {
        this(chart, 200);
    }

    public PieChartRotationAnimatorV14(PieChartView chart, long duration) {
        this.startRotation = 0.0f;
        this.targetRotation = 0.0f;
        this.animationListener = new DummyChartAnimationListener();
        this.chart = chart;
        this.animator = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
        this.animator.setDuration(duration);
        this.animator.addListener(this);
        this.animator.addUpdateListener(this);
    }

    public void startAnimation(float startRotation, float targetRotation) {
        this.startRotation = ((startRotation % 360.0f) + 360.0f) % 360.0f;
        this.targetRotation = ((targetRotation % 360.0f) + 360.0f) % 360.0f;
        this.animator.start();
    }

    public void cancelAnimation() {
        this.animator.cancel();
    }

    public void onAnimationUpdate(ValueAnimator animation) {
        this.chart.setChartRotation((int) ((((this.startRotation + ((this.targetRotation - this.startRotation) * animation.getAnimatedFraction())) % 360.0f) + 360.0f) % 360.0f), false);
    }

    public void onAnimationCancel(Animator animation) {
    }

    public void onAnimationEnd(Animator animation) {
        this.chart.setChartRotation((int) this.targetRotation, false);
        this.animationListener.onAnimationFinished();
    }

    public void onAnimationRepeat(Animator animation) {
    }

    public void onAnimationStart(Animator animation) {
        this.animationListener.onAnimationStarted();
    }

    public boolean isAnimationStarted() {
        return this.animator.isStarted();
    }

    public void setChartAnimationListener(ChartAnimationListener animationListener) {
        if (animationListener == null) {
            this.animationListener = new DummyChartAnimationListener();
        } else {
            this.animationListener = animationListener;
        }
    }
}
