package com.boohee.more;

import android.os.Bundle;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.boohee.main.GestureActivity;
import com.boohee.model.Alarm;
import com.boohee.model.Alarm.AlarmType;
import com.boohee.modeldao.AlarmDao;
import com.boohee.modeldao.NoticeDao;
import com.boohee.one.R;
import com.boohee.utility.BuilderIntent;

import java.util.ArrayList;

public class BooheeNoticeActivity extends GestureActivity {
    static final String TAG = BooheeNoticeActivity.class.getName();
    private AlarmDao alarm_dao;
    private boolean isDietOpen        = false;
    private boolean isGoodmorningOpen = false;
    private boolean isSportOpen       = false;
    private boolean isWaterOpen       = false;
    private NoticeDao    noticeDao;
    private ToggleButton tb_notice_diet;
    private ToggleButton tb_notice_good_morning;
    private ToggleButton tb_notice_sport;
    private ToggleButton tb_notice_water;
    private TextView     txt_notice_box_count;
    private TextView     txt_notice_diet_sub_title;
    private TextView     txt_notice_good_morning_sub_title;
    private TextView     txt_notice_sport_sub_title;
    private TextView     txt_notice_water_sub_title;

    public void onCreate(Bundle outState) {
        super.onCreate(outState);
        setTitle(R.string.wa);
        setContentView(R.layout.e9);
        initData();
        findView();
        addListener();
    }

    protected void onResume() {
        super.onResume();
        initUI();
    }

