package com.boohee.one.ui.fragment;

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
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import boohee.lib.share.ShareManager;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.boohee.api.RecordApi;
import com.boohee.main.GestureActivity;
import com.boohee.model.MicroNutrientsItem;
import com.boohee.model.TodayAnalysis;
import com.boohee.model.TodayDiets;
import com.boohee.model.TodayItem;
import com.boohee.model.TodayItem.DESCRIPTION_TYPE;
import com.boohee.model.TodayMicroNutrients;
import com.boohee.model.TodayNutrients;
import com.boohee.model.TodayOverview;
import com.boohee.myview.risenumber.RiseNumberTextView;
import com.boohee.one.R;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.ui.BrowserActivity;
import com.boohee.one.ui.TodayNutritionActivity;
import com.boohee.one.ui.adapter.MicroNutrientsAdapter;
import com.boohee.utility.Event;
import com.boohee.utils.BitmapUtil;
import com.boohee.utils.ChartAnalysisHelper;
import com.boohee.utils.DateFormatUtils;
import com.boohee.utils.FastJsonUtils;
import com.boohee.utils.FileUtil;
import com.boohee.utils.Helper;
import com.boohee.utils.ViewUtils;
import com.boohee.widgets.CircularProgressBar;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.view.PieChartView;

import org.json.JSONObject;

public class TodayAnalysisActivity extends GestureActivity {
    private static final String EXTRA_DATE = "extra_date";
    @InjectView(2131429227)
    Button             mBtMicroMore;
    @InjectView(2131428180)
    ImageView          mIvCaloryStatus;
    @InjectView(2131429235)
    ImageView          mIvCarbohydrateState;
    @InjectView(2131429252)
    ImageView          mIvDietStatus;
    @InjectView(2131429241)
    ImageView          mIvFatState;
    @InjectView(2131429217)
    ImageView          mIvMealsBreakfast;
    @InjectView(2131429223)
    ImageView          mIvMealsDinner;
    @InjectView(2131429220)
    ImageView          mIvMealsLunch;
    @InjectView(2131429254)
    ImageView          mIvNuturitionStatus;
    @InjectView(2131429246)
    ImageView          mIvProteinState;
    @InjectView(2131429212)
    LinearLayout       mLlAnalysisMeals;
    @InjectView(2131429228)
    LinearLayout       mLlAnalysisNutrients;
    @InjectView(2131429249)
    LinearLayout       mLlAnalysisScore;
    @InjectView(2131429224)
    LinearLayout       mLlMicroNutrients;
    @InjectView(2131427603)
    LinearLayout       mLlTodayAnalysis;
    @InjectView(2131429226)
    ListView           mLvMicroNutrients;
    @InjectView(2131427957)
    ScrollView         mScrollView;
    @InjectView(2131429233)
    RiseNumberTextView mTvCalorieAmount;
    @InjectView(2131429234)
    RiseNumberTextView mTvCaloriePercent;
    @InjectView(2131429236)
    TextView           mTvCarbohydrateState;
    @InjectView(2131429239)
    RiseNumberTextView mTvFatAmount;
    @InjectView(2131429240)
    RiseNumberTextView mTvFatPercent;
    @InjectView(2131429242)
    TextView           mTvFatState;
    @InjectView(2131429215)
    RiseNumberTextView mTvMealsBreakfast;
    @InjectView(2131429216)
    TextView           mTvMealsBreakfastState;
    @InjectView(2131429221)
    RiseNumberTextView mTvMealsDinner;
    @InjectView(2131429222)
    TextView           mTvMealsDinnerState;
    @InjectView(2131429218)
    RiseNumberTextView mTvMealsLunch;
    @InjectView(2131429219)
    TextView           mTvMealsLunchState;
    @InjectView(2131429225)
    TextView           mTvMicroTitle;
    @InjectView(2131429244)
    RiseNumberTextView mTvProteinAmount;
    @InjectView(2131429245)
    RiseNumberTextView mTvProteinPercent;
    @InjectView(2131429247)
    TextView           mTvProteinState;
    @InjectView(2131429250)
    RiseNumberTextView mTvScore;
    private List<MicroNutrientsItem> microList = new ArrayList();
    @InjectView(2131429213)
    PieChartView        pie_meals;
    @InjectView(2131429229)
    PieChartView        pie_nutrients;
    @InjectView(2131427773)
    CircularProgressBar progress_bar;
    private String record_on;

