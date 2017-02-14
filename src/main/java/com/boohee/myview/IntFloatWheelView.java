package com.boohee.myview;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.boohee.one.R;
import com.boohee.utils.Helper;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.NumericWheelAdapter;

public class IntFloatWheelView extends FrameLayout {
    public static final float  DEFAULT_VALUE = 50.0f;
    public static final int    MAX_VALUE     = 180;
    public static final int    MIN_VALUE     = 15;
    static final        String TAG           = IntFloatWheelView.class.getName();
    private Context           ctx;
    private float             defaultValue;
    private WheelView         floatWheel;
    private WheelView         intWheel;
    private int               maxValue;
    private int               minValue;
    private OnGetCurrentValue onGetCurrentValue;

    public interface OnGetCurrentValue {
        void onGetValue(float f);
    }

    public void setOnGetCurrentValue(final OnGetCurrentValue onGetCurrentValue) {
        this.onGetCurrentValue = onGetCurrentValue;
        OnWheelChangedListener changedListener = new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                if (onGetCurrentValue != null) {
                    onGetCurrentValue.onGetValue(IntFloatWheelView.this.getCurrentFloat());
                }
            }
        };
        this.intWheel.addChangingListener(changedListener);
        this.floatWheel.addChangingListener(changedListener);
    }

    public IntFloatWheelView(Context context) {
        this(context, 180, 15, DEFAULT_VALUE);
    }

    public IntFloatWheelView(Context context, float defaultValue) {
        this(context, 180, 15, defaultValue);
    }

    public IntFloatWheelView(Context context, int maxValue, int minValue) {
        this(context, maxValue, minValue, DEFAULT_VALUE);
    }

    public IntFloatWheelView(Context context, int maxValue, int minValue, float defaultValue) {
        super(context);
        this.maxValue = maxValue;
        this.minValue = minValue;
        if (defaultValue > 180.0f || defaultValue < 15.0f) {
            this.defaultValue = DEFAULT_VALUE;
        } else {
            this.defaultValue = defaultValue;
        }
        initUI();
    }

    private void initUI() {
        this.ctx = getContext();
        addView(LayoutInflater.from(this.ctx).inflate(R.layout.mz, null));
        findView();
    }

    private int getIntIndex() {
        return ((int) this.defaultValue) - this.minValue;
    }

    private int getFloatIndex() {
        return ((int) (this.defaultValue * 10.0f)) % 10;
    }

    private void findView() {
        this.intWheel = (WheelView) findViewById(R.id.hour);
        this.intWheel.setViewAdapter(new NumericWheelAdapter(this.ctx, this.minValue, this
                .maxValue));
        this.intWheel.setCurrentItem(getIntIndex());
        this.floatWheel = (WheelView) findViewById(R.id.mins);
        this.floatWheel.setViewAdapter(new ZeroFloatAdapter(this.ctx));
        this.floatWheel.setCyclic(true);
        this.floatWheel.setCurrentItem(getFloatIndex());
    }

    public int getIntNum() {
        return this.intWheel.getCurrentItem() + this.minValue;
    }

    public float getFloatNum() {
        return (float) (((double) this.floatWheel.getCurrentItem()) / 10.0d);
    }

    public float getCurrentFloat() {
        Helper.showLog(TAG, (((float) getIntNum()) + getFloatNum()) + "");
        return ((float) getIntNum()) + getFloatNum();
    }
}
