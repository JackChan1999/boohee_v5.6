package com.boohee.myview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.baidu.location.aj;
import com.boohee.utils.ResolutionUtils;

public class PullToZoomScrollView extends NestedScrollView {
    private int   DEFAULT_PULL_HEIGHT = 100;
    private float distance            = 0.0f;
    private boolean isBig;
    private boolean isPreparePull;
    private boolean isTouchOne;
    private boolean isonce;
    private int mCurrentOffset = 0;
    private LinearLayout         mParentView;
    private int                  mScreenHeight;
    private ViewGroup            mTopView;
    private int                  mTopViewHeight;
    private ObjectAnimator       mAnimator;
    private OnPullToZoomListener onPullToZoomListener;
    private float startY = 0.0f;
    private int touchSlop;

    public interface OnPullToZoomListener {
        void onCancelPull();

        void onPreparePull();

        void onPull();

        void onScrollChanged(int i);

        void onStartDrag();
    }

    public PullToZoomScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOverScrollMode(View.OVER_SCROLL_NEVER);
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        this.mScreenHeight = metrics.heightPixels;
        this.mTopViewHeight = ResolutionUtils.getHeight(context, metrics.widthPixels, 3, 2);
        setVerticalScrollBarEnabled(false);
        this.touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (!this.isonce) {
            this.mParentView = (LinearLayout) getChildAt(0);
            this.mTopView = (ViewGroup) this.mParentView.getChildAt(0);
            this.mTopView.getLayoutParams().height = this.mTopViewHeight;
            this.isonce = true;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_UP:
                if (this.isBig) {
                    reset();
                    this.isBig = false;
                }
                this.isTouchOne = false;
                break;
            case MotionEvent.ACTION_MOVE:
                if (this.mCurrentOffset <= 0) {
                    if (!this.isTouchOne) {
                        this.startY = ev.getY();
                        this.isTouchOne = true;
                    }
                    this.distance = ev.getY() - this.startY;
                    if (this.distance >= ((float) this.touchSlop) && this.distance > 0.0f) {
                        this.isBig = true;
                        setT(((int) (-this.distance)) / 4);
                        break;
                    }
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    public void setT(int t) {
        scrollTo(0, 0);
        if (t < 0) {
            this.mTopView.getLayoutParams().height = this.mTopViewHeight - t;
            this.mTopView.requestLayout();
            if ((-t) <= this.DEFAULT_PULL_HEIGHT || !this.isBig) {
                if (this.onPullToZoomListener != null) {
                    this.onPullToZoomListener.onStartDrag();
                }
            } else if (this.onPullToZoomListener != null) {
                this.isPreparePull = true;
                this.onPullToZoomListener.onPreparePull();
            }
        }
    }

    private void reset() {
        if (this.mAnimator == null || !this.mAnimator.isRunning()) {
            this.mAnimator = ObjectAnimator.ofInt(this, "t", new int[]{((int) (-this.distance)) /
                    4, 0});
            this.mAnimator.setDuration(150);
            this.mAnimator.addListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    PullToZoomScrollView.this.isPreparePull = false;
                    if (PullToZoomScrollView.this.distance / aj.hA > ((float)
                            PullToZoomScrollView.this.DEFAULT_PULL_HEIGHT)) {
                        if (PullToZoomScrollView.this.onPullToZoomListener != null) {
                            PullToZoomScrollView.this.onPullToZoomListener.onPull();
                        }
                    } else if (PullToZoomScrollView.this.onPullToZoomListener != null) {
                        PullToZoomScrollView.this.onPullToZoomListener.onCancelPull();
                    }
                }
            });
            this.mAnimator.start();
        }
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        this.mCurrentOffset = t;
        if (this.onPullToZoomListener != null) {
            this.onPullToZoomListener.onScrollChanged(t);
        }
        if (t <= this.mTopViewHeight && t >= 0 && !this.isBig) {
            this.mTopView.setTranslationY((float) (t / 2));
        }
        if (this.isBig) {
            scrollTo(0, 0);
        }
    }

    public void setOnPullToZoomListener(OnPullToZoomListener onPullToZoomListener) {
        this.onPullToZoomListener = onPullToZoomListener;
    }
}