    class BitmapAsync extends AsyncTask<Void, Void, Bitmap> {
        ImageView iv_content;
        View      view_share_summary;

        BitmapAsync() {
        }

        protected void onPreExecute() {
            super.onPreExecute();
            Helper.showToast((CharSequence) "正在分享中...");
            this.view_share_summary = LayoutInflater.from(TodayAnalysisActivity.this.activity)
                    .inflate(R.layout.qq, null);
            ((TextView) this.view_share_summary.findViewById(R.id.tv_date)).setText((TextUtils
                    .isEmpty(TodayAnalysisActivity.this.record_on) ? "" : DateFormatUtils
                    .string2String(TodayAnalysisActivity.this.record_on, "M月d日")) + "薄荷饮食分析");
            this.iv_content = (ImageView) this.view_share_summary.findViewById(R.id.iv_content);
        }

        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (bitmap != null) {
                Bitmap bitmap2;
                this.iv_content.setImageBitmap(bitmap);
                Bitmap bitmap_share = BitmapUtil.loadBitmapFromView(this.view_share_summary);
                Context access$700 = TodayAnalysisActivity.this.activity;
                if (bitmap_share == null) {
                    bitmap2 = bitmap;
                } else {
                    bitmap2 = bitmap_share;
                }
                if (!TextUtils.isEmpty(FileUtil.getPNGImagePath(access$700, bitmap2,
                        "SHARE_4_LINECHART"))) {
                    ShareManager.shareLocalImage(TodayAnalysisActivity.this.activity, filePath);
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
            return BitmapUtil.getBitmapByView(TodayAnalysisActivity.this.mLlTodayAnalysis);
        }
    }

    public static void start(Context context, String record_on) {
        Intent starter = new Intent(context, TodayAnalysisActivity.class);
        starter.putExtra(EXTRA_DATE, record_on);
        context.startActivity(starter);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.y, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                MobclickAgent.onEvent(this.activity, Event.TOOL_DAILYANALYZE_SHARE);
                share();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dl);
        ButterKnife.inject((Activity) this);
        this.record_on = getIntent().getStringExtra(EXTRA_DATE);
        requestDailyAnalysis();
    }

    private void requestDailyAnalysis() {
        showLoading();
        RecordApi.getDailyAnalysis(this.activity, this.record_on, new JsonCallback(this.activity) {
            public void ok(JSONObject object) {
                super.ok(object);
                TodayAnalysis todayAnalysis = (TodayAnalysis) FastJsonUtils.fromJson(object,
                        TodayAnalysis.class);
                if (todayAnalysis != null) {
                    TodayAnalysisActivity.this.initScore(todayAnalysis.score);
                    if (todayAnalysis.overview != null) {
                        TodayAnalysisActivity.this.initTodayOverview(todayAnalysis.overview);
                    }
                    if (todayAnalysis.diets != null) {
                        TodayAnalysisActivity.this.initTodayDiets(todayAnalysis.diets);
                    }
                    if (todayAnalysis.nuturitions != null) {
                        TodayAnalysisActivity.this.initTodayNutrients(todayAnalysis.nuturitions);
                    }
                    if (todayAnalysis.micro_nuturitions != null) {
                        TodayAnalysisActivity.this.initTodayMicroNutrients(todayAnalysis
                                .micro_nuturitions);
                    }
                    TodayAnalysisActivity.this.mScrollView.setVisibility(0);
                }
            }

            public void onFinish() {
                super.onFinish();
                TodayAnalysisActivity.this.dismissLoading();
            }
        });
    }

