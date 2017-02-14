package com.boohee.record;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;

import com.boohee.database.UserPreference;
import com.boohee.model.mine.Measure;
import com.boohee.model.mine.Measure.MeasureType;
import com.boohee.one.R;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.http.client.BooheeClient;
import com.boohee.one.ui.BaseActivity;
import com.boohee.one.ui.fragment.AddMeasureFragment;
import com.boohee.one.ui.fragment.BaseDialogFragment.onChangeListener;
import com.boohee.utils.ChartMeasureHelper;
import com.boohee.utils.DateFormatUtils;
import com.boohee.utils.DateHelper;
import com.boohee.utils.FastJsonUtils;
import com.boohee.utils.WheelUtils;

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

public class MeasureCurveActivity extends BaseActivity {
    public static final String KEY_RECORD_TYPE = "key_record_type";
    public static final String MEASURE_SEARCH  =
            "/api/v2/graph_data/region?data_type=%s&start_on=%s&end_on=%s&token=%s";
    static final        String TAG             = MeasureCurveActivity.class.getSimpleName();
    private ChartMeasureHelper chartHelper;
    @InjectView(2131427770)
    LineChartView lineChart;
    @InjectView(2131427647)
    LinearLayout  ll_content;
    private String  mBeginDate;
    private String  mCurrentDate;
    private String  mEndDate;
    private boolean mIsLandscape;
    private List<Measure> mMeasure       = new ArrayList();
    private int           mRightCount    = 0;
    private float         mViewportLeft  = 0.0f;
    private float         mViewportRight = 0.0f;
    private String        measureType    = MeasureType.WAIST.getType();
    @InjectView(2131429465)
    RadioGroup rg_weight;
    @InjectView(2131427769)
    Spinner    spinner;
    private int typeMode = 1;
    private Date upperLimit;

