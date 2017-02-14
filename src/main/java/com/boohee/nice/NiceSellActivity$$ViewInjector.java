package com.boohee.nice;

import android.view.View;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

import com.boohee.myview.MultiViewPager;
import com.boohee.one.R;
import com.viewpagerindicator.CirclePageIndicator;

public class NiceSellActivity$$ViewInjector<T extends NiceSellActivity> implements Injector<T> {
    public void inject(Finder finder, T target, Object source) {
        target.viewpager = (MultiViewPager) finder.castView((View) finder.findRequiredView
                (source, R.id.viewpager, "field 'viewpager'"), R.id.viewpager, "field 'viewpager'");
        target.indicator = (CirclePageIndicator) finder.castView((View) finder.findRequiredView
                (source, R.id.indicator, "field 'indicator'"), R.id.indicator, "field 'indicator'");
    }

    public void reset(T target) {
        target.viewpager = null;
        target.indicator = null;
    }
}
