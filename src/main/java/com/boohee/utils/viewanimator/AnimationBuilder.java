package com.boohee.utils.viewanimator;

import android.graphics.Path;
import android.graphics.PathMeasure;
import android.support.annotation.IntRange;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.CycleInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.TextView;

import com.boohee.utils.viewanimator.AnimationListener.Start;
import com.boohee.utils.viewanimator.AnimationListener.Stop;
import com.boohee.utils.viewanimator.AnimationListener.Update;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ArgbEvaluator;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener;
import com.nineoldandroids.view.ViewHelper;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.gesture.ChartZoomer;
import lecho.lib.hellocharts.model.ColumnChartData;
import uk.co.senab.photoview.IPhotoView;

public class AnimationBuilder {
    private final List<Animator> animatorList      = new ArrayList();
    private       boolean        nextValueWillBeDp = false;
    private final ViewAnimator viewAnimator;
    private final View[]       views;
    private       boolean      waitForHeight;

    public AnimationBuilder(ViewAnimator viewAnimator, View... views) {
        this.viewAnimator = viewAnimator;
        this.views = views;
    }

    public AnimationBuilder dp() {
        this.nextValueWillBeDp = true;
        return this;
    }

    protected AnimationBuilder add(Animator animator) {
        this.animatorList.add(animator);
        return this;
    }

    protected float toDp(float px) {
        return px / this.views[0].getContext().getResources().getDisplayMetrics().density;
    }

    protected float toPx(float dp) {
        return this.views[0].getContext().getResources().getDisplayMetrics().density * dp;
    }

    protected float[] getValues(float... values) {
        if (!this.nextValueWillBeDp) {
            return values;
        }
        float[] pxValues = new float[values.length];
        for (int i = 0; i < values.length; i++) {
            pxValues[i] = toPx(values[i]);
        }
        return pxValues;
    }

    public AnimationBuilder property(String propertyName, float... values) {
        for (View view : this.views) {
            this.animatorList.add(ObjectAnimator.ofFloat(view, propertyName, getValues(values)));
        }
        return this;
    }

    public AnimationBuilder translationY(float... y) {
        return property("translationY", y);
    }

    public AnimationBuilder translationX(float... x) {
        return property("translationX", x);
    }

    public AnimationBuilder alpha(float... alpha) {
        return property("alpha", alpha);
    }

    public AnimationBuilder scaleX(float... scaleX) {
        return property("scaleX", scaleX);
    }

    public AnimationBuilder scaleY(float... scaleY) {
        return property("scaleY", scaleY);
    }

    public AnimationBuilder scale(float... scale) {
        scaleX(scale);
        scaleY(scale);
        return this;
    }

    public AnimationBuilder pivotX(float pivotX) {
        for (View view : this.views) {
            ViewHelper.setPivotX(view, pivotX);
        }
        return this;
    }

    public AnimationBuilder pivotY(float pivotY) {
        for (View view : this.views) {
            ViewHelper.setPivotY(view, pivotY);
        }
        return this;
    }

    public AnimationBuilder pivotX(float... pivotX) {
        ObjectAnimator.ofFloat(getView(), "pivotX", getValues(pivotX));
        return this;
    }

    public AnimationBuilder pivotY(float... pivotY) {
        ObjectAnimator.ofFloat(getView(), "pivotY", getValues(pivotY));
        return this;
    }

    public AnimationBuilder rotationX(float... rotationX) {
        return property("rotationX", rotationX);
    }

    public AnimationBuilder rotationY(float... rotationY) {
        return property("rotationY", rotationY);
    }

    public AnimationBuilder rotation(float... rotation) {
        return property("rotation", rotation);
    }

    public AnimationBuilder backgroundColor(int... colors) {
        for (View view : this.views) {
            ObjectAnimator objectAnimator = ObjectAnimator.ofInt(view, "backgroundColor", colors);
            objectAnimator.setEvaluator(new ArgbEvaluator());
            this.animatorList.add(objectAnimator);
        }
        return this;
    }

    public AnimationBuilder textColor(int... colors) {
        for (View view : this.views) {
            if (view instanceof TextView) {
                ObjectAnimator objectAnimator = ObjectAnimator.ofInt(view, "textColor", colors);
                objectAnimator.setEvaluator(new ArgbEvaluator());
                this.animatorList.add(objectAnimator);
            }
        }
        return this;
    }

    public AnimationBuilder custom(final Update update, float... values) {
        for (final View view : this.views) {
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(getValues(values));
            if (update != null) {
                valueAnimator.addUpdateListener(new AnimatorUpdateListener() {
                    public void onAnimationUpdate(ValueAnimator animation) {
                        update.update(view, ((Float) animation.getAnimatedValue()).floatValue());
                    }
                });
            }
            add(valueAnimator);
        }
        return this;
    }

    public AnimationBuilder height(float... height) {
        return custom(new Update() {
            public void update(View view, float value) {
                view.getLayoutParams().height = (int) value;
                view.requestLayout();
            }
        }, height);
    }

