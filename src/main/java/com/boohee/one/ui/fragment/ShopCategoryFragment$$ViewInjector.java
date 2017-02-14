package com.boohee.one.ui.fragment;

import android.view.View;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

import com.boohee.one.R;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class ShopCategoryFragment$$ViewInjector<T extends ShopCategoryFragment> implements
        Injector<T> {
    public void inject(Finder finder, T target, Object source) {
        target.mPullListView = (PullToRefreshListView) finder.castView((View) finder
                .findRequiredView(source, R.id.listview, "field 'mPullListView'"), R.id.listview,
                "field 'mPullListView'");
    }

    public void reset(T target) {
        target.mPullListView = null;
    }
}
