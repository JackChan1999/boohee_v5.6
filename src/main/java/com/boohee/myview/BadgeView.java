package com.boohee.myview;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewParent;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.TabWidget;
import android.widget.TextView;

import com.boohee.one.R;

public class BadgeView extends TextView {
    private static final int DEFAULT_CORNER_RADIUS_DIP = 8;
    private static final int DEFAULT_LR_PADDING_DIP    = 5;
    private static final int DEFAULT_MARGIN_DIP        = 5;
    private static final int DEFAULT_POSITION          = 2;
    private static final int DEFAULT_TEXT_COLOR        = -1;
    public static final  int POSITION_BOTTOM_LEFT      = 3;
    public static final  int POSITION_BOTTOM_RIGHT     = 4;
    public static final  int POSITION_CENTER           = 5;
    public static final  int POSITION_CENTER_RIGHT     = 6;
    public static final  int POSITION_TOP_LEFT         = 1;
    public static final  int POSITION_TOP_RIGHT        = 2;
    private static Animation     fadeIn;
    private static Animation     fadeOut;
    private        ShapeDrawable badgeBg;
    private        int           badgeColor;
    private        int           badgeMarginH;
    private        int           badgeMarginV;
    private        int           badgePosition;
    private        Context       context;
    private        boolean       isShown;
    private        View          target;
    private        int           targetTabIndex;

    public BadgeView(Context context) {
        this(context, (AttributeSet) null, 16842884);
    }

    public BadgeView(Context context, AttributeSet attrs) {
        this(context, attrs, 16842884);
    }

    public BadgeView(Context context, View target) {
        this(context, null, 16842884, target, 0);
    }

    public BadgeView(Context context, TabWidget target, int index) {
        this(context, null, 16842884, target, index);
    }

    public BadgeView(Context context, AttributeSet attrs, int defStyle) {
        this(context, attrs, defStyle, null, 0);
    }

    public BadgeView(Context context, AttributeSet attrs, int defStyle, View target, int tabIndex) {
        super(context, attrs, defStyle);
        init(context, target, tabIndex);
    }

    private void init(Context context, View target, int tabIndex) {
        this.context = context;
        this.target = target;
        this.targetTabIndex = tabIndex;
        this.badgePosition = 2;
        this.badgeMarginH = dipToPixels(5);
        this.badgeMarginV = this.badgeMarginH;
        this.badgeColor = R.color.he;
        setTypeface(Typeface.DEFAULT_BOLD);
        int paddingPixels = dipToPixels(5);
        setPadding(paddingPixels, 0, paddingPixels, 0);
        setTextColor(-1);
        fadeIn = new AlphaAnimation(0.0f, 1.0f);
        fadeIn.setInterpolator(new DecelerateInterpolator());
        fadeIn.setDuration(200);
        fadeOut = new AlphaAnimation(1.0f, 0.0f);
        fadeOut.setInterpolator(new AccelerateInterpolator());
        fadeOut.setDuration(200);
        this.isShown = false;
        if (this.target != null) {
            applyTo(this.target);
        } else {
            show();
        }
    }

    public void applyTo(View target) {
        LayoutParams lp = target.getLayoutParams();
        ViewParent parent = target.getParent();
        FrameLayout container = new FrameLayout(this.context);
        if (target instanceof TabWidget) {
            target = ((TabWidget) target).getChildTabViewAt(this.targetTabIndex);
            this.target = target;
            ((ViewGroup) target).addView(container, new LayoutParams(-1, -1));
            setVisibility(8);
            container.addView(this);
            return;
        }
        ViewGroup group = (ViewGroup) parent;
        int index = group.indexOfChild(target);
        group.removeView(target);
        group.addView(container, index, lp);
        container.addView(target);
        setVisibility(8);
        container.addView(this);
        group.invalidate();
    }

    public void show() {
        show(false, null);
    }

    public void show(boolean animate) {
        show(animate, fadeIn);
    }

    public void show(Animation anim) {
        show(true, anim);
    }

    public void hide() {
        hide(false, null);
    }

    public void hide(boolean animate) {
        hide(animate, fadeOut);
    }

