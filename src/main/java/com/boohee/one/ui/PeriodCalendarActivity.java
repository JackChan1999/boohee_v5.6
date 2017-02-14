package com.boohee.one.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar.LayoutParams;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import butterknife.ButterKnife;
import butterknife.InjectView;

import com.boohee.api.RecordApi;
import com.boohee.calendar.PeriodCalendarAdapter;
import com.boohee.main.GestureActivity;
import com.boohee.model.MonthMc;
import com.boohee.model.MonthMc.Section;
import com.boohee.model.PeriodRecord;
import com.boohee.model.mine.McLatest;
import com.boohee.myview.NewPeroidDialog;
import com.boohee.one.R;
import com.boohee.one.event.PeriodEvent;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.http.JsonParams;
import com.boohee.one.mine.BiologyClockSettingActivity;
import com.boohee.utility.Event;
import com.boohee.utils.DateFormatUtils;
import com.boohee.utils.DateHelper;
import com.boohee.utils.Helper;
import com.umeng.analytics.MobclickAgent;

import de.greenrobot.event.EventBus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;

public class PeriodCalendarActivity extends GestureActivity {
    private PeriodCalendarAdapter adapter;
    @InjectView(2131427628)
    GridView calendarGrid;
    private MonthMc currentMmc;
    OnClickListener dateClickListener = new OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.rl_left:
                    PeriodCalendarActivity.this.record_on = DateFormatUtils.date2string
                            (DateFormatUtils.getYM(PeriodCalendarActivity.this.record_on, -1),
                                    "yyyyMM");
                    PeriodCalendarActivity.this.txt_date.setText(DateFormatUtils.date2string
                            (DateFormatUtils.string2date(PeriodCalendarActivity.this.record_on,
                                    "yyyyMM"), "yyyy年M月"));
                    PeriodCalendarActivity.this.flipper.showPrevious();
                    break;
                case R.id.rl_right:
                    PeriodCalendarActivity.this.record_on = DateFormatUtils.date2string
                            (DateFormatUtils.getYM(PeriodCalendarActivity.this.record_on, 1),
                                    "yyyyMM");
                    PeriodCalendarActivity.this.txt_date.setText(DateFormatUtils.date2string
                            (DateFormatUtils.string2date(PeriodCalendarActivity.this.record_on,
                                    "yyyyMM"), "yyyy年M月"));
                    PeriodCalendarActivity.this.flipper.showNext();
                    break;
            }
            PeriodCalendarActivity.this.requestRecords();
        }
    };
    private NewPeroidDialog dialog;
    @InjectView(2131427627)
    ViewFlipper flipper;
    OnItemClickListener itemClickListener = new OnItemClickListener() {
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            if (position >= PeriodCalendarActivity.this.adapter.startPosition() && position <
                    PeriodCalendarActivity.this.adapter.endPosition()) {
                if (PeriodCalendarActivity.this.adapter.getDate(position).after(new Date())) {
                    Helper.showToast((int) R.string.ey);
                    return;
                }
                PeriodCalendarActivity.this.dialog = new NewPeroidDialog(PeriodCalendarActivity
                        .this, PeriodCalendarActivity.this.adapter.getDate(position));
                PeriodCalendarActivity.this.dialog.setMcComingListener(new MCComingListener());
                PeriodCalendarActivity.this.dialog.setMcLeaveListener(new MCLeaveListener());
                if (PeriodCalendarActivity.this.couldDelete(PeriodCalendarActivity.this.adapter
                        .getDate(position))) {
                    PeriodCalendarActivity.this.dialog.setMcDeleteListener(new MCDeleteListener());
                } else {
                    PeriodCalendarActivity.this.dialog.setMcDeleteListener(null);
                }
                PeriodCalendarActivity.this.dialog.show();
                PeriodCalendarActivity.this.adapter.setCurrentPosition(position);
                PeriodCalendarActivity.this.adapter.notifyDataSetChanged();
            }
        }
    };
    private List<PeriodRecord> recordList = new ArrayList();
    private String   record_on;
    private TextView txt_date;

    private class MCComingListener implements OnClickListener {
        private MCComingListener() {
        }

        public void onClick(View v) {
            PeriodCalendarActivity.this.dialog.dismiss();
            if (v.getTag() != null) {
                PeriodCalendarActivity.this.recordMc("start", (Date) v.getTag());
            }
        }
    }

    private class MCDeleteListener implements OnClickListener {
        private MCDeleteListener() {
        }

        public void onClick(View v) {
            PeriodCalendarActivity.this.dialog.dismiss();
            if (v.getTag() != null) {
                RecordApi.deleteMcRecords(PeriodCalendarActivity.this.activity, DateHelper.format
                        ((Date) v.getTag()), new JsonCallback(PeriodCalendarActivity.this
                        .activity) {
                    public void ok(JSONObject object) {
                        super.ok(object);
                        if (!PeriodCalendarActivity.this.isFinishing()) {
                            PeriodCalendarActivity.this.requestRecords();
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
            PeriodCalendarActivity.this.dialog.dismiss();
            if (v.getTag() != null) {
                PeriodCalendarActivity.this.recordMc("end", (Date) v.getTag());
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cg);
        ButterKnife.inject((Activity) this);
        initToolsBar();
        initDate();
        initListener();
        requestRecords();
    }

    private void initToolsBar() {
        View view_date_top = LayoutInflater.from(this).inflate(R.layout.om, null);
        this.txt_date = (TextView) view_date_top.findViewById(R.id.txt_date);
        view_date_top.findViewById(R.id.rl_left).setOnClickListener(this.dateClickListener);
        view_date_top.findViewById(R.id.rl_right).setOnClickListener(this.dateClickListener);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(view_date_top, new LayoutParams(-1, -1));
    }

    private void initDate() {
        this.record_on = DateFormatUtils.date2string(new Date(), "yyyyMM");
        this.txt_date.setText(DateFormatUtils.date2string(DateFormatUtils.string2date(this
                .record_on, "yyyyMM"), "yyyy年M月"));
    }

    private void initListener() {
        this.calendarGrid.setOnItemClickListener(this.itemClickListener);
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

    private void requestRecords() {
        showLoading();
        RecordApi.getMcPeriodsMonth(this.activity, this.record_on, new JsonCallback(this.activity) {
            public void ok(JSONObject object) {
                super.ok(object);
                PeriodCalendarActivity.this.currentMmc = MonthMc.parse(object.optString
                        ("month_mc"));
                if (PeriodCalendarActivity.this.currentMmc != null) {
                    PeriodCalendarActivity.this.getPeriodRecordData();
                    if (PeriodCalendarActivity.this.recordList.size() > 0 && !TextUtils.isEmpty
                            (PeriodCalendarActivity.this.record_on)) {
                        PeriodCalendarActivity.this.adapter = new PeriodCalendarAdapter
                                (PeriodCalendarActivity.this.ctx, DateFormatUtils.string2date
                                        (PeriodCalendarActivity.this.record_on, "yyyyMM"),
                                        PeriodCalendarActivity.this.recordList);
                        PeriodCalendarActivity.this.calendarGrid.setAdapter
                                (PeriodCalendarActivity.this.adapter);
                    }
                }
            }

            public void onFinish() {
                super.onFinish();
                PeriodCalendarActivity.this.dismissLoading();
            }
        });
    }

    private void getPeriodRecordData() {
        if (this.currentMmc.sections != null) {
            this.recordList.clear();
            for (int i = 0; i < this.currentMmc.sections.size(); i++) {
                Section section = (Section) this.currentMmc.sections.get(i);
                for (int j = section.start; j <= section.end; j++) {
                    PeriodRecord record;
                    String date = String.valueOf((Integer.parseInt(this.record_on) * 100) + j);
                    if (j == section.start) {
                        record = new PeriodRecord(date, section.type, PeriodRecord.LEFT);
                    } else if (j == section.end) {
                        record = new PeriodRecord(date, section.type, PeriodRecord.RIGHT);
                    } else {
                        record = new PeriodRecord(date, section.type);
                    }
                    if (!TextUtils.isEmpty(this.currentMmc.oviposit_day) && DateFormatUtils
                            .string2String(this.currentMmc.oviposit_day, "yyyyMMdd").equals(date)) {
                        record.type = PeriodRecord.OVIPOSIT;
                    }
                    this.recordList.add(record);
                }
            }
        }
    }

    private boolean couldDelete(Date date) {
        if (this.currentMmc == null || this.currentMmc.sections == null) {
            return false;
        }
        int day = DateHelper.getDay(date);
        for (Section section : this.currentMmc.sections) {
            if ("mc".equals(section.type) && (section.start == day || section.end == day)) {
                return true;
            }
        }
        return false;
    }

    private void recordMc(String action, Date day) {
        if (day != null) {
            JsonParams params = new JsonParams();
            params.put("record_on", DateHelper.format(day));
            if (McLatest.ACTION_START.equals(action)) {
                params.put("mc_status", "1");
            } else if (McLatest.ACTION_END.equals(action)) {
                params.put("mc_status", "2");
            }
            RecordApi.postMcRecords(this.activity, params, new JsonCallback(this.activity) {
                public void ok(JSONObject object) {
                    super.ok(object);
                    if (!PeriodCalendarActivity.this.isFinishing()) {
                        PeriodCalendarActivity.this.requestRecords();
                        PeriodCalendarActivity.this.setResult(-1);
                        EventBus.getDefault().post(new PeriodEvent());
                        MobclickAgent.onEvent(PeriodCalendarActivity.this.ctx, Event
                                .tool_updateMcRecordOK);
                        MobclickAgent.onEvent(PeriodCalendarActivity.this.ctx, Event.tool_recordOK);
                    }
                }
            });
        }
    }
}
