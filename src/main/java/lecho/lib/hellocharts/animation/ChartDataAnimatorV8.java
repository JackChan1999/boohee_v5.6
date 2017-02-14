package lecho.lib.hellocharts.animation;

import android.os.Handler;
import android.os.SystemClock;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import lecho.lib.hellocharts.view.Chart;

public class ChartDataAnimatorV8 implements ChartDataAnimator {
    private ChartAnimationListener animationListener = new DummyChartAnimationListener();
    final Chart chart;
    long duration;
    final Handler handler;
    final Interpolator interpolator = new AccelerateDecelerateInterpolator();
    boolean isAnimationStarted = false;
    private final Runnable runnable = new Runnable() {
        public void run() {
            long elapsed = SystemClock.uptimeMillis() - ChartDataAnimatorV8.this.start;
            if (elapsed > ChartDataAnimatorV8.this.duration) {
                ChartDataAnimatorV8.this.isAnimationStarted = false;
                ChartDataAnimatorV8.this.handler.removeCallbacks(ChartDataAnimatorV8.this.runnable);
                ChartDataAnimatorV8.this.chart.animationDataFinished();
                return;
            }
            ChartDataAnimatorV8.this.chart.animationDataUpdate(Math.min(ChartDataAnimatorV8.this.interpolator.getInterpolation(((float) elapsed) / ((float) ChartDataAnimatorV8.this.duration)), 1.0f));
            ChartDataAnimatorV8.this.handler.postDelayed(this, 16);
        }
    };
    long start;

    public ChartDataAnimatorV8(Chart chart) {
        this.chart = chart;
        this.handler = new Handler();
    }

    public void startAnimation(long duration) {
        if (duration >= 0) {
            this.duration = duration;
        } else {
            this.duration = 500;
        }
        this.isAnimationStarted = true;
        this.animationListener.onAnimationStarted();
        this.start = SystemClock.uptimeMillis();
        this.handler.post(this.runnable);
    }

    public void cancelAnimation() {
        this.isAnimationStarted = false;
        this.handler.removeCallbacks(this.runnable);
        this.chart.animationDataFinished();
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
