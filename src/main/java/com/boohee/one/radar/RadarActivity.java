package com.boohee.one.radar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;
import android.view.ViewTreeObserver.OnScrollChangedListener;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.boohee.main.GestureActivity;
import com.boohee.one.R;
import com.boohee.one.radar.entity.Balance;
import com.boohee.one.radar.entity.Dietary;
import com.boohee.one.radar.entity.Element;
import com.boohee.one.radar.entity.Radar;
import com.boohee.one.radar.entity.Social;
import com.boohee.one.radar.entity.Spirit;
import com.boohee.one.radar.entity.Sports;

import java.util.Map;

public class RadarActivity extends GestureActivity {
    public static final String STATUS = "status";
    public static final String TYPE   = "type";
    @InjectView(2131428610)
    TextView emptyDietElement;
    @InjectView(2131428599)
    TextView emptyHeat;
    private final int[]   heatColor          = new int[]{-16744961, -19790};
    private final float[] heatDefaultPercent = new float[]{0.5f, 0.5f};
    private final int[]   heatEmptyColor     = new int[]{-4990979, -4627};
    @InjectView(2131428600)
    View      heatText;
    @InjectView(2131428625)
    View      ivQuestion;
    @InjectView(2131427841)
    ImageView ivWeightTrend;
    private RadarPresenter mPresenter;
    private final int[]   nutritionColor          = new int[]{-12477447, -7937036, -132133};
    private final float[] nutritionDefaultPercent = new float[]{0.333f, 0.334f, 0.333f};
    private final int[]   nutritionEmptyColor     = new int[]{-6238724, -4001287, -66069};
    @InjectView(2131428606)
    NutritionLayout  nutritionLayout;
    @InjectView(2131428628)
    RadarLayout      radarView;
    @InjectView(2131428598)
    RingView         ringHeat;
    @InjectView(2131428609)
    RingView         ringNutrition;
    @InjectView(2131427838)
    ScrollView       scrollView;
    @InjectView(2131428622)
    SportLayout      sportLayout;
    @InjectView(2131427842)
    ViewStub         stubNew;
    @InjectView(2131427839)
    ConclusionLayout summaryConclusion;
    @InjectView(2131428605)
    TextView         tvBalanceContent;
    @InjectView(2131428604)
    TextView         tvBalanceTitle;
    @InjectView(2131428603)
    TextView         tvHeatDay;
    @InjectView(2131428612)
    TextView         tvProteinContent;
    @InjectView(2131428611)
    TextView         tvProteinTitle;
    @InjectView(2131428626)
    TextView         tvRadarIndex;
    @InjectView(2131428627)
    TextView         tvRank;
    @InjectView(2131428630)
    TextView         tvReportTime;
    @InjectView(2131428616)
    TextView         tvSocialContent;
    @InjectView(2131428614)
    TextView         tvSocialFriend;
    @InjectView(2131428613)
    TextView         tvSocialPost;
    @InjectView(2131428615)
    TextView         tvSocialTitle;
    @InjectView(2131428621)
    TextView         tvSpiritContent;
    @InjectView(2131428618)
    TextView         tvSpiritDiet;
    @InjectView(2131428617)
    TextView         tvSpiritSport;
    @InjectView(2131428620)
    TextView         tvSpiritTitle;
    @InjectView(2131428619)
    TextView         tvSpiritWeight;
    @InjectView(2131428624)
    TextView         tvSportContent;
    @InjectView(2131428623)
    TextView         tvSportTitle;
    @InjectView(2131428608)
    TextView         tvVegContent;
    @InjectView(2131428607)
    TextView         tvVegTitle;
    @InjectView(2131427840)
    TextView         tvWeightTrend;

