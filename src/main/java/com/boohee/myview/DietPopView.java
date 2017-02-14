package com.boohee.myview;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.boohee.api.RecordApi;
import com.boohee.calendar.DietCalendarAdapter;
import com.boohee.model.mine.DietRecord;
import com.boohee.one.R;
import com.boohee.one.http.JsonCallback;
import com.boohee.utils.DateFormatUtils;
import com.boohee.utils.FastJsonUtils;
import com.boohee.utils.Helper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;

public class DietPopView extends LinearLayout {
    private static final String TAG = DietPopView.class.getSimpleName();
    private DietCalendarAdapter adapter;
    @InjectView(2131429277)
    View bottomShadow;
    @InjectView(2131429276)
    public Button bt_today;
    @InjectView(2131427628)
    GridView calendarGrid;
    private String              chooseDate;
    private Context             context;
    public  OnDateClickListener dateClickListener;
    @InjectView(2131427627)
    ViewFlipper flipper;
    OnItemClickListener itemClickListener;
    private List<DietRecord> mRecords;
    private String           record_on;
    @InjectView(2131427614)
    TextView tvDate;
    private View view;

    public interface OnDateClickListener {
        void onBottomClick();

        void onDateClick(Date date);
    }

    public DietPopView(Context context) {
        this(context, null);
    }

    public DietPopView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DietPopView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mRecords = new ArrayList();
        this.itemClickListener = new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, final int position,
                                    long id) {
                if (position >= DietPopView.this.adapter.startPosition() && position <
                        DietPopView.this.adapter.endPosition()) {
                    if (DietPopView.this.dateClickListener != null) {
                        DietPopView.this.postDelayed(new Runnable() {
                            public void run() {
                                DietPopView.this.dateClickListener.onDateClick(DietPopView.this
                                        .adapter.getDate(position));
                            }
                        }, 500);
                    }
                    DietPopView.this.dateClickListener.onBottomClick();
                    DietPopView.this.dismiss();
                }
            }
        };
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.op, this);
        ButterKnife.inject((View) this);
        initListener();
    }

    public void init(String record_on) {
        this.record_on = record_on;
        this.chooseDate = record_on;
        initDate();
        getDietRecord();
    }

    private void initDate() {
        this.record_on = DateFormatUtils.date2string(DateFormatUtils.string2date(this.record_on,
                "yyyy-MM-dd"), "yyyyMM");
        this.tvDate.setText(DateFormatUtils.date2string(DateFormatUtils.string2date(this
                .record_on, "yyyyMM"), "yyyy年M月"));
    }

    private void initListener() {
        this.calendarGrid.setOnItemClickListener(this.itemClickListener);
        this.bottomShadow.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                DietPopView.this.dismiss();
                if (DietPopView.this.dateClickListener != null) {
                    DietPopView.this.dateClickListener.onBottomClick();
                }
            }
        });
    }

    private void getDietRecord() {
        RecordApi.getCanRecordsDates(this.context, DateFormatUtils.getNextMonthFirstDay(this
                .record_on), new JsonCallback(this.context) {
            public void ok(JSONObject object) {
                super.ok(object);
                Helper.showLog(DietPopView.TAG, DateFormatUtils.getNextMonthFirstDay(DietPopView
                        .this.record_on));
                DietPopView.this.mRecords.clear();
                List<DietRecord> dietRecords = FastJsonUtils.parseList(object.optString("data"),
                        DietRecord.class);
                if (dietRecords != null && dietRecords.size() > 0) {
                    DietPopView.this.mRecords.addAll(dietRecords);
                    Collections.reverse(DietPopView.this.mRecords);
                }
                if (!TextUtils.isEmpty(DietPopView.this.record_on)) {
                    DietPopView.this.adapter = new DietCalendarAdapter(DietPopView.this.context,
                            DateFormatUtils.string2date(DietPopView.this.record_on, "yyyyMM"),
                            DietPopView.this.mRecords, DateFormatUtils.string2date(DietPopView
                            .this.chooseDate, "yyyy-MM-dd"));
                    DietPopView.this.calendarGrid.setAdapter(DietPopView.this.adapter);
                }
            }

            public void onFinish() {
                super.onFinish();
            }
        });
    }

    @OnClick({2131429256, 2131429257})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_left:
                this.record_on = DateFormatUtils.date2string(DateFormatUtils.getYM(this
                        .record_on, -1), "yyyyMM");
                this.tvDate.setText(DateFormatUtils.date2string(DateFormatUtils.string2date(this
                        .record_on, "yyyyMM"), "yyyy年M月"));
                this.flipper.showPrevious();
                break;
            case R.id.rl_right:
                this.record_on = DateFormatUtils.date2string(DateFormatUtils.getYM(this
                        .record_on, 1), "yyyyMM");
                this.tvDate.setText(DateFormatUtils.date2string(DateFormatUtils.string2date(this
                        .record_on, "yyyyMM"), "yyyy年M月"));
                this.flipper.showNext();
                break;
        }
        getDietRecord();
    }

    public void show() {
        if (this.context != null && !isShowing()) {
            setVisibility(0);
            ObjectAnimator animator = ObjectAnimator.ofFloat(this, "translationY", new float[]{
                    (float) (-this.context.getResources().getDisplayMetrics().widthPixels), 0.0f});
            animator.setDuration(500);
            animator.addListener(new AnimatorListener() {
                public void onAnimationStart(Animator animation) {
                    DietPopView.this.bottomShadow.setBackgroundColor(DietPopView.this
                            .getResources().getColor(R.color.in));
                }

                public void onAnimationEnd(Animator animation) {
                    DietPopView.this.bottomShadow.setBackgroundColor(DietPopView.this
                            .getResources().getColor(R.color.ip));
                }

                public void onAnimationCancel(Animator animation) {
                }

                public void onAnimationRepeat(Animator animation) {
                }
            });
            animator.start();
        }
    }

    public boolean isShowing() {
        return getVisibility() == 0;
    }

    public void dismiss() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(this, "translationY", new float[]{0.0f,
                (float) (-this.context.getResources().getDisplayMetrics().widthPixels)});
        animator.setDuration(500);
        animator.addListener(new AnimatorListener() {
            public void onAnimationStart(Animator animation) {
                DietPopView.this.bottomShadow.setBackgroundColor(DietPopView.this.getResources()
                        .getColor(R.color.in));
            }

            public void onAnimationEnd(Animator animation) {
                DietPopView.this.bottomShadow.setBackgroundColor(DietPopView.this.getResources()
                        .getColor(R.color.in));
                DietPopView.this.setVisibility(8);
            }

            public void onAnimationCancel(Animator animation) {
            }

            public void onAnimationRepeat(Animator animation) {
            }
        });
        animator.start();
    }

    public void setOnDateClickListener(OnDateClickListener dateClickListener) {
        this.dateClickListener = dateClickListener;
    }
}
