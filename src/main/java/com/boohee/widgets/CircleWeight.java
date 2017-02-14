package com.boohee.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.boohee.one.R;

import uk.co.senab.photoview.IPhotoView;

public class CircleWeight extends LinearLayout {
    public static final int TEXT_SIZE_2      = 22;
    public static final int TEXT_SIZE_3      = 20;
    public static final int TEXT_SIZE_4      = 18;
    public static final int TEXT_SIZE_5      = 16;
    public static final int UNIT_PADDING_TOP = 6;
    public static final int VIEW_SIZE        = 48;
    public  int      VIEW_COLOR;
    private int      mUnitPaddingTop;
    private TextView tvUnit;
    private TextView tvWeight;

    public CircleWeight(Context context) {
        this(context, null);
    }

    public CircleWeight(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleWeight(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
        initView();
    }

    private void init() {
        this.VIEW_COLOR = getResources().getColor(R.color.dn);
        float displayMetrics = getResources().getDisplayMetrics().density;
        setLayoutParams(new LayoutParams(((int) displayMetrics) * 48, ((int) displayMetrics) * 48));
        setGravity(17);
        setBackgroundResource(R.drawable.bs);
        setOrientation(1);
        this.mUnitPaddingTop = (-((int) displayMetrics)) * 6;
    }

    private void initView() {
        LayoutParams paramsWeight = new LayoutParams(-1, 0);
        paramsWeight.weight = IPhotoView.DEFAULT_MAX_SCALE;
        this.tvWeight = new TextView(getContext());
        this.tvWeight.setLayoutParams(paramsWeight);
        this.tvWeight.setGravity(81);
        this.tvWeight.setText("65");
        this.tvWeight.setTextColor(this.VIEW_COLOR);
        this.tvWeight.setTextSize(2, 22.0f);
        LayoutParams paramsUnit = new LayoutParams(-1, 0);
        paramsUnit.weight = 2.0f;
        this.tvUnit = new TextView(getContext());
        this.tvUnit.setLayoutParams(paramsUnit);
        this.tvUnit.setGravity(49);
        this.tvUnit.setPadding(0, this.mUnitPaddingTop, 0, 0);
        this.tvUnit.setText("kg");
        this.tvUnit.setTextSize(2, 16.0f);
        this.tvUnit.setTextColor(this.VIEW_COLOR);
        addView(this.tvWeight);
        addView(this.tvUnit);
    }

    public void setWeight(float weight) {
        String weightStr = "";
        if (weight == ((float) ((int) weight))) {
            weightStr = String.valueOf((int) weight);
        } else {
            weightStr = String.valueOf(weight);
        }
        switch (weightStr.length()) {
            case 2:
                this.tvWeight.setTextSize(2, 22.0f);
                break;
            case 3:
                this.tvWeight.setTextSize(2, 20.0f);
                break;
            case 4:
                this.tvWeight.setTextSize(2, 18.0f);
                break;
            case 5:
                this.tvWeight.setTextSize(2, 16.0f);
                break;
            default:
                this.tvWeight.setTextSize(2, 16.0f);
                break;
        }
        this.tvWeight.setText(weightStr);
    }
}
