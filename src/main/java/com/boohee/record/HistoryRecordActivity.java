package com.boohee.record;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ListView;

import butterknife.ButterKnife;
import butterknife.InjectView;

import com.boohee.api.RecordApi;
import com.boohee.main.GestureActivity;
import com.boohee.model.HistoryRecord;
import com.boohee.one.R;
import com.boohee.one.http.JsonCallback;
import com.boohee.utils.DateFormatUtils;
import com.boohee.utils.FastJsonUtils;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public class HistoryRecordActivity extends GestureActivity {
    private static final String KEY_DATE = "key_date";
    @InjectView(2131427552)
    ListView listView;
    private HistoryRecordListAdapter mHistoryRecordListAdapter;
    private List<HistoryRecord> mHistoryRecords = new ArrayList();
    private String record_on;

    public static void start(Context context, String record_on) {
        Intent starter = new Intent(context, HistoryRecordActivity.class);
        starter.putExtra("key_date", record_on);
        context.startActivity(starter);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bl);
        ButterKnife.inject((Activity) this);
        handleIntent();
        initListView();
        loadData();
    }

    private void handleIntent() {
        this.record_on = getStringExtra("key_date");
    }

    private void initListView() {
        this.mHistoryRecordListAdapter = new HistoryRecordListAdapter(this.activity, this
                .mHistoryRecords, this.record_on);
        this.listView.setAdapter(this.mHistoryRecordListAdapter);
    }

    private void loadData() {
        showLoading();
        RecordApi.getCanRecordsDates(this.activity, DateFormatUtils.date2string(DateFormatUtils
                .getDay(this.record_on, 7), "yyyy-MM-dd"), new JsonCallback(this.activity) {
            public void ok(JSONObject object) {
                super.ok(object);
                HistoryRecordActivity.this.refreshData(object);
            }

            public void onFinish() {
                super.onFinish();
                HistoryRecordActivity.this.dismissLoading();
            }
        });
    }

    private void refreshData(JSONObject object) {
        List<HistoryRecord> historyRecords = FastJsonUtils.parseList(object.optString("data"),
                HistoryRecord.class);
        if (historyRecords != null && historyRecords.size() > 0) {
            for (int i = 0; i < historyRecords.size(); i++) {
                HistoryRecord historyRecord = (HistoryRecord) historyRecords.get(i);
                if (!(((TextUtils.isEmpty(historyRecord.eating_calory) || Float.parseFloat
                        (historyRecord.eating_calory) == 0.0f) && (TextUtils.isEmpty
                        (historyRecord.activity_calory) || Float.parseFloat(historyRecord
                        .activity_calory) == 0.0f)) || DateFormatUtils.isToday(historyRecord
                        .record_on))) {
                    this.mHistoryRecords.add(historyRecord);
                }
            }
        }
        this.mHistoryRecordListAdapter.notifyDataSetChanged();
    }
}
