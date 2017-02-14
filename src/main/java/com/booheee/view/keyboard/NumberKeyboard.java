package com.booheee.view.keyboard;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class NumberKeyboard extends FrameLayout {
    private Button           btn0;
    private Button           btn1;
    private Button           btn2;
    private Button           btn3;
    private Button           btn4;
    private Button           btn5;
    private Button           btn6;
    private Button           btn7;
    private Button           btn8;
    private Button           btn9;
    private Button           btn_back;
    private Button           btn_dot;
    private String           currentUnit;
    private boolean          hasDot;
    private boolean          hasNumAfterDot;
    private StringBuffer     inputBuffer;
    private float            mValue;
    private float            maxValue;
    private float            minValue;
    private OnResultListener onResultListener;
    private TextView         txt_calory;
    private TextView         txt_gram;
    private TextView         txt_unit;
    private TextView         txt_value;

    private class NumBtnListener implements OnClickListener {
        private NumBtnListener() {
        }

        public void onClick(View v) {
            if (v.getId() == R.id.btn_dot) {
                if (!(NumberKeyboard.this.hasDot || NumberKeyboard.this.inputBuffer.toString()
                        .equals(""))) {
                    NumberKeyboard.this.inputBuffer.append(".");
                    NumberKeyboard.this.hasDot = true;
                }
            } else if (v.getId() == R.id.btn_back) {
                NumberKeyboard.this.doBack();
            } else {
                NumberKeyboard.this.doNum(v);
            }
            NumberKeyboard.this.txt_value.setText(NumberKeyboard.this.inputBuffer.toString());
            NumberKeyboard.this.onResultListener.onValue(NumberKeyboard.this.inputBuffer.toString
                    ());
        }
    }

    public NumberKeyboard(Context context, float value, String unit, float min, float max) {
        super(context);
        this.inputBuffer = new StringBuffer();
        this.hasDot = false;
        this.hasNumAfterDot = false;
        this.mValue = value;
        this.minValue = min;
        this.maxValue = max;
        this.currentUnit = unit;
        findViews();
    }

    public NumberKeyboard(Context context) {
        this(context, null);
    }

    public NumberKeyboard(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NumberKeyboard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.inputBuffer = new StringBuffer();
        this.hasDot = false;
        this.hasNumAfterDot = false;
        findViews();
    }

    private void findViews() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_number_keyboard, null);
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
        this.txt_calory = (TextView) view.findViewById(R.id.txt_calory);
        this.txt_value = (TextView) view.findViewById(R.id.txt_value);
        this.txt_unit = (TextView) view.findViewById(R.id.txt_unit);
        this.txt_gram = (TextView) view.findViewById(R.id.txt_gram);
        this.txt_gram.setVisibility(8);
        this.txt_calory.setVisibility(8);
        addView(view, new LayoutParams(-1, -2));
        if (this.mValue < 0.0f) {
            this.mValue = 0.0f;
        }
        this.txt_value.setText(((int) this.mValue) + "");
        this.txt_unit.setText(this.currentUnit);
    }

    public void init(float value, String unit, float min, float max) {
        this.mValue = value;
        this.currentUnit = unit;
        this.minValue = min;
        this.maxValue = max;
        if (this.mValue < 0.0f) {
            this.mValue = 0.0f;
        }
        this.txt_value.setText(((int) this.mValue) + "");
        this.txt_unit.setText(this.currentUnit);
    }

    private void doNum(View v) {
        if (!this.hasDot) {
            this.inputBuffer.append(v.getTag());
        } else if (!this.hasNumAfterDot) {
            this.inputBuffer.append(v.getTag());
            this.hasNumAfterDot = true;
        }
        if (Float.parseFloat(this.inputBuffer.toString()) > this.maxValue) {
            this.inputBuffer.replace(0, this.inputBuffer.length(), ((int) this.maxValue) + "");
            this.hasNumAfterDot = false;
            this.hasDot = false;
        }
    }

    private void doBack() {
        if (!TextUtils.isEmpty(this.inputBuffer)) {
            this.inputBuffer.deleteCharAt(this.inputBuffer.length() - 1);
            if (!this.inputBuffer.toString().contains(".")) {
                this.hasDot = false;
            }
            if (this.inputBuffer.length() - 1 > 0 && this.inputBuffer.charAt(this.inputBuffer
                    .length() - 1) == '.') {
                this.hasNumAfterDot = false;
            }
        }
    }

    public void setOnResultListener(OnResultListener onResultListener) {
        this.onResultListener = onResultListener;
    }
}
