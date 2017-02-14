package com.boohee.myview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.boohee.one.R;
import com.boohee.utils.DateHelper;

import java.util.Date;

public class TopMonthView extends FrameLayout implements OnClickListener {
    static final String TAG = TopMonthView.class.getName();
    private Context ctx;
    private Date date = new Date();
    private TextView dateString;
    OnDateChangeListener listener;
    private Button nextBtn;
    private Button previousBtn;
    private int sinceNow = 0;

    public interface OnDateChangeListener {
        void onMonthAdd();

        void onMonthReduce();
    }

    public TopMonthView(Context context) {
        super(context);
        initUI();
    }

    public TopMonthView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initUI();
    }

    public TopMonthView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initUI();
    }

    private void initUI() {
        this.ctx = getContext();
        addView(LayoutInflater.from(this.ctx).inflate(R.layout.n9, null));
        findView();
    }

    private void findView() {
        this.previousBtn = (Button) findViewById(R.id.previous_btn);
        this.previousBtn.setOnClickListener(this);
        this.nextBtn = (Button) findViewById(R.id.next_btn);
        this.nextBtn.setOnClickListener(this);
        this.dateString = (TextView) findViewById(R.id.date_string);
        this.dateString.setText(DateHelper.format(this.date, this.ctx.getString(R.string.a_d)));
    }

    public Date getDate() {
        return this.date;
    }

    public void setDateChangeListener(OnDateChangeListener listener) {
        this.listener = listener;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.previous_btn:
                this.sinceNow--;
                this.date = DateHelper.addMonths(new Date(), this.sinceNow);
                this.dateString.setText(DateHelper.format(this.date, this.ctx.getString(R.string
                        .a_d)));
                if (this.listener != null) {
                    this.listener.onMonthReduce();
                    return;
                }
                return;
            case R.id.next_btn:
                this.sinceNow++;
                this.date = DateHelper.addMonths(new Date(), this.sinceNow);
                this.dateString.setText(DateHelper.format(this.date, this.ctx.getString(R.string
                        .a_d)));
                if (this.listener != null) {
                    this.listener.onMonthAdd();
                    return;
                }
                return;
            default:
                return;
        }
    }
}
