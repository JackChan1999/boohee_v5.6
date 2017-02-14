package com.boohee.myview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.boohee.one.R;

public class NiceCheckBox extends LinearLayout {
    private Context context;
    @InjectView(2131429387)
    ToggleButton tbNo;
    @InjectView(2131429384)
    ToggleButton tbYes;

    public NiceCheckBox(Context context) {
        this(context, null);
    }

    public NiceCheckBox(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NiceCheckBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.py, this);
        ButterKnife.inject((View) this);
    }

    @OnClick({2131429383, 2131429386})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_yes:
                this.tbYes.setChecked(true);
                this.tbNo.setChecked(false);
                return;
            case R.id.ll_no:
                this.tbNo.setChecked(true);
                this.tbYes.setChecked(false);
                return;
            default:
                return;
        }
    }

    public void setChecked(boolean checked) {
        if (checked) {
            this.tbYes.setChecked(true);
            this.tbNo.setChecked(false);
            return;
        }
        this.tbNo.setChecked(true);
        this.tbYes.setChecked(false);
    }

    public boolean isChecked() {
        return this.tbYes.isChecked();
    }

    public void clearCheck() {
        this.tbYes.setChecked(false);
        this.tbNo.setChecked(false);
    }

    public boolean isNotCheck() {
        return (this.tbYes.isChecked() || this.tbNo.isChecked()) ? false : true;
    }
}
