package com.boohee.more;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.boohee.main.GestureActivity;
import com.boohee.model.Alarm;
import com.boohee.model.Alarm.AlarmType;
import com.boohee.modeldao.AlarmDao;
import com.boohee.one.R;
import com.boohee.utils.ViewUtils;

import java.util.ArrayList;

public class DietNoticeActivity extends GestureActivity {
    static final String TAG = DietNoticeActivity.class.getName();
    private AlarmDao         alarm_dao;
    private ArrayList<Alarm> alarms;
    private ToggleButton     tb_notice_breakfast;
    private ToggleButton     tb_notice_greedy;
    private ToggleButton     tb_notice_lunch;
    private ToggleButton     tb_notice_supper;
    private TextView         txt_notice_breakfast_time;
    private TextView         txt_notice_greedy_time;
    private TextView         txt_notice_lunch_time;
    private TextView         txt_notice_supper_time;

    class TimeOnclick implements OnClickListener {
        Alarm    alarm;
        TextView text_view;

        public TimeOnclick(TextView text_view, Alarm alarm) {
            this.text_view = text_view;
            this.alarm = alarm;
        }

        public void onClick(View v) {
            ViewUtils.showTimeDialog(DietNoticeActivity.this.ctx, this.alarm, DietNoticeActivity
                    .this.alarm_dao, this.text_view);
        }
    }

    public void onCreate(Bundle outState) {
        super.onCreate(outState);
        setTitle(R.string.j8);
        setContentView(R.layout.f3);
        initData();
        findView();
        addListener();
        init();
    }

    void initData() {
        this.alarm_dao = new AlarmDao(this.ctx);
        this.alarms = this.alarm_dao.getAlarmsByNoticeType(AlarmType.DIET.getType());
    }

    void addListener() {
        this.tb_notice_breakfast.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (DietNoticeActivity.this.alarms != null || DietNoticeActivity.this.alarms.size
                        () != 0) {
                    DietNoticeActivity.this.openOrClose((Alarm) DietNoticeActivity.this.alarms
                            .get(0), isChecked);
                }
            }
        });
        this.tb_notice_lunch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (DietNoticeActivity.this.alarms != null || DietNoticeActivity.this.alarms.size
                        () != 0) {
                    DietNoticeActivity.this.openOrClose((Alarm) DietNoticeActivity.this.alarms
                            .get(1), isChecked);
                }
            }
        });
        this.tb_notice_supper.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (DietNoticeActivity.this.alarms != null || DietNoticeActivity.this.alarms.size
                        () != 0) {
                    DietNoticeActivity.this.openOrClose((Alarm) DietNoticeActivity.this.alarms
                            .get(2), isChecked);
                }
            }
        });
        this.tb_notice_greedy.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (DietNoticeActivity.this.alarms != null || DietNoticeActivity.this.alarms.size
                        () != 0) {
                    DietNoticeActivity.this.openOrClose((Alarm) DietNoticeActivity.this.alarms
                            .get(3), isChecked);
                }
            }
        });
        this.txt_notice_breakfast_time.setOnClickListener(new TimeOnclick(this
                .txt_notice_breakfast_time, (Alarm) this.alarms.get(0)));
        this.txt_notice_lunch_time.setOnClickListener(new TimeOnclick(this.txt_notice_lunch_time,
                (Alarm) this.alarms.get(1)));
        this.txt_notice_supper_time.setOnClickListener(new TimeOnclick(this
                .txt_notice_supper_time, (Alarm) this.alarms.get(2)));
        this.txt_notice_greedy_time.setOnClickListener(new TimeOnclick(this
                .txt_notice_greedy_time, (Alarm) this.alarms.get(3)));
    }

    void init() {
        if (this.alarms != null || this.alarms.size() != 0) {
            this.txt_notice_breakfast_time.setText(((Alarm) this.alarms.get(0)).formatTime());
            this.txt_notice_lunch_time.setText(((Alarm) this.alarms.get(1)).formatTime());
            this.txt_notice_supper_time.setText(((Alarm) this.alarms.get(2)).formatTime());
            this.txt_notice_greedy_time.setText(((Alarm) this.alarms.get(3)).formatTime());
            this.tb_notice_breakfast.setChecked(((Alarm) this.alarms.get(0)).is_open());
            this.tb_notice_lunch.setChecked(((Alarm) this.alarms.get(1)).is_open());
            this.tb_notice_supper.setChecked(((Alarm) this.alarms.get(2)).is_open());
            this.tb_notice_greedy.setChecked(((Alarm) this.alarms.get(3)).is_open());
        }
    }

    void openOrClose(Alarm alarm, boolean isChecked) {
        if (isChecked) {
            alarm.enabled = 1;
        } else {
            alarm.enabled = 0;
        }
        this.alarm_dao.update(alarm);
        RemindService.start(alarm, this.ctx);
    }

    private void findView() {
        this.txt_notice_breakfast_time = (TextView) findViewById(R.id.txt_notice_breakfast_time);
        this.txt_notice_lunch_time = (TextView) findViewById(R.id.txt_notice_lunch_time);
        this.txt_notice_supper_time = (TextView) findViewById(R.id.txt_notice_supper_time);
        this.txt_notice_greedy_time = (TextView) findViewById(R.id.txt_notice_greedy_time);
        this.tb_notice_breakfast = (ToggleButton) findViewById(R.id.tb_notice_breakfast);
        this.tb_notice_lunch = (ToggleButton) findViewById(R.id.tb_notice_lunch);
        this.tb_notice_supper = (ToggleButton) findViewById(R.id.tb_notice_supper);
        this.tb_notice_greedy = (ToggleButton) findViewById(R.id.tb_notice_greedy);
    }

    protected void onDestroy() {
        super.onDestroy();
        this.alarm_dao.closeDB();
    }
}
