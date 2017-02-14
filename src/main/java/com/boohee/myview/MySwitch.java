package com.boohee.myview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.boohee.one.R;

public class MySwitch extends FrameLayout {
    static final String TAG = MySwitch.class.getSimpleName();
    private Context   ctx;
    private ImageView off_btn;
    private ImageView on_btn;

    public interface SwitchListener {
        void off();

        void on();
    }

    public MySwitch(Context context) {
        super(context);
        this.ctx = context;
        initUI();
    }

    public MySwitch(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.ctx = context;
        initUI();
    }

    public MySwitch(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.ctx = context;
        initUI();
    }

    private void initUI() {
        View view = LayoutInflater.from(this.ctx).inflate(R.layout.lb, null);
        addView(view);
        this.on_btn = (ImageView) view.findViewById(R.id.on_btn);
        this.off_btn = (ImageView) view.findViewById(R.id.off_btn);
    }

    public void setOnOff(int enabled) {
        if (enabled == 1) {
            this.on_btn.setVisibility(0);
            this.off_btn.setVisibility(8);
            return;
        }
        this.on_btn.setVisibility(8);
        this.off_btn.setVisibility(0);
    }

    public void setListener(final SwitchListener listener) {
        this.on_btn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                MySwitch.this.on_btn.setVisibility(8);
                MySwitch.this.off_btn.setVisibility(0);
                listener.off();
            }
        });
        this.off_btn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                MySwitch.this.on_btn.setVisibility(0);
                MySwitch.this.off_btn.setVisibility(8);
                listener.on();
            }
        });
    }
}
