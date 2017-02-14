package com.boohee.one.video.ui;

import android.view.View;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

import com.boohee.one.R;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class VideoHistoryActivity$$ViewInjector<T extends VideoHistoryActivity> implements
        Injector<T> {
    public void inject(Finder finder, T target, Object source) {
        target.refreshLayout = (PullToRefreshListView) finder.castView((View) finder
                .findRequiredView(source, R.id.listview, "field 'refreshLayout'"), R.id.listview,
                "field 'refreshLayout'");
    }

    public void reset(T target) {
        target.refreshLayout = null;
    }
}
