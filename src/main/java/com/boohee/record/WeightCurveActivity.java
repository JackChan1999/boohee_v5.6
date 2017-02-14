package com.boohee.record;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;

import com.boohee.database.UserPreference;
import com.boohee.model.User;
import com.boohee.model.mine.WeightPhoto;
import com.boohee.model.mine.WeightRecord;
import com.boohee.modeldao.UserDao;
import com.boohee.one.R;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.http.client.BooheeClient;
import com.boohee.one.ui.BaseActivity;
import com.boohee.one.ui.fragment.BaseDialogFragment;
import com.boohee.one.ui.fragment.BaseDialogFragment.onChangeListener;
import com.boohee.utility.Event;
import com.boohee.utils.ChartHelper;
import com.boohee.utils.DateFormatUtils;
import com.boohee.utils.DateHelper;
import com.boohee.utils.FastJsonUtils;
import com.boohee.utils.WheelUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import lecho.lib.hellocharts.listener.LineChartOnValueSelectListener;
import lecho.lib.hellocharts.listener.ViewportChangeListener;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.renderer.LineChartRenderer;
import lecho.lib.hellocharts.view.LineChartView;

import org.json.JSONObject;

public class WeightCurveActivity extends BaseActivity {
    public static final String KEY_RECORD_DATE = "key_record_date";
    public static final String WEIGHTS_SEARCH  =
            "/api/v2/weights/search?date_begin=%s&date_end=%s&token=%s";
    private BaseDialogFragment addWeightFragment;
    private ChartHelper        chartHelper;
    @InjectView(2131427770)
    LineChartView lineChart;
    @InjectView(2131427647)
    LinearLayout  ll_content;
    private String  mBeginDate;
    private String  mCurrentDate;
    private String  mEndDate;
    private boolean mIsLandscape;
    private int mRightCount = 0;
    private String mTargetDate;
    private float  mTargetWeight;
    private float              mViewportLeft  = 0.0f;
    private float              mViewportRight = 0.0f;
    private List<WeightRecord> mWeightRecords = new ArrayList();
    @InjectView(2131429465)
    RadioGroup rg_weight;
    @InjectView(2131428019)
    TextView   txt_orientation;
    private int typeMode = 1;
    private Date upperLimit;
    private User user;

    static /* synthetic */ int access$1204(WeightCurveActivity x0) {
        int i = x0.mRightCount + 1;
        x0.mRightCount = i;
        return i;
    }

    public static void start(Context context, String latest_on) {
        Intent starter = new Intent(context, WeightCurveActivity.class);
        starter.putExtra(KEY_RECORD_DATE, latest_on);
        context.startActivity(starter);
    }

    public void onCreate(Bundle outState) {
        super.onCreate(outState);
        setContentView(R.layout.dx);
        ButterKnife.inject((Activity) this);
        handleIntent();
        addListener();
        init();
        initTargetWeight();
        openRotation();
    }

    private void handleIntent() {
        this.mCurrentDate = getStringExtra(KEY_RECORD_DATE);
    }

    private void initTargetWeight() {
        this.user = new UserDao(this.ctx).queryWithToken(UserPreference.getToken(this.ctx));
        if (this.user != null) {
            this.mTargetWeight = this.user.target_weight;
            this.mTargetDate = this.user.target_date;
            this.lineChart.setTargetWeight(this.mTargetWeight);
        }
    }

    private void openRotation() {
        setRequestedOrientation(10);
    }

    private void init() {
        this.chartHelper = new ChartHelper();
        this.mCurrentDate = TextUtils.isEmpty(this.mCurrentDate) ? DateHelper.format(new Date())
                : this.mCurrentDate;
        this.mEndDate = this.mCurrentDate;
        this.mBeginDate = getDateByMode(this.mEndDate, this.typeMode, true);
        this.upperLimit = DateFormatUtils.getYear(this.mCurrentDate, -1);
        this.lineChart.setUnit(getString(R.string.aan));
        requestWeights();
    }

