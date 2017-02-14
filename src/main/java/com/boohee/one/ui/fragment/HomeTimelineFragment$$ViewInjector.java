package com.boohee.one.ui.fragment;

import android.view.View;
import android.widget.TextView;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

import com.boohee.one.R;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class HomeTimelineFragment$$ViewInjector<T extends HomeTimelineFragment> implements
        Injector<T> {
    public void inject(Finder finder, T target, Object source) {
        target.mPullRefreshListView = (PullToRefreshListView) finder.castView((View) finder
                .findRequiredView(source, R.id.listview, "field 'mPullRefreshListView'"), R.id
                .listview, "field 'mPullRefreshListView'");
        target.tvHint = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_hint, "field 'tvHint'"), R.id.tv_hint, "field 'tvHint'");
    }

    public void reset(T target) {
        target.mPullRefreshListView = null;
        target.tvHint = null;
    }
}
