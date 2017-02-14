package com.boohee.one.ui.fragment;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

import com.boohee.one.R;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.viewpagerindicator.LinePageIndicator;

public class NewPartnerFragment$$ViewInjector<T extends NewPartnerFragment> implements Injector<T> {
    public void inject(Finder finder, T target, Object source) {
        target.viewPager = (ViewPager) finder.castView((View) finder.findRequiredView(source, R
                .id.viewpager_partner, "field 'viewPager'"), R.id.viewpager_partner, "field " +
                "'viewPager'");
        target.indicator = (LinePageIndicator) finder.castView((View) finder.findRequiredView
                (source, R.id.indicator, "field 'indicator'"), R.id.indicator, "field 'indicator'");
        target.flPartner = (FrameLayout) finder.castView((View) finder.findRequiredView(source, R
                .id.fl_partner, "field 'flPartner'"), R.id.fl_partner, "field 'flPartner'");
        target.llContent = (LinearLayout) finder.castView((View) finder.findRequiredView(source,
                R.id.ll_content, "field 'llContent'"), R.id.ll_content, "field 'llContent'");
        target.scrollView = (PullToRefreshScrollView) finder.castView((View) finder
                .findRequiredView(source, R.id.scrollview, "field 'scrollView'"), R.id
                .scrollview, "field 'scrollView'");
    }

    public void reset(T target) {
        target.viewPager = null;
        target.indicator = null;
        target.flPartner = null;
        target.llContent = null;
        target.scrollView = null;
    }
}
