package com.boohee.myview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boohee.model.LocalCalorieDistribution;
import com.boohee.model.User;
import com.boohee.one.R;

import java.util.ArrayList;
import java.util.List;

public class DietRecordBar extends FrameLayout {
    private final int          COUNT;
    private final String[]     TITLE;
    private       List<View>   barView;
    private       LinearLayout llGroup;
    private       float[]      percent;

    public DietRecordBar(Context context) {
        super(context);
        this.barView = new ArrayList();
        this.COUNT = 5;
        this.TITLE = new String[]{"早", "午", "晚", "加", "运动"};
        this.percent = new float[]{0.0f, 0.0f, 0.0f, 0.0f, 0.0f};
        init(context);
    }

    public DietRecordBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.barView = new ArrayList();
        this.COUNT = 5;
        this.TITLE = new String[]{"早", "午", "晚", "加", "运动"};
        this.percent = new float[]{0.0f, 0.0f, 0.0f, 0.0f, 0.0f};
        init(context);
    }

    public DietRecordBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.barView = new ArrayList();
        this.COUNT = 5;
        this.TITLE = new String[]{"早", "午", "晚", "加", "运动"};
        this.percent = new float[]{0.0f, 0.0f, 0.0f, 0.0f, 0.0f};
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.jp, this, true);
        this.llGroup = (LinearLayout) findViewById(R.id.ll);
        for (int i = 0; i < 5; i++) {
            View view = LayoutInflater.from(context).inflate(R.layout.im, this.llGroup, false);
            this.barView.add(view);
            this.llGroup.addView(view);
        }
        refreshView();
    }

    public void setPercent(float[] percent) {
        if (percent != null && percent.length == 5) {
            this.percent = percent;
            refreshView();
        }
    }

    public void setData(LocalCalorieDistribution distribution, User user) {
        if (distribution != null && user != null) {
            float target = (float) user.target_calory;
            if (target > 0.0f) {
                this.percent = new float[5];
                this.percent[0] = distribution.breakfastCalory / ((target * 0.3f) * 1.1f);
                this.percent[1] = distribution.lunchCalory / ((0.4f * target) * 1.1f);
                this.percent[2] = distribution.dinnerCalory / ((target * 0.3f) * 1.1f);
                this.percent[3] = distribution.snackCalory / (0.1f * target);
                this.percent[4] = distribution.sportCalory / 400.0f;
                refreshView();
            }
        }
    }

    public void showEmpty() {
        this.percent = new float[5];
        this.percent[0] = 0.0f;
        this.percent[1] = 0.0f;
        this.percent[2] = 0.0f;
        this.percent[3] = 0.0f;
        this.percent[4] = 0.0f;
        refreshView();
    }

    private void refreshView() {
        int i = 0;
        while (i < this.barView.size()) {
            TextView tv = (TextView) ((View) this.barView.get(i)).findViewById(R.id.tv_title);
            if (tv != null) {
                tv.setText(this.TITLE[i]);
            }
            ProgressLine line = (ProgressLine) ((View) this.barView.get(i)).findViewById(R.id.line);
            if (line != null) {
                if (this.percent[i] <= 1.0f || i == 4) {
                    line.setProgressColor(getResources().getColor(R.color.hb));
                } else {
                    line.setProgressColor(getResources().getColor(R.color.jr));
                }
                line.setPercent(this.percent[i]);
            }
            i++;
        }
    }
}
