package com.boohee.one.pedometer;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.boohee.database.StepsPreference;
import com.boohee.main.GestureActivity;
import com.boohee.one.R;
import com.boohee.one.http.JsonCallback;
import com.boohee.utility.DensityUtil;
import com.boohee.utils.FastJsonUtils;
import com.boohee.utils.WheelUtils;
import com.umeng.socialize.common.SocializeConstants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import lecho.lib.hellocharts.listener.ViewportChangeListener;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.renderer.ColumnChartRenderer;
import lecho.lib.hellocharts.view.ColumnChartView;

import org.json.JSONObject;

import uk.co.senab.photoview.IPhotoView;

public class StepHistoryActivity extends GestureActivity {
    public static float PER_PAGE = 7.5f;
    private boolean canload;
    @InjectView(2131428043)
    ColumnChartView columnChartView;
    private int currentIndex;
    @InjectView(2131428048)
    LinearLayout llSetTarget;
    private int maxY;
    private int monthSteps;
    private int page = 1;
    @InjectView(2131428044)
    RelativeLayout rlTarget;
    private StepChartHelper stepChartHelper;
    private List<StepModel> stepList = new ArrayList();
    private int target;
    Integer[]    targetIndex = new Integer[]{Integer.valueOf(2), Integer.valueOf(4), Integer
            .valueOf(6), Integer.valueOf(8), Integer.valueOf(10), Integer.valueOf(12), Integer
            .valueOf(14), Integer.valueOf(16), Integer.valueOf(18), Integer.valueOf(20)};
    List<String> targetList  = new ArrayList();
    private int targetValue;
    @InjectView(2131428047)
    TextView tvMonthSteps;
    @InjectView(2131428045)
    TextView tvMyTarget;
    @InjectView(2131428049)
    TextView tvTargetCancel;
    @InjectView(2131428050)
    TextView tvTargetConfirm;
    @InjectView(2131428046)
    TextView tvWeekSteps;
    private float viewportLeft  = 0.0f;
    private float viewportRight = 0.0f;
    private int weekSteps;
    @InjectView(2131428051)
    WheelView wheelTarget;

    @OnClick({2131428049, 2131428050, 2131428044})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_target:
                this.llSetTarget.setVisibility(0);
                setTargetCurrentItem();
                return;
            case R.id.tv_target_cancel:
                this.llSetTarget.setVisibility(8);
                return;
            case R.id.tv_target_confirm:
                this.llSetTarget.setVisibility(8);
                this.target = this.targetValue;
                StepsPreference.putStepsTarget(this.targetValue);
                refreshChartView();
                this.tvMyTarget.setText(this.target + "步");
                return;
            default:
                return;
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.e4);
        ButterKnife.inject((Activity) this);
        initTargetValue();
        initView();
        getStepHistory();
    }

    private void initView() {
        this.stepChartHelper = new StepChartHelper();
        initListener();
        initTargetView();
    }

    private void initTargetView() {
        for (int i = 0; i < this.targetIndex.length; i++) {
            this.targetList.add(this.targetIndex[i] + "000" + " 步 " + SocializeConstants
                    .OP_OPEN_PAREN + StepCounterUtil.getCalory(this.targetIndex[i].intValue() *
                    1000) + " 千卡" + SocializeConstants.OP_CLOSE_PAREN);
        }
        WheelUtils.setWheelArrayText(this.wheelTarget, this.ctx, (String[]) this.targetList
                .toArray(new String[this.targetList.size()]));
        this.wheelTarget.addChangingListener(new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                WheelUtils.setHighlightArray(StepHistoryActivity.this.wheelTarget,
                        StepHistoryActivity.this.ctx, (String[]) StepHistoryActivity.this
                                .targetList.toArray(new String[StepHistoryActivity.this
                                        .targetList.size()]), newValue);
                StepHistoryActivity.this.currentIndex = newValue;
                StepHistoryActivity.this.targetValue = StepHistoryActivity.this
                        .targetIndex[StepHistoryActivity.this.currentIndex].intValue() * 1000;
            }
        });
    }

    private void setTargetCurrentItem() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                StepHistoryActivity.this.wheelTarget.setCurrentItem(StepHistoryActivity.this
                        .currentIndex, true);
            }
        }, 500);
    }

    private void initListener() {
        this.columnChartView.setViewportChangeListener(new ViewportChangeListener() {
            public void onViewportChanged(Viewport viewport) {
                if (viewport.left <= -1.0f && StepHistoryActivity.this.canload && ((float)
                        StepHistoryActivity.this.stepList.size()) > StepHistoryActivity.PER_PAGE
                        && StepHistoryActivity.this.page < 4) {
                    StepHistoryActivity.this.page = StepHistoryActivity.this.page + 1;
                    StepHistoryActivity.this.getStepHistory();
                }
            }
        });
    }

    private void initTargetValue() {
        int stepsTarget = StepsPreference.getStepsTarget();
        this.target = stepsTarget;
        this.targetValue = stepsTarget;
        this.tvMyTarget.setText(this.target + " 步");
        for (int i = 0; i < this.targetIndex.length; i++) {
            if (this.targetIndex[i].intValue() == this.target / 1000) {
                this.currentIndex = i;
            }
        }
    }

    private void refreshChartView() {
        this.maxY = (int) (((double) this.target) * 1.2d);
        ColumnChartRenderer renderer = (ColumnChartRenderer) this.columnChartView
                .getChartRenderer();
        if (renderer != null) {
            renderer.setTargetCalory((float) this.target, ContextCompat.getColor(this.ctx, R
                    .color.jv), ContextCompat.getColor(this.ctx, R.color.he));
            renderer.setMaxCaloryLimit((float) this.maxY);
            renderer.setHighlightAboveTarget(true, ContextCompat.getColor(this.ctx, R.color.ju));
            renderer.setIsRound(true, DensityUtil.dip2px(this.ctx, IPhotoView.DEFAULT_MAX_SCALE));
        }
        this.stepChartHelper.initLine(this.ctx, this.columnChartView, this.stepList, this
                .viewportLeft, this.viewportRight);
    }

    private void getStepHistory() {
        showLoading();
        this.canload = false;
        StepApi.getStepsHistory(this, this.page, new JsonCallback(this.ctx) {
            public void onFinish() {
                super.onFinish();
                StepHistoryActivity.this.dismissLoading();
                StepHistoryActivity.this.canload = true;
            }

            public void ok(JSONObject object) {
                super.ok(object);
                StepHistoryActivity.this.handleData(object);
            }
        });
    }

    private void handleData(JSONObject object) {
        if (object != null) {
            JSONObject summary = object.optJSONObject("summary");
            if (summary != null) {
                this.weekSteps = summary.optInt("week_step_count");
                this.monthSteps = summary.optInt("month_step_count");
            }
            List<StepModel> steps = FastJsonUtils.parseList(object.optString("steps"), StepModel
                    .class);
            if (steps != null) {
                Collections.reverse(steps);
                this.stepList.addAll(0, steps);
            }
            if (this.page == 1) {
                this.viewportRight = 0.0f;
                this.viewportLeft = 0.0f;
            } else {
                this.viewportLeft = (float) ((steps.size() / 2) - 1);
                this.viewportRight = this.viewportLeft + PER_PAGE;
            }
            if (this.weekSteps > 0) {
                this.tvWeekSteps.setText(this.weekSteps + " 步");
            }
            if (this.monthSteps > 0) {
                this.tvMonthSteps.setText(this.monthSteps + " 步");
            }
            refreshChartView();
        }
    }
}
