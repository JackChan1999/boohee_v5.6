package com.boohee.one.pedometer.v2;

import android.support.design.widget.FloatingActionButton;
import android.view.View;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;
import butterknife.internal.DebouncingOnClickListener;

import com.boohee.one.R;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class StepMainActivity$$ViewInjector<T extends StepMainActivity> implements Injector<T> {
    public void inject(Finder finder, final T target, Object source) {
        target.mPullRefreshListView = (PullToRefreshListView) finder.castView((View) finder
                .findRequiredView(source, R.id.listview, "field 'mPullRefreshListView'"), R.id
                .listview, "field 'mPullRefreshListView'");
        View view = (View) finder.findRequiredView(source, R.id.fab_button, "field 'fabButton' " +
                "and method 'onClick'");
        target.fabButton = (FloatingActionButton) finder.castView(view, R.id.fab_button, "field " +
                "'fabButton'");
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        target.tvIndicator = (View) finder.findRequiredView(source, R.id.tv_indicator, "field " +
                "'tvIndicator'");
    }

    public void reset(T target) {
        target.mPullRefreshListView = null;
        target.fabButton = null;
        target.tvIndicator = null;
    }
}
