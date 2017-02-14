package com.boohee.one.ui;

import android.view.View;
import android.widget.LinearLayout;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

import com.boohee.one.R;

public class TodayNutritionActivity$$ViewInjector<T extends TodayNutritionActivity> implements
        Injector<T> {
    public void inject(Finder finder, T target, Object source) {
        target.mLlNutritionGroup = (LinearLayout) finder.castView((View) finder.findRequiredView
                (source, R.id.ll_nutrition_group, "field 'mLlNutritionGroup'"), R.id
                .ll_nutrition_group, "field 'mLlNutritionGroup'");
    }

    public void reset(T target) {
        target.mLlNutritionGroup = null;
    }
}