    void addListener() {
        this.tb_notice_good_morning.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                BooheeNoticeActivity.this.isGoodmorningOpen = isChecked;
            }
        });
        this.tb_notice_diet.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                BooheeNoticeActivity.this.isDietOpen = isChecked;
            }
        });
        this.tb_notice_sport.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                BooheeNoticeActivity.this.isSportOpen = isChecked;
            }
        });
        this.tb_notice_water.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                BooheeNoticeActivity.this.isWaterOpen = isChecked;
            }
        });
    }

    void initData() {
        this.alarm_dao = new AlarmDao(this.ctx);
        this.noticeDao = new NoticeDao(this.ctx);
    }

    void setUnReadCount() {
        int unReadCount = this.noticeDao.unReadCount();
        if (unReadCount == 0) {
            this.txt_notice_box_count.setVisibility(8);
            return;
        }
        this.txt_notice_box_count.setVisibility(0);
        this.txt_notice_box_count.setText(unReadCount + "");
    }

    void initUI() {
        initNotice(AlarmType.MORNING.getType(), this.tb_notice_good_morning, this
                .txt_notice_good_morning_sub_title);
        initNotice(AlarmType.DIET.getType(), this.tb_notice_diet, this.txt_notice_diet_sub_title);
        initNotice(AlarmType.SPORT.getType(), this.tb_notice_sport, this
                .txt_notice_sport_sub_title);
        initNotice(AlarmType.DRINK.getType(), this.tb_notice_water, this
                .txt_notice_water_sub_title);
    }

    void initNotice(int type, ToggleButton toggleButton, TextView textView) {
        ArrayList<Alarm> alarms = this.alarm_dao.getAlarmsByNoticeType(type);
        if (alarms != null && alarms.size() != 0) {
            String timeString = "";
            for (int i = 0; i < alarms.size(); i++) {
                Alarm alarm = (Alarm) alarms.get(i);
                if (alarm.is_open()) {
                    timeString = timeString + " " + ((Alarm) alarms.get(i)).formatTime();
                    this.alarm_dao.update(alarm);
                    RemindService.start(alarm, this.ctx);
                }
            }
            if (TextUtils.isEmpty(timeString)) {
                toggleButton.setChecked(false);
                textView.setText(R.string.ft);
                return;
            }
            toggleButton.setChecked(true);
            textView.setText("每天 " + timeString);
        }
    }

    void openAllNotice(int type, TextView textView) {
        ArrayList<Alarm> alarms = this.alarm_dao.getAlarmsByNoticeType(type);
        if (alarms != null && alarms.size() != 0) {
            String timeString = "";
            for (int i = 0; i < alarms.size(); i++) {
                Alarm alarm = (Alarm) alarms.get(i);
                alarm.enabled = 1;
                timeString = timeString + " " + ((Alarm) alarms.get(i)).formatTime();
                this.alarm_dao.update(alarm);
                RemindService.start(alarm, this.ctx);
            }
            if (!TextUtils.isEmpty(timeString)) {
                textView.setText("每天 " + timeString);
            }
        }
    }

    void closeAllNotice(int type, TextView textView) {
        ArrayList<Alarm> alarms = this.alarm_dao.getAlarmsByNoticeType(type);
        if (alarms != null && alarms.size() != 0) {
            for (int i = 0; i < alarms.size(); i++) {
                Alarm alarm = (Alarm) alarms.get(i);
                alarm.enabled = 0;
                this.alarm_dao.update(alarm);
                RemindService.start(alarm, this.ctx);
            }
            textView.setText(R.string.ft);
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_notice_good_morning:
                new BuilderIntent(this.activity, GoodMorningNoticeActivity.class).startActivity();
                return;
            case R.id.tb_notice_good_morning:
                if (this.isGoodmorningOpen) {
                    openAllNotice(AlarmType.MORNING.getType(), this
                            .txt_notice_good_morning_sub_title);
                    return;
                } else {
                    closeAllNotice(AlarmType.MORNING.getType(), this
                            .txt_notice_good_morning_sub_title);
                    return;
                }
            case R.id.rl_notice_diet:
                new BuilderIntent(this.activity, DietNoticeActivity.class).startActivity();
                return;
            case R.id.tb_notice_diet:
                if (this.isDietOpen) {
                    openAllNotice(AlarmType.DIET.getType(), this.txt_notice_diet_sub_title);
                    return;
                } else {
                    closeAllNotice(AlarmType.DIET.getType(), this.txt_notice_diet_sub_title);
                    return;
                }
            case R.id.rl_notice_sport:
                new BuilderIntent(this.activity, SportNoticeActivity.class).startActivity();
                return;
            case R.id.tb_notice_sport:
                if (this.isSportOpen) {
                    openAllNotice(AlarmType.SPORT.getType(), this.txt_notice_sport_sub_title);
                    return;
                } else {
                    closeAllNotice(AlarmType.SPORT.getType(), this.txt_notice_sport_sub_title);
                    return;
                }
            case R.id.rl_notice_water:
                new BuilderIntent(this.activity, WaterNoticeActivity.class).startActivity();
                return;
            case R.id.tb_notice_water:
                if (this.isWaterOpen) {
                    openAllNotice(AlarmType.DRINK.getType(), this.txt_notice_water_sub_title);
                    return;
                } else {
                    closeAllNotice(AlarmType.DRINK.getType(), this.txt_notice_water_sub_title);
                    return;
                }
            case R.id.rl_notice_box:
                new BuilderIntent(this.activity, NoticeListActivity.class).startActivity();
                return;
            default:
                return;
        }
    }

    private void findView() {
        this.txt_notice_good_morning_sub_title = (TextView) findViewById(R.id
                .txt_notice_good_morning_sub_title);
        this.txt_notice_diet_sub_title = (TextView) findViewById(R.id.txt_notice_diet_sub_title);
        this.txt_notice_sport_sub_title = (TextView) findViewById(R.id.txt_notice_sport_sub_title);
        this.txt_notice_water_sub_title = (TextView) findViewById(R.id.txt_notice_water_sub_title);
        this.txt_notice_box_count = (TextView) findViewById(R.id.txt_notice_box_count);
        this.txt_notice_good_morning_sub_title.setEllipsize(TruncateAt.END);
        this.txt_notice_diet_sub_title.setEllipsize(TruncateAt.END);
        this.txt_notice_sport_sub_title.setEllipsize(TruncateAt.END);
        this.txt_notice_water_sub_title.setEllipsize(TruncateAt.END);
        this.tb_notice_good_morning = (ToggleButton) findViewById(R.id.tb_notice_good_morning);
        this.tb_notice_diet = (ToggleButton) findViewById(R.id.tb_notice_diet);
        this.tb_notice_sport = (ToggleButton) findViewById(R.id.tb_notice_sport);
        this.tb_notice_water = (ToggleButton) findViewById(R.id.tb_notice_water);
    }

    protected void onDestroy() {
        super.onDestroy();
        this.alarm_dao.closeDB();
        this.noticeDao.closeDB();
    }
}
