package com.boohee.food;

import android.view.View;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

import com.boohee.one.R;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class UploadFoodFragment$$ViewInjector<T extends UploadFoodFragment> implements Injector<T> {
    public void inject(Finder finder, T target, Object source) {
        target.mPullRefreshListView = (PullToRefreshListView) finder.castView((View) finder
                .findRequiredView(source, R.id.listview, "field 'mPullRefreshListView'"), R.id
                .listview, "field 'mPullRefreshListView'");
    }

    public void reset(T target) {
        target.mPullRefreshListView = null;
    }
}
