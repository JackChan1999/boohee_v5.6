package com.boohee.record;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar.LayoutParams;
import android.support.v7.app.AlertDialog.Builder;
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
import butterknife.OnClick;

import com.boohee.api.RecordApi;
import com.boohee.calendar.WeightCalendarAdapter;
import com.boohee.database.OnePreference;
import com.boohee.database.UserPreference;
import com.boohee.main.GestureActivity;
import com.boohee.model.User;
import com.boohee.model.WeightScale;
import com.boohee.model.mine.BaseRecord;
import com.boohee.model.mine.WeightRecord;
import com.boohee.modeldao.UserDao;
import com.boohee.modeldao.WeightRecordDao;
import com.boohee.one.R;
import com.boohee.one.event.RefreshWeightEvent;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.ui.WeightStatusActivity;
import com.boohee.one.ui.fragment.BaseDialogFragment;
import com.boohee.one.ui.fragment.BaseDialogFragment.onChangeListener;
import com.boohee.utility.Event;
import com.boohee.utils.BleUtil;
import com.boohee.utils.DateFormatUtils;
import com.boohee.utils.DateHelper;
import com.boohee.utils.FastJsonUtils;
import com.boohee.utils.Helper;
import com.boohee.utils.HttpUtils;
import com.boohee.utils.WheelUtils;
import com.kitnew.ble.QNBleDevice;
import com.kitnew.ble.QNData;
import com.umeng.analytics.MobclickAgent;

import de.greenrobot.event.EventBus;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;