    private void initScore(int score) {
        ViewUtils.startRiseNumber(this.mTvScore, score);
        this.progress_bar.animateProgess(score, new OvershootInterpolator());
    }

    private void initTodayOverview(TodayOverview todayOverview) {
        int i = R.drawable.a1j;
        if (todayOverview != null) {
            this.mIvCaloryStatus.setImageResource(todayOverview.calory_status ? R.drawable.a1j :
                    R.drawable.a1p);
            this.mIvDietStatus.setImageResource(todayOverview.diet_status ? R.drawable.a1j : R
                    .drawable.a1p);
            ImageView imageView = this.mIvNuturitionStatus;
            if (!todayOverview.nuturition_status) {
                i = R.drawable.a1p;
            }
            imageView.setImageResource(i);
        }
    }

    private void initTodayDiets(TodayDiets todayDiets) {
        if (todayDiets == null || (todayDiets.breakfast.percentage == 0.0f && todayDiets.lunch
                .percentage == 0.0f && todayDiets.dinner.percentage == 0.0f)) {
            this.mLlAnalysisMeals.setVisibility(8);
            return;
        }
        this.mLlAnalysisMeals.setVisibility(0);
        switchMeals(this.mTvMealsBreakfast, this.mTvMealsBreakfastState, this.mIvMealsBreakfast,
                todayDiets.breakfast);
        switchMeals(this.mTvMealsLunch, this.mTvMealsLunchState, this.mIvMealsLunch, todayDiets
                .lunch);
        switchMeals(this.mTvMealsDinner, this.mTvMealsDinnerState, this.mIvMealsDinner,
                todayDiets.dinner);
        ChartAnalysisHelper.initMealsPie(this.pie_meals, todayDiets);
    }

    private void initTodayNutrients(TodayNutrients todayNutrients) {
        if (todayNutrients == null || (todayNutrients.carbohydrate.percentage == 0.0f &&
                todayNutrients.protein.percentage == 0.0f && todayNutrients.fat.percentage == 0
        .0f)) {
            this.mLlAnalysisNutrients.setVisibility(8);
            return;
        }
        this.mLlAnalysisNutrients.setVisibility(0);
        switchNutrients(this.mTvCaloriePercent, this.mTvCalorieAmount, this.mTvCarbohydrateState,
                this.mIvCarbohydrateState, todayNutrients.carbohydrate);
        switchNutrients(this.mTvProteinPercent, this.mTvProteinAmount, this.mTvProteinState, this
                .mIvProteinState, todayNutrients.protein);
        switchNutrients(this.mTvFatPercent, this.mTvFatAmount, this.mTvFatState, this
                .mIvFatState, todayNutrients.fat);
        ChartAnalysisHelper.initNutrientsPie(this.pie_nutrients, todayNutrients);
    }

    private void initTodayMicroNutrients(TodayMicroNutrients microNutrients) {
        if (microNutrients == null || microNutrients.items.size() == 0) {
            this.mLlMicroNutrients.setVisibility(8);
            return;
        }
        this.mLlMicroNutrients.setVisibility(0);
        this.mTvMicroTitle.setText("微量营养素");
        if (microNutrients.good_count > 0) {
            this.mBtMicroMore.setText(String.format("有%d项达标", new Object[]{Integer.valueOf
                    (microNutrients.good_count)}));
        } else {
            this.mBtMicroMore.setVisibility(8);
        }
        this.microList.clear();
        this.microList.addAll(microNutrients.items);
        this.mLvMicroNutrients.setAdapter(new MicroNutrientsAdapter(this.activity,
                filterMicroData(microNutrients.items)));
    }

