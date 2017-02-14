package com.boohee.one.ui;

import android.view.View;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;
import butterknife.internal.DebouncingOnClickListener;

import com.boohee.one.R;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class SuccessStoryActivity$$ViewInjector<T extends SuccessStoryActivity> implements
        Injector<T> {
    public void inject(Finder finder, final T target, Object source) {
        target.pullToRefreshListView = (PullToRefreshListView) finder.castView((View) finder
                .findRequiredView(source, R.id.listview, "field 'pullToRefreshListView'"), R.id
                .listview, "field 'pullToRefreshListView'");
        ((View) finder.findRequiredView(source, R.id.tv_bottom, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
    }

    public void reset(T target) {
        target.pullToRefreshListView = null;
    }
}
