package com.balysv.materialripple;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.util.Property;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewParent;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import com.baidu.location.a.a;
import com.boohee.widgets.PathListView;

public class MaterialRippleLayout extends FrameLayout {
    private static final float DEFAULT_ALPHA = 0.2f;
    private static final int DEFAULT_BACKGROUND = 0;
    private static final int DEFAULT_COLOR = -16777216;
    private static final boolean DEFAULT_DELAY_CLICK = true;
    private static final float DEFAULT_DIAMETER_DP = 35.0f;
    private static final int DEFAULT_DURATION = 350;
    private static final int DEFAULT_FADE_DURATION = 75;
    private static final boolean DEFAULT_HOVER = true;
    private static final boolean DEFAULT_PERSISTENT = false;
    private static final boolean DEFAULT_RIPPLE_OVERLAY = false;
    private static final int DEFAULT_ROUNDED_CORNERS = 0;
    private static final boolean DEFAULT_SEARCH_ADAPTER = false;
    private static final int FADE_EXTRA_DELAY = 50;
    private static final long HOVER_DURATION = 2500;
    private final Rect bounds;
    private View childView;
    private Property<MaterialRippleLayout, Integer> circleAlphaProperty;
    private Point currentCoords;
    private boolean eventCancelled;
    private GestureDetector gestureDetector;
    private boolean hasPerformedLongPress;
    private ObjectAnimator hoverAnimator;
    private int layerType;
    private SimpleOnGestureListener longClickListener;
    private final Paint paint;
    private AdapterView parentAdapter;
    private PerformClickEvent pendingClickEvent;
    private PressedEvent pendingPressEvent;
    private int positionInAdapter;
    private boolean prepressed;
    private Point previousCoords;
    private float radius;
    private Property<MaterialRippleLayout, Float> radiusProperty;
    private int rippleAlpha;
    private AnimatorSet rippleAnimator;
    private Drawable rippleBackground;
    private int rippleColor;
    private boolean rippleDelayClick;
    private int rippleDiameter;
    private int rippleDuration;
    private int rippleFadeDuration;
    private boolean rippleHover;
    private boolean rippleInAdapter;
    private boolean rippleOverlay;
    private boolean ripplePersistent;
    private float rippleRoundedCorners;

    private class PerformClickEvent implements Runnable {
        private PerformClickEvent() {
        }

        public void run() {
            if (!MaterialRippleLayout.this.hasPerformedLongPress) {
                if (MaterialRippleLayout.this.getParent() instanceof AdapterView) {
                    clickAdapterView((AdapterView) MaterialRippleLayout.this.getParent());
                } else if (MaterialRippleLayout.this.rippleInAdapter) {
                    clickAdapterView(MaterialRippleLayout.this.findParentAdapterView());
                } else {
                    MaterialRippleLayout.this.childView.performClick();
                }
            }
        }

        private void clickAdapterView(AdapterView parent) {
            int position = parent.getPositionForView(MaterialRippleLayout.this);
            long itemId = parent.getAdapter() != null ? parent.getAdapter().getItemId(position) : 0;
            if (position != -1) {
                parent.performItemClick(MaterialRippleLayout.this, position, itemId);
            }
        }
    }

    private final class PressedEvent implements Runnable {
        private final MotionEvent event;

        public PressedEvent(MotionEvent event) {
            this.event = event;
        }

        public void run() {
            MaterialRippleLayout.this.prepressed = false;
            MaterialRippleLayout.this.childView.setLongClickable(false);
            MaterialRippleLayout.this.childView.onTouchEvent(this.event);
            MaterialRippleLayout.this.childView.setPressed(true);
            if (MaterialRippleLayout.this.rippleHover) {
                MaterialRippleLayout.this.startHover();
            }
        }
    }

