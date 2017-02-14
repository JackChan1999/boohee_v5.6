package com.boohee.myview;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.boohee.one.R;
import com.boohee.record.PickerScrollListener;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.NumericWheelAdapter;

public class IntWheelView extends FrameLayout {
    public static final int    DEFAULT_VALUE = 160;
    public static final int    MAX_VALUE     = 200;
    public static final int    MIN_VALUE     = 140;
    static final        String TAG           = IntWheelView.class.getName();
    private Context   ctx;
    private int       defaultValue;
    private WheelView intWheel;
    private int       maxValue;
    private int       minValue;

    public IntWheelView(Context context) {
        this(context, 200, 140, 160);
    }

    public IntWheelView(Context context, int defaultValue) {
        this(context, 200, 140, defaultValue);
    }

    public IntWheelView(Context context, int maxValue, int minValue) {
        this(context, maxValue, minValue, 160);
    }

    public IntWheelView(Context context, int maxValue, int minValue, int defaultValue) {
        super(context);
        this.maxValue = maxValue;
        this.minValue = minValue;
        this.defaultValue = defaultValue;
        initUI();
    }

    private void initUI() {
        this.ctx = getContext();
        addView(LayoutInflater.from(this.ctx).inflate(R.layout.hc, null));
        findView();
    }

    private void findView() {
        this.intWheel = (WheelView) findViewById(R.id.hour);
        this.intWheel.setViewAdapter(new NumericWheelAdapter(this.ctx, this.minValue, this
                .maxValue));
        this.intWheel.setCurrentItem(this.defaultValue - this.minValue);
    }

    public int getIntNum() {
        return this.intWheel.getCurrentItem() + 140;
    }

    public int getCurrentNum() {
        return this.intWheel.getCurrentItem() + this.minValue;
    }

    public void setPickNumListener(final PickerScrollListener listener) {
        this.intWheel.addChangingListener(new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                listener.onScroll();
            }
        });
    }

    public void addChangingListener(OnWheelChangedListener listener) {
        this.intWheel.addChangingListener(listener);
    }

    public void removeChangingListener(OnWheelChangedListener listener) {
        this.intWheel.removeChangingListener(listener);
    }
}
