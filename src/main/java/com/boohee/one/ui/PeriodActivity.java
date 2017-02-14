package com.boohee.one.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

import com.boohee.api.RecordApi;
import com.boohee.main.GestureActivity;
import com.boohee.model.MonthMc;
import com.boohee.model.MonthMc.Section;
import com.boohee.model.PeriodRecord;
import com.boohee.model.mine.McLatest;
import com.boohee.myview.BooheePeroidDialog;
import com.boohee.one.R;
import com.boohee.one.event.PeriodEvent;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.http.JsonParams;
import com.boohee.one.mine.BiologyClockSettingActivity;
import com.boohee.one.mine.McInitActivity;
import com.boohee.utils.DateHelper;
import com.prolificinteractive.materialcalendarview.BooheeCalendarDay;
import com.prolificinteractive.materialcalendarview.BooheeDayView;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView.OnDayClickListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;
import com.umeng.socialize.common.SocializeConstants;

import de.greenrobot.event.EventBus;

import java.util.Calendar;

import org.json.JSONObject;

public class PeriodActivity extends GestureActivity {
    private MonthMc currentMmc;
    private int currentMonth = (CalendarDay.today().getMonth() + 1);
    private int currentYear  = CalendarDay.today().getYear();
    private BooheePeroidDialog dialog;
    private CalendarDay today = CalendarDay.today();
    private MaterialCalendarView widget;

    private class DayClickListener implements OnDayClickListener {
        private DayClickListener() {
        }

        public void onClick(BooheeDayView booheeDayView) {
            if (booheeDayView != null && booheeDayView.getDate() != null && !booheeDayView
                    .getDate().isAfter(PeriodActivity.this.today)) {
                PeriodActivity.this.dialog = new BooheePeroidDialog(PeriodActivity.this,
                        booheeDayView.getDate());
                PeriodActivity.this.dialog.setMcComingListener(new MCComingListener());
                PeriodActivity.this.dialog.setMcLeaveListener(new MCLeaveListener());
                if (PeriodActivity.this.couldDelete(booheeDayView.getDate())) {
                    PeriodActivity.this.dialog.setMcDeleteListener(new MCDeleteListener());
                } else {
                    PeriodActivity.this.dialog.setMcDeleteListener(null);
                }
                PeriodActivity.this.dialog.show();
            }
        }
    }

    private class MCComingListener implements OnClickListener {
        private MCComingListener() {
        }

        public void onClick(View v) {
            PeriodActivity.this.dialog.dismiss();
            if (v.getTag() != null) {
                PeriodActivity.this.recordMc("start", (CalendarDay) v.getTag());
            }
        }
    }

    private class MCDeleteListener implements OnClickListener {
        private MCDeleteListener() {
        }

        public void onClick(View v) {
            PeriodActivity.this.dialog.dismiss();
            if (v.getTag() != null) {
                CalendarDay day = (CalendarDay) v.getTag();
                RecordApi.deleteMcRecords(PeriodActivity.this.activity, day.getYear() +
                        SocializeConstants.OP_DIVIDER_MINUS + (day.getMonth() + 1) +
                        SocializeConstants.OP_DIVIDER_MINUS + day.getDay(), new JsonCallback
                        (PeriodActivity.this.activity) {
                    public void ok(JSONObject object) {
                        super.ok(object);
                        if (!PeriodActivity.this.isFinishing()) {
                            PeriodActivity.this.getMcStatus(PeriodActivity.this
                                    .getCurrentYearMonth());
                            PeriodActivity.this.setResult(-1);
                        }
                    }
                });
            }
        }
    }

    private class MCLeaveListener implements OnClickListener {
        private MCLeaveListener() {
        }

        public void onClick(View v) {
            PeriodActivity.this.dialog.dismiss();
            if (v.getTag() != null) {
                PeriodActivity.this.recordMc("end", (CalendarDay) v.getTag());
            }
        }
    }

    private class MonthChangeListener implements OnMonthChangedListener {
        private MonthChangeListener() {
        }

        public void onMonthChanged(MaterialCalendarView materialCalendarView, CalendarDay
                calendarDay) {
            PeriodActivity.this.currentYear = calendarDay.getYear();
            PeriodActivity.this.currentMonth = calendarDay.getMonth() + 1;
            PeriodActivity.this.getMcStatus(PeriodActivity.this.getCurrentYearMonth());
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch);
        init();
    }

