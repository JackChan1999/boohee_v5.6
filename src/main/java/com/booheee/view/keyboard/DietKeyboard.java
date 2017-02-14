package com.booheee.view.keyboard;

import android.content.Context;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class DietKeyboard extends LinearLayout {
    private static final int    DEFAULT_GRAM_VALUE  = 100;
    private static final int    DEFAULT_OTHER_VALUE = 1;
    private static final int    MAX_GRAM            = 999;
    private static final int    MAX_OTHER           = 100;
    private static final String UNIT_GRAM_CN        = "克";
    private static final String UNIT_ML_CN          = "毫升";
    private Button               btn0;
    private Button               btn1;
    private Button               btn2;
    private Button               btn3;
    private Button               btn4;
    private Button               btn5;
    private Button               btn6;
    private Button               btn7;
    private Button               btn8;
    private Button               btn9;
    private Button               btn_back;
    private Button               btn_dot;
    private Unit                 currentUnit;
    private boolean              hasDot;
    private boolean              hasNumAfterDot;
    private StringBuffer         inputBuffer;
    private LinearLayout         ll_diet_unit;
    private float                mAmount;
    private float                mCalory;
    private float                mGram;
    private List<Unit>           mUnits;
    private float                mWeight;
    private OnDietResultListener onResultListener;
    private TextView             txt_calory;
    private TextView             txt_gram;
    private TextView             txt_unit;
    private TextView             txt_value;
    private List<TextView>       unitTextViews;

    private class NumBtnListener implements OnClickListener {
        private NumBtnListener() {
        }

        public void onClick(View v) {
            if (v.getId() == R.id.btn_dot) {
                if (!(DietKeyboard.this.hasDot || DietKeyboard.this.inputBuffer.toString().equals
                        (""))) {
                    DietKeyboard.this.inputBuffer.append(".");
                    DietKeyboard.this.hasDot = true;
                }
            } else if (v.getId() == R.id.btn_back) {
                DietKeyboard.this.doBack();
            } else {
                DietKeyboard.this.doNum(v);
            }
            if (DietKeyboard.this.currentUnit != null) {
                if (TextUtils.isEmpty(DietKeyboard.this.inputBuffer.toString())) {
                    DietKeyboard.this.mAmount = 0.0f;
                    DietKeyboard.this.mGram = DietKeyboard.this.mAmount;
                    DietKeyboard.this.mCalory = 0.0f;
                } else {
                    DietKeyboard.this.mAmount = Float.parseFloat(DietKeyboard.this.inputBuffer
                            .toString());
                    DietKeyboard.this.mGram = Float.parseFloat(DietKeyboard.this.currentUnit
                            .eat_weight) * DietKeyboard.this.mAmount;
                    DietKeyboard.this.mCalory = DietKeyboard.this.mWeight * DietKeyboard.this.mGram;
                }
                DietKeyboard.this.txt_value.setText(DietKeyboard.this.inputBuffer.toString());
                DietKeyboard.this.swithTxt();
            }
        }
    }

    private class UnitListener implements OnClickListener {
        private UnitListener() {
        }

        public void onClick(View v) {
            if (DietKeyboard.this.unitTextViews != null || DietKeyboard.this.unitTextViews.size()
                    != 0) {
                for (int i = 0; i < DietKeyboard.this.unitTextViews.size(); i++) {
                    if (v.getId() == i) {
                        ((TextView) DietKeyboard.this.unitTextViews.get(i)).setTextColor(-1);
                        ((TextView) DietKeyboard.this.unitTextViews.get(i)).setBackgroundColor
                                (DietKeyboard.this.getResources().getColor(R.color
                                        .color_bg_number));
                        DietKeyboard.this.currentUnit = (Unit) DietKeyboard.this.mUnits.get(i);
                    } else {
                        ((TextView) DietKeyboard.this.unitTextViews.get(i)).setTextColor
                                (DietKeyboard.this.getResources().getColor(R.color.color_light));
                        ((TextView) DietKeyboard.this.unitTextViews.get(i)).setBackgroundColor
                                (DietKeyboard.this.getResources().getColor(R.color.color_divider));
                    }
                }
                DietKeyboard.this.inputBuffer.delete(0, DietKeyboard.this.inputBuffer.toString()
                        .length());
                if (TextUtils.equals(DietKeyboard.this.currentUnit.unit_name, DietKeyboard
                        .UNIT_GRAM_CN)) {
                    DietKeyboard.this.mAmount = 100.0f;
                    DietKeyboard.this.mGram = 100.0f;
                    DietKeyboard.this.mCalory = DietKeyboard.this.mWeight * DietKeyboard.this.mGram;
                } else if (DietKeyboard.this.currentUnit != null) {
                    if (TextUtils.isEmpty(DietKeyboard.this.currentUnit.eat_weight)) {
                        DietKeyboard.this.currentUnit.eat_weight = "1.0";
                    }
                    DietKeyboard.this.mAmount = 1.0f;
                    DietKeyboard.this.mGram = Float.parseFloat(DietKeyboard.this.currentUnit
                            .eat_weight);
                    DietKeyboard.this.mCalory = DietKeyboard.this.mWeight * DietKeyboard.this.mGram;
                } else {
                    return;
                }
                DietKeyboard.this.txt_value.setText(DietKeyboard.this.mAmount + "");
                DietKeyboard.this.swithTxt();
            }
        }
    }

    public DietKeyboard(Context context) {
        this(context, null);
    }

    public DietKeyboard(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DietKeyboard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mUnits = new ArrayList();
        this.mAmount = -1.0f;
        this.inputBuffer = new StringBuffer();
        this.unitTextViews = new ArrayList();
        this.hasDot = false;
        this.hasNumAfterDot = false;
        findViews();
    }

    private void findViews() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_diet_keyboard, null);
        this.ll_diet_unit = (LinearLayout) view.findViewById(R.id.ll_diet_unit);
        this.btn1 = (Button) view.findViewById(R.id.btn1);
        this.btn2 = (Button) view.findViewById(R.id.btn2);
        this.btn3 = (Button) view.findViewById(R.id.btn3);
        this.btn4 = (Button) view.findViewById(R.id.btn4);
        this.btn5 = (Button) view.findViewById(R.id.btn5);
        this.btn6 = (Button) view.findViewById(R.id.btn6);
        this.btn7 = (Button) view.findViewById(R.id.btn7);
        this.btn8 = (Button) view.findViewById(R.id.btn8);
        this.btn9 = (Button) view.findViewById(R.id.btn9);
        this.btn0 = (Button) view.findViewById(R.id.btn0);
        this.btn_dot = (Button) view.findViewById(R.id.btn_dot);
        this.btn_back = (Button) view.findViewById(R.id.btn_back);
        NumBtnListener numBtnListener = new NumBtnListener();
        this.btn1.setOnClickListener(numBtnListener);
        this.btn2.setOnClickListener(numBtnListener);
        this.btn3.setOnClickListener(numBtnListener);
        this.btn4.setOnClickListener(numBtnListener);
        this.btn5.setOnClickListener(numBtnListener);
        this.btn6.setOnClickListener(numBtnListener);
        this.btn7.setOnClickListener(numBtnListener);
        this.btn8.setOnClickListener(numBtnListener);
        this.btn9.setOnClickListener(numBtnListener);
        this.btn0.setOnClickListener(numBtnListener);
        this.btn_dot.setOnClickListener(numBtnListener);
        this.btn_back.setOnClickListener(numBtnListener);
        this.txt_value = (TextView) view.findViewById(R.id.txt_value);
        this.txt_unit = (TextView) view.findViewById(R.id.txt_unit);
        this.txt_gram = (TextView) view.findViewById(R.id.txt_gram);
        this.txt_calory = (TextView) view.findViewById(R.id.txt_calory);
        addView(view);
    }

    public void init(float amount, float weight, Unit currentUnit, List<Unit> units) {
        this.mAmount = amount;
        this.mWeight = weight;
        this.currentUnit = currentUnit;
        this.mUnits = units;
        initUnit();
    }

    public void initUnit() {
        if (this.mUnits != null && this.mUnits.size() != 0 && this.currentUnit != null) {
            LayoutParams parms = new LayoutParams(-1, -2, 1.0f);
            this.ll_diet_unit.removeAllViews();
            this.unitTextViews.clear();
            for (int i = 0; i < this.mUnits.size(); i++) {
                TextView textView = new TextView(getContext());
                textView.setText(((Unit) this.mUnits.get(i)).unit_name);
                textView.setGravity(17);
                textView.setTextSize(18.0f);
                textView.setTextColor(getResources().getColor(R.color.color_light));
                textView.setBackgroundColor(getResources().getColor(R.color.color_divider));
                textView.setSingleLine(true);
                textView.setEllipsize(TruncateAt.END);
                textView.setId(i);
                textView.setOnClickListener(new UnitListener());
                if (TextUtils.equals(((Unit) this.mUnits.get(i)).unit_name, this.currentUnit
                        .unit_name)) {
                    textView.setTextColor(-1);
                    textView.setBackgroundColor(getResources().getColor(R.color.color_bg_number));
                }
                this.unitTextViews.add(textView);
                this.ll_diet_unit.addView(textView, parms);
            }
            if (this.mAmount < 0.0f) {
                this.mAmount = 100.0f;
            }
            this.mGram = Float.parseFloat(this.currentUnit.eat_weight) * this.mAmount;
            this.mCalory = this.mWeight * this.mGram;
            this.txt_value.setText(this.mAmount + "");
            swithTxt();
        }
    }

    private void swithTxt() {
        this.txt_gram.setText(String.format("%.1f 克", new Object[]{Float.valueOf(this.mGram)}));
        this.txt_calory.setText(String.format("%d 千卡", new Object[]{Integer.valueOf(Math.round
                (this.mCalory))}));
        this.txt_unit.setText(this.currentUnit.unit_name);
        this.onResultListener.onResult(this.mAmount, this.mCalory, this.currentUnit.food_unit_id,
                this.currentUnit.unit_name);
    }

    private void doNum(View v) {
        if (!TextUtils.isEmpty(this.inputBuffer.toString()) || !v.getTag().toString().equals("0")) {
            if (!this.hasDot) {
                this.inputBuffer.append(v.getTag());
            } else if (!this.hasNumAfterDot) {
                this.inputBuffer.append(v.getTag());
                this.hasNumAfterDot = true;
            }
            if (this.currentUnit != null && !TextUtils.isEmpty(this.inputBuffer)) {
                if (TextUtils.equals(this.currentUnit.unit_name, UNIT_GRAM_CN) || TextUtils
                        .equals(this.currentUnit.unit_name, UNIT_ML_CN)) {
                    if (Float.parseFloat(this.inputBuffer.toString()) > 999.0f) {
                        this.inputBuffer.replace(0, this.inputBuffer.length(), "999");
                        this.hasNumAfterDot = false;
                        this.hasDot = false;
                    }
                } else if (Float.parseFloat(this.inputBuffer.toString()) > 100.0f) {
                    this.inputBuffer.replace(0, this.inputBuffer.length(), "100");
                    this.hasNumAfterDot = false;
                    this.hasDot = false;
                }
            }
        }
    }

    private void doBack() {
        if (!TextUtils.isEmpty(this.inputBuffer)) {
            this.inputBuffer.deleteCharAt(this.inputBuffer.length() - 1);
            if (!this.inputBuffer.toString().contains(".")) {
                this.hasDot = false;
            }
            if (this.inputBuffer.length() - 1 > 0 && this.inputBuffer.charAt(this.inputBuffer.length() - 1) == '.') {
                this.hasNumAfterDot = false;
            }
        }
    }

    public void setOnResultListener(OnDietResultListener onResultListener) {
        this.onResultListener = onResultListener;
    }
}