    private class SpinnerItemSelectedListener implements OnItemSelectedListener {
        private SpinnerItemSelectedListener() {
        }

        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long arg3) {
            MeasureCurveActivity.this.setSpinnerText(view);
            MeasureCurveActivity.this.measureType = MeasureCurveActivity.this.getMeasureType
                    (position);
            MeasureCurveActivity.this.mMeasure.clear();
            MeasureCurveActivity.this.mCurrentDate = DateHelper.format(new Date());
            MeasureCurveActivity.this.mEndDate = MeasureCurveActivity.this.mCurrentDate;
            MeasureCurveActivity.this.mBeginDate = MeasureCurveActivity.this.getDateByMode
                    (MeasureCurveActivity.this.mEndDate, MeasureCurveActivity.this.typeMode, true);
            MeasureCurveActivity.this.mViewportLeft = 0.0f;
            MeasureCurveActivity.this.mViewportRight = 0.0f;
            ((LineChartRenderer) MeasureCurveActivity.this.lineChart.getChartRenderer())
                    .resetPointIndex();
            MeasureCurveActivity.this.requestMeasure();
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    static /* synthetic */ int access$1304(MeasureCurveActivity x0) {
        int i = x0.mRightCount + 1;
        x0.mRightCount = i;
        return i;
    }

    public static void start(Context context, String record_type) {
        Intent starter = new Intent(context, MeasureCurveActivity.class);
        starter.putExtra("key_record_type", record_type);
        context.startActivity(starter);
    }

    public void onCreate(Bundle outState) {
        super.onCreate(outState);
        setContentView(R.layout.c0);
        ButterKnife.inject((Activity) this);
        intSpinner();
        addListener();
        handleIntent();
        init();
        openRotation();
    }

    private void handleIntent() {
        this.measureType = getIntent().getStringExtra("key_record_type");
        this.measureType = this.measureType == null ? MeasureType.WAIST.getType() : this
                .measureType;
        if (MeasureType.WAIST.getType().equals(this.measureType)) {
            this.spinner.setSelection(0);
        } else if (MeasureType.BRASS.getType().equals(this.measureType)) {
            this.spinner.setSelection(1);
        } else if (MeasureType.HIP.getType().equals(this.measureType)) {
            this.spinner.setSelection(2);
        } else if (MeasureType.ARM.getType().equals(this.measureType)) {
            this.spinner.setSelection(3);
        } else if (MeasureType.THIGH.getType().equals(this.measureType)) {
            this.spinner.setSelection(4);
        } else if (MeasureType.SHANK.getType().equals(this.measureType)) {
            this.spinner.setSelection(5);
        }
    }

    private void openRotation() {
        setRequestedOrientation(10);
    }

    private void init() {
        this.chartHelper = new ChartMeasureHelper();
        this.mCurrentDate = DateHelper.format(new Date());
        this.mEndDate = this.mCurrentDate;
        this.mBeginDate = getDateByMode(this.mEndDate, this.typeMode, true);
        this.upperLimit = DateFormatUtils.getYear(this.mCurrentDate, -1);
        this.lineChart.setUnit(getString(R.string.aal));
        requestMeasure();
    }

    private void intSpinner() {
        this.spinner.setAdapter(new ArrayAdapter(this, 17367049, Measure.getMeasureList()));
        this.spinner.setSelection(0, true);
        this.spinner.setOnItemSelectedListener(new SpinnerItemSelectedListener());
    }

    private void setSpinnerText(View view) {
        view.setBackgroundColor(getResources().getColor(R.color.in));
        TextView textView = (TextView) view;
        textView.setTextColor(getResources().getColor(R.color.ax));
        textView.setTextSize(16.0f);
        textView.setGravity(19);
    }

    private String getMeasureType(int position) {
        String type = MeasureType.WAIST.getType();
        switch (position) {
            case 0:
                return MeasureType.WAIST.getType();
            case 1:
                return MeasureType.BRASS.getType();
            case 2:
                return MeasureType.HIP.getType();
            case 3:
                return MeasureType.ARM.getType();
            case 4:
                return MeasureType.THIGH.getType();
            case 5:
                return MeasureType.SHANK.getType();
            default:
                return type;
        }
    }

    private String getDateByMode(String endDate, int typeMode, boolean isFirst) {
        int i = -1;
        String end = "";
        switch (typeMode) {
            case 0:
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
                AddMeasureFragment addMeasureFragment = AddMeasureFragment.newInstance(DateHelper
                        .format(new Date()));
                addMeasureFragment.show(getSupportFragmentManager(), "addMeasureFragment");
                addMeasureFragment.setChangeListener(new onChangeListener() {
                    public void onFinish() {
                        MeasureCurveActivity.this.mViewportLeft = MeasureCurveActivity.this
                                .lineChart.getCurrentViewport().left;
                        MeasureCurveActivity.this.mViewportRight = MeasureCurveActivity.this
                                .lineChart.getCurrentViewport().right;
                        MeasureCurveActivity.this.requestMeasure();
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

            public void onValueDeselected() {
            }

            public void onImageSelected(int lineIndex, int pointIndex, PointValue value) {
            }

            public void onPopSelected(int lineIndex, int pointIndex, PointValue value) {
                if (!WheelUtils.isFastDoubleClick() && MeasureCurveActivity.this.mMeasure != null
                        && MeasureCurveActivity.this.mMeasure.size() != 0 && MeasureCurveActivity
                        .this.typeMode <= 0) {
                    AddMeasureFragment addMeasureFragment = AddMeasureFragment.newInstance((
                            (Measure) MeasureCurveActivity.this.mMeasure.get(pointIndex - 1))
                            .record_on);
                    addMeasureFragment.show(MeasureCurveActivity.this.getSupportFragmentManager()
                            , "addMeasureFragment");
                    addMeasureFragment.setChangeListener(new onChangeListener() {
                        public void onFinish() {
                            MeasureCurveActivity.this.mViewportLeft = MeasureCurveActivity.this
                                    .lineChart.getCurrentViewport().left;
                            MeasureCurveActivity.this.mViewportRight = MeasureCurveActivity.this
                                    .lineChart.getCurrentViewport().right;
                            MeasureCurveActivity.this.requestMeasure();
                        }
                    });
                }
            }
        });
        this.lineChart.setViewportChangeListener(new ViewportChangeListener() {
            public void onViewportChanged(Viewport viewport) {
                if (viewport.left <= -1.0f && MeasureCurveActivity.access$1304
                        (MeasureCurveActivity.this) % 3 == 0 && DateFormatUtils.string2date
                        (MeasureCurveActivity.this.mBeginDate, "yyyy-MM-dd").after
                        (MeasureCurveActivity.this.upperLimit)) {
                    MeasureCurveActivity.this.mEndDate = DateFormatUtils.date2string
                            (DateFormatUtils.getDay(MeasureCurveActivity.this.mBeginDate, -1),
                                    "yyyy-MM-dd");
                    MeasureCurveActivity.this.mBeginDate = MeasureCurveActivity.this
                            .getDateByMode(MeasureCurveActivity.this.mEndDate,
                                    MeasureCurveActivity.this.typeMode, false);
                    switch (MeasureCurveActivity.this.typeMode) {
                        case 0:
                            MeasureCurveActivity.this.mViewportLeft = 7.0f;
                            MeasureCurveActivity.this.mViewportRight = 14.0f;
                            break;
                        case 1:
                            MeasureCurveActivity.this.mViewportLeft = 30.0f;
                            MeasureCurveActivity.this.mViewportRight = 60.0f;
                            break;
                        case 2:
                            return;
                    }
                    MeasureCurveActivity.this.requestMeasure();
                }
            }
        });
        this.rg_weight.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                MeasureCurveActivity.this.mMeasure.clear();
                MeasureCurveActivity.this.mCurrentDate = DateHelper.format(new Date());
                MeasureCurveActivity.this.mEndDate = MeasureCurveActivity.this.mCurrentDate;
                MeasureCurveActivity.this.mViewportLeft = 0.0f;
                MeasureCurveActivity.this.mViewportRight = 0.0f;
                switch (checkedId) {
                    case R.id.rb_week:
                        MeasureCurveActivity.this.typeMode = 0;
                        break;
                    case R.id.rb_month:
                        MeasureCurveActivity.this.typeMode = 1;
                        break;
                    case R.id.rb_year:
                        MeasureCurveActivity.this.typeMode = 2;
                        break;
                }
                MeasureCurveActivity.this.mBeginDate = MeasureCurveActivity.this.getDateByMode
                        (MeasureCurveActivity.this.mEndDate, MeasureCurveActivity.this.typeMode,
                                true);
                ((LineChartRenderer) MeasureCurveActivity.this.lineChart.getChartRenderer())
                        .resetPointIndex();
                MeasureCurveActivity.this.requestMeasure();
            }
        });
    }

