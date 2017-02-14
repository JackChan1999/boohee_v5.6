package com.boohee.one.video.fragment;

import android.view.View;
import android.widget.LinearLayout;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

import com.boohee.one.R;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;

public class SportPlanFragment$$ViewInjector<T extends SportPlanFragment> implements Injector<T> {
    public void inject(Finder finder, T target, Object source) {
        target.layoutContainer = (LinearLayout) finder.castView((View) finder.findRequiredView
                (source, R.id.layout_container, "field 'layoutContainer'"), R.id
                .layout_container, "field 'layoutContainer'");
        target.scrollview = (PullToRefreshScrollView) finder.castView((View) finder
                .findRequiredView(source, R.id.scrollview, "field 'scrollview'"), R.id
                .scrollview, "field 'scrollview'");
    }

    public void reset(T target) {
        target.layoutContainer = null;
        target.scrollview = null;
    }
}
