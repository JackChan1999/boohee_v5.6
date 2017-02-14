package com.boohee.uchoice;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.boohee.one.R;
import com.boohee.record.PickerScrollListener;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;

public class AreaPicker extends FrameLayout {
    static final String TAG = AreaPicker.class.getSimpleName();
    public  String[]  addressString;
    private WheelView areaPicker;
    private Context   context;
    private int       defaultItem;

    public AreaPicker(Context context) {
        super(context);
        initUI();
    }

    public AreaPicker(Context context, String[] addresssString, int defaultItem) {
        super(context);
        this.addressString = addresssString;
        this.defaultItem = defaultItem;
        initUI();
    }

    public AreaPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        initUI();
    }

    public AreaPicker(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initUI();
    }

    private void initUI() {
        this.context = getContext();
        View view = LayoutInflater.from(this.context).inflate(R.layout.nd, null);
        this.areaPicker = (WheelView) view.findViewById(R.id.address_picker);
        addView(view);
        initPicker();
    }

    private void initPicker() {
        this.areaPicker.setViewAdapter(new ArrayWheelAdapter(this.context, this.addressString));
        this.areaPicker.setCurrentItem(this.defaultItem);
    }

    public String getAddress() {
        return this.addressString[this.areaPicker.getCurrentItem()];
    }

    public int getId() {
        return this.areaPicker.getCurrentItem();
    }

    public void setAddressListener(final PickerScrollListener listener) {
        this.areaPicker.addChangingListener(new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                listener.onScroll();
            }
        });
    }
}