    public void hide(Animation anim) {
        hide(true, anim);
    }

    public void toggle() {
        toggle(false, null, null);
    }

    public void toggle(boolean animate) {
        toggle(animate, fadeIn, fadeOut);
    }

    public void toggle(Animation animIn, Animation animOut) {
        toggle(true, animIn, animOut);
    }

    private void show(boolean animate, Animation anim) {
        if (getBackground() == null) {
            if (this.badgeBg == null) {
                this.badgeBg = getDefaultBackground();
            }
            setBackgroundDrawable(this.badgeBg);
        }
        applyLayoutParams();
        if (animate) {
            startAnimation(anim);
        }
        setVisibility(0);
        this.isShown = true;
    }

    private void hide(boolean animate, Animation anim) {
        setVisibility(8);
        if (animate) {
            startAnimation(anim);
        }
        this.isShown = false;
    }

    private void toggle(boolean animate, Animation animIn, Animation animOut) {
        boolean z = true;
        if (this.isShown) {
            if (!animate || animOut == null) {
                z = false;
            }
            hide(z, animOut);
            return;
        }
        if (!animate || animIn == null) {
            z = false;
        }
        show(z, animIn);
    }

    public int increment(int offset) {
        int i;
        CharSequence txt = getText();
        if (txt != null) {
            try {
                i = Integer.parseInt(txt.toString());
            } catch (NumberFormatException e) {
                i = 0;
            }
        } else {
            i = 0;
        }
        i += offset;
        setText(String.valueOf(i));
        return i;
    }

    public int decrement(int offset) {
        return increment(-offset);
    }

    private ShapeDrawable getDefaultBackground() {
        int r = dipToPixels(8);
        ShapeDrawable drawable = new ShapeDrawable(new RoundRectShape(new float[]{(float) r,
                (float) r, (float) r, (float) r, (float) r, (float) r, (float) r, (float) r},
                null, null));
        drawable.getPaint().setColor(this.badgeColor);
        return drawable;
    }

    private void applyLayoutParams() {
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(-2, -2);
        switch (this.badgePosition) {
            case 1:
                lp.gravity = 51;
                lp.setMargins(this.badgeMarginH, this.badgeMarginV, 0, 0);
                break;
            case 2:
                lp.gravity = 53;
                lp.setMargins(0, this.badgeMarginV, this.badgeMarginH, 0);
                break;
            case 3:
                lp.gravity = 83;
                lp.setMargins(this.badgeMarginH, 0, 0, this.badgeMarginV);
                break;
            case 4:
                lp.gravity = 85;
                lp.setMargins(0, 0, this.badgeMarginH, this.badgeMarginV);
                break;
            case 5:
                lp.gravity = 17;
                lp.setMargins(0, 0, 0, 0);
                break;
            case 6:
                lp.gravity = 21;
                lp.setMargins(0, 0, 0, 0);
                break;
        }
        setLayoutParams(lp);
    }

    public View getTarget() {
        return this.target;
    }

    public boolean isShown() {
        return this.isShown;
    }

    public int getBadgePosition() {
        return this.badgePosition;
    }

    public void setBadgePosition(int layoutPosition) {
        this.badgePosition = layoutPosition;
    }

    public int getHorizontalBadgeMargin() {
        return this.badgeMarginH;
    }

    public int getVerticalBadgeMargin() {
        return this.badgeMarginV;
    }

    public void setBadgeMargin(int badgeMargin) {
        this.badgeMarginH = dipToPixels(badgeMargin);
        this.badgeMarginV = dipToPixels(badgeMargin);
    }

    public void setBadgeMargin(int horizontal, int vertical) {
        this.badgeMarginH = horizontal;
        this.badgeMarginV = vertical;
    }

    public int getBadgeBackgroundColor() {
        return this.badgeColor;
    }

    public void setBadgeBackgroundColor(int badgeColor) {
        this.badgeColor = badgeColor;
        this.badgeBg = getDefaultBackground();
    }

    private int dipToPixels(int dip) {
        return (int) TypedValue.applyDimension(1, (float) dip, getResources().getDisplayMetrics());
    }
}