    public static void startActivity(Context context, String status, String type) {
        Intent i = new Intent(context, RadarActivity.class);
        i.putExtra("status", status);
        i.putExtra("type", type);
        context.startActivity(i);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.RadarActivity);
        ButterKnife.inject((Activity) this);
        this.mPresenter = new RadarPresenter(this);
        this.mPresenter.onCreate(getStringExtra("status"), getStringExtra("type"));
        initView();
    }

    private void initView() {
        this.scrollView.getViewTreeObserver().addOnScrollChangedListener(new OnScrollChangedListener() {
            int HEIGHT = RadarActivity.this.getResources().getDisplayMetrics().heightPixels;
            int[] area = new int[2];

            public void onScrollChanged() {
                RadarActivity.this.nutritionLayout.getLocationOnScreen(this.area);
                if (this.area[1] + RadarActivity.this.nutritionLayout.getHeight() < this.HEIGHT) {
                    RadarActivity.this.nutritionLayout.startAnim();
                }
                RadarActivity.this.sportLayout.getLocationOnScreen(this.area);
                if (this.area[1] + RadarActivity.this.sportLayout.getHeight() < this.HEIGHT) {
                    RadarActivity.this.sportLayout.startAnim();
                }
                RadarActivity.this.ringNutrition.getLocationOnScreen(this.area);
                if (this.area[1] + RadarActivity.this.ringNutrition.getHeight() < this.HEIGHT) {
                    RadarActivity.this.ringNutrition.startAnim();
                }
                RadarActivity.this.ringHeat.getLocationInWindow(this.area);
                if (this.area[1] + RadarActivity.this.ringHeat.getHeight() < this.HEIGHT) {
                    RadarActivity.this.ringHeat.startAnim();
                }
            }
        });
    }

    public void showNew() {
        this.scrollView.setVisibility(8);
        this.stubNew.setVisibility(0);
    }

    public void setRadar(Radar radar, boolean showAddAnim) {
        this.radarView.setData(radar);
        this.radarView.startAnim(showAddAnim);
        this.tvRadarIndex.setText(String.valueOf(radar.total.total_index));
        this.tvRank.setText(radar.total.text);
        this.tvReportTime.setText(radar.total.report_at);
    }

    public void setDietary(Dietary dietary) {
        if (dietary == null || dietary.isEmpty()) {
            this.nutritionLayout.showEmpty();
        } else {
            this.nutritionLayout.setTarget(new float[]{((float) dietary.grain) / 7.0f, ((float)
                    dietary.meat) / 7.0f, ((float) dietary.veggie) / 7.0f, ((float) dietary
                    .fattiness) / 7.0f});
        }
        if (dietary != null && dietary.message != null) {
            this.tvVegTitle.setText(dietary.message.title);
            this.tvVegContent.setText(dietary.message.content);
        }
    }

    public void setElement(Element element) {
        if (element == null || element.isEmpty()) {
            this.ringNutrition.setColor(this.nutritionEmptyColor);
            this.ringNutrition.setPercent(this.nutritionDefaultPercent);
            this.emptyDietElement.setVisibility(0);
            this.emptyDietElement.setText("暂无数据");
        } else {
            float[] percent = new float[3];
            int sum = (element.carbohydrate + element.protein) + element.fattiness;
            if (sum != 0) {
                percent[0] = (((float) element.carbohydrate) * 1.0f) / ((float) sum);
                percent[1] = (((float) element.protein) * 1.0f) / ((float) sum);
                percent[2] = (((float) element.fattiness) * 1.0f) / ((float) sum);
            }
            this.ringNutrition.setColor(this.nutritionColor);
            this.ringNutrition.setDrawText(true);
            this.ringNutrition.setTarget(percent);
        }
        if (element != null && element.message != null) {
            this.tvProteinTitle.setText(element.message.title);
            this.tvProteinContent.setText(element.message.content);
        }
    }

    public void setSpirit(Spirit spirit) {
        if (spirit == null) {
            this.tvSpiritSport.setText("0天");
            this.tvSpiritDiet.setText("0天");
            this.tvSpiritWeight.setText("0次");
            return;
        }
        this.tvSpiritSport.setText(spirit.sports + "天");
        this.tvSpiritDiet.setText(spirit.food + "天");
        this.tvSpiritWeight.setText(spirit.weight + "次");
        if (spirit.message != null) {
            this.tvSpiritTitle.setText(spirit.message.title);
            this.tvSpiritContent.setText(spirit.message.content);
        }
    }

    public void setBalance(Balance balance) {
        if (balance == null || balance.isEmpty()) {
            this.emptyHeat.setVisibility(0);
            this.emptyHeat.setText("暂无数据");
            this.ringHeat.setPercent(this.heatDefaultPercent);
            this.ringHeat.setColor(this.heatEmptyColor);
            this.heatText.setVisibility(8);
        } else {
            this.ringHeat.setColor(this.heatColor);
            float[] percent = new float[2];
            int sum = balance.qualified + balance.unqualified;
            if (sum != 0) {
                percent[0] = (((float) balance.qualified) * 1.0f) / ((float) sum);
                percent[1] = (((float) balance.unqualified) * 1.0f) / ((float) sum);
                this.ringHeat.setTarget(percent);
            }
            this.tvHeatDay.setText(String.valueOf(balance.qualified));
        }
        if (balance != null && balance.message != null) {
            this.tvBalanceTitle.setText(balance.message.title);
            this.tvBalanceContent.setText(balance.message.content);
        }
    }

    public void setSports(Sports sports) {
        if (sports == null || sports.isEmpty()) {
            this.sportLayout.showEmpty();
        } else {
            this.sportLayout.setTarget(sports.calorie);
        }
        if (sports != null && sports.message != null) {
            this.tvSportTitle.setText(sports.message.title);
            this.tvSportContent.setText(sports.message.content);
        }
    }

    public void setSocial(Social social) {
        if (social == null) {
            this.tvSocialPost.setText("0次");
            this.tvSocialFriend.setText("0次");
            return;
        }
        this.tvSocialPost.setText(social.post + "次");
        this.tvSocialFriend.setText(social.comment + "次");
        if (social.message != null) {
            this.tvSocialTitle.setText(social.message.title);
            this.tvSocialContent.setText(social.message.content);
        }
    }

    public void setSummary(Map<String, String> data) {
        if (data != null) {
            this.summaryConclusion.setData(data);
            String trend = (String) data.get("weight_trend");
            if ("down".equalsIgnoreCase(trend)) {
                this.tvWeightTrend.setText("你的体重呈下降趋势");
                this.ivWeightTrend.setImageResource(R.drawable.a_3);
            } else if ("up".equalsIgnoreCase(trend)) {
                this.tvWeightTrend.setText("你的体重呈上升趋势");
                this.ivWeightTrend.setImageResource(R.drawable.a_o);
            } else {
                this.tvWeightTrend.setText("你的体重为保持状态");
                this.ivWeightTrend.setImageResource(R.drawable.a9w);
            }
        }
    }

    public void showLoadingView() {
        this.emptyDietElement.setText("加载中");
        this.emptyHeat.setText("加载中");
        this.nutritionLayout.showLoading();
        this.sportLayout.showLoading();
        this.radarView.showLoading();
        showDefaultChart();
    }

    private void showDefaultChart() {
        this.emptyDietElement.setVisibility(0);
        this.emptyHeat.setVisibility(0);
        this.ringNutrition.setPercent(this.nutritionDefaultPercent);
        this.ringNutrition.setColor(this.nutritionEmptyColor);
        this.ringNutrition.setDrawText(false);
        this.ringHeat.setPercent(this.heatDefaultPercent);
        this.ringHeat.setColor(this.heatEmptyColor);
        this.heatText.setVisibility(8);
    }

    public void hideEmptyView() {
        this.emptyDietElement.setVisibility(8);
        this.emptyHeat.setVisibility(8);
        this.heatText.setVisibility(0);
    }

    @OnClick({2131428625})
    public void startAnim(View view) {
        RadarDialog.newInstance().show(getSupportFragmentManager(), "Intro");
    }

    protected void onStop() {
        super.onStop();
        this.mPresenter.onStop();
    }
}
