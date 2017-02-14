package com.boohee.food;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;
import butterknife.internal.DebouncingOnClickListener;

import com.boohee.one.R;
import com.boohee.widgets.PagerSlidingTabStrip;

public class AddFoodListActivity$$ViewInjector<T extends AddFoodListActivity> implements
        Injector<T> {
    public void inject(Finder finder, final T target, Object source) {
        target.mSlidingTab = (PagerSlidingTabStrip) finder.castView((View) finder
                .findRequiredView(source, R.id.sliding_tabs, "field 'mSlidingTab'"), R.id
                .sliding_tabs, "field 'mSlidingTab'");
        target.mViewPager = (ViewPager) finder.castView((View) finder.findRequiredView(source, R
                .id.viewpager, "field 'mViewPager'"), R.id.viewpager, "field 'mViewPager'");
        target.iv_diet_cart = (ImageView) finder.castView((View) finder.findRequiredView(source,
                R.id.iv_diet_cart, "field 'iv_diet_cart'"), R.id.iv_diet_cart, "field " +
                "'iv_diet_cart'");
        target.rlCamera = (View) finder.findRequiredView(source, R.id.rl_camera, "field " +
                "'rlCamera'");
        target.rlCalorie = (View) finder.findRequiredView(source, R.id.rl_calorie, "field " +
                "'rlCalorie'");
        target.tvSuggest = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .suggest, "field 'tvSuggest'"), R.id.suggest, "field 'tvSuggest'");
        target.tvCalorie = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .calorie, "field 'tvCalorie'"), R.id.calorie, "field 'tvCalorie'");
        target.tvCalorieAdd = (TextView) finder.castView((View) finder.findRequiredView(source, R
                .id.calorie_add, "field 'tvCalorieAdd'"), R.id.calorie_add, "field 'tvCalorieAdd'");
        ((View) finder.findRequiredView(source, R.id.fl_camera, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        ((View) finder.findRequiredView(source, R.id.iv_food_scan, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        ((View) finder.findRequiredView(source, R.id.rl_food_search, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
    }

    public void reset(T target) {
        target.mSlidingTab = null;
        target.mViewPager = null;
        target.iv_diet_cart = null;
        target.rlCamera = null;
        target.rlCalorie = null;
        target.tvSuggest = null;
        target.tvCalorie = null;
        target.tvCalorieAdd = null;
    }
}
