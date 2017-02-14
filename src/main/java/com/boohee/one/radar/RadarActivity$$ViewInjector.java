package com.boohee.one.radar;

import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;
import butterknife.internal.DebouncingOnClickListener;

import com.boohee.one.R;

public class RadarActivity$$ViewInjector<T extends RadarActivity> implements Injector<T> {
    public void inject(Finder finder, final T target, Object source) {
        View view = (View) finder.findRequiredView(source, R.id.iv_question, "field 'ivQuestion' " +
                "and method 'startAnim'");
        target.ivQuestion = view;
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.startAnim(p0);
            }
        });
        target.radarView = (RadarLayout) finder.castView((View) finder.findRequiredView(source, R
                .id.razor_layout, "field 'radarView'"), R.id.razor_layout, "field 'radarView'");
        target.tvRadarIndex = (TextView) finder.castView((View) finder.findRequiredView(source, R
                .id.tv_radar_index, "field 'tvRadarIndex'"), R.id.tv_radar_index, "field " +
                "'tvRadarIndex'");
        target.tvRank = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_rank, "field 'tvRank'"), R.id.tv_rank, "field 'tvRank'");
        target.tvReportTime = (TextView) finder.castView((View) finder.findRequiredView(source, R
                .id.tv_report_time, "field 'tvReportTime'"), R.id.tv_report_time, "field " +
                "'tvReportTime'");
        target.nutritionLayout = (NutritionLayout) finder.castView((View) finder.findRequiredView
                (source, R.id.layout_nutrition, "field 'nutritionLayout'"), R.id
                .layout_nutrition, "field 'nutritionLayout'");
        target.ringNutrition = (RingView) finder.castView((View) finder.findRequiredView(source,
                R.id.ring_nutrition, "field 'ringNutrition'"), R.id.ring_nutrition, "field " +
                "'ringNutrition'");
        target.tvVegTitle = (TextView) finder.castView((View) finder.findRequiredView(source, R
                .id.tv_veg_title, "field 'tvVegTitle'"), R.id.tv_veg_title, "field 'tvVegTitle'");
        target.tvVegContent = (TextView) finder.castView((View) finder.findRequiredView(source, R
                .id.tv_veg_content, "field 'tvVegContent'"), R.id.tv_veg_content, "field " +
                "'tvVegContent'");
        target.tvProteinTitle = (TextView) finder.castView((View) finder.findRequiredView(source,
                R.id.tv_protein_title, "field 'tvProteinTitle'"), R.id.tv_protein_title, "field " +
                "'tvProteinTitle'");
        target.tvProteinContent = (TextView) finder.castView((View) finder.findRequiredView
                (source, R.id.tv_protein_content, "field 'tvProteinContent'"), R.id
                .tv_protein_content, "field 'tvProteinContent'");
        target.tvSpiritSport = (TextView) finder.castView((View) finder.findRequiredView(source,
                R.id.tv_spirit_sport, "field 'tvSpiritSport'"), R.id.tv_spirit_sport, "field " +
                "'tvSpiritSport'");
        target.tvSpiritDiet = (TextView) finder.castView((View) finder.findRequiredView(source, R
                .id.tv_spirit_diet, "field 'tvSpiritDiet'"), R.id.tv_spirit_diet, "field " +
                "'tvSpiritDiet'");
        target.tvSpiritWeight = (TextView) finder.castView((View) finder.findRequiredView(source,
                R.id.tv_spirit_weight, "field 'tvSpiritWeight'"), R.id.tv_spirit_weight, "field " +
                "'tvSpiritWeight'");
        target.tvSpiritTitle = (TextView) finder.castView((View) finder.findRequiredView(source,
                R.id.tv_spirit_title, "field 'tvSpiritTitle'"), R.id.tv_spirit_title, "field " +
                "'tvSpiritTitle'");
        target.tvSpiritContent = (TextView) finder.castView((View) finder.findRequiredView
                (source, R.id.tv_spirit_content, "field 'tvSpiritContent'"), R.id
                .tv_spirit_content, "field 'tvSpiritContent'");
        target.ringHeat = (RingView) finder.castView((View) finder.findRequiredView(source, R.id
                .ring_heat, "field 'ringHeat'"), R.id.ring_heat, "field 'ringHeat'");
        target.heatText = (View) finder.findRequiredView(source, R.id.heat_text, "field " +
                "'heatText'");
        target.tvHeatDay = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_heat_day, "field 'tvHeatDay'"), R.id.tv_heat_day, "field 'tvHeatDay'");
        target.tvBalanceTitle = (TextView) finder.castView((View) finder.findRequiredView(source,
                R.id.tv_balance_title, "field 'tvBalanceTitle'"), R.id.tv_balance_title, "field " +
                "'tvBalanceTitle'");
        target.tvBalanceContent = (TextView) finder.castView((View) finder.findRequiredView
                (source, R.id.tv_balance_content, "field 'tvBalanceContent'"), R.id
                .tv_balance_content, "field 'tvBalanceContent'");
        target.sportLayout = (SportLayout) finder.castView((View) finder.findRequiredView(source,
                R.id.layout_sport, "field 'sportLayout'"), R.id.layout_sport, "field " +
                "'sportLayout'");
        target.tvSportTitle = (TextView) finder.castView((View) finder.findRequiredView(source, R
                .id.tv_sports_title, "field 'tvSportTitle'"), R.id.tv_sports_title, "field " +
                "'tvSportTitle'");
        target.tvSportContent = (TextView) finder.castView((View) finder.findRequiredView(source,
                R.id.tv_sports_content, "field 'tvSportContent'"), R.id.tv_sports_content, "field" +
                " 'tvSportContent'");
        target.tvSocialFriend = (TextView) finder.castView((View) finder.findRequiredView(source,
                R.id.tv_social_friend, "field 'tvSocialFriend'"), R.id.tv_social_friend, "field " +
                "'tvSocialFriend'");
        target.tvSocialPost = (TextView) finder.castView((View) finder.findRequiredView(source, R
                .id.tv_social_post, "field 'tvSocialPost'"), R.id.tv_social_post, "field " +
                "'tvSocialPost'");
        target.tvSocialTitle = (TextView) finder.castView((View) finder.findRequiredView(source,
                R.id.tv_social_title, "field 'tvSocialTitle'"), R.id.tv_social_title, "field " +
                "'tvSocialTitle'");
        target.tvSocialContent = (TextView) finder.castView((View) finder.findRequiredView
                (source, R.id.tv_social_content, "field 'tvSocialContent'"), R.id
                .tv_social_content, "field 'tvSocialContent'");
        target.summaryConclusion = (ConclusionLayout) finder.castView((View) finder
                .findRequiredView(source, R.id.summary_conclusion, "field 'summaryConclusion'"),
                R.id.summary_conclusion, "field 'summaryConclusion'");
        target.ivWeightTrend = (ImageView) finder.castView((View) finder.findRequiredView(source,
                R.id.iv_weight_trend, "field 'ivWeightTrend'"), R.id.iv_weight_trend, "field " +
                "'ivWeightTrend'");
        target.tvWeightTrend = (TextView) finder.castView((View) finder.findRequiredView(source,
                R.id.tv_weight_trend, "field 'tvWeightTrend'"), R.id.tv_weight_trend, "field " +
                "'tvWeightTrend'");
        target.emptyDietElement = (TextView) finder.castView((View) finder.findRequiredView
                (source, R.id.empty_diet_element, "field 'emptyDietElement'"), R.id
                .empty_diet_element, "field 'emptyDietElement'");
        target.emptyHeat = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .empty_heat, "field 'emptyHeat'"), R.id.empty_heat, "field 'emptyHeat'");
        target.scrollView = (ScrollView) finder.castView((View) finder.findRequiredView(source, R
                .id.scroll, "field 'scrollView'"), R.id.scroll, "field 'scrollView'");
        target.stubNew = (ViewStub) finder.castView((View) finder.findRequiredView(source, R.id
                .newby, "field 'stubNew'"), R.id.newby, "field 'stubNew'");
    }

    public void reset(T target) {
        target.ivQuestion = null;
        target.radarView = null;
        target.tvRadarIndex = null;
        target.tvRank = null;
        target.tvReportTime = null;
        target.nutritionLayout = null;
        target.ringNutrition = null;
        target.tvVegTitle = null;
        target.tvVegContent = null;
        target.tvProteinTitle = null;
        target.tvProteinContent = null;
        target.tvSpiritSport = null;
        target.tvSpiritDiet = null;
        target.tvSpiritWeight = null;
        target.tvSpiritTitle = null;
        target.tvSpiritContent = null;
        target.ringHeat = null;
        target.heatText = null;
        target.tvHeatDay = null;
        target.tvBalanceTitle = null;
        target.tvBalanceContent = null;
        target.sportLayout = null;
        target.tvSportTitle = null;
        target.tvSportContent = null;
        target.tvSocialFriend = null;
        target.tvSocialPost = null;
        target.tvSocialTitle = null;
        target.tvSocialContent = null;
        target.summaryConclusion = null;
        target.ivWeightTrend = null;
        target.tvWeightTrend = null;
        target.emptyDietElement = null;
        target.emptyHeat = null;
        target.scrollView = null;
        target.stubNew = null;
    }
}
