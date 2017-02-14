package com.boohee.myview.homeMenu;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.boohee.utility.DensityUtil;

public class PopSubView extends LinearLayout {
    private static final float factor = 1.2f;
    private ImageView icon;
    private TextView  textView;

    public PopSubView(Context context) {
        this(context, null);
    }

    public PopSubView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PopSubView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setGravity(17);
        setOrientation(1);
        this.icon = new ImageView(context);
        this.icon.setScaleType(ScaleType.CENTER_INSIDE);
        addView(this.icon, new LayoutParams(-2, -2));
        this.textView = new TextView(context);
        LayoutParams tvLp = new LayoutParams(-1, -2);
        this.textView.setMaxLines(1);
        this.textView.setTextSize(12.0f);
        this.textView.setGravity(17);
        tvLp.topMargin = DensityUtil.dip2px(context, 8.0f);
        addView(this.textView, tvLp);
        setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case 0:
                        PopSubView.this.scaleViewAnimation(PopSubView.this, PopSubView.factor);
                        break;
                    case 1:
                        PopSubView.this.scaleViewAnimation(PopSubView.this, 1.0f);
                        break;
                }
                return false;
            }
        });
    }

    public void setPopMenuItem(PopMenuItem popMenuItem) {
        if (popMenuItem != null) {
            this.icon.setImageDrawable(popMenuItem.getDrawable());
            this.textView.setText(popMenuItem.getTitle());
        }
    }

    private void scaleViewAnimation(View view, float value) {
        view.animate().scaleX(value).scaleY(value).setDuration(80).start();
    }
}
