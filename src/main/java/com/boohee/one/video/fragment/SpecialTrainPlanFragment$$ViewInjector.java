package com.boohee.one.video.fragment;

import android.view.View;
import android.widget.RelativeLayout;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;
import butterknife.internal.DebouncingOnClickListener;

import com.boohee.one.R;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class SpecialTrainPlanFragment$$ViewInjector<T extends SpecialTrainPlanFragment>
        implements Injector<T> {
    public void inject(Finder finder, final T target, Object source) {
        target.pullToRefresh = (PullToRefreshListView) finder.castView((View) finder
                .findRequiredView(source, R.id.pull_to_refresh, "field 'pullToRefresh'"), R.id
                .pull_to_refresh, "field 'pullToRefresh'");
        target.addLayout = (RelativeLayout) finder.castView((View) finder.findRequiredView
                (source, R.id.add_layout, "field 'addLayout'"), R.id.add_layout, "field " +
                "'addLayout'");
        ((View) finder.findRequiredView(source, R.id.tv_add_sport, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
    }

    public void reset(T target) {
        target.pullToRefresh = null;
        target.addLayout = null;
    }
}
