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

public class WaterNoticeActivity extends GestureActivity {
    static final String TAG = WaterNoticeActivity.class.getName();
    private AlarmDao         alarm_dao;
    private ArrayList<Alarm> alarms;
    private ToggleButton     tb_notice_water_eight;
    private ToggleButton     tb_notice_water_five;
    private ToggleButton     tb_notice_water_four;
    private ToggleButton     tb_notice_water_one;
    private ToggleButton     tb_notice_water_seven;
    private ToggleButton     tb_notice_water_six;
    private ToggleButton     tb_notice_water_three;
    private ToggleButton     tb_notice_water_two;
    private TextView         txt_notice_water_time_eight;
    private TextView         txt_notice_water_time_five;
    private TextView         txt_notice_water_time_four;
    private TextView         txt_notice_water_time_one;
    private TextView         txt_notice_water_time_seven;
    private TextView         txt_notice_water_time_six;
    private TextView         txt_notice_water_time_three;
    private TextView         txt_notice_water_time_two;

    class TimeOnclick implements OnClickListener {
        Alarm    alarm;
        TextView text_view;

        public TimeOnclick(TextView text_view, Alarm alarm) {
            this.text_view = text_view;
            this.alarm = alarm;
        }

        public void onClick(View v) {
            ViewUtils.showTimeDialog(WaterNoticeActivity.this.ctx, this.alarm,
                    WaterNoticeActivity.this.alarm_dao, this.text_view);
        }
    }

    public void onCreate(Bundle outState) {
        super.onCreate(outState);
        setTitle(R.string.acy);
        setContentView(R.layout.ra);
        initData();
        findView();
        addListener();
        initUI();
    }

    private void findView() {
        this.txt_notice_water_time_one = (TextView) findViewById(R.id.txt_notice_water_time_one);
        this.txt_notice_water_time_two = (TextView) findViewById(R.id.txt_notice_water_time_two);
        this.txt_notice_water_time_three = (TextView) findViewById(R.id
                .txt_notice_water_time_three);
        this.txt_notice_water_time_four = (TextView) findViewById(R.id.txt_notice_water_time_four);
        this.txt_notice_water_time_five = (TextView) findViewById(R.id.txt_notice_water_time_five);
        this.txt_notice_water_time_six = (TextView) findViewById(R.id.txt_notice_water_time_six);
        this.txt_notice_water_time_seven = (TextView) findViewById(R.id
                .txt_notice_water_time_seven);
        this.txt_notice_water_time_eight = (TextView) findViewById(R.id
                .txt_notice_water_time_eight);
        this.tb_notice_water_one = (ToggleButton) findViewById(R.id.tb_notice_water_one);
        this.tb_notice_water_two = (ToggleButton) findViewById(R.id.tb_notice_water_two);
        this.tb_notice_water_three = (ToggleButton) findViewById(R.id.tb_notice_water_three);
        this.tb_notice_water_four = (ToggleButton) findViewById(R.id.tb_notice_water_four);
        this.tb_notice_water_five = (ToggleButton) findViewById(R.id.tb_notice_water_five);
        this.tb_notice_water_six = (ToggleButton) findViewById(R.id.tb_notice_water_six);
        this.tb_notice_water_seven = (ToggleButton) findViewById(R.id.tb_notice_water_seven);
        this.tb_notice_water_eight = (ToggleButton) findViewById(R.id.tb_notice_water_eight);
    }

    void initData() {
        this.alarm_dao = new AlarmDao(this.ctx);
        this.alarms = this.alarm_dao.getAlarmsByNoticeType(AlarmType.DRINK.getType());
    }

