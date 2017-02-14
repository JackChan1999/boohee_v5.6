package com.boohee.status;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

import com.boohee.one.R;
import com.viewpagerindicator.LinePageIndicator;

public class HotTopicActivity$$ViewInjector<T extends HotTopicActivity> implements Injector<T> {
    public void inject(Finder finder, T target, Object source) {
        target.viewBanner = (View) finder.findRequiredView(source, R.id.view_banner, "field " +
                "'viewBanner'");
        target.viewPager = (ViewPager) finder.castView((View) finder.findRequiredView(source, R
                .id.viewpager, "field 'viewPager'"), R.id.viewpager, "field 'viewPager'");
        target.mIndicator = (LinePageIndicator) finder.castView((View) finder.findRequiredView
                (source, R.id.indicator, "field 'mIndicator'"), R.id.indicator, "field " +
                "'mIndicator'");
        target.tlTab = (TabLayout) finder.castView((View) finder.findRequiredView(source, R.id
                .tl_tab, "field 'tlTab'"), R.id.tl_tab, "field 'tlTab'");
    }

    public void reset(T target) {
        target.viewBanner = null;
        target.viewPager = null;
        target.mIndicator = null;
        target.tlTab = null;
    }
}
