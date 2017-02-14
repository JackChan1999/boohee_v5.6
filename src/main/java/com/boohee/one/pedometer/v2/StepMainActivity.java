package com.boohee.one.pedometer.v2;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

import boohee.lib.share.ShareManager;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.boohee.database.StepsPreference;
import com.boohee.database.UserPreference;
import com.boohee.myview.swipeback.SwipeBackActivity;
import com.boohee.one.R;
import com.boohee.one.event.BandTypeEvent;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.pedometer.StepApi;
import com.boohee.one.pedometer.StepCounterUtil;
import com.boohee.one.pedometer.StepModel;
import com.boohee.one.pedometer.StepSettingActivity;
import com.boohee.one.pedometer.manager.AbstractStepManager;
import com.boohee.one.pedometer.manager.StepListener;
import com.boohee.one.pedometer.manager.StepManagerFactory;
import com.boohee.one.pedometer.v2.adapter.StepDayAdapter;
import com.boohee.one.pedometer.v2.model.StepDayItem;
import com.boohee.one.pedometer.v2.model.StepReward;
import com.boohee.one.pedometer.v2.view.ProgressIndicator;
import com.boohee.one.ui.HardwareActivity;
import com.boohee.utility.Event;
import com.boohee.utils.DateFormatUtils;
import com.boohee.utils.Helper;
import com.boohee.utils.WheelUtils;
import com.boohee.widgets.LightAlertDialog;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.umeng.analytics.MobclickAgent;

import de.greenrobot.event.EventBus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;

public class StepMainActivity extends SwipeBackActivity implements StepListener {
    private static final String KEY_DATE = "key_date";
    @InjectView(2131427531)
    FloatingActionButton fabButton;
    private boolean        isShowCoupon;
    private boolean        isShowDiamond;
    private StepDayAdapter mAdapter;
    private OnClickListener   mClickListener = new OnClickListener() {
        public void onClick(View v) {
            boolean z = true;
            StepMainActivity stepMainActivity;
            switch (v.getId()) {
                case R.id.pi_coupon:
                    if (StepMainActivity.this.isShowCoupon) {
                        StepMainActivity.this.tvIconAlert.setText("");
                    } else {
                        StepMainActivity.this.tvIconAlert.setText(StepMainActivity.this.getString
                                (R.string.a79));
                    }
                    stepMainActivity = StepMainActivity.this;
                    if (StepMainActivity.this.isShowCoupon) {
                        z = false;
                    }
                    stepMainActivity.isShowCoupon = z;
                    return;
                case R.id.pi_diamond:
                    if (StepMainActivity.this.isShowDiamond) {
                        StepMainActivity.this.tvIconAlert.setText("");
                    } else {
                        StepMainActivity.this.tvIconAlert.setText(StepMainActivity.this.getString
                                (R.string.a7_));
                    }
                    stepMainActivity = StepMainActivity.this;
                    if (StepMainActivity.this.isShowDiamond) {
                        z = false;
                    }
                    stepMainActivity.isShowDiamond = z;
                    return;
                default:
                    return;
            }
        }
    };
    private List<StepDayItem> mDataList      = new ArrayList();
    private String           mDate;
    private StepGiftFragment mGift;
    private Handler mHandler = new Handler();
    private int mMonthStepCount;
    @InjectView(2131427552)
    PullToRefreshListView mPullRefreshListView;
    private StepModel           mStepModel;
    private ProgressIndicator   piCoupon;
    private ProgressIndicator   piDiamond;
    private AbstractStepManager stepManager;
    private TextView            tvIconAlert;
    @InjectView(2131427946)
    View tvIndicator;
    private TextView tvStep;
    private TextView tvStepAlert;
    private View     viewProgress;
    private View     viewReward;