    public static class RippleBuilder {
        private final View child;
        private final Context context;
        private float rippleAlpha = MaterialRippleLayout.DEFAULT_ALPHA;
        private int rippleBackground = 0;
        private int rippleColor = -16777216;
        private boolean rippleDelayClick = true;
        private float rippleDiameter = MaterialRippleLayout.DEFAULT_DIAMETER_DP;
        private int rippleDuration = 350;
        private int rippleFadeDuration = 75;
        private boolean rippleHover = true;
        private boolean rippleOverlay = false;
        private boolean ripplePersistent = false;
        private float rippleRoundedCorner = 0.0f;
        private boolean rippleSearchAdapter = false;

        public RippleBuilder(View child) {
            this.child = child;
            this.context = child.getContext();
        }

        public RippleBuilder rippleColor(int color) {
            this.rippleColor = color;
            return this;
        }

        public RippleBuilder rippleOverlay(boolean overlay) {
            this.rippleOverlay = overlay;
            return this;
        }

        public RippleBuilder rippleHover(boolean hover) {
            this.rippleHover = hover;
            return this;
        }

        public RippleBuilder rippleDiameterDp(int diameterDp) {
            this.rippleDiameter = (float) diameterDp;
            return this;
        }

        public RippleBuilder rippleDuration(int duration) {
            this.rippleDuration = duration;
            return this;
        }

        public RippleBuilder rippleAlpha(float alpha) {
            this.rippleAlpha = 255.0f * alpha;
            return this;
        }

        public RippleBuilder rippleDelayClick(boolean delayClick) {
            this.rippleDelayClick = delayClick;
            return this;
        }

        public RippleBuilder rippleFadeDuration(int fadeDuration) {
            this.rippleFadeDuration = fadeDuration;
            return this;
        }

        public RippleBuilder ripplePersistent(boolean persistent) {
            this.ripplePersistent = persistent;
            return this;
        }

        public RippleBuilder rippleBackground(int color) {
            this.rippleBackground = color;
            return this;
        }

        public RippleBuilder rippleInAdapter(boolean inAdapter) {
            this.rippleSearchAdapter = inAdapter;
            return this;
        }

        public RippleBuilder rippleRoundedCorners(int radiusDp) {
            this.rippleRoundedCorner = (float) radiusDp;
            return this;
        }

        public MaterialRippleLayout create() {
            MaterialRippleLayout layout = new MaterialRippleLayout(this.context);
            layout.setRippleColor(this.rippleColor);
            layout.setDefaultRippleAlpha((int) this.rippleAlpha);
            layout.setRippleDelayClick(this.rippleDelayClick);
            layout.setRippleDiameter((int) MaterialRippleLayout.dpToPx(this.context.getResources(), this.rippleDiameter));
            layout.setRippleDuration(this.rippleDuration);
            layout.setRippleFadeDuration(this.rippleFadeDuration);
            layout.setRippleHover(this.rippleHover);
            layout.setRipplePersistent(this.ripplePersistent);
            layout.setRippleOverlay(this.rippleOverlay);
            layout.setRippleBackground(this.rippleBackground);
            layout.setRippleInAdapter(this.rippleSearchAdapter);
            layout.setRippleRoundedCorners((int) MaterialRippleLayout.dpToPx(this.context.getResources(), this.rippleRoundedCorner));
            LayoutParams params = this.child.getLayoutParams();
            ViewGroup parent = (ViewGroup) this.child.getParent();
            int index = 0;
            if (parent == null || !(parent instanceof MaterialRippleLayout)) {
                if (parent != null) {
                    index = parent.indexOfChild(this.child);
                    parent.removeView(this.child);
                }
                layout.addView(this.child, new LayoutParams(-1, -1));
                if (parent != null) {
                    parent.addView(layout, index, params);
                }
                return layout;
            }
            throw new IllegalStateException("MaterialRippleLayout could not be created: parent of the view already is a MaterialRippleLayout");
        }
    }

    public static RippleBuilder on(View view) {
        return new RippleBuilder(view);
    }

    public MaterialRippleLayout(Context context) {
        this(context, null, 0);
    }