    public AnimationBuilder width(float... width) {
        return custom(new Update() {
            public void update(View view, float value) {
                view.getLayoutParams().width = (int) value;
                view.requestLayout();
            }
        }, width);
    }

    public AnimationBuilder waitForHeight() {
        this.waitForHeight = true;
        return this;
    }

    protected List<Animator> createAnimators() {
        return this.animatorList;
    }

    public AnimationBuilder andAnimate(View... views) {
        return this.viewAnimator.addAnimationBuilder(views);
    }

    public AnimationBuilder thenAnimate(View... views) {
        return this.viewAnimator.thenAnimate(views);
    }

    public AnimationBuilder duration(long duration) {
        this.viewAnimator.duration(duration);
        return this;
    }

    public AnimationBuilder startDelay(long startDelay) {
        this.viewAnimator.startDelay(startDelay);
        return this;
    }

    public AnimationBuilder repeatCount(@IntRange(from = -1) int repeatCount) {
        this.viewAnimator.repeatCount(repeatCount);
        return this;
    }

    public AnimationBuilder repeatMode(int repeatMode) {
        this.viewAnimator.repeatMode(repeatMode);
        return this;
    }

    public AnimationBuilder onStart(Start startListener) {
        this.viewAnimator.onStart(startListener);
        return this;
    }

    public AnimationBuilder onStop(Stop stopListener) {
        this.viewAnimator.onStop(stopListener);
        return this;
    }

    public AnimationBuilder interpolator(Interpolator interpolator) {
        this.viewAnimator.interpolator(interpolator);
        return this;
    }

    public ViewAnimator accelerate() {
        return this.viewAnimator.interpolator(new AccelerateInterpolator());
    }

    public ViewAnimator descelerate() {
        return this.viewAnimator.interpolator(new DecelerateInterpolator());
    }

    public void start() {
        this.viewAnimator.start();
    }

    public View[] getViews() {
        return this.views;
    }

    public View getView() {
        return this.views[0];
    }

    public boolean isWaitForHeight() {
        return this.waitForHeight;
    }

    public AnimationBuilder bounce() {
        return translationY(0.0f, 0.0f, -30.0f, 0.0f, -15.0f, 0.0f, 0.0f);
    }

    public AnimationBuilder bounceIn() {
        alpha(0.0f, 1.0f, 1.0f, 1.0f);
        scaleX(0.3f, 1.05f, 0.9f, 1.0f);
        scaleY(0.3f, 1.05f, 0.9f, 1.0f);
        return this;
    }

    public AnimationBuilder bounceOut() {
        scaleY(1.0f, 0.9f, 1.05f, 0.3f);
        scaleX(1.0f, 0.9f, 1.05f, 0.3f);
        alpha(1.0f, 1.0f, 1.0f, 0.0f);
        return this;
    }

    public AnimationBuilder fadeIn() {
        return alpha(0.0f, ChartZoomer.ZOOM_AMOUNT, 0.5f, ColumnChartData.DEFAULT_FILL_RATIO, 1.0f);
    }

    public AnimationBuilder fadeOut() {
        return alpha(1.0f, ColumnChartData.DEFAULT_FILL_RATIO, 0.5f, ChartZoomer.ZOOM_AMOUNT, 0.0f);
    }

    public AnimationBuilder flash() {
        return alpha(1.0f, 0.0f, 1.0f, 0.0f, 1.0f);
    }

    public AnimationBuilder flipHorizontal() {
        return rotationX(90.0f, -15.0f, 15.0f, 0.0f);
    }

    public AnimationBuilder flipVertical() {
        return rotationY(90.0f, -15.0f, 15.0f, 0.0f);
    }

    public AnimationBuilder pulse() {
        scaleY(1.0f, 1.1f, 1.0f);
        scaleX(1.0f, 1.1f, 1.0f);
        return this;
    }

    public AnimationBuilder rollIn() {
        for (View view : this.views) {
            alpha(0.0f, 1.0f);
            translationX((float) (-((view.getWidth() - view.getPaddingLeft()) - view
                    .getPaddingRight())), 0.0f);
            rotation(-120.0f, 0.0f);
        }
        return this;
    }

    public AnimationBuilder rollOut() {
        for (View view : this.views) {
            alpha(1.0f, 0.0f);
            translationX(0.0f, (float) view.getWidth());
            rotation(0.0f, 120.0f);
        }
        return this;
    }

    public AnimationBuilder rubber() {
        scaleX(1.0f, 1.25f, ColumnChartData.DEFAULT_FILL_RATIO, 1.15f, 1.0f);
        scaleY(1.0f, ColumnChartData.DEFAULT_FILL_RATIO, 1.25f, 0.85f, 1.0f);
        return this;
    }

    public AnimationBuilder shake() {
        translationX(0.0f, 25.0f, -25.0f, 25.0f, -25.0f, 15.0f, -15.0f, 6.0f, -6.0f, 0.0f);
        interpolator(new CycleInterpolator(5.0f));
        return this;
    }