    @OnClick({2131427531})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab_button:
                this.mGift.show(getSupportFragmentManager(), "");
                return;
            default:
                return;
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.de);
        ButterKnife.inject((Activity) this);
        EventBus.getDefault().register(this);
        init();
        requestData();
        this.mHandler.postDelayed(new Runnable() {
            public void run() {
                StepMainActivity.this.setPermission();
            }
        }, 1000);
    }

    private void init() {
        int i;
        this.mDate = getStringExtra("key_date");
        if (TextUtils.isEmpty(this.mDate)) {
            this.mDate = DateFormatUtils.date2string(new Date(), "yyyy-MM-dd");
        }
        this.stepManager = StepManagerFactory.getInstance().createStepManager(this);
        this.stepManager.setListener(this);
        this.stepManager.getCurrentStepAsyncs();
        this.mGift = StepGiftFragment.newInstance();
        View headerView = LayoutInflater.from(this).inflate(R.layout.mt, null, false);
        this.viewReward = headerView.findViewById(R.id.view_reward);
        this.piCoupon = (ProgressIndicator) headerView.findViewById(R.id.pi_coupon);
        this.viewProgress = headerView.findViewById(R.id.view_progress);
        this.piDiamond = (ProgressIndicator) headerView.findViewById(R.id.pi_diamond);
        this.piCoupon.setImage(R.drawable.a2p);
        this.piDiamond.setImage(R.drawable.a32);
        this.piCoupon.setOnClickListener(this.mClickListener);
        this.piDiamond.setOnClickListener(this.mClickListener);
        this.tvStep = (TextView) headerView.findViewById(R.id.tv_step);
        this.tvIconAlert = (TextView) headerView.findViewById(R.id.tv_icon_alert);
        this.tvStepAlert = (TextView) headerView.findViewById(R.id.tv_step_alert);
        ((ListView) this.mPullRefreshListView.getRefreshableView()).addHeaderView(headerView);
        this.mAdapter = new StepDayAdapter(this, this.mDataList);
        this.mPullRefreshListView.setAdapter(this.mAdapter);
        this.mPullRefreshListView.setMode(Mode.PULL_FROM_START);
        this.mPullRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
            public void onRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                StepMainActivity.this.requestData();
            }
        });
        boolean notShowReward = "201609".equals(DateFormatUtils.date2string(new Date(), "yyyyMM"));
        View view = this.viewReward;
        if (notShowReward) {
            i = 8;
        } else {
            i = 0;
        }
        view.setVisibility(i);
    }

    private void setPermission() {
        if (this.stepManager.isPedometer() && StepsPreference.isStepOpen() && !StepCounterUtil
                .checkNotificationPermission(this.ctx)) {
            LightAlertDialog dialog = LightAlertDialog.create(this.ctx, (int) R.string.a74, (int)
                    R.string.a73);
            dialog.setCancelable(false);
            dialog.setPositiveButton((CharSequence) "去设置", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    StepCounterUtil.goNLPermission(StepMainActivity.this.ctx);
                    Helper.showToast((int) R.string.a75);
                    StepsPreference.putIsFirst(true);
                }
            });
            dialog.setNegativeButton((CharSequence) "取消", null);
            dialog.show();
        }
    }

    public void requestData() {
        showLoading();
        StepApi.getStepsMonth(this, this.mDate, new JsonCallback(this) {
            public void ok(JSONObject object) {
                List<StepDayItem> temp = StepDayItem.parseArray(object.optString("steps"));
                StepMainActivity.this.mDataList.clear();
                int step = StepMainActivity.this.getTodayStep();
                if (StepApi.isCurrentMonth(StepMainActivity.this.mDate)) {
                    StepDayItem item = new StepDayItem();
                    item.setRecord_on(DateFormatUtils.date2string(new Date(), "yyyy-MM-dd"));
                    item.setStep(step);
                    StepMainActivity.this.mDataList.add(item);
                    JSONObject percentages = object.optJSONObject("percentages");
                    double coupon = percentages.optDouble("coupon");
                    double diamond = percentages.optDouble("diamond");
                    StepMainActivity.this.piCoupon.setProgress((float) coupon);
                    StepMainActivity.this.piDiamond.setProgress((float) diamond);
                    if (coupon > 0.0d) {
                        StepMainActivity.this.piCoupon.setImage(R.drawable.a2r);
                    }
                    if (diamond > 0.0d) {
                        StepMainActivity.this.piDiamond.setImage(R.drawable.a34);
                    }
                    StepMainActivity.this.viewProgress.setVisibility(0);
                } else {
                    step = 0;
                    StepMainActivity.this.viewProgress.setVisibility(8);
                }
                StepMainActivity.this.mMonthStepCount = object.optInt("month_step_count");
                StepMainActivity.this.tvStep.setText(String.valueOf(StepMainActivity.this
                        .mMonthStepCount + step));
                if (temp != null && temp.size() > 0) {
                    StepMainActivity.this.mDataList.addAll(temp);
                }
                StepMainActivity.this.mAdapter.notifyDataSetChanged();
                String slogan = object.optString("slogan");
                if (!TextUtils.isEmpty(slogan)) {
                    StepMainActivity.this.tvStepAlert.setText(slogan);
                }
                List<StepReward> data = StepReward.parseArray(object.optString
                        ("unclaimed_rewards"));
                StepMainActivity.this.mGift.setData(data);
                if (data == null || data.size() <= 0) {
                    StepMainActivity.this.tvIndicator.setVisibility(8);
                    StepMainActivity.this.fabButton.setImageResource(R.drawable.a4i);
                    return;
                }
                StepMainActivity.this.tvIndicator.setVisibility(0);
                StepMainActivity.this.fabButton.setImageResource(R.drawable.a4h);
            }

            public void fail(String message) {
                Helper.showToast((CharSequence) message);
            }

            public void onFinish() {
                StepMainActivity.this.dismissLoading();
                StepMainActivity.this.mPullRefreshListView.onRefreshComplete();
            }
        });
    }

    private int getTodayStep() {
        if (this.mStepModel != null) {
            return this.mStepModel.step;
        }
        return 0;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.v, menu);
        return true;
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        if (menu != null && menu.size() > 0) {
            menu.findItem(R.id.action_bind).setVisible(this.stepManager.isPedometer());
            if (StepApi.isCurrentMonth(this.mDate)) {
                menu.findItem(R.id.action_share).setVisible(true);
                menu.findItem(R.id.action_history).setVisible(true);
                menu.findItem(R.id.action_bind).setVisible(true);
                menu.findItem(R.id.action_setting).setVisible(true);
            } else {
                menu.findItem(R.id.action_share).setVisible(false);
                menu.findItem(R.id.action_history).setVisible(false);
                menu.findItem(R.id.action_bind).setVisible(false);
                menu.findItem(R.id.action_setting).setVisible(false);
            }
        }
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (WheelUtils.isFastDoubleClick()) {
            return true;
        }
        switch (item.getItemId()) {
            case R.id.action_share:
                share();
                return true;
            case R.id.action_history:
                MobclickAgent.onEvent(this.ctx, Event.BINGO_VIEWSTEPHISTORY);
                startActivity(new Intent(this.ctx, StepHistoryActivity.class));
                return true;
            case R.id.action_bind:
                startActivity(new Intent(this.ctx, HardwareActivity.class));
                return true;
            case R.id.action_setting:
                startActivity(new Intent(this.ctx, StepSettingActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void share() {
        String userKey = UserPreference.getUserKey(this.ctx);
        int step = getTodayStep();
        String url = String.format("%s?uk=%s&s=%d", new Object[]{"http://shop.boohee" +
                ".com/store/pages/step", userKey, Integer.valueOf(step)});
        String month = DateFormatUtils.string2String(this.mDate, "MM");
        String content = String.format("我今天走了 %d 步，%s 月已走了 %d 步。", new Object[]{Integer.valueOf
                (step), month, Integer.valueOf(this.mMonthStepCount)});
        ShareManager.share(this.ctx, content, content, url, "http://one.boohee" +
                ".cn/a/2013/10/22/cded0e6168868392e033f27ba0f877e3.png");
    }

    protected void onDestroy() {
        super.onDestroy();
        this.mHandler.removeCallbacksAndMessages(null);
        EventBus.getDefault().unregister(this);
        this.stepManager.onDestroy();
    }

    public void onGetCurrentStep(StepModel stepModel, boolean Error) {
        this.mStepModel = stepModel;
        if (stepModel != null && this.mDataList.size() != 0 && StepApi.isCurrentMonth(this.mDate)) {
            StepDayItem item = (StepDayItem) this.mDataList.get(0);
            if (isCurrentDay(item.getRecord_on())) {
                item.setStep(stepModel.step);
                this.tvStep.setText(String.valueOf(this.mMonthStepCount + stepModel.step));
                this.mAdapter.notifyDataSetChanged();
            }
        }
    }

    private boolean isCurrentDay(String date) {
        String format = "dd";
        return DateFormatUtils.date2string(new Date(), format).equals(DateFormatUtils
                .string2String(date, format));
    }

    public void onEventMainThread(BandTypeEvent event) {
        this.stepManager = StepManagerFactory.getInstance().changeStepManager(this, event, this
                .stepManager);
        this.stepManager.setListener(this);
        this.stepManager.getCurrentStepAsyncs();
    }

    public static void comeOnBaby(Context context, String date) {
        if (context != null) {
            Intent intent = new Intent(context, StepMainActivity.class);
            intent.putExtra("key_date", date);
            context.startActivity(intent);
        }
    }
}
