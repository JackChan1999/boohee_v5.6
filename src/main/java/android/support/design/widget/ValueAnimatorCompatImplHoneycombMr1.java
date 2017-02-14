package android.support.design.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.view.animation.Interpolator;

class ValueAnimatorCompatImplHoneycombMr1 extends Impl {
    final ValueAnimator mValueAnimator = new ValueAnimator();

    ValueAnimatorCompatImplHoneycombMr1() {
    }

    public void start() {
        this.mValueAnimator.start();
    }

    public boolean isRunning() {
        return this.mValueAnimator.isRunning();
    }

    public void setInterpolator(Interpolator interpolator) {
        this.mValueAnimator.setInterpolator(interpolator);
    }

    public void setUpdateListener(final AnimatorUpdateListenerProxy updateListener) {
        this.mValueAnimator.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                updateListener.onAnimationUpdate();
            }
        });
    }

    public void setListener(final AnimatorListenerProxy listener) {
        this.mValueAnimator.addListener(new AnimatorListenerAdapter() {
            public void onAnimationStart(Animator animator) {
                listener.onAnimationStart();
            }

            public void onAnimationEnd(Animator animator) {
                listener.onAnimationEnd();
            }

            public void onAnimationCancel(Animator animator) {
                listener.onAnimationCancel();
            }
        });
    }

    public void setIntValues(int from, int to) {
        this.mValueAnimator.setIntValues(new int[]{from, to});
    }

    public int getAnimatedIntValue() {
        return ((Integer) this.mValueAnimator.getAnimatedValue()).intValue();
    }

    public void setFloatValues(float from, float to) {
        this.mValueAnimator.setFloatValues(new float[]{from, to});
    }

    public float getAnimatedFloatValue() {
        return ((Float) this.mValueAnimator.getAnimatedValue()).floatValue();
    }

    public void setDuration(int duration) {
        this.mValueAnimator.setDuration((long) duration);
    }

    public void cancel() {
        this.mValueAnimator.cancel();
    }

    public float getAnimatedFraction() {
        return this.mValueAnimator.getAnimatedFraction();
    }

    public void end() {
        this.mValueAnimator.end();
    }

    public long getDuration() {
        return this.mValueAnimator.getDuration();
    }
}