    private void init() {
        this.widget = (MaterialCalendarView) findViewById(R.id.calendarView);
        this.widget.setOnDayClickListener(new DayClickListener());
        this.widget.setShowOtherDates(false);
        this.widget.setOnMonthChangedListener(new MonthChangeListener());
        this.widget.setSelectedDate(Calendar.getInstance().getTime());
        getMcStatus(getCurrentYearMonth());
    }

    private BooheeCalendarDay getBooheeCalendarDay(MonthMc mmc) {
        if (mmc == null || mmc.sections == null) {
            return null;
        }
        BooheeCalendarDay booheeCalendarDay = new BooheeCalendarDay();
        for (int i = 0; i < mmc.sections.size(); i++) {
            Section section = (Section) mmc.sections.get(i);
            if ("mc".equals(section.type)) {
                booheeCalendarDay.setColorDayRange(CalendarDay.from(this.currentYear, this
                        .currentMonth - 1, section.start), CalendarDay.from(this.currentYear,
                        this.currentMonth - 1, section.end), Color.parseColor("#FE52B7"));
            } else if (PeriodRecord.PREDICTION.equals(section.type)) {
                booheeCalendarDay.setColorDayRange(CalendarDay.from(this.currentYear, this
                        .currentMonth - 1, section.start), CalendarDay.from(this.currentYear,
                        this.currentMonth - 1, section.end), Color.parseColor("#FAB5D4"));
            } else if (PeriodRecord.PREGNACY.equals(section.type)) {
                booheeCalendarDay.setColorDayRange(CalendarDay.from(this.currentYear, this
                        .currentMonth - 1, section.start), CalendarDay.from(this.currentYear,
                        this.currentMonth - 1, section.end), Color.parseColor("#5856D6"));
            }
        }
        if (TextUtils.isEmpty(mmc.oviposit_day)) {
            return booheeCalendarDay;
        }
        CalendarDay oviCalendarDay = CalendarDay.from(DateHelper.parseString(mmc.oviposit_day));
        booheeCalendarDay.setDrawableDayRange(oviCalendarDay, oviCalendarDay, R.drawable.qt);
        return booheeCalendarDay;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, 1, R.string.a4i).setShowAsAction(2);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != 1) {
            return super.onOptionsItemSelected(item);
        }
        startActivity(new Intent(this.ctx, BiologyClockSettingActivity.class));
        return true;
    }

    private void getMcStatus(String year_month) {
        showLoading();
        RecordApi.getMcPeriodsMonth(this.activity, year_month, new JsonCallback(this.activity) {
            public void ok(JSONObject object) {
                super.ok(object);
                if (!PeriodActivity.this.isFinishing() && PeriodActivity.this.widget != null) {
                    PeriodActivity.this.currentMmc = MonthMc.parse(object.optString("month_mc"));
                    if (PeriodActivity.this.currentMmc == null || PeriodActivity.this.currentMmc
                            .sections == null) {
                        PeriodActivity.this.startActivity(new Intent(PeriodActivity.this.ctx,
                                McInitActivity.class));
                        return;
                    }
                    PeriodActivity.this.widget.setBooheeCalendarDay(PeriodActivity.this
                            .getBooheeCalendarDay(PeriodActivity.this.currentMmc));
                    PeriodActivity.this.widget.invalidateDays();
                }
            }

            public void onFinish() {
                super.onFinish();
                PeriodActivity.this.dismissLoading();
            }
        });
    }

    private void recordMc(String action, CalendarDay day) {
        if (day != null) {
            JsonParams params = new JsonParams();
            params.put("record_on", day.toString());
            if (McLatest.ACTION_START.equals(action)) {
                params.put("mc_status", "1");
            } else if (McLatest.ACTION_END.equals(action)) {
                params.put("mc_status", "2");
            }
            RecordApi.postMcRecords(this.activity, params, new JsonCallback(this.activity) {
                public void ok(JSONObject object) {
                    super.ok(object);
                    if (!PeriodActivity.this.isFinishing()) {
                        PeriodActivity.this.getMcStatus(PeriodActivity.this.getCurrentYearMonth());
                        PeriodActivity.this.setResult(-1);
                        EventBus.getDefault().post(new PeriodEvent());
                    }
                }
            });
        }
    }

    private String getCurrentYearMonth() {
        return this.currentYear + (this.currentMonth < 10 ? "0" : "") + this.currentMonth;
    }

    private boolean couldDelete(CalendarDay day) {
        if (this.currentMmc == null || this.currentMmc.sections == null) {
            return false;
        }
        for (Section section : this.currentMmc.sections) {
            if ("mc".equals(section.type) && (section.start == day.getDay() || section.end == day
                    .getDay())) {
                return true;
            }
        }
        return false;
    }
}