    private void requestMeasure() {
        showLoading();
        if (!TextUtils.isEmpty(this.mBeginDate) && !TextUtils.isEmpty(this.mCurrentDate)) {
            BooheeClient.build("record").get(String.format(MEASURE_SEARCH, new Object[]{this
                    .measureType, this.mBeginDate, this.mCurrentDate, UserPreference.getToken
                    (this.ctx)}), new JsonCallback(this) {
                public void ok(JSONObject object) {
                    super.ok(object);
                    List<Measure> measures = FastJsonUtils.parseList(object.optString("data")
                            .toString(), Measure.class);
                    if (measures != null && measures.size() > 0) {
                        MeasureCurveActivity.this.mMeasure.clear();
                        Collections.reverse(measures);
                        MeasureCurveActivity.this.mMeasure.addAll(0, measures);
                    }
                    MeasureCurveActivity.this.chartHelper.initLine(MeasureCurveActivity.this.ctx,
                            MeasureCurveActivity.this.lineChart, MeasureCurveActivity.this
                                    .mBeginDate, MeasureCurveActivity.this.mCurrentDate,
                            MeasureCurveActivity.this.mMeasure, MeasureCurveActivity.this
                                    .mViewportLeft, MeasureCurveActivity.this.mViewportRight,
                            MeasureCurveActivity.this.typeMode, MeasureCurveActivity.this
                                    .mIsLandscape);
                }

                public void onFinish() {
                    super.onFinish();
                    MeasureCurveActivity.this.dismissLoading();
                    MeasureCurveActivity.this.mRightCount = 0;
                }
            }, this);
        }
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
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
                this.mMeasure, this.mViewportLeft, this.mViewportRight, this.typeMode, this
                        .mIsLandscape);
    }

    protected void onDestroy() {
        super.onDestroy();
        this.chartHelper.clear();
    }
}
