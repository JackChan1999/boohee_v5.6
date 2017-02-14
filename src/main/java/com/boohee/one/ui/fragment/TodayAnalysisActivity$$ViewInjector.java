package com.boohee.one.ui.fragment;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;
import butterknife.internal.DebouncingOnClickListener;

import com.boohee.myview.risenumber.RiseNumberTextView;
import com.boohee.one.R;
import com.boohee.widgets.CircularProgressBar;

import lecho.lib.hellocharts.view.PieChartView;

public class TodayAnalysisActivity$$ViewInjector<T extends TodayAnalysisActivity> implements
        Injector<T> {
    public void inject(Finder finder, final T target, Object source) {
        target.mScrollView = (ScrollView) finder.castView((View) finder.findRequiredView(source,
                R.id.sv_today_analysis, "field 'mScrollView'"), R.id.sv_today_analysis, "field " +
                "'mScrollView'");
        target.mTvScore = (RiseNumberTextView) finder.castView((View) finder.findRequiredView
                (source, R.id.tv_score, "field 'mTvScore'"), R.id.tv_score, "field 'mTvScore'");
        target.mIvCaloryStatus = (ImageView) finder.castView((View) finder.findRequiredView
                (source, R.id.iv_calory_status, "field 'mIvCaloryStatus'"), R.id
                .iv_calory_status, "field 'mIvCaloryStatus'");
        target.mIvDietStatus = (ImageView) finder.castView((View) finder.findRequiredView(source,
                R.id.iv_diet_status, "field 'mIvDietStatus'"), R.id.iv_diet_status, "field " +
                "'mIvDietStatus'");
        target.mIvNuturitionStatus = (ImageView) finder.castView((View) finder.findRequiredView
                (source, R.id.iv_nuturition_status, "field 'mIvNuturitionStatus'"), R.id
                .iv_nuturition_status, "field 'mIvNuturitionStatus'");
        target.mLlAnalysisScore = (LinearLayout) finder.castView((View) finder.findRequiredView
                (source, R.id.ll_analysis_score, "field 'mLlAnalysisScore'"), R.id
                .ll_analysis_score, "field 'mLlAnalysisScore'");
        target.mTvMealsBreakfast = (RiseNumberTextView) finder.castView((View) finder
                .findRequiredView(source, R.id.tv_meals_breakfast, "field 'mTvMealsBreakfast'"),
                R.id.tv_meals_breakfast, "field 'mTvMealsBreakfast'");
        target.mTvMealsBreakfastState = (TextView) finder.castView((View) finder.findRequiredView
                (source, R.id.tv_meals_breakfast_state, "field 'mTvMealsBreakfastState'"), R.id
                .tv_meals_breakfast_state, "field 'mTvMealsBreakfastState'");
        target.mIvMealsBreakfast = (ImageView) finder.castView((View) finder.findRequiredView
                (source, R.id.iv_meals_breakfast, "field 'mIvMealsBreakfast'"), R.id
                .iv_meals_breakfast, "field 'mIvMealsBreakfast'");
        target.mTvMealsLunch = (RiseNumberTextView) finder.castView((View) finder
                .findRequiredView(source, R.id.tv_meals_lunch, "field 'mTvMealsLunch'"), R.id
                .tv_meals_lunch, "field 'mTvMealsLunch'");
        target.mTvMealsLunchState = (TextView) finder.castView((View) finder.findRequiredView
                (source, R.id.tv_meals_lunch_state, "field 'mTvMealsLunchState'"), R.id
                .tv_meals_lunch_state, "field 'mTvMealsLunchState'");
        target.mIvMealsLunch = (ImageView) finder.castView((View) finder.findRequiredView(source,
                R.id.iv_meals_lunch, "field 'mIvMealsLunch'"), R.id.iv_meals_lunch, "field " +
                "'mIvMealsLunch'");
        target.mTvMealsDinner = (RiseNumberTextView) finder.castView((View) finder
                .findRequiredView(source, R.id.tv_meals_dinner, "field 'mTvMealsDinner'"), R.id
                .tv_meals_dinner, "field 'mTvMealsDinner'");
        target.mTvMealsDinnerState = (TextView) finder.castView((View) finder.findRequiredView
                (source, R.id.tv_meals_dinner_state, "field 'mTvMealsDinnerState'"), R.id
                .tv_meals_dinner_state, "field 'mTvMealsDinnerState'");
        target.mIvMealsDinner = (ImageView) finder.castView((View) finder.findRequiredView
                (source, R.id.iv_meals_dinner, "field 'mIvMealsDinner'"), R.id.iv_meals_dinner,
                "field 'mIvMealsDinner'");
        target.mLlAnalysisMeals = (LinearLayout) finder.castView((View) finder.findRequiredView
                (source, R.id.ll_analysis_meals, "field 'mLlAnalysisMeals'"), R.id
                .ll_analysis_meals, "field 'mLlAnalysisMeals'");
        target.mTvCaloriePercent = (RiseNumberTextView) finder.castView((View) finder
                .findRequiredView(source, R.id.tv_calorie_percent, "field 'mTvCaloriePercent'"),
                R.id.tv_calorie_percent, "field 'mTvCaloriePercent'");
        target.mTvCalorieAmount = (RiseNumberTextView) finder.castView((View) finder
                .findRequiredView(source, R.id.tv_calorie_amount, "field 'mTvCalorieAmount'"), R
                .id.tv_calorie_amount, "field 'mTvCalorieAmount'");
        target.mTvCarbohydrateState = (TextView) finder.castView((View) finder.findRequiredView
                (source, R.id.tv_carbohydrate_state, "field 'mTvCarbohydrateState'"), R.id
                .tv_carbohydrate_state, "field 'mTvCarbohydrateState'");
        target.mIvCarbohydrateState = (ImageView) finder.castView((View) finder.findRequiredView
                (source, R.id.iv_carbohydrate_state, "field 'mIvCarbohydrateState'"), R.id
                .iv_carbohydrate_state, "field 'mIvCarbohydrateState'");
        target.mTvFatPercent = (RiseNumberTextView) finder.castView((View) finder
                .findRequiredView(source, R.id.tv_fat_percent, "field 'mTvFatPercent'"), R.id
                .tv_fat_percent, "field 'mTvFatPercent'");
        target.mTvFatAmount = (RiseNumberTextView) finder.castView((View) finder.findRequiredView
                (source, R.id.tv_fat_amount, "field 'mTvFatAmount'"), R.id.tv_fat_amount, "field " +
                "'mTvFatAmount'");
        target.mTvFatState = (TextView) finder.castView((View) finder.findRequiredView(source, R
                .id.tv_fat_state, "field 'mTvFatState'"), R.id.tv_fat_state, "field 'mTvFatState'");
        target.mIvFatState = (ImageView) finder.castView((View) finder.findRequiredView(source, R
                .id.iv_fat_state, "field 'mIvFatState'"), R.id.iv_fat_state, "field 'mIvFatState'");
        target.mTvProteinPercent = (RiseNumberTextView) finder.castView((View) finder
                .findRequiredView(source, R.id.tv_protein_percent, "field 'mTvProteinPercent'"),
                R.id.tv_protein_percent, "field 'mTvProteinPercent'");
        target.mTvProteinAmount = (RiseNumberTextView) finder.castView((View) finder
                .findRequiredView(source, R.id.tv_protein_amount, "field 'mTvProteinAmount'"), R
                .id.tv_protein_amount, "field 'mTvProteinAmount'");
        target.mTvProteinState = (TextView) finder.castView((View) finder.findRequiredView
                (source, R.id.tv_protein_state, "field 'mTvProteinState'"), R.id
                .tv_protein_state, "field 'mTvProteinState'");
        target.mIvProteinState = (ImageView) finder.castView((View) finder.findRequiredView
                (source, R.id.iv_protein_state, "field 'mIvProteinState'"), R.id
                .iv_protein_state, "field 'mIvProteinState'");
        target.mLlAnalysisNutrients = (LinearLayout) finder.castView((View) finder
                .findRequiredView(source, R.id.ll_analysis_nutrients, "field " +
                        "'mLlAnalysisNutrients'"), R.id.ll_analysis_nutrients, "field " +
                "'mLlAnalysisNutrients'");
        target.mLlTodayAnalysis = (LinearLayout) finder.castView((View) finder.findRequiredView
                (source, R.id.ll_today_analysis, "field 'mLlTodayAnalysis'"), R.id
                .ll_today_analysis, "field 'mLlTodayAnalysis'");
        target.mLlMicroNutrients = (LinearLayout) finder.castView((View) finder.findRequiredView
                (source, R.id.ll_analysis_micro_nutrients, "field 'mLlMicroNutrients'"), R.id
                .ll_analysis_micro_nutrients, "field 'mLlMicroNutrients'");
        target.mTvMicroTitle = (TextView) finder.castView((View) finder.findRequiredView(source,
                R.id.tv_micro_title, "field 'mTvMicroTitle'"), R.id.tv_micro_title, "field " +
                "'mTvMicroTitle'");
        View view = (View) finder.findRequiredView(source, R.id.bt_micro_more, "field " +
                "'mBtMicroMore' and method 'onClick'");
        target.mBtMicroMore = (Button) finder.castView(view, R.id.bt_micro_more, "field " +
                "'mBtMicroMore'");
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        target.mLvMicroNutrients = (ListView) finder.castView((View) finder.findRequiredView
                (source, R.id.lv_micro_nutrients, "field 'mLvMicroNutrients'"), R.id
                .lv_micro_nutrients, "field 'mLvMicroNutrients'");
        target.progress_bar = (CircularProgressBar) finder.castView((View) finder
                .findRequiredView(source, R.id.progress_bar, "field 'progress_bar'"), R.id
                .progress_bar, "field 'progress_bar'");
        target.pie_meals = (PieChartView) finder.castView((View) finder.findRequiredView(source,
                R.id.pie_meals, "field 'pie_meals'"), R.id.pie_meals, "field 'pie_meals'");
        target.pie_nutrients = (PieChartView) finder.castView((View) finder.findRequiredView
                (source, R.id.pie_nutrients, "field 'pie_nutrients'"), R.id.pie_nutrients, "field" +
                " 'pie_nutrients'");
        ((View) finder.findRequiredView(source, R.id.tv_meals_knowledge, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        ((View) finder.findRequiredView(source, R.id.tv_nurients_knowledge, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        ((View) finder.findRequiredView(source, R.id.btn_today_nutrition, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
    }

    public void reset(T target) {
        target.mScrollView = null;
        target.mTvScore = null;
        target.mIvCaloryStatus = null;
        target.mIvDietStatus = null;
        target.mIvNuturitionStatus = null;
        target.mLlAnalysisScore = null;
        target.mTvMealsBreakfast = null;
        target.mTvMealsBreakfastState = null;
        target.mIvMealsBreakfast = null;
        target.mTvMealsLunch = null;
        target.mTvMealsLunchState = null;
        target.mIvMealsLunch = null;
        target.mTvMealsDinner = null;
        target.mTvMealsDinnerState = null;
        target.mIvMealsDinner = null;
        target.mLlAnalysisMeals = null;
        target.mTvCaloriePercent = null;
        target.mTvCalorieAmount = null;
        target.mTvCarbohydrateState = null;
        target.mIvCarbohydrateState = null;
        target.mTvFatPercent = null;
        target.mTvFatAmount = null;
        target.mTvFatState = null;
        target.mIvFatState = null;
        target.mTvProteinPercent = null;
        target.mTvProteinAmount = null;
        target.mTvProteinState = null;
        target.mIvProteinState = null;
        target.mLlAnalysisNutrients = null;
        target.mLlTodayAnalysis = null;
        target.mLlMicroNutrients = null;
        target.mTvMicroTitle = null;
        target.mBtMicroMore = null;
        target.mLvMicroNutrients = null;
        target.progress_bar = null;
        target.pie_meals = null;
        target.pie_nutrients = null;
    }
}