    private String getDateByMode(String endDate, int typeMode, boolean isFirst) {
        int i = -1;
        String end = "";
        switch (typeMode) {
            case 0:
                MobclickAgent.onEvent(this.ctx, Event.tool_weight_curveweek);
                if (isFirst) {
                    i = -12;
                }
                return DateFormatUtils.date2string(DateFormatUtils.getWeek(endDate, i),
                        "yyyy-MM-dd");
            case 1:
                if (isFirst) {
                    i = -6;
                }
                return DateFormatUtils.date2string(DateFormatUtils.getMonth(endDate, i),
                        "yyyy-MM-dd");
            case 2:
                MobclickAgent.onEvent(this.ctx, Event.tool_weight_curveyear);
                return DateFormatUtils.date2string(DateFormatUtils.getYear(endDate, -1),
                        "yyyy-MM-dd");
            default:
                return end;
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.i, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (WheelUtils.isFastDoubleClick()) {
            return true;
        }
        switch (item.getItemId()) {
            case R.id.action_add:
                this.addWeightFragment = WeightRecordFragment.newInstance(null, DateFormatUtils
                        .date2string(new Date(), "yyyy-MM-dd"));
                this.addWeightFragment.show(getSupportFragmentManager(), "weight_record");
                this.addWeightFragment.setChangeListener(new onChangeListener() {
                    public void onFinish() {
                        WeightCurveActivity.this.mWeightRecords.clear();
                        WeightCurveActivity.this.mCurrentDate = DateHelper.format(new Date());
                        WeightCurveActivity.this.mEndDate = WeightCurveActivity.this.mCurrentDate;
                        WeightCurveActivity.this.mViewportLeft = 0.0f;
                        WeightCurveActivity.this.mViewportRight = 0.0f;
                        WeightCurveActivity.this.mBeginDate = WeightCurveActivity.this
                                .getDateByMode(WeightCurveActivity.this.mEndDate,
                                        WeightCurveActivity.this.typeMode, true);
                        ((LineChartRenderer) WeightCurveActivity.this.lineChart.getChartRenderer
                                ()).resetPointIndex();
                        WeightCurveActivity.this.requestWeights();
                    }
                });
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void addListener() {
        this.lineChart.setOnValueTouchListener(new LineChartOnValueSelectListener() {
            public void onValueSelected(int lineIndex, int pointIndex, PointValue value) {
            }

            public void onPopSelected(int lineIndex, int pointIndex, PointValue value) {
                if (!WheelUtils.isFastDoubleClick() && WeightCurveActivity.this.mWeightRecords !=
                        null && WeightCurveActivity.this.mWeightRecords.size() != 0 &&
                        WeightCurveActivity.this.typeMode <= 0 && !WeightCurveActivity.this
                        .mIsLandscape) {
                    WeightRecord weightRecord = (WeightRecord) WeightCurveActivity.this
                            .mWeightRecords.get(pointIndex - 1);
                    if (weightRecord == null || weightRecord.isByHand()) {
                        WeightCurveActivity.this.addWeightFragment = WeightRecordFragment
                                .newInstance(weightRecord, weightRecord.record_on);
                        WeightCurveActivity.this.addWeightFragment.show(WeightCurveActivity.this
                                .getSupportFragmentManager(), "weight_record");
                    } else {
                        WeightCurveActivity.this.addWeightFragment = ScaleRecordFragment
                                .newInstance(weightRecord, weightRecord.record_on);
                        WeightCurveActivity.this.addWeightFragment.show(WeightCurveActivity.this
                                .getSupportFragmentManager(), ScaleRecordFragment.TAG);
                    }
                    WeightCurveActivity.this.addWeightFragment.setChangeListener(new onChangeListener() {
                        public void onFinish() {
                            WeightCurveActivity.this.mViewportLeft = WeightCurveActivity.this
                                    .lineChart.getCurrentViewport().left;
                            WeightCurveActivity.this.mViewportRight = WeightCurveActivity.this
                                    .lineChart.getCurrentViewport().right;
                            WeightCurveActivity.this.requestWeights();
                        }
                    });
                }
            }

            public void onValueDeselected() {
            }

            public void onImageSelected(int lineIndex, int pointIndex, PointValue value) {
                if (WeightCurveActivity.this.mWeightRecords != null && WeightCurveActivity.this
                        .mWeightRecords.size() != 0 && !WeightCurveActivity.this.mIsLandscape) {
                    WeightRecord weightRecord = (WeightRecord) WeightCurveActivity.this
                            .mWeightRecords.get(pointIndex - 1);
                    if (weightRecord != null) {
                        List<WeightPhoto> photos = weightRecord.photos;
                        if (photos != null && photos.size() > 0) {
                            WeightCurveActivity.this.startActivity(new Intent(WeightCurveActivity
                                    .this.ctx, WeightGalleryActivity.class).putExtra
                                    (WeightGalleryActivity.KEY_WEIGHT_RECORD, weightRecord));
                        }
                    }
                }
            }
        });
        this.lineChart.setViewportChangeListener(new ViewportChangeListener() {
            public void onViewportChanged(Viewport viewport) {
                if (viewport.left <= -1.0f && WeightCurveActivity.access$1204(WeightCurveActivity
                        .this) % 3 == 0 && DateFormatUtils.string2date(WeightCurveActivity.this
                        .mBeginDate, "yyyy-MM-dd").after(WeightCurveActivity.this.upperLimit)) {
                    WeightCurveActivity.this.mEndDate = DateFormatUtils.date2string
                            (DateFormatUtils.getDay(WeightCurveActivity.this.mBeginDate, -1),
                                    "yyyy-MM-dd");
                    WeightCurveActivity.this.mBeginDate = WeightCurveActivity.this.getDateByMode
                            (WeightCurveActivity.this.mEndDate, WeightCurveActivity.this
                                    .typeMode, false);
                    switch (WeightCurveActivity.this.typeMode) {
                        case 0:
                            if (!WeightCurveActivity.this.mIsLandscape) {
                                WeightCurveActivity.this.mViewportLeft = 7.0f;
                                WeightCurveActivity.this.mViewportRight = 14.0f;
                                break;
                            }
                            WeightCurveActivity.this.mViewportLeft = 7.0f;
                            WeightCurveActivity.this.mViewportRight = 21.0f;
                            break;
                        case 1:
                            WeightCurveActivity.this.mViewportLeft = 30.0f;
                            WeightCurveActivity.this.mViewportRight = 60.0f;
                            break;
                        case 2:
                            return;
                    }
                    WeightCurveActivity.this.requestWeights();
                }
            }
        });
        this.rg_weight.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                WeightCurveActivity.this.mWeightRecords.clear();
                WeightCurveActivity.this.mCurrentDate = TextUtils.isEmpty(WeightCurveActivity
                        .this.mCurrentDate) ? DateHelper.format(new Date()) : WeightCurveActivity
                        .this.mCurrentDate;
                WeightCurveActivity.this.mEndDate = WeightCurveActivity.this.mCurrentDate;
                WeightCurveActivity.this.mViewportLeft = 0.0f;
                WeightCurveActivity.this.mViewportRight = 0.0f;
                switch (checkedId) {
                    case R.id.rb_week:
                        WeightCurveActivity.this.typeMode = 0;
                        break;
                    case R.id.rb_month:
                        WeightCurveActivity.this.typeMode = 1;
                        break;
                    case R.id.rb_year:
                        WeightCurveActivity.this.typeMode = 2;
                        break;
                }
                WeightCurveActivity.this.mBeginDate = WeightCurveActivity.this.getDateByMode
                        (WeightCurveActivity.this.mEndDate, WeightCurveActivity.this.typeMode,
                                true);
                ((LineChartRenderer) WeightCurveActivity.this.lineChart.getChartRenderer())
                        .resetPointIndex();
                WeightCurveActivity.this.requestWeights();
            }
        });
        this.txt_orientation.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                new Builder(WeightCurveActivity.this.ctx).setMessage("别戳我，请旋转手机。p.s. " +
                        "请确保手机没有锁定旋转。").setPositiveButton("好的", null).show();
            }
        });
    }

    private void requestWeights() {
        showLoading();
        if (!TextUtils.isEmpty(this.mBeginDate) && !TextUtils.isEmpty(this.mCurrentDate)) {
            BooheeClient.build("record").get(String.format(WEIGHTS_SEARCH, new Object[]{this
                    .mBeginDate, this.mCurrentDate, UserPreference.getToken(this.ctx)}), new
                    JsonCallback(this) {
                public void ok(JSONObject object) {
                    super.ok(object);
                    List<WeightRecord> weightRecords = FastJsonUtils.parseList(object.optString
                            ("records").toString(), WeightRecord.class);
                    if (weightRecords != null && weightRecords.size() > 0) {
                        WeightCurveActivity.this.mWeightRecords.clear();
                        Collections.reverse(weightRecords);
                        WeightCurveActivity.this.mWeightRecords.addAll(0, weightRecords);
                    }
                    WeightCurveActivity.this.chartHelper.initLine(WeightCurveActivity.this.ctx,
                            WeightCurveActivity.this.lineChart, WeightCurveActivity.this
                                    .mBeginDate, WeightCurveActivity.this.mCurrentDate,
                            WeightCurveActivity.this.mWeightRecords, WeightCurveActivity.this
                                    .mTargetWeight, WeightCurveActivity.this.mTargetDate,
                            WeightCurveActivity.this.mViewportLeft, WeightCurveActivity.this
                                    .mViewportRight, WeightCurveActivity.this.typeMode,
                            WeightCurveActivity.this.mIsLandscape);
                }

                public void onFinish() {
                    super.onFinish();
                    WeightCurveActivity.this.dismissLoading();
                    WeightCurveActivity.this.mRightCount = 0;
                }
            }, this);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (this.addWeightFragment != null) {
            this.addWeightFragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        MobclickAgent.onEvent(this.ctx, Event.tool_weight_rotate);
        if (newConfig.orientation == 2) {
            this.mIsLandscape = true;
            this.ll_content.setSystemUiVisibility(4);
            getSupportActionBar().hide();
        } else if (newConfig.orientation == 1) {
            this.mIsLandscape = false;
            this.ll_content.setSystemUiVisibility(0);
            getSupportActionBar().show();
        }
        this.mViewportRight = this.lineChart.getCurrentViewport().right;
        switch (this.typeMode) {
            case 0:
                if (!this.mIsLandscape) {
                    this.mViewportLeft = this.mViewportRight - 7.0f;
                    break;
                } else {
                    this.mViewportLeft = this.mViewportRight - 14.0f;
                    break;
                }
            case 1:
                this.mViewportLeft = this.mViewportRight - 30.0f;
                break;
            case 2:
                this.mViewportLeft = 182.0f;
                this.mViewportRight = 365.0f;
                return;
        }
        this.chartHelper.initLine(this.ctx, this.lineChart, this.mBeginDate, this.mCurrentDate,
                this.mWeightRecords, this.mTargetWeight, this.mTargetDate, this.mViewportLeft,
                this.mViewportRight, this.typeMode, this.mIsLandscape);
    }

    protected void onDestroy() {
        super.onDestroy();
        this.chartHelper.clear();
    }
}
