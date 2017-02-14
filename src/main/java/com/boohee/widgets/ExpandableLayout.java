package com.boohee.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.Transformation;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.boohee.one.R;

public class ExpandableLayout extends RelativeLayout {
    private Animation   animation;
    private FrameLayout contentLayout;
    private Integer     duration;
    private FrameLayout headerLayout;
    private Boolean isAnimationRunning = Boolean.valueOf(false);
    private Boolean isOpened           = Boolean.valueOf(false);

    public ExpandableLayout(Context context) {
        super(context);
    }

    public ExpandableLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ExpandableLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        View rootView = View.inflate(context, R.layout.p2, this);
        this.headerLayout = (FrameLayout) rootView.findViewById(R.id.view_expandable_headerlayout);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ExpandableLayout);
        int headerID = typedArray.getResourceId(0, -1);
        int contentID = typedArray.getResourceId(1, -1);
        this.contentLayout = (FrameLayout) rootView.findViewById(R.id
                .view_expandable_contentLayout);
        if (headerID == -1 || contentID == -1) {
            throw new IllegalArgumentException("HeaderLayout and ContentLayout cannot be null!");
        } else if (!isInEditMode()) {
            this.duration = Integer.valueOf(typedArray.getInt(2, getContext().getResources()
                    .getInteger(17694720)));
            View headerView = View.inflate(context, headerID, null);
            headerView.setLayoutParams(new LayoutParams(-1, -2));
            this.headerLayout.addView(headerView);
            View contentView = View.inflate(context, contentID, null);
            contentView.setLayoutParams(new LayoutParams(-2, -2));
            this.contentLayout.addView(contentView);
            this.contentLayout.setVisibility(8);
            this.headerLayout.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    if (!ExpandableLayout.this.isAnimationRunning.booleanValue()) {
                        if (ExpandableLayout.this.contentLayout.getVisibility() == 0) {
                            ExpandableLayout.this.collapse(ExpandableLayout.this.contentLayout);
                        } else {
                            ExpandableLayout.this.expand(ExpandableLayout.this.contentLayout);
                        }
                        ExpandableLayout.this.isAnimationRunning = Boolean.valueOf(true);
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                ExpandableLayout.this.isAnimationRunning = Boolean.valueOf(false);
                            }
                        }, (long) ExpandableLayout.this.duration.intValue());
                    }
                }
            });
            typedArray.recycle();
        }
    }

    private void expand(final View v) {
        v.measure(-1, -2);
        final int targetHeight = v.getMeasuredHeight();
        v.getLayoutParams().height = 0;
        v.setVisibility(0);
        this.animation = new Animation() {
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1.0f) {
                    ExpandableLayout.this.isOpened = Boolean.valueOf(true);
                }
                v.getLayoutParams().height = interpolatedTime == 1.0f ? -2 : (int) (((float)
                        targetHeight) * interpolatedTime);
                v.requestLayout();
            }

            public boolean willChangeBounds() {
                return true;
            }
        };
        this.animation.setDuration((long) this.duration.intValue());
        v.startAnimation(this.animation);
    }

    private void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();
        this.animation = new Animation() {
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1.0f) {
                    v.setVisibility(8);
                    ExpandableLayout.this.isOpened = Boolean.valueOf(false);
                    return;
                }
                v.getLayoutParams().height = initialHeight - ((int) (((float) initialHeight) *
                        interpolatedTime));
                v.requestLayout();
            }

            public boolean willChangeBounds() {
                return true;
            }
        };
        this.animation.setDuration((long) this.duration.intValue());
        v.startAnimation(this.animation);
    }

    public Boolean isOpened() {
        return this.isOpened;
    }

    public void show() {
        if (!this.isAnimationRunning.booleanValue()) {
            expand(this.contentLayout);
            this.isAnimationRunning = Boolean.valueOf(true);
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    ExpandableLayout.this.isAnimationRunning = Boolean.valueOf(false);
                }
            }, (long) this.duration.intValue());
        }
    }

    public FrameLayout getHeaderLayout() {
        return this.headerLayout;
    }

    public FrameLayout getContentLayout() {
        return this.contentLayout;
    }

    public void hide() {
        if (!this.isAnimationRunning.booleanValue()) {
            collapse(this.contentLayout);
            this.isAnimationRunning = Boolean.valueOf(true);
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    ExpandableLayout.this.isAnimationRunning = Boolean.valueOf(false);
                }
            }, (long) this.duration.intValue());
        }
    }

    public void setLayoutAnimationListener(AnimationListener animationListener) {
        this.animation.setAnimationListener(animationListener);
    }
}
