package com.boohee.record;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.boohee.one.R;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;
import kankan.wheel.widget.adapters.NumericWheelAdapter;

public class SportUnitPicker extends FrameLayout {
    static final String TAG = SportUnitPicker.class.getSimpleName();
    private Context context;
    private int default_index = 44;
    private WheelView num_picker_wheel;

    public SportUnitPicker(Context context) {
        super(context);
        initUI();
    }

    public SportUnitPicker(Context context, int index) {
        super(context);
        this.default_index = index;
        initUI();
    }

    public SportUnitPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        initUI();
    }

    public SportUnitPicker(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initUI();
    }

    private void initUI() {
        this.context = getContext();
        View view = LayoutInflater.from(this.context).inflate(R.layout.mb, null);
        this.num_picker_wheel = (WheelView) view.findViewById(R.id.num_picker);
        addView(view);
        initNumPicker();
    }

    private void initNumPicker() {
        this.num_picker_wheel.setViewAdapter(new NumericWheelAdapter(this.context, 1, 120));
        this.num_picker_wheel.setCurrentItem(this.default_index);
        ((WheelView) findViewById(R.id.unit_picker)).setViewAdapter(new ArrayWheelAdapter(this
                .context, new String[]{this.context.getString(R.string.u1)}));
    }

    public int getNum() {
        return this.num_picker_wheel.getCurrentItem() + 1;
    }

    public String getNumString() {
        return (this.num_picker_wheel.getCurrentItem() + 1) + this.context.getString(R.string.u1);
    }

    public void setScrollListener(final PickerScrollListener listener) {
        this.num_picker_wheel.addChangingListener(new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                listener.onScroll();
            }
        });
    }
}
