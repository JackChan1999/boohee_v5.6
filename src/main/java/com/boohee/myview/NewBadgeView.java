package com.boohee.myview;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import com.boohee.one.R;

public class NewBadgeView extends TextView {
    private boolean mHideOnNull;

    public NewBadgeView(Context context) {
        this(context, null);
    }

    public NewBadgeView(Context context, AttributeSet attrs) {
        this(context, attrs, 16842884);
    }

    public NewBadgeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mHideOnNull = true;
        init();
    }

    private void init() {
        if (!(getLayoutParams() instanceof LayoutParams)) {
            setLayoutParams(new LayoutParams(-2, -2, 53));
        }
        setTextColor(-1);
        setTypeface(Typeface.DEFAULT_BOLD);
        setTextSize(2, 11.0f);
        setPadding(dip2Px(5.0f), dip2Px(1.0f), dip2Px(5.0f), dip2Px(1.0f));
        setBackground(9, getContext().getResources().getColor(R.color.gn));
        setGravity(17);
        setHideOnNull(true);
        setBadgeCount(0);
    }

    public void setBackground(int dipRadius, int badgeColor) {
        int radius = dip2Px((float) dipRadius);
        ShapeDrawable bgDrawable = new ShapeDrawable(new RoundRectShape(new float[]{(float)
                radius, (float) radius, (float) radius, (float) radius, (float) radius, (float)
                radius, (float) radius, (float) radius}, null, null));
        bgDrawable.getPaint().setColor(badgeColor);
        setBackgroundDrawable(bgDrawable);
    }

    public boolean isHideOnNull() {
        return this.mHideOnNull;
    }

    public void setHideOnNull(boolean hideOnNull) {
        this.mHideOnNull = hideOnNull;
        setText(getText());
    }

    public void setText(CharSequence text, BufferType type) {
        if (isHideOnNull() && (text == null || text.toString().equalsIgnoreCase("0"))) {
            setVisibility(8);
        } else {
            setVisibility(0);
        }
        super.setText(text, type);
    }

    public void setBadgeCount(int count) {
        setText(String.valueOf(count));
    }

    public Integer getBadgeCount() {
        Integer num = null;
        if (getText() != null) {
            try {
                num = Integer.valueOf(Integer.parseInt(getText().toString()));
            } catch (NumberFormatException e) {
            }
        }
        return num;
    }

    public void setBadgeGravity(int gravity) {
        LayoutParams params = (LayoutParams) getLayoutParams();
        params.gravity = gravity;
        setLayoutParams(params);
    }

    public int getBadgeGravity() {
        return ((LayoutParams) getLayoutParams()).gravity;
    }

    public void setBadgeMargin(int dipMargin) {
        setBadgeMargin(dipMargin, dipMargin, dipMargin, dipMargin);
    }

    public void setBadgeMargin(int leftDipMargin, int topDipMargin, int rightDipMargin, int
            bottomDipMargin) {
        LayoutParams params = (LayoutParams) getLayoutParams();
        params.leftMargin = dip2Px((float) leftDipMargin);
        params.topMargin = dip2Px((float) topDipMargin);
        params.rightMargin = dip2Px((float) rightDipMargin);
        params.bottomMargin = dip2Px((float) bottomDipMargin);
        setLayoutParams(params);
    }

    public int[] getBadgeMargin() {
        LayoutParams params = (LayoutParams) getLayoutParams();
        return new int[]{params.leftMargin, params.topMargin, params.rightMargin, params
                .bottomMargin};
    }

    public void incrementBadgeCount(int increment) {
        Integer count = getBadgeCount();
        if (count == null) {
            setBadgeCount(increment);
        } else {
            setBadgeCount(count.intValue() + increment);
        }
    }

    public void decrementBadgeCount(int decrement) {
        incrementBadgeCount(-decrement);
    }

    public void setTargetView(TabWidget target, int tabIndex) {
        setTargetView(target.getChildTabViewAt(tabIndex));
    }

    public void setTargetView(View target) {
        if (getParent() != null) {
            ((ViewGroup) getParent()).removeView(this);
        }
        if (target != null) {
            if (target.getParent() instanceof FrameLayout) {
                ((FrameLayout) target.getParent()).addView(this);
            } else if (target.getParent() instanceof ViewGroup) {
                ViewGroup parentContainer = (ViewGroup) target.getParent();
                int groupIndex = parentContainer.indexOfChild(target);
                parentContainer.removeView(target);
                FrameLayout badgeContainer = new FrameLayout(getContext());
                ViewGroup.LayoutParams parentlayoutParams = target.getLayoutParams();
                badgeContainer.setLayoutParams(parentlayoutParams);
                target.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
                parentContainer.addView(badgeContainer, groupIndex, parentlayoutParams);
                badgeContainer.addView(target);
                badgeContainer.addView(this);
            } else if (target.getParent() == null) {
                Log.e(getClass().getSimpleName(), "ParentView is needed");
            }
        }
    }

    private int dip2Px(float dip) {
        return (int) ((getContext().getResources().getDisplayMetrics().density * dip) + 0.5f);
    }
}
