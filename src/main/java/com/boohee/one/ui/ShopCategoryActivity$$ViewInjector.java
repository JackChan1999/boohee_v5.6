package com.boohee.one.ui;

import android.support.v4.view.ViewPager;
import android.view.View;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

import com.boohee.one.R;
import com.boohee.widgets.PagerSlidingTabStrip;

public class ShopCategoryActivity$$ViewInjector<T extends ShopCategoryActivity> implements
        Injector<T> {
    public void inject(Finder finder, T target, Object source) {
        target.mSlidingTab = (PagerSlidingTabStrip) finder.castView((View) finder
                .findRequiredView(source, R.id.sliding_tabs, "field 'mSlidingTab'"), R.id
                .sliding_tabs, "field 'mSlidingTab'");
        target.mViewPager = (ViewPager) finder.castView((View) finder.findRequiredView(source, R
                .id.viewpager, "field 'mViewPager'"), R.id.viewpager, "field 'mViewPager'");
    }

    public void reset(T target) {
        target.mSlidingTab = null;
        target.mViewPager = null;
    }
}
