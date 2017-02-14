package com.boohee.one.ui.fragment;

import android.view.View;
import android.widget.TextView;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

import com.boohee.one.R;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class StatusFavoriteFragment$$ViewInjector<T extends StatusFavoriteFragment> implements
        Injector<T> {
    public void inject(Finder finder, T target, Object source) {
        target.pullRefreshLayout = (PullToRefreshListView) finder.castView((View) finder
                .findRequiredView(source, R.id.listview, "field 'pullRefreshLayout'"), R.id
                .listview, "field 'pullRefreshLayout'");
        target.tvHint = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .favorite_hint, "field 'tvHint'"), R.id.favorite_hint, "field 'tvHint'");
    }

    public void reset(T target) {
        target.pullRefreshLayout = null;
        target.tvHint = null;
    }
}
