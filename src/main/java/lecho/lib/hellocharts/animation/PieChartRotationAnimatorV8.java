package lecho.lib.hellocharts.animation;

import android.os.Handler;
import android.os.SystemClock;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import lecho.lib.hellocharts.view.PieChartView;

public class PieChartRotationAnimatorV8 implements PieChartRotationAnimator {
    private ChartAnimationListener animationListener;
    final PieChartView chart;
    final long duration;
    final Handler handler;
    final Interpolator interpolator;
    boolean isAnimationStarted;
    private final Runnable runnable;
    long start;
    private float startRotation;
    private float targetRotation;

    public PieChartRotationAnimatorV8(PieChartView chart) {
        this(chart, 200);
    }

    public PieChartRotationAnimatorV8(PieChartView chart, long duration) {
        this.interpolator = new AccelerateDecelerateInterpolator();
        this.isAnimationStarted = false;
        this.startRotation = 0.0f;
        this.targetRotation = 0.0f;
        this.animationListener = new DummyChartAnimationListener();
        this.runnable = new Runnable() {
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - PieChartRotationAnimatorV8.this.start;
                if (elapsed > PieChartRotationAnimatorV8.this.duration) {
                    PieChartRotationAnimatorV8.this.isAnimationStarted = false;
                    PieChartRotationAnimatorV8.this.handler.removeCallbacks(PieChartRotationAnimatorV8.this.runnable);
                    PieChartRotationAnimatorV8.this.chart.setChartRotation((int) PieChartRotationAnimatorV8.this.targetRotation, false);
                    PieChartRotationAnimatorV8.this.animationListener.onAnimationFinished();
                    return;
                }
                PieChartRotationAnimatorV8.this.chart.setChartRotation((int) ((((PieChartRotationAnimatorV8.this.startRotation + ((PieChartRotationAnimatorV8.this.targetRotation - PieChartRotationAnimatorV8.this.startRotation) * Math.min(PieChartRotationAnimatorV8.this.interpolator.getInterpolation(((float) elapsed) / ((float) PieChartRotationAnimatorV8.this.duration)), 1.0f))) % 360.0f) + 360.0f) % 360.0f), false);
                PieChartRotationAnimatorV8.this.handler.postDelayed(this, 16);
            }
        };
        this.chart = chart;
        this.duration = duration;
        this.handler = new Handler();
    }

    public void startAnimation(float startRotation, float targetRotation) {
        this.startRotation = ((startRotation % 360.0f) + 360.0f) % 360.0f;
        this.targetRotation = ((targetRotation % 360.0f) + 360.0f) % 360.0f;
        this.isAnimationStarted = true;
        this.animationListener.onAnimationStarted();
        this.start = SystemClock.uptimeMillis();
        this.handler.post(this.runnable);
    }

    public void cancelAnimation() {
        this.isAnimationStarted = false;
        this.handler.removeCallbacks(this.runnable);
        this.chart.setChartRotation((int) this.targetRotation, false);
        this.animationListener.onAnimationFinished();
    }

    public boolean isAnimationStarted() {
        return this.isAnimationStarted;
    }

    public void setChartAnimationListener(ChartAnimationListener animationListener) {
        if (animationListener == null) {
            this.animationListener = new DummyChartAnimationListener();
        } else {
            this.animationListener = animationListener;
        }
    }
}
