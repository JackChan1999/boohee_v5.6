package com.boohee.myview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.boohee.one.R;
import com.boohee.utils.DateHelper;
import com.boohee.utils.Helper;

import java.util.Date;

public class TopDateView extends FrameLayout implements OnClickListener {
    static final String TAG = TopDateView.class.getName();
    private Context ctx;
    private Date date = new Date();
    private TextView dateString;
    OnDateChangeListener listener;
    private ImageView nextBtn;
    private ImageView previousBtn;
    private int sinceNow = 0;

    public interface OnDateChangeListener {
        void onDateChange();
    }

    public TopDateView(Context context) {
        super(context);
        initUI();
    }

    public TopDateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initUI();
    }

    public TopDateView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initUI();
    }

    private void initUI() {
        this.ctx = getContext();
        addView(LayoutInflater.from(this.ctx).inflate(R.layout.n8, null));
        findView();
    }

    private void findView() {
        this.previousBtn = (ImageView) findViewById(R.id.previous_btn);
        this.previousBtn.setOnClickListener(this);
        this.nextBtn = (ImageView) findViewById(R.id.next_btn);
        this.nextBtn.setOnClickListener(this);
        this.dateString = (TextView) findViewById(R.id.date_string);
        this.dateString.setText(R.string.a9h);
    }

    public Date getDate() {
        return this.date;
    }

    public void setDate(Date date) {
        this.date = date;
        this.sinceNow = (int) (((((date.getTime() - System.currentTimeMillis()) / 1000) / 60) /
                60) / 24);
        if (this.sinceNow == 0) {
            this.dateString.setText(R.string.a9h);
        } else {
            this.dateString.setText(DateHelper.monthDay(date));
        }
    }

    public void setDateChangeListener(OnDateChangeListener listener) {
        this.listener = listener;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.previous_btn:
                this.sinceNow--;
                break;
            case R.id.next_btn:
                this.sinceNow++;
                break;
        }
        this.date = DateHelper.addDays(new Date(), this.sinceNow);
        if (this.sinceNow == 0) {
            this.dateString.setText(R.string.a9h);
        } else {
            this.dateString.setText(DateHelper.monthDay(this.date));
        }
        Helper.showLog(TAG, "sicneNow" + this.sinceNow + ":" + DateHelper.monthDay(this.date));
        if (this.listener != null) {
            this.listener.onDateChange();
        }
    }
}
