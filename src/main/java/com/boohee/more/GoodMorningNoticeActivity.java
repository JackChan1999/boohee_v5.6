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

public class GoodMorningNoticeActivity extends GestureActivity {
    static final String TAG = GoodMorningNoticeActivity.class.getName();
    private AlarmDao         alarm_dao;
    private ArrayList<Alarm> alarms;
    private ToggleButton     tb_notice_good_morning;
    private TextView         txt_notice_good_morning_time;

    class TimeOnclick implements OnClickListener {
        Alarm    alarm;
        TextView text_view;

        public TimeOnclick(TextView text_view, Alarm alarm) {
            this.text_view = text_view;
            this.alarm = alarm;
        }

        public void onClick(View v) {
            ViewUtils.showTimeDialog(GoodMorningNoticeActivity.this.ctx, this.alarm,
                    GoodMorningNoticeActivity.this.alarm_dao, this.text_view);
        }
    }

    public void onCreate(Bundle outState) {
        super.onCreate(outState);
        setTitle(R.string.na);
        setContentView(R.layout.h0);
        initData();
        findView();
        addListener();
        initUI();
    }

    void addListener() {
        this.txt_notice_good_morning_time.setOnClickListener(new TimeOnclick(this
                .txt_notice_good_morning_time, (Alarm) this.alarms.get(0)));
        this.tb_notice_good_morning.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (GoodMorningNoticeActivity.this.alarms != null || GoodMorningNoticeActivity
                        .this.alarms.size() != 0) {
                    GoodMorningNoticeActivity.this.openOrClose((Alarm) GoodMorningNoticeActivity
                            .this.alarms.get(0), isChecked);
                }
            }
        });
    }

    void initData() {
        this.alarm_dao = new AlarmDao(this.ctx);
        this.alarms = this.alarm_dao.getAlarmsByNoticeType(AlarmType.MORNING.getType());
    }

    private void findView() {
        this.txt_notice_good_morning_time = (TextView) findViewById(R.id
                .txt_notice_good_morning_time);
        this.tb_notice_good_morning = (ToggleButton) findViewById(R.id.tb_notice_good_morning);
    }

    void initUI() {
        if (this.alarms != null || this.alarms.size() != 0) {
            this.txt_notice_good_morning_time.setText(((Alarm) this.alarms.get(0)).formatTime());
            this.tb_notice_good_morning.setChecked(((Alarm) this.alarms.get(0)).is_open());
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

    protected void onDestroy() {
        super.onDestroy();
        this.alarm_dao.closeDB();
    }
}
