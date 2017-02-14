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

public class SportNoticeActivity extends GestureActivity {
    static final String TAG = SportNoticeActivity.class.getName();
    private AlarmDao         alarm_dao;
    private ArrayList<Alarm> alarms;
    private ToggleButton     tb_notice_sport;
    private TextView         txt_notice_sport_time;

    class TimeOnclick implements OnClickListener {
        Alarm    alarm;
        TextView text_view;

        public TimeOnclick(TextView text_view, Alarm alarm) {
            this.text_view = text_view;
            this.alarm = alarm;
        }

        public void onClick(View v) {
            ViewUtils.showTimeDialog(SportNoticeActivity.this.ctx, this.alarm,
                    SportNoticeActivity.this.alarm_dao, this.text_view);
        }
    }

    public void onCreate(Bundle outState) {
        super.onCreate(outState);
        setTitle(R.string.a67);
        setContentView(R.layout.ma);
        initData();
        findView();
        addListener();
        initUI();
    }

    void addListener() {
        if (this.alarms != null || this.alarms.size() != 0) {
            this.txt_notice_sport_time.setOnClickListener(new TimeOnclick(this
                    .txt_notice_sport_time, (Alarm) this.alarms.get(0)));
            this.tb_notice_sport.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (SportNoticeActivity.this.alarms != null || SportNoticeActivity.this
                            .alarms.size() != 0) {
                        SportNoticeActivity.this.openOrClose((Alarm) SportNoticeActivity.this
                                .alarms.get(0), isChecked);
                    }
                }
            });
        }
    }

    void initData() {
        this.alarm_dao = new AlarmDao(this.ctx);
        this.alarms = this.alarm_dao.getAlarmsByNoticeType(AlarmType.SPORT.getType());
    }

    private void findView() {
        this.txt_notice_sport_time = (TextView) findViewById(R.id.txt_notice_sport_time);
        this.tb_notice_sport = (ToggleButton) findViewById(R.id.tb_notice_sport);
    }

    void initUI() {
        if (this.alarms != null || this.alarms.size() != 0) {
            this.txt_notice_sport_time.setText(((Alarm) this.alarms.get(0)).formatTime());
            this.tb_notice_sport.setChecked(((Alarm) this.alarms.get(0)).is_open());
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
