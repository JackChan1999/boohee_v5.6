package com.boohee.one.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import boohee.lib.share.ShareManager;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.boohee.account.NewUserInitActivity;
import com.boohee.api.RecordApi;
import com.boohee.circleview.CircleIndicator;
import com.boohee.circleview.CircleProgress;
import com.boohee.circleview.IndicatorItem;
import com.boohee.circleview.LineIndicator;
import com.boohee.database.OnePreference;
import com.boohee.database.UserPreference;
import com.boohee.model.User;
import com.boohee.modeldao.UserDao;
import com.boohee.myview.IntFloatWheelView;
import com.boohee.myview.swipeback.SwipeBackActivity;
import com.boohee.one.R;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.http.client.BooheeClient;
import com.boohee.utility.Event;
import com.boohee.utils.BitmapUtil;
import com.boohee.utils.FileUtil;
import com.boohee.utils.Helper;
import com.boohee.utils.WheelUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public class WeightStatusActivity extends SwipeBackActivity {
    @InjectView(2131428038)
    CircleIndicator ciBmi;
    @InjectView(2131428039)
    CircleIndicator ciBodyFatRate;
    @InjectView(2131428040)
    CircleProgress  cpBodyAge;
    @InjectView(2131428034)
    LineIndicator   liWeightProgress;
    private int  mCircleGreen;
    private int  mCircleRed;
    private int  mCircleYellow;
    private User mUser;
    @InjectView(2131428033)
    TextView     tvLoseWeight;
    @InjectView(2131427613)
    LinearLayout viewContent;
    @InjectView(2131428036)
    LinearLayout viewContentReset;
    @InjectView(2131428032)
    LinearLayout viewHeader;

    private class BitmapAsync extends AsyncTask<Void, Void, Bitmap> {
        ImageView iv_content;
        TextView  tv_title;
        View      view_share_summary;

        private BitmapAsync() {
        }

        protected void onPreExecute() {
            Helper.showToast((CharSequence) "正在分享，请稍等...");
            this.view_share_summary = LayoutInflater.from(WeightStatusActivity.this.activity)
                    .inflate(R.layout.qq, null);
            this.iv_content = (ImageView) this.view_share_summary.findViewById(R.id.iv_content);
            this.tv_title = (TextView) this.view_share_summary.findViewById(R.id.tv_date);
            this.tv_title.setText("我的体重记录");
        }

        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                Bitmap bitmap2;
                this.iv_content.setImageBitmap(bitmap);
                Bitmap bitmap_share = BitmapUtil.loadBitmapFromView(this.view_share_summary);
                Context context = WeightStatusActivity.this.activity;
                if (bitmap_share == null) {
                    bitmap2 = bitmap;
                } else {
                    bitmap2 = bitmap_share;
                }
                if (!TextUtils.isEmpty(FileUtil.getPNGImagePath(context, bitmap2,
                        "SHARE_4_LINECHART"))) {
                    ShareManager.shareLocalImage(WeightStatusActivity.this.activity, filePath);
                }
                if (!(bitmap_share == null || bitmap_share.isRecycled())) {
                    bitmap_share.recycle();
                }
                if (bitmap != null && !bitmap.isRecycled()) {
                    bitmap.recycle();
                }
            }
        }

        protected Bitmap doInBackground(Void... params) {
            return BitmapUtil.getBitmapByView(WeightStatusActivity.this.viewContent);
        }
    }

    @OnClick({2131428035, 2131428037})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_reset_1:
            case R.id.bt_reset_2:
                NewUserInitActivity.comeOn(this);
                return;
            default:
                return;
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.e1);
        ButterKnife.inject((Activity) this);
        this.mCircleGreen = getResources().getColor(R.color.bk);
        this.mCircleYellow = getResources().getColor(R.color.bp);
        this.mCircleRed = getResources().getColor(R.color.bl);
    }

    protected void onStart() {
        super.onStart();
        initViewAndData();
        requestData();
    }

    private void initViewAndData() {
        if (isNeedShowProgress()) {
            setTitle("我的进度");
        } else {
            setTitle("我的状态");
        }
        this.mUser = new UserDao(this.ctx).queryWithToken(UserPreference.getToken(this.ctx));
        float reduce = this.mUser.begin_weight - OnePreference.getLatestWeight();
        if (reduce < 0.0f) {
            reduce = 0.0f;
        }
        this.tvLoseWeight.setText(String.format("%.1f", new Object[]{Float.valueOf(reduce)}));
        if (isNeedShowProgress()) {
            this.viewHeader.setVisibility(0);
            this.viewContentReset.setVisibility(8);
            setWeightProgress();
            return;
        }
        this.viewHeader.setVisibility(8);
        this.viewContentReset.setVisibility(0);
    }

    private void requestData() {
        showLoading();
        BooheeClient.build("record").get(RecordApi.WEIGHT_PROGRESS, new JsonCallback(this) {
            public void ok(JSONObject object) {
                double bmi = object.optDouble("bmi");
                WeightStatusActivity.this.setBMI((float) bmi, object.optString("bmi_string"));
                double bfr = object.optDouble("bfr");
                WeightStatusActivity.this.countBodyFatRate((float) bfr, object.optString
                        ("bfr_string"));
                WeightStatusActivity.this.setBodyAge(object.optInt("bodyage"), object.optString
                        ("bodyage_string"));
            }

            public void fail(String message) {
                Helper.showToast((CharSequence) message);
            }

            public void onFinish() {
                WeightStatusActivity.this.dismissLoading();
            }
        }, this);
    }

    private void setWeightProgress() {
        float target;
        Object[] objArr = new Object[]{Float.valueOf(this.mUser.getWeight())};
        String rightContent = String.format("%.1f 公斤", new Object[]{Float.valueOf(this.mUser
                .targetWeight())});
        this.liWeightProgress.setContent("开始", String.format("%.1f 公斤", objArr), "目标",
                rightContent);
        if (OnePreference.getLatestWeight() > this.mUser.getWeight()) {
            target = this.mUser.getWeight();
        } else if (OnePreference.getLatestWeight() > this.mUser.targetWeight()) {
            target = OnePreference.getLatestWeight();
        } else {
            target = this.mUser.targetWeight();
        }
        this.liWeightProgress.setIndicator(this.mUser.getWeight(), this.mUser.targetWeight(),
                target);
    }

    private void setBMI(float bmi, String alert) {
        List<IndicatorItem> dividerIndicator = new ArrayList();
        IndicatorItem item1 = new IndicatorItem();
        item1.start = 15.0f;
        item1.end = 18.5f;
        item1.value = "轻体重";
        item1.color = this.mCircleYellow;
        dividerIndicator.add(item1);
        IndicatorItem item2 = new IndicatorItem();
        item2.start = 18.5f;
        item2.end = 24.0f;
        item2.value = "健康体重";
        item2.color = this.mCircleGreen;
        dividerIndicator.add(item2);
        IndicatorItem item3 = new IndicatorItem();
        item3.start = 24.0f;
        item3.end = 28.0f;
        item3.value = "超重";
        item3.color = this.mCircleYellow;
        dividerIndicator.add(item3);
        IndicatorItem item4 = new IndicatorItem();
        item4.start = 28.0f;
        item4.end = 40.0f;
        item4.value = "肥胖";
        item4.color = this.mCircleRed;
        dividerIndicator.add(item4);
        String title = "BMI";
        String content = String.valueOf(bmi);
        String unit = "";
        if (bmi > 24.0f) {
            this.ciBmi.setContentColor(this.mCircleRed, this.mCircleRed);
        } else {
            this.ciBmi.setContentColor(this.mCircleGreen, this.mCircleGreen);
        }
        this.ciBmi.setContentColor(this.mCircleGreen, this.mCircleGreen);
        this.ciBmi.setContent(title, content, unit, alert);
        this.ciBmi.setIndicatorValue(dividerIndicator, bmi);
    }

    private void countBodyFatRate(float bfr, String alert) {
        float low;
        float high;
        int age = this.mUser.getAge();
        if (this.mUser.isMale()) {
            if (age < 18 || age > 30) {
                low = 17.0f;
                high = 23.0f;
            } else {
                low = 14.0f;
                high = 20.0f;
            }
        } else if (age < 18 || age > 30) {
            low = 20.0f;
            high = 27.0f;
        } else {
            low = 17.0f;
            high = 24.0f;
        }
        setBodyFatRate(bfr, alert, low, high, bfr > high ? this.mCircleRed : this.mCircleGreen);
    }

    private void setBodyFatRate(float bfr, String alert, float low, float hight, int color) {
        List<IndicatorItem> dividerIndicator = new ArrayList();
        IndicatorItem item1 = new IndicatorItem();
        item1.start = 5.0f;
        item1.end = low;
        item1.value = "过低";
        item1.color = this.mCircleYellow;
        dividerIndicator.add(item1);
        IndicatorItem item2 = new IndicatorItem();
        item2.start = low;
        item2.end = hight;
        item2.value = "正常";
        item2.color = this.mCircleGreen;
        dividerIndicator.add(item2);
        IndicatorItem item3 = new IndicatorItem();
        item3.start = hight;
        item3.end = 60.0f;
        item3.value = "过高";
        item3.color = this.mCircleRed;
        dividerIndicator.add(item3);
        this.ciBodyFatRate.setContentColor(color, color);
        this.ciBodyFatRate.setContent("体脂率", String.valueOf(bfr), "％", alert);
        this.ciBodyFatRate.setIndicatorValue(dividerIndicator, bfr);
    }

    private void setBodyAge(int bodyAge, String alert) {
        String title = "身体年龄";
        String content = String.valueOf(bodyAge);
        String unit = "岁";
        if (this.mUser.getAge() >= bodyAge) {
            this.cpBodyAge.setContentColor(this.mCircleGreen, this.mCircleGreen);
        } else {
            this.cpBodyAge.setContentColor(this.mCircleRed, this.mCircleRed);
        }
        this.cpBodyAge.setContent(title, content, unit, alert);
        this.cpBodyAge.setIndicatorValue(10.0f, 60.0f, (float) this.mUser.getAge(), "实际年龄",
                (float) bodyAge, 20.0f, 30.0f, 40.0f, IntFloatWheelView.DEFAULT_VALUE);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.a0, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (WheelUtils.isFastDoubleClick()) {
            return true;
        }
        switch (item.getItemId()) {
            case R.id.action_weight_status:
                share();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void share() {
        MobclickAgent.onEvent(this.activity, Event.RECORD_WEIGHT_STATUS_SHARE);
        new BitmapAsync().execute(new Void[0]);
    }

    private boolean isNeedShowProgress() {
        return this.mUser != null && this.mUser.target_weight >= 0.0f;
    }

    public static final void comeOnBaby(Context context) {
        if (context != null) {
            context.startActivity(new Intent(context, WeightStatusActivity.class));
        }
    }
}