    public AnimationBuilder standUp() {
        for (View view : this.views) {
            float x = (float) ((((view.getWidth() - view.getPaddingLeft()) - view.getPaddingRight
                    ()) / 2) + view.getPaddingLeft());
            float y = (float) (view.getHeight() - view.getPaddingBottom());
            pivotX(x, x, x, x, x);
            pivotY(y, y, y, y, y);
            rotationX(55.0f, -30.0f, 15.0f, -15.0f, 0.0f);
        }
        return this;
    }

    public AnimationBuilder swing() {
        return rotation(0.0f, 10.0f, -10.0f, 6.0f, -6.0f, IPhotoView.DEFAULT_MAX_SCALE, -3.0f, 0
        .0f);
    }

    public AnimationBuilder tada() {
        scaleX(1.0f, 0.9f, 0.9f, 1.1f, 1.1f, 1.1f, 1.1f, 1.1f, 1.1f, 1.0f);
        scaleY(1.0f, 0.9f, 0.9f, 1.1f, 1.1f, 1.1f, 1.1f, 1.1f, 1.1f, 1.0f);
        rotation(0.0f, -3.0f, -3.0f, IPhotoView.DEFAULT_MAX_SCALE, -3.0f, IPhotoView
                .DEFAULT_MAX_SCALE, -3.0f, IPhotoView.DEFAULT_MAX_SCALE, -3.0f, 0.0f);
        return this;
    }

    public AnimationBuilder wave() {
        for (View view : this.views) {
            float x = (float) ((((view.getWidth() - view.getPaddingLeft()) - view.getPaddingRight
                    ()) / 2) + view.getPaddingLeft());
            float y = (float) (view.getHeight() - view.getPaddingBottom());
            rotation(12.0f, -12.0f, IPhotoView.DEFAULT_MAX_SCALE, -3.0f, 0.0f);
            pivotX(x, x, x, x, x);
            pivotY(y, y, y, y, y);
        }
        return this;
    }

    public AnimationBuilder wobble() {
        for (View view : this.views) {
            float one = (float) (((double) ((float) view.getWidth())) / 100.0d);
            translationX(0.0f * one, -25.0f * one, 20.0f * one, -15.0f * one, 10.0f * one, -5.0f
                    * one, 0.0f * one, 0.0f);
            rotation(0.0f, -5.0f, IPhotoView.DEFAULT_MAX_SCALE, -3.0f, 2.0f, -1.0f, 0.0f);
        }
        return this;
    }

    public AnimationBuilder zoomIn() {
        scaleX(0.45f, 1.0f);
        scaleY(0.45f, 1.0f);
        alpha(0.0f, 1.0f);
        return this;
    }

    public AnimationBuilder zoomOut() {
        scaleX(1.0f, 0.3f, 0.0f);
        scaleY(1.0f, 0.3f, 0.0f);
        alpha(1.0f, 0.0f, 0.0f);
        return this;
    }

    public AnimationBuilder fall() {
        rotation(1080.0f, 720.0f, 360.0f, 0.0f);
        return this;
    }

    public AnimationBuilder newsPaper() {
        alpha(0.0f, 1.0f);
        scaleX(0.1f, 0.5f, 1.0f);
        scaleY(0.1f, 0.5f, 1.0f);
        return this;
    }

    public AnimationBuilder slit() {
        rotationY(90.0f, 88.0f, 88.0f, 45.0f, 0.0f);
        alpha(0.0f, 0.4f, 0.8f, 1.0f);
        scaleX(0.0f, 0.5f, 0.9f, 0.9f, 1.0f);
        scaleY(0.0f, 0.5f, 0.9f, 0.9f, 1.0f);
        return this;
    }

    public AnimationBuilder slideLeft() {
        translationX(-300.0f, 0.0f);
        alpha(0.0f, 1.0f);
        return this;
    }

    public AnimationBuilder slideRight() {
        translationX(300.0f, 0.0f);
        alpha(0.0f, 1.0f);
        return this;
    }

    public AnimationBuilder slideTop() {
        translationY(-300.0f, 0.0f);
        alpha(0.0f, 1.0f);
        return this;
    }

    public AnimationBuilder slideBottom() {
        translationY(300.0f, 0.0f);
        alpha(0.0f, 1.0f);
        return this;
    }

    public AnimationBuilder path(Path path) {
        if (path == null) {
            return this;
        }
        final PathMeasure pathMeasure = new PathMeasure(path, false);
        return custom(new Update() {
            public void update(View view, float value) {
                float[] currentPosition = new float[2];
                pathMeasure.getPosTan(value, currentPosition, null);
                float x = currentPosition[0];
                float y = currentPosition[1];
                ViewHelper.setX(view, x);
                ViewHelper.setY(view, y);
                Log.d(null, "path: value=" + value + ", x=" + x + ", y=" + y);
            }
        }, 0.0f, pathMeasure.getLength());
    }

    public AnimationBuilder svgPath(String dAttributeOfPath) {
        return path(SvgPathParser.tryParsePath(dAttributeOfPath));
    }
}
