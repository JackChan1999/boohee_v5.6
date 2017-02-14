package com.boohee.record;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;
import butterknife.internal.DebouncingOnClickListener;

import com.boohee.one.R;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;

public class WeightPhotosActivity$$ViewInjector<T extends WeightPhotosActivity> implements
        Injector<T> {
    public void inject(Finder finder, final T target, Object source) {
        target.pullToRefreshGrid = (PullToRefreshGridView) finder.castView((View) finder
                .findRequiredView(source, R.id.pull_to_refresh_grid, "field 'pullToRefreshGrid'")
                , R.id.pull_to_refresh_grid, "field 'pullToRefreshGrid'");
        target.view_operate = (RelativeLayout) finder.castView((View) finder.findRequiredView
                (source, R.id.view_operate, "field 'view_operate'"), R.id.view_operate, "field " +
                "'view_operate'");
        View view = (View) finder.findRequiredView(source, R.id.tv_make_compare, "field " +
                "'tv_make_compare' and method 'onClick'");
        target.tv_make_compare = (TextView) finder.castView(view, R.id.tv_make_compare, "field " +
                "'tv_make_compare'");
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        target.tv_alert = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_alert, "field 'tv_alert'"), R.id.tv_alert, "field 'tv_alert'");
        ((View) finder.findRequiredView(source, R.id.tv_cancel, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        ((View) finder.findRequiredView(source, R.id.tv_submit, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
    }

    public void reset(T target) {
        target.pullToRefreshGrid = null;
        target.view_operate = null;
        target.tv_make_compare = null;
        target.tv_alert = null;
    }
}
