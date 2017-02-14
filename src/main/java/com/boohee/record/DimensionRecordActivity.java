package com.boohee.record;

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
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.ViewFlipper;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.boohee.api.RecordApi;
import com.boohee.calendar.WeightCalendarAdapter;
import com.boohee.main.GestureActivity;
import com.boohee.model.mine.BaseRecord;
import com.boohee.model.mine.Measure;
import com.boohee.model.mine.Measure.MeasureType;
import com.boohee.model.mine.WeightRecord;
import com.boohee.modeldao.WeightRecordDao;
import com.boohee.one.R;
import com.boohee.one.event.RefreshWeightEvent;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.ui.fragment.AddMeasureFragment;
import com.boohee.one.ui.fragment.BaseDialogFragment.onChangeListener;
import com.boohee.utility.Event;
import com.boohee.utils.DateFormatUtils;
import com.boohee.utils.DateHelper;
import com.boohee.utils.FastJsonUtils;
import com.boohee.utils.Helper;
import com.boohee.utils.HttpUtils;
import com.boohee.utils.WheelUtils;
import com.umeng.analytics.MobclickAgent;

import de.greenrobot.event.EventBus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;

public class DimensionRecordActivity extends GestureActivity implements OnItemClickListener,
        OnClickListener {
    public static final String KEY_RECORD_TYPE = "key_record_type";
    private WeightCalendarAdapter adapter;
    @InjectView(2131427628)
    GridView    calendarGrid;
    @InjectView(2131427627)
    ViewFlipper flipper;
    private String latest_on;
    private List<BaseRecord> mRecords = new ArrayList();
    @InjectView(2131427632)
    RadioButton rbtn_record_arm;
    @InjectView(2131427630)
    RadioButton rbtn_record_brass;
    @InjectView(2131427631)
    RadioButton rbtn_record_hip;
    @InjectView(2131427634)
    RadioButton rbtn_record_shank;
    @InjectView(2131427633)
    RadioButton rbtn_record_thigh;
    @InjectView(2131427629)
    RadioButton rbtn_record_waist;
    private String record_on;
    private String record_type;
    TextView txt_date;

    public void onCreate(Bundle outState) {
        super.onCreate(outState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.b6);
        ButterKnife.inject((Activity) this);
        findView();
        handleIntent();
        initToolsBar();
        initDate();
    }

    private void initDate() {
        this.record_on = DateFormatUtils.date2string(new Date(), "yyyyMM");
        this.txt_date.setText(DateFormatUtils.date2string(DateFormatUtils.string2date(this
                .record_on, "yyyyMM"), "yyyy年M月"));
    }

    private void initToolsBar() {
        View view_date_top = LayoutInflater.from(this).inflate(R.layout.om, null);
        this.txt_date = (TextView) view_date_top.findViewById(R.id.txt_date);
        view_date_top.findViewById(R.id.rl_left).setOnClickListener(this);
        view_date_top.findViewById(R.id.rl_right).setOnClickListener(this);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(view_date_top, new LayoutParams(-1, -1));
    }

    protected void onResume() {
        super.onResume();
        requestRecords();
        requestWeightsLatest();
    }

    public void onEventMainThread(RefreshWeightEvent event) {
        requestRecords();
        requestWeightsLatest();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.p, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (WheelUtils.isFastDoubleClick()) {
            return true;
        }
        switch (item.getItemId()) {
            case R.id.action_record:
                MobclickAgent.onEvent(this.ctx, Event.tool_weight_curve);
                if (MeasureType.WEIGHT.getType().equals(this.record_type)) {
                    WeightCurveActivity.start(this.activity, this.latest_on);
                    return true;
                }
                MeasureCurveActivity.start(this.activity, this.record_type);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void handleIntent() {
        this.record_type = getIntent().getStringExtra("key_record_type");
        this.record_type = this.record_type == null ? MeasureType.WEIGHT.getType() : this
                .record_type;
        if (MeasureType.WAIST.getType().equals(this.record_type)) {
            this.rbtn_record_waist.setChecked(true);
        } else if (MeasureType.BRASS.getType().equals(this.record_type)) {
            this.rbtn_record_brass.setChecked(true);
        } else if (MeasureType.ARM.getType().equals(this.record_type)) {
            this.rbtn_record_arm.setChecked(true);
        } else if (MeasureType.HIP.getType().equals(this.record_type)) {
            this.rbtn_record_hip.setChecked(true);
        } else if (MeasureType.THIGH.getType().equals(this.record_type)) {
            this.rbtn_record_thigh.setChecked(true);
        } else if (MeasureType.SHANK.getType().equals(this.record_type)) {
            this.rbtn_record_shank.setChecked(true);
        }
    }

    private void findView() {
        this.calendarGrid.setOnItemClickListener(this);
    }

    private void requestRecords() {
        if (!MeasureType.WEIGHT.getType().equals(this.record_type)) {
            RecordApi.getMeasureMonthList(this.record_type, this.record_on, this.activity, new
                    JsonCallback(this.activity) {
                public void ok(JSONObject object) {
                    super.ok(object);
                    DimensionRecordActivity.this.mRecords.clear();
                    List<BaseRecord> records = Measure.parseLists(object.optJSONArray("records"));
                    if (records != null && records.size() > 0) {
                        DimensionRecordActivity.this.mRecords.addAll(records);
                    }
                    if (!TextUtils.isEmpty(DimensionRecordActivity.this.record_on)) {
                        DimensionRecordActivity.this.adapter = new WeightCalendarAdapter
                                (DimensionRecordActivity.this.ctx, DateFormatUtils.string2date
                                        (DimensionRecordActivity.this.record_on, "yyyyMM"),
                                        DimensionRecordActivity.this.mRecords);
                        DimensionRecordActivity.this.calendarGrid.setAdapter
                                (DimensionRecordActivity.this.adapter);
                    }
                }

                public void fail(String message) {
                }
            });
        } else if (HttpUtils.isNetworkAvailable(this)) {
            RecordApi.getWeightsList(this.record_on, this.activity, new JsonCallback(this
                    .activity) {
                public void ok(JSONObject object) {
                    super.ok(object);
                    DimensionRecordActivity.this.mRecords.clear();
                    List<WeightRecord> records = FastJsonUtils.parseList(object.optString
                            ("records").toString(), WeightRecord.class);
                    if (records != null && records.size() > 0) {
                        DimensionRecordActivity.this.mRecords.addAll(records);
                        Collections.reverse(DimensionRecordActivity.this.mRecords);
                    }
                    if (!TextUtils.isEmpty(DimensionRecordActivity.this.record_on)) {
                        DimensionRecordActivity.this.adapter = new WeightCalendarAdapter
                                (DimensionRecordActivity.this.ctx, DateFormatUtils.string2date
                                        (DimensionRecordActivity.this.record_on, "yyyyMM"),
                                        DimensionRecordActivity.this.mRecords);
                        DimensionRecordActivity.this.calendarGrid.setAdapter
                                (DimensionRecordActivity.this.adapter);
                    }
                }

                public void fail(String message) {
                }
            });
        } else {
            this.mRecords.clear();
            List<WeightRecord> records = new WeightRecordDao(this).getMonthLists(DateHelper
                    .parseFromString(this.record_on, "yyyyMM"));
            if (records != null && records.size() > 0) {
                this.mRecords.addAll(records);
            }
            if (!TextUtils.isEmpty(this.record_on)) {
                this.adapter = new WeightCalendarAdapter(this.ctx, DateFormatUtils.string2date
                        (this.record_on, "yyyyMM"), this.mRecords);
                this.calendarGrid.setAdapter(this.adapter);
            }
        }
    }

    private void requestWeightsLatest() {
        RecordApi.getWeightsLatest(this.activity, new JsonCallback(this) {
            public void ok(JSONObject object) {
                super.ok(object);
                WeightRecord weightRecord = (WeightRecord) FastJsonUtils.fromJson(object
                        .optString("record"), WeightRecord.class);
                if (weightRecord != null) {
                    DimensionRecordActivity.this.latest_on = weightRecord.record_on;
                }
            }

            public void fail(String message) {
            }
        });
    }

    @OnClick({2131427629, 2131427630, 2131427631, 2131427632, 2131427633, 2131427634})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rbtn_record_waist:
                MobclickAgent.onEvent(this.ctx, Event.tool_circumference_waist);
                this.record_type = MeasureType.WAIST.getType();
                break;
            case R.id.rbtn_record_brass:
                MobclickAgent.onEvent(this.ctx, Event.tool_circumference_chest);
                this.record_type = MeasureType.BRASS.getType();
                break;
            case R.id.rbtn_record_hip:
                MobclickAgent.onEvent(this.ctx, Event.tool_circumference_ass);
                this.record_type = MeasureType.HIP.getType();
                break;
            case R.id.rbtn_record_arm:
                MobclickAgent.onEvent(this.ctx, Event.tool_circumference_arm);
                this.record_type = MeasureType.ARM.getType();
                break;
            case R.id.rbtn_record_thigh:
                MobclickAgent.onEvent(this.ctx, Event.tool_circumference_leg);
                this.record_type = MeasureType.THIGH.getType();
                break;
            case R.id.rbtn_record_shank:
                MobclickAgent.onEvent(this.ctx, Event.tool_circumference_shank);
                this.record_type = MeasureType.SHANK.getType();
                break;
            case R.id.rl_left:
                this.record_on = DateFormatUtils.date2string(DateFormatUtils.getYM(this
                        .record_on, -1), "yyyyMM");
                this.txt_date.setText(DateFormatUtils.date2string(DateFormatUtils.string2date
                        (this.record_on, "yyyyMM"), "yyyy年M月"));
                this.flipper.showPrevious();
                break;
            case R.id.rl_right:
                this.record_on = DateFormatUtils.date2string(DateFormatUtils.getYM(this
                        .record_on, 1), "yyyyMM");
                this.txt_date.setText(DateFormatUtils.date2string(DateFormatUtils.string2date
                        (this.record_on, "yyyyMM"), "yyyy年M月"));
                this.flipper.showNext();
                break;
        }
        requestRecords();
    }

    public void onItemClick(AdapterView<?> adapterView, View arg1, int position, long arg3) {
        if (position >= this.adapter.startPosition() && position < this.adapter.endPosition()) {
            if (this.adapter.getDate(position).after(new Date())) {
                Helper.showToast((int) R.string.ey);
                return;
            }
            AddMeasureFragment addMeasureFragment = AddMeasureFragment.newInstance(DateHelper
                    .format(this.adapter.getDate(position)));
            addMeasureFragment.show(getSupportFragmentManager(), "addMeasureFragment");
            addMeasureFragment.setChangeListener(new onChangeListener() {
                public void onFinish() {
                    DimensionRecordActivity.this.requestRecords();
                }
            });
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