    private List<MicroNutrientsItem> filterMicroData(List<MicroNutrientsItem> list) {
        List<MicroNutrientsItem> result = new ArrayList();
        if (!(list == null || list.size() == 0)) {
            for (MicroNutrientsItem item : list) {
                if (!TextUtils.equals(DESCRIPTION_TYPE.good.name(), item.desc)) {
                    result.add(item);
                }
            }
        }
        return result;
    }

    private void switchMeals(TextView tv_meal, TextView tv_meals_state, ImageView iv_meals_state,
                             TodayItem todayItem) {
        tv_meal.setText((todayItem.percentage > 0.0f ? (int) (todayItem.percentage * 100.0f) : 0)
                + "%");
        if (TextUtils.equals(DESCRIPTION_TYPE.less.name(), todayItem.description)) {
            tv_meals_state.setText(getString(R.string.bq));
            iv_meals_state.setImageResource(R.drawable.a1k);
        } else if (TextUtils.equals(DESCRIPTION_TYPE.much.name(), todayItem.description)) {
            tv_meals_state.setText(getString(R.string.bp));
            iv_meals_state.setImageResource(R.drawable.a1o);
        } else if (TextUtils.equals(DESCRIPTION_TYPE.good.name(), todayItem.description)) {
            tv_meals_state.setText(getString(R.string.br));
            iv_meals_state.setImageResource(R.drawable.a1j);
        }
    }

    private void switchNutrients(TextView tv_percent, TextView tv_amount, TextView
            tv_meals_state, ImageView iv_meals_state, TodayItem todayItem) {
        if (todayItem != null) {
            int i;
            String format;
            StringBuilder stringBuilder = new StringBuilder();
            if (todayItem.percentage > 0.0f) {
                i = (int) (todayItem.percentage * 100.0f);
            } else {
                i = 0;
            }
            tv_percent.setText(stringBuilder.append(i).append("%").toString());
            stringBuilder = new StringBuilder();
            if (todayItem.weight > 0.0f) {
                format = String.format("%.1f", new Object[]{Float.valueOf(todayItem.weight)});
            } else {
                format = "0";
            }
            tv_amount.setText(stringBuilder.append(format).append("克").toString());
            if (TextUtils.equals(DESCRIPTION_TYPE.less.name(), todayItem.description)) {
                tv_meals_state.setText(getString(R.string.bq));
                iv_meals_state.setImageResource(R.drawable.a1k);
            } else if (TextUtils.equals(DESCRIPTION_TYPE.much.name(), todayItem.description)) {
                tv_meals_state.setText(getString(R.string.bp));
                iv_meals_state.setImageResource(R.drawable.a1o);
            } else if (TextUtils.equals(DESCRIPTION_TYPE.good.name(), todayItem.description)) {
                tv_meals_state.setText(getString(R.string.br));
                iv_meals_state.setImageResource(R.drawable.a1j);
            }
        }
    }

    @OnClick({2131429214, 2131429230, 2131429227, 2131429248})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_meals_knowledge:
                BrowserActivity.comeOnBaby(this.activity, "三餐比例知识", "http://shop.boohee" +
                        ".com/store/pages/three_meal");
                return;
            case R.id.bt_micro_more:
                if (this.microList.size() > 0) {
                    this.mLvMicroNutrients.setAdapter(new MicroNutrientsAdapter(this.activity,
                            this.microList));
                }
                this.mBtMicroMore.setVisibility(8);
                return;
            case R.id.tv_nurients_knowledge:
                BrowserActivity.comeOnBaby(this.activity, "营养比例知识", "http://shop.boohee" +
                        ".com/store/pages/nutrition_distribution");
                return;
            case R.id.btn_today_nutrition:
                TodayNutritionActivity.start(this, this.record_on);
                return;
            default:
                return;
        }
    }

    private void share() {
        new BitmapAsync().execute(new Void[0]);
    }

    public void onDestroy() {
        super.onDestroy();
        ButterKnife.reset(this);
    }
}
