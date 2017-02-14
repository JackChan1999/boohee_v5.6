package com.boohee.one.ui;

import android.support.v4.view.ViewPager;
import android.view.View;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

import com.boohee.one.R;
import com.boohee.widgets.PagerSlidingTabStrip;

public class MyFavoriteActivity$$ViewInjector<T extends MyFavoriteActivity> implements Injector<T> {
    public void inject(Finder finder, T target, Object source) {
        target.slidingTabs = (PagerSlidingTabStrip) finder.castView((View) finder
                .findRequiredView(source, R.id.sliding_tabs, "field 'slidingTabs'"), R.id
                .sliding_tabs, "field 'slidingTabs'");
        target.viewpager = (ViewPager) finder.castView((View) finder.findRequiredView(source, R
                .id.viewpager, "field 'viewpager'"), R.id.viewpager, "field 'viewpager'");
    }

    public void reset(T target) {
        target.slidingTabs = null;
        target.viewpager = null;
    }
}