    void addListener() {
        this.tb_notice_water_one.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (WaterNoticeActivity.this.alarms != null || WaterNoticeActivity.this.alarms
                        .size() != 0) {
                    WaterNoticeActivity.this.openOrClose((Alarm) WaterNoticeActivity.this.alarms
                            .get(0), isChecked);
                }
            }
        });
        this.tb_notice_water_two.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (WaterNoticeActivity.this.alarms != null || WaterNoticeActivity.this.alarms
                        .size() != 0) {
                    WaterNoticeActivity.this.openOrClose((Alarm) WaterNoticeActivity.this.alarms
                            .get(1), isChecked);
                }
            }
        });
        this.tb_notice_water_three.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (WaterNoticeActivity.this.alarms != null || WaterNoticeActivity.this.alarms
                        .size() != 0) {
                    WaterNoticeActivity.this.openOrClose((Alarm) WaterNoticeActivity.this.alarms
                            .get(2), isChecked);
                }
            }
        });
        this.tb_notice_water_four.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (WaterNoticeActivity.this.alarms != null || WaterNoticeActivity.this.alarms
                        .size() != 0) {
                    WaterNoticeActivity.this.openOrClose((Alarm) WaterNoticeActivity.this.alarms
                            .get(3), isChecked);
                }
            }
        });
        this.tb_notice_water_five.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (WaterNoticeActivity.this.alarms != null || WaterNoticeActivity.this.alarms
                        .size() != 0) {
                    WaterNoticeActivity.this.openOrClose((Alarm) WaterNoticeActivity.this.alarms
                            .get(4), isChecked);
                }
            }
        });
        this.tb_notice_water_six.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (WaterNoticeActivity.this.alarms != null || WaterNoticeActivity.this.alarms
                        .size() != 0) {
                    WaterNoticeActivity.this.openOrClose((Alarm) WaterNoticeActivity.this.alarms
                            .get(5), isChecked);
                }
            }
        });
        this.tb_notice_water_seven.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (WaterNoticeActivity.this.alarms != null || WaterNoticeActivity.this.alarms
                        .size() != 0) {
                    WaterNoticeActivity.this.openOrClose((Alarm) WaterNoticeActivity.this.alarms
                            .get(6), isChecked);
                }
            }
        });
        this.tb_notice_water_eight.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (WaterNoticeActivity.this.alarms != null || WaterNoticeActivity.this.alarms
                        .size() != 0) {
                    WaterNoticeActivity.this.openOrClose((Alarm) WaterNoticeActivity.this.alarms
                            .get(7), isChecked);
                }
            }
        });
        this.txt_notice_water_time_one.setOnClickListener(new TimeOnclick(this
                .txt_notice_water_time_one, (Alarm) this.alarms.get(0)));
        this.txt_notice_water_time_two.setOnClickListener(new TimeOnclick(this
                .txt_notice_water_time_two, (Alarm) this.alarms.get(1)));
        this.txt_notice_water_time_three.setOnClickListener(new TimeOnclick(this
                .txt_notice_water_time_three, (Alarm) this.alarms.get(2)));
        this.txt_notice_water_time_four.setOnClickListener(new TimeOnclick(this
                .txt_notice_water_time_four, (Alarm) this.alarms.get(3)));
        this.txt_notice_water_time_five.setOnClickListener(new TimeOnclick(this
                .txt_notice_water_time_five, (Alarm) this.alarms.get(4)));
        this.txt_notice_water_time_six.setOnClickListener(new TimeOnclick(this
                .txt_notice_water_time_six, (Alarm) this.alarms.get(5)));
        this.txt_notice_water_time_seven.setOnClickListener(new TimeOnclick(this
                .txt_notice_water_time_seven, (Alarm) this.alarms.get(6)));
        this.txt_notice_water_time_eight.setOnClickListener(new TimeOnclick(this
                .txt_notice_water_time_eight, (Alarm) this.alarms.get(7)));
    }

    void initUI() {
        if (this.alarms != null || this.alarms.size() != 0) {
            this.txt_notice_water_time_one.setText(((Alarm) this.alarms.get(0)).formatTime());
            this.txt_notice_water_time_two.setText(((Alarm) this.alarms.get(1)).formatTime());
            this.txt_notice_water_time_three.setText(((Alarm) this.alarms.get(2)).formatTime());
            this.txt_notice_water_time_four.setText(((Alarm) this.alarms.get(3)).formatTime());
            this.txt_notice_water_time_five.setText(((Alarm) this.alarms.get(4)).formatTime());
            this.txt_notice_water_time_six.setText(((Alarm) this.alarms.get(5)).formatTime());
            this.txt_notice_water_time_seven.setText(((Alarm) this.alarms.get(6)).formatTime());
            this.txt_notice_water_time_eight.setText(((Alarm) this.alarms.get(7)).formatTime());
            this.tb_notice_water_one.setChecked(((Alarm) this.alarms.get(0)).is_open());
            this.tb_notice_water_two.setChecked(((Alarm) this.alarms.get(1)).is_open());
            this.tb_notice_water_three.setChecked(((Alarm) this.alarms.get(2)).is_open());
            this.tb_notice_water_four.setChecked(((Alarm) this.alarms.get(3)).is_open());
            this.tb_notice_water_five.setChecked(((Alarm) this.alarms.get(4)).is_open());
            this.tb_notice_water_six.setChecked(((Alarm) this.alarms.get(5)).is_open());
            this.tb_notice_water_seven.setChecked(((Alarm) this.alarms.get(6)).is_open());
            this.tb_notice_water_eight.setChecked(((Alarm) this.alarms.get(7)).is_open());
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