    public MaterialRippleLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MaterialRippleLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.paint = new Paint(1);
        this.bounds = new Rect();
        this.currentCoords = new Point();
        this.previousCoords = new Point();
        this.longClickListener = new SimpleOnGestureListener() {
            public void onLongPress(MotionEvent e) {
                MaterialRippleLayout.this.hasPerformedLongPress = MaterialRippleLayout.this.childView.performLongClick();
                if (MaterialRippleLayout.this.hasPerformedLongPress) {
                    if (MaterialRippleLayout.this.rippleHover) {
                        MaterialRippleLayout.this.startRipple(null);
                    }
                    MaterialRippleLayout.this.cancelPressedEvent();
                }
            }

            public boolean onDown(MotionEvent e) {
                MaterialRippleLayout.this.hasPerformedLongPress = false;
                return super.onDown(e);
            }
        };
        this.radiusProperty = new Property<MaterialRippleLayout, Float>(Float.class, a.else) {
            public Float get(MaterialRippleLayout object) {
                return Float.valueOf(object.getRadius());
            }

            public void set(MaterialRippleLayout object, Float value) {
                object.setRadius(value.floatValue());
            }
        };
        this.circleAlphaProperty = new Property<MaterialRippleLayout, Integer>(Integer.class, "rippleAlpha") {
            public Integer get(MaterialRippleLayout object) {
                return Integer.valueOf(object.getRippleAlpha());
            }

            public void set(MaterialRippleLayout object, Integer value) {
                object.setRippleAlpha(value);
            }
        };
        setWillNotDraw(false);
        this.gestureDetector = new GestureDetector(context, this.longClickListener);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MaterialRippleLayout);
        this.rippleColor = a.getColor(R.styleable.MaterialRippleLayout_mrl_rippleColor, -16777216);
        this.rippleDiameter = a.getDimensionPixelSize(R.styleable.MaterialRippleLayout_mrl_rippleDimension, (int) dpToPx(getResources(), DEFAULT_DIAMETER_DP));
        this.rippleOverlay = a.getBoolean(R.styleable.MaterialRippleLayout_mrl_rippleOverlay, false);
        this.rippleHover = a.getBoolean(R.styleable.MaterialRippleLayout_mrl_rippleHover, true);
        this.rippleDuration = a.getInt(R.styleable.MaterialRippleLayout_mrl_rippleDuration, 350);
        this.rippleAlpha = (int) (255.0f * a.getFloat(R.styleable.MaterialRippleLayout_mrl_rippleAlpha, DEFAULT_ALPHA));
        this.rippleDelayClick = a.getBoolean(R.styleable.MaterialRippleLayout_mrl_rippleDelayClick, true);
        this.rippleFadeDuration = a.getInteger(R.styleable.MaterialRippleLayout_mrl_rippleFadeDuration, 75);
        this.rippleBackground = new ColorDrawable(a.getColor(R.styleable.MaterialRippleLayout_mrl_rippleBackground, 0));
        this.ripplePersistent = a.getBoolean(R.styleable.MaterialRippleLayout_mrl_ripplePersistent, false);
        this.rippleInAdapter = a.getBoolean(R.styleable.MaterialRippleLayout_mrl_rippleInAdapter, false);
        this.rippleRoundedCorners = (float) a.getDimensionPixelSize(R.styleable.MaterialRippleLayout_mrl_rippleRoundedCorners, 0);
        a.recycle();
        this.paint.setColor(this.rippleColor);
        this.paint.setAlpha(this.rippleAlpha);
        enableClipPathSupportIfNecessary();
    }

    public <T extends View> T getChildView() {
        return this.childView;
    }

    public final void addView(View child, int index, LayoutParams params) {
        if (getChildCount() > 0) {
            throw new IllegalStateException("MaterialRippleLayout can host only one child");
        }
        this.childView = child;
        super.addView(child, index, params);
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        if (this.childView == null) {
            throw new IllegalStateException("MaterialRippleLayout must have a child view to handle clicks");
        }
        this.childView.setOnClickListener(onClickListener);
    }

    public void setOnLongClickListener(OnLongClickListener onClickListener) {
        if (this.childView == null) {
            throw new IllegalStateException("MaterialRippleLayout must have a child view to handle clicks");
        }
        this.childView.setOnLongClickListener(onClickListener);
    }

    public boolean onInterceptTouchEvent(MotionEvent event) {
        return !findClickableViewInChild(this.childView, (int) event.getX(), (int) event.getY());
    }

    public boolean onTouchEvent(MotionEvent event) {
        boolean superOnTouchEvent = super.onTouchEvent(event);
        if (!isEnabled() || !this.childView.isEnabled()) {
            return superOnTouchEvent;
        }
        boolean isEventInBounds = this.bounds.contains((int) event.getX(), (int) event.getY());
        if (isEventInBounds) {
            this.previousCoords.set(this.currentCoords.x, this.currentCoords.y);
            this.currentCoords.set((int) event.getX(), (int) event.getY());
        }
        if (this.gestureDetector.onTouchEvent(event) || this.hasPerformedLongPress) {
            return true;
        }
        switch (event.getActionMasked()) {
            case 0:
                setPositionInAdapter();
                this.eventCancelled = false;
                this.pendingPressEvent = new PressedEvent(event);
                if (!isInScrollingContainer()) {
                    this.pendingPressEvent.run();
                    break;
                }
                cancelPressedEvent();
                this.prepressed = true;
                postDelayed(this.pendingPressEvent, (long) ViewConfiguration.getTapTimeout());
                break;
            case 1:
                this.pendingClickEvent = new PerformClickEvent();
                if (this.prepressed) {
                    this.childView.setPressed(true);
                    postDelayed(new Runnable() {
                        public void run() {
                            MaterialRippleLayout.this.childView.setPressed(false);
                        }
                    }, (long) ViewConfiguration.getPressedStateDuration());
                }
                if (isEventInBounds) {
                    startRipple(this.pendingClickEvent);
                } else if (!this.rippleHover) {
                    setRadius(0.0f);
                }
                if (!this.rippleDelayClick && isEventInBounds) {
                    this.pendingClickEvent.run();
                }
                cancelPressedEvent();
                break;
            case 2:
                if (this.rippleHover) {
                    if (isEventInBounds && !this.eventCancelled) {
                        invalidate();
                    } else if (!isEventInBounds) {
                        startRipple(null);
                    }
                }
                if (!isEventInBounds) {
                    cancelPressedEvent();
                    if (this.hoverAnimator != null) {
                        this.hoverAnimator.cancel();
                    }
                    this.childView.onTouchEvent(event);
                    this.eventCancelled = true;
                    break;
                }
                break;
            case 3:
                if (this.rippleInAdapter) {
                    this.currentCoords.set(this.previousCoords.x, this.previousCoords.y);
                    this.previousCoords = new Point();
                }
                this.childView.onTouchEvent(event);
                if (!this.rippleHover) {
                    this.childView.setPressed(false);
                } else if (!this.prepressed) {
                    startRipple(null);
                }
                cancelPressedEvent();
                break;
        }
        return true;
    }

    private void cancelPressedEvent() {
        if (this.pendingPressEvent != null) {
            removeCallbacks(this.pendingPressEvent);
            this.prepressed = false;
        }
    }

    private void startHover() {
        if (!this.eventCancelled) {
            if (this.hoverAnimator != null) {
                this.hoverAnimator.cancel();
            }
            float radius = (float) (Math.sqrt(Math.pow((double) getWidth(), PathListView.ZOOM_X2) + Math.pow((double) getHeight(), PathListView.ZOOM_X2)) * 1.2000000476837158d);
            this.hoverAnimator = ObjectAnimator.ofFloat(this, this.radiusProperty, new float[]{(float) this.rippleDiameter, radius}).setDuration(HOVER_DURATION);
            this.hoverAnimator.setInterpolator(new LinearInterpolator());
            this.hoverAnimator.start();
        }
    }

    private void startRipple(final Runnable animationEndRunnable) {
        if (!this.eventCancelled) {
            float endRadius = getEndRadius();
            cancelAnimations();
            this.rippleAnimator = new AnimatorSet();
            this.rippleAnimator.addListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animation) {
                    if (!MaterialRippleLayout.this.ripplePersistent) {
                        MaterialRippleLayout.this.setRadius(0.0f);
                        MaterialRippleLayout.this.setRippleAlpha(Integer.valueOf(MaterialRippleLayout.this.rippleAlpha));
                    }
                    if (animationEndRunnable != null && MaterialRippleLayout.this.rippleDelayClick) {
                        animationEndRunnable.run();
                    }
                    MaterialRippleLayout.this.childView.setPressed(false);
                }
            });
            ObjectAnimator ripple = ObjectAnimator.ofFloat(this, this.radiusProperty, new float[]{this.radius, endRadius});
            ripple.setDuration((long) this.rippleDuration);
            ripple.setInterpolator(new DecelerateInterpolator());
            ObjectAnimator fade = ObjectAnimator.ofInt(this, this.circleAlphaProperty, new int[]{this.rippleAlpha, 0});
            fade.setDuration((long) this.rippleFadeDuration);
            fade.setInterpolator(new AccelerateInterpolator());
            fade.setStartDelay((long) ((this.rippleDuration - this.rippleFadeDuration) - 50));
            if (this.ripplePersistent) {
                this.rippleAnimator.play(ripple);
            } else if (getRadius() > endRadius) {
                fade.setStartDelay(0);
                this.rippleAnimator.play(fade);
            } else {
                this.rippleAnimator.playTogether(new Animator[]{ripple, fade});
            }
            this.rippleAnimator.start();
        }
    }

    private void cancelAnimations() {
        if (this.rippleAnimator != null) {
            this.rippleAnimator.cancel();
            this.rippleAnimator.removeAllListeners();
        }
        if (this.hoverAnimator != null) {
            this.hoverAnimator.cancel();
        }
    }

    private float getEndRadius() {
        int width = getWidth();
        int height = getHeight();
        return ((float) Math.sqrt(Math.pow((double) (width / 2 > this.currentCoords.x ? (float) (width - this.currentCoords.x) : (float) this.currentCoords.x), PathListView.ZOOM_X2) + Math.pow((double) (height / 2 > this.currentCoords.y ? (float) (height - this.currentCoords.y) : (float) this.currentCoords.y), PathListView.ZOOM_X2))) * 1.2f;
    }

    private boolean isInScrollingContainer() {
        ViewParent p = getParent();
        while (p != null && (p instanceof ViewGroup)) {
            if (((ViewGroup) p).shouldDelayChildPressedState()) {
                return true;
            }
            p = p.getParent();
        }
        return false;
    }

    private AdapterView findParentAdapterView() {
        if (this.parentAdapter != null) {
            return this.parentAdapter;
        }
        ViewParent current = getParent();
        while (!(current instanceof AdapterView)) {
            try {
                current = current.getParent();
            } catch (NullPointerException e) {
                throw new RuntimeException("Could not find a parent AdapterView");
            }
        }
        this.parentAdapter = (AdapterView) current;
        return this.parentAdapter;
    }

    private void setPositionInAdapter() {
        if (this.rippleInAdapter) {
            this.positionInAdapter = findParentAdapterView().getPositionForView(this);
        }
    }

    private boolean adapterPositionChanged() {
        if (!this.rippleInAdapter) {
            return false;
        }
        boolean changed;
        int newPosition = findParentAdapterView().getPositionForView(this);
        if (newPosition != this.positionInAdapter) {
            changed = true;
        } else {
            changed = false;
        }
        this.positionInAdapter = newPosition;
        if (!changed) {
            return changed;
        }
        cancelPressedEvent();
        cancelAnimations();
        this.childView.setPressed(false);
        setRadius(0.0f);
        return changed;
    }

    private boolean findClickableViewInChild(View view, int x, int y) {
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View child = viewGroup.getChildAt(i);
                Rect rect = new Rect();
                child.getHitRect(rect);
                if (rect.contains(x, y)) {
                    return findClickableViewInChild(child, x - rect.left, y - rect.top);
                }
            }
        } else if (view != this.childView) {
            return view.isEnabled() && (view.isClickable() || view.isLongClickable() || view.isFocusableInTouchMode());
        }
        return view.isFocusableInTouchMode();
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.bounds.set(0, 0, w, h);
        this.rippleBackground.setBounds(this.bounds);
    }

    public boolean isInEditMode() {
        return true;
    }

    public void draw(Canvas canvas) {
        boolean positionChanged = adapterPositionChanged();
        if (this.rippleOverlay) {
            if (!positionChanged) {
                this.rippleBackground.draw(canvas);
            }
            super.draw(canvas);
            if (!positionChanged) {
                if (this.rippleRoundedCorners != 0.0f) {
                    Path clipPath = new Path();
                    clipPath.addRoundRect(new RectF(0.0f, 0.0f, (float) canvas.getWidth(), (float) canvas.getHeight()), this.rippleRoundedCorners, this.rippleRoundedCorners, Direction.CW);
                    canvas.clipPath(clipPath);
                }
                canvas.drawCircle((float) this.currentCoords.x, (float) this.currentCoords.y, this.radius, this.paint);
                return;
            }
            return;
        }
        if (!positionChanged) {
            this.rippleBackground.draw(canvas);
            canvas.drawCircle((float) this.currentCoords.x, (float) this.currentCoords.y, this.radius, this.paint);
        }
        super.draw(canvas);
    }

    private float getRadius() {
        return this.radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
        invalidate();
    }

    public int getRippleAlpha() {
        return this.paint.getAlpha();
    }

    public void setRippleAlpha(Integer rippleAlpha) {
        this.paint.setAlpha(rippleAlpha.intValue());
        invalidate();
    }

    public void setRippleColor(int rippleColor) {
        this.rippleColor = rippleColor;
        this.paint.setColor(rippleColor);
        this.paint.setAlpha(this.rippleAlpha);
        invalidate();
    }

    public void setRippleOverlay(boolean rippleOverlay) {
        this.rippleOverlay = rippleOverlay;
    }

    public void setRippleDiameter(int rippleDiameter) {
        this.rippleDiameter = rippleDiameter;
    }

    public void setRippleDuration(int rippleDuration) {
        this.rippleDuration = rippleDuration;
    }

    public void setRippleBackground(int color) {
        this.rippleBackground = new ColorDrawable(color);
        this.rippleBackground.setBounds(this.bounds);
        invalidate();
    }

    public void setRippleHover(boolean rippleHover) {
        this.rippleHover = rippleHover;
    }

    public void setRippleDelayClick(boolean rippleDelayClick) {
        this.rippleDelayClick = rippleDelayClick;
    }

    public void setRippleFadeDuration(int rippleFadeDuration) {
        this.rippleFadeDuration = rippleFadeDuration;
    }

    public void setRipplePersistent(boolean ripplePersistent) {
        this.ripplePersistent = ripplePersistent;
    }

    public void setRippleInAdapter(boolean rippleInAdapter) {
        this.rippleInAdapter = rippleInAdapter;
    }

    public void setRippleRoundedCorners(int rippleRoundedCorner) {
        this.rippleRoundedCorners = (float) rippleRoundedCorner;
        enableClipPathSupportIfNecessary();
    }

    public void setDefaultRippleAlpha(int alpha) {
        this.rippleAlpha = alpha;
        this.paint.setAlpha(alpha);
        invalidate();
    }

    public void performRipple() {
        this.currentCoords = new Point(getWidth() / 2, getHeight() / 2);
        startRipple(null);
    }

    public void performRipple(Point anchor) {
        this.currentCoords = new Point(anchor.x, anchor.y);
        startRipple(null);
    }

    private void enableClipPathSupportIfNecessary() {
        if (VERSION.SDK_INT > 17) {
            return;
        }
        if (this.rippleRoundedCorners != 0.0f) {
            this.layerType = getLayerType();
            setLayerType(1, null);
            return;
        }
        setLayerType(this.layerType, null);
    }

    static float dpToPx(Resources resources, float dp) {
        return TypedValue.applyDimension(1, dp, resources.getDisplayMetrics());
    }
}
