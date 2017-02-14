package com.boohee.food;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

import com.boohee.one.R;

public class FoodListActivity$$ViewInjector<T extends FoodListActivity> implements Injector<T> {
    public void inject(Finder finder, T target, Object source) {
        target.ll_food_list = (LinearLayout) finder.castView((View) finder.findRequiredView
                (source, R.id.ll_food_list, "field 'll_food_list'"), R.id.ll_food_list, "field " +
                "'ll_food_list'");
        target.tv_total = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_total, "field 'tv_total'"), R.id.tv_total, "field 'tv_total'");
        target.tvMorePercent = (TextView) finder.castView((View) finder.findRequiredView(source,
                R.id.tv_more_percent, "field 'tvMorePercent'"), R.id.tv_more_percent, "field " +
                "'tvMorePercent'");
        target.tvCalorySuggest = (TextView) finder.castView((View) finder.findRequiredView
                (source, R.id.tv_calory_suggest, "field 'tvCalorySuggest'"), R.id
                .tv_calory_suggest, "field 'tvCalorySuggest'");
    }

    public void reset(T target) {
        target.ll_food_list = null;
        target.tv_total = null;
        target.tvMorePercent = null;
        target.tvCalorySuggest = null;
    }
}
