package com.boohee.one.bet;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

import com.boohee.one.R;

public class BetActivity$$ViewInjector<T extends BetActivity> implements Injector<T> {
    public void inject(Finder finder, T target, Object source) {
        target.viewPager = (ViewPager) finder.castView((View) finder.findRequiredView(source, R
                .id.view_pager, "field 'viewPager'"), R.id.view_pager, "field 'viewPager'");
        target.viewTabs = (TabLayout) finder.castView((View) finder.findRequiredView(source, R.id
                .view_tabs, "field 'viewTabs'"), R.id.view_tabs, "field 'viewTabs'");
    }

    public void reset(T target) {
        target.viewPager = null;
        target.viewTabs = null;
    }
}