public class WeightRecordActivity extends GestureActivity implements OnItemClickListener,
        OnClickListener {
    public static final String KEY_RECORD_TYPE  = "key_record_type";
    public static final int    REQUEST_OPEN_BLE = 2;
    private WeightCalendarAdapter adapter;
    @InjectView(2131427628)
    GridView    calendarGrid;
    @InjectView(2131427627)
    ViewFlipper flipper;
    private float  latestWeight;
    private String latest_on;
    private List<BaseRecord> mRecords = new ArrayList();
    private User               mUser;
    private int                photosCount;
    private BaseDialogFragment recordFragment;
    private String             record_on;
    @InjectView(2131427698)
    View rlScale;
    private ScaleConnHelper scaleConnHelper;
    @InjectView(2131428031)
    TextView tvPhotos;
    @InjectView(2131427702)
    TextView tvScaleGo;
    @InjectView(2131428025)
    TextView tvScaleHint;
    @InjectView(2131427700)
    TextView tvScaleName;
    @InjectView(2131427690)
    TextView tvStatus;
    @InjectView(2131428029)
    TextView tvStatusTitle;
    @InjectView(2131428027)
    TextView tvWeightCount;
    TextView txt_date;
    private int weightCount;

    public void onCreate(Bundle outState) {
        super.onCreate(outState);
        MobclickAgent.onEvent(this, Event.TOOL_WEIGHT_ENTER);
        EventBus.getDefault().register(this);
        setContentView(R.layout.e0);
        ButterKnife.inject((Activity) this);
        this.scaleConnHelper = new ScaleConnHelper(this);
        initView();
        initDate();
        refreshData();
    }

    private void initDate() {
        this.record_on = DateFormatUtils.date2string(new Date(), "yyyyMM");
        this.txt_date.setText(DateFormatUtils.date2string(DateFormatUtils.string2date(this
                .record_on, "yyyyMM"), "yyyy年M月"));
        this.mUser = new UserDao(this.ctx).queryWithToken(UserPreference.getToken(this.ctx));
    }

    private void initView() {
        View view_date_top = LayoutInflater.from(this).inflate(R.layout.om, null);
        this.txt_date = (TextView) view_date_top.findViewById(R.id.txt_date);
        view_date_top.findViewById(R.id.rl_left).setOnClickListener(this);
        view_date_top.findViewById(R.id.rl_right).setOnClickListener(this);
        this.calendarGrid.setOnItemClickListener(this);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(view_date_top, new LayoutParams(-1, -1));
        showOpenBleMsg();
    }

    private void refreshData() {
        requestRecords();
        requestWeightsLatest();
    }

    protected void onResume() {
        super.onResume();
        showWeightScale();
        this.scaleConnHelper.registerBle();
    }

    public void onEventMainThread(RefreshWeightEvent event) {
        requestRecords();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.p, menu);
        MenuItem item = menu.findItem(R.id.action_record);
        item.setTitle("");
        item.setIcon(0);
        return true;
    }

    private void requestRecords() {
        if (HttpUtils.isNetworkAvailable(this)) {
            showLoading();
            RecordApi.getWeightsList(this.record_on, this.activity, new JsonCallback(this
                    .activity) {
                public void ok(JSONObject object) {
                    super.ok(object);
                    WeightRecordActivity.this.mRecords.clear();
                    List<WeightRecord> records = FastJsonUtils.parseList(object.optString
                            ("records").toString(), WeightRecord.class);
                    if (records != null && records.size() > 0) {
                        WeightRecordActivity.this.mRecords.addAll(records);
                        Collections.reverse(WeightRecordActivity.this.mRecords);
                    }
                    if (!TextUtils.isEmpty(WeightRecordActivity.this.record_on)) {
                        WeightRecordActivity.this.adapter = new WeightCalendarAdapter
                                (WeightRecordActivity.this.ctx, DateFormatUtils.string2date
                                        (WeightRecordActivity.this.record_on, "yyyyMM"),
                                        WeightRecordActivity.this.mRecords);
                        WeightRecordActivity.this.calendarGrid.setAdapter(WeightRecordActivity
                                .this.adapter);
                    }
                    try {
                        WeightRecordActivity.this.weightCount = object.optJSONObject("summary")
                                .optInt("weight_record_count");
                        WeightRecordActivity.this.photosCount = object.optJSONObject("summary")
                                .optInt("weight_photos_count");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    WeightRecordActivity.this.tvWeightCount.setText(String.format("%d次记录", new
                            Object[]{Integer.valueOf(WeightRecordActivity.this.weightCount)}));
                    WeightRecordActivity.this.tvPhotos.setText(String.format("%d张照片", new
                            Object[]{Integer.valueOf(WeightRecordActivity.this.photosCount)}));
                }

                public void fail(String message) {
                    Helper.showToast((CharSequence) message);
                }

                public void onFinish() {
                    WeightRecordActivity.this.dismissLoading();
                }
            });
            return;
        }
        this.mRecords.clear();
        List<WeightRecord> records = new WeightRecordDao(this).getMonthLists(DateHelper
                .parseFromString(this.record_on, "yyyyMM"));
        if (records != null && records.size() > 0) {
            this.mRecords.addAll(records);
        }
        if (!TextUtils.isEmpty(this.record_on)) {
            this.adapter = new WeightCalendarAdapter(this.ctx, DateFormatUtils.string2date(this
                    .record_on, "yyyyMM"), this.mRecords);
            this.calendarGrid.setAdapter(this.adapter);
        }
    }

    private void requestWeightsLatest() {
        RecordApi.getWeightsLatest(this.activity, new JsonCallback(this) {
            public void ok(JSONObject object) {
                super.ok(object);
                WeightRecord weightRecord = (WeightRecord) FastJsonUtils.fromJson(object
                        .optString("record"), WeightRecord.class);
                if (weightRecord != null) {
                    WeightRecordActivity.this.latest_on = weightRecord.record_on;
                    if (!TextUtils.isEmpty(weightRecord.weight)) {
                        WeightRecordActivity.this.latestWeight = Float.parseFloat(weightRecord
                                .weight);
                        OnePreference.setLatestWeight(WeightRecordActivity.this.latestWeight);
                    }
                }
                WeightRecordActivity.this.refreshProgress();
            }

            public void fail(String message) {
            }
        });
    }

    private void refreshProgress() {
        if (isNeedShowProgress()) {
            this.tvStatusTitle.setText("我的进度");
            initProgressView();
            return;
        }
        this.tvStatusTitle.setText("我的状态");
        this.tvStatus.setText("保持");
    }

    private boolean isNeedShowProgress() {
        return this.mUser != null && this.mUser.target_weight >= 0.0f;
    }

    private void initProgressView() {
        float weightProgressRate;
        if (this.latestWeight <= 0.0f) {
            this.latestWeight = OnePreference.getLatestWeight();
        }
        if (this.mUser.begin_weight - this.latestWeight < 0.0f) {
            weightProgressRate = 0.0f;
        } else if (this.mUser.begin_weight - this.mUser.target_weight <= 0.0f) {
            weightProgressRate = 0.0f;
        } else {
            weightProgressRate = (this.mUser.begin_weight - this.latestWeight) / (this.mUser
                    .begin_weight - this.mUser.target_weight);
        }
        if (weightProgressRate > 1.0f) {
            weightProgressRate = 1.0f;
        } else if (weightProgressRate < 0.0f) {
            weightProgressRate = 0.0f;
        }
        this.tvStatus.setText(NumberFormat.getPercentInstance().format((double)
                weightProgressRate));
    }

    @OnClick({2131428026, 2131428030, 2131428028})
    public void onClick(View v) {
        if (!WheelUtils.isFastDoubleClick()) {
            switch (v.getId()) {
                case R.id.ll_weight_curve:
                    MobclickAgent.onEvent(this.ctx, Event.tool_weight_curve);
                    WeightCurveActivity.start(this.activity, this.latest_on);
                    return;
                case R.id.ll_status:
                    WeightStatusActivity.comeOnBaby(this);
                    return;
                case R.id.ll_weight_photos:
                    if (this.photosCount == 0) {
                        Helper.showToast((CharSequence) "还没有记录体重照片哦~");
                        return;
                    } else {
                        WeightPhotosActivity.comeOn(this.ctx);
                        return;
                    }
                case R.id.rl_left:
                    this.record_on = DateFormatUtils.date2string(DateFormatUtils.getYM(this
                            .record_on, -1), "yyyyMM");
                    this.txt_date.setText(DateFormatUtils.date2string(DateFormatUtils.string2date
                            (this.record_on, "yyyyMM"), "yyyy年M月"));
                    this.flipper.showPrevious();
                    requestRecords();
                    return;
                case R.id.rl_right:
                    this.record_on = DateFormatUtils.date2string(DateFormatUtils.getYM(this
                            .record_on, 1), "yyyyMM");
                    this.txt_date.setText(DateFormatUtils.date2string(DateFormatUtils.string2date
                            (this.record_on, "yyyyMM"), "yyyy年M月"));
                    this.flipper.showNext();
                    requestRecords();
                    return;
                default:
                    return;
            }
        }
    }

    public void onItemClick(AdapterView<?> adapterView, View arg1, int position, long arg3) {
        if (position >= this.adapter.startPosition() && position < this.adapter.endPosition()) {
            if (this.adapter.getDate(position).after(new Date())) {
                Helper.showToast((int) R.string.ey);
                return;
            }
            if (this.recordFragment != null && this.recordFragment.isAdded()) {
                this.recordFragment.dismissAllowingStateLoss();
            }
            WeightRecord record = (WeightRecord) this.adapter.getData(position);
            if (record == null || record.isByHand()) {
                this.recordFragment = WeightRecordFragment.newInstance(record, DateHelper.format
                        (this.adapter.getDate(position)));
                this.recordFragment.show(getSupportFragmentManager(), "weight_record");
            } else {
                this.recordFragment = ScaleRecordFragment.newInstance(record, DateHelper.format
                        (this.adapter.getDate(position)));
                this.recordFragment.show(getSupportFragmentManager(), ScaleRecordFragment.TAG);
            }
            this.recordFragment.setChangeListener(new onChangeListener() {
                public void onFinish() {
                    WeightRecordActivity.this.requestRecords();
                }
            });
        }
    }

    public void showWeightScale() {
        if (BleUtil.hasBleFeature(this)) {
            this.rlScale.setVisibility(0);
            this.tvScaleGo.setBackgroundResource(R.drawable.ca);
            WeightScale scale = OnePreference.getWeightScale();
            if (scale == null) {
                this.tvScaleName.setText("拥有智能秤？");
                this.tvScaleHint.setText("体验更便捷的记录方式");
                this.tvScaleGo.setText("去体验");
                this.tvScaleGo.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        ScaleIntroActivity.startActivity(WeightRecordActivity.this);
                    }
                });
                return;
            }
            this.tvScaleName.setText(scale.showName());
            this.tvScaleHint.setText("打开蓝牙，站在秤上会自动测量");
            this.tvScaleGo.setText("解绑");
            this.tvScaleGo.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    WeightRecordActivity.this.showUnbindDialog();
                }
            });
            this.scaleConnHelper.startScan(scale);
            return;
        }
        this.rlScale.setVisibility(8);
    }

    private void showUnbindDialog() {
        new Builder(this).setMessage((CharSequence) "确定解绑体重秤？").setPositiveButton((CharSequence)
                "确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                OnePreference.clearWeightScale();
                WeightRecordActivity.this.scaleConnHelper.stopAll();
                WeightRecordActivity.this.showWeightScale();
            }
        }).setNegativeButton((CharSequence) "取消", null).show();
    }

    public void showScaleConnect(float unsteadyWeight, boolean showIfDismiss) {
        if (!(this.recordFragment instanceof ScaleRecordFragment) || !this.recordFragment.isAdded
                ()) {
            if (this.recordFragment != null && this.recordFragment.isAdded()) {
                this.recordFragment.dismiss();
            }
            if (this.recordFragment == null || !(this.recordFragment instanceof
                    ScaleRecordFragment) || !this.recordFragment.isRemoved()) {
                showScaleFragment();
            } else if (showIfDismiss) {
                showScaleFragment();
            }
        } else if (unsteadyWeight > 0.001f) {
            ((ScaleRecordFragment) this.recordFragment).showUnSteadyWeight(unsteadyWeight);
        }
    }

    private void showScaleFragment() {
        ScaleRecordFragment fragment = ScaleRecordFragment.newInstance(null, DateFormatUtils
                .date2string(new Date(), "yyyy-MM-dd"));
        fragment.show(getSupportFragmentManager(), ScaleRecordFragment.TAG);
        this.recordFragment = fragment;
        this.recordFragment.setChangeListener(new onChangeListener() {
            public void onFinish() {
                WeightRecordActivity.this.requestRecords();
            }
        });
    }

    public void showScaleResult(QNData qnData) {
        if (this.recordFragment != null && !this.recordFragment.isRemoved() && (this
                .recordFragment instanceof ScaleRecordFragment)) {
            ((ScaleRecordFragment) this.recordFragment).setRecord(new WeightRecord(qnData,
                    DateHelper.format(new Date())));
        }
    }

    public void disconnect() {
        if (this.recordFragment != null && !this.recordFragment.isRemoved() && (this
                .recordFragment instanceof ScaleRecordFragment)) {
            ((ScaleRecordFragment) this.recordFragment).disconnect();
        }
    }

    private void showOpenBleMsg() {
        WeightScale scale = OnePreference.getWeightScale();
        if (BleUtil.hasBleFeature(this) && scale != null && !BleUtil.isBleOpen()) {
            Snackbar.make(this.flipper, (CharSequence) "若要使用体重秤测量，请打开蓝牙", -1).setAction(
                    (CharSequence) "打开蓝牙", new OnClickListener() {
                public void onClick(View v) {
                    WeightRecordActivity.this.startActivityForResult(new Intent("android" +
                            ".bluetooth.adapter.action.REQUEST_ENABLE"), 2);
                }
            }).show();
        }
    }

    protected void onPause() {
        super.onPause();
        this.scaleConnHelper.unRegisterAndPause();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ScaleIntroActivity.REQUEST) {
            if (resultCode == -1 && data != null) {
                QNBleDevice device = (QNBleDevice) data.getParcelableExtra("device");
                if (device != null) {
                    this.scaleConnHelper.connectDevice(device);
                }
            }
        } else if (this.recordFragment != null) {
            this.recordFragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
