package lecho.lib.hellocharts.animation;

import android.os.Handler;
import android.os.SystemClock;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.Chart;

public class ChartViewportAnimatorV8 implements ChartViewportAnimator {
    private ChartAnimationListener animationListener = new DummyChartAnimationListener();
    final Chart chart;
    private long duration;
    final Handler handler;
    final Interpolator interpolator = new AccelerateDecelerateInterpolator();
    boolean isAnimationStarted = false;
    private Viewport newViewport = new Viewport();
    private final Runnable runnable = new Runnable() {
        public void run() {
            long elapsed = SystemClock.uptimeMillis() - ChartViewportAnimatorV8.this.start;
            if (elapsed > ChartViewportAnimatorV8.this.duration) {
                ChartViewportAnimatorV8.this.isAnimationStarted = false;
                ChartViewportAnimatorV8.this.handler.removeCallbacks(ChartViewportAnimatorV8.this.runnable);
                ChartViewportAnimatorV8.this.chart.setCurrentViewport(ChartViewportAnimatorV8.this.targetViewport);
                ChartViewportAnimatorV8.this.animationListener.onAnimationFinished();
                return;
            }
            float scale = Math.min(ChartViewportAnimatorV8.this.interpolator.getInterpolation(((float) elapsed) / ((float) ChartViewportAnimatorV8.this.duration)), 1.0f);
            ChartViewportAnimatorV8.this.newViewport.set(ChartViewportAnimatorV8.this.startViewport.left + ((ChartViewportAnimatorV8.this.targetViewport.left - ChartViewportAnimatorV8.this.startViewport.left) * scale), ChartViewportAnimatorV8.this.startViewport.top + ((ChartViewportAnimatorV8.this.targetViewport.top - ChartViewportAnimatorV8.this.startViewport.top) * scale), ChartViewportAnimatorV8.this.startViewport.right + ((ChartViewportAnimatorV8.this.targetViewport.right - ChartViewportAnimatorV8.this.startViewport.right) * scale), ChartViewportAnimatorV8.this.startViewport.bottom + ((ChartViewportAnimatorV8.this.targetViewport.bottom - ChartViewportAnimatorV8.this.startViewport.bottom) * scale));
            ChartViewportAnimatorV8.this.chart.setCurrentViewport(ChartViewportAnimatorV8.this.newViewport);
            ChartViewportAnimatorV8.this.handler.postDelayed(this, 16);
        }
    };
    long start;
    private Viewport startViewport = new Viewport();
    private Viewport targetViewport = new Viewport();

    public ChartViewportAnimatorV8(Chart chart) {
        this.chart = chart;
        this.duration = 300;
        this.handler = new Handler();
    }

    public void startAnimation(Viewport startViewport, Viewport targetViewport) {
        this.startViewport.set(startViewport);
        this.targetViewport.set(targetViewport);
        this.duration = 300;
        this.isAnimationStarted = true;
        this.animationListener.onAnimationStarted();
        this.start = SystemClock.uptimeMillis();
        this.handler.post(this.runnable);
    }

    public void startAnimation(Viewport startViewport, Viewport targetViewport, long duration) {
        this.startViewport.set(startViewport);
        this.targetViewport.set(targetViewport);
        this.duration = duration;
        this.isAnimationStarted = true;
        this.animationListener.onAnimationStarted();
        this.start = SystemClock.uptimeMillis();
        this.handler.post(this.runnable);
    }

    public void cancelAnimation() {
        this.isAnimationStarted = false;
        this.handler.removeCallbacks(this.runnable);
        this.chart.setCurrentViewport(this.targetViewport);
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
