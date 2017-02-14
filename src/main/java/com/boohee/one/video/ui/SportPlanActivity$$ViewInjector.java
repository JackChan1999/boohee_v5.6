package com.boohee.one.video.ui;

import android.view.View;
import android.widget.Button;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;
import butterknife.internal.DebouncingOnClickListener;

import com.boohee.one.R;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class SportPlanActivity$$ViewInjector<T extends SportPlanActivity> implements Injector<T> {
    public void inject(Finder finder, final T target, Object source) {
        target.pullToRefreshListView = (PullToRefreshListView) finder.castView((View) finder
                .findRequiredView(source, R.id.listview, "field 'pullToRefreshListView'"), R.id
                .listview, "field 'pullToRefreshListView'");
        View view = (View) finder.findRequiredView(source, R.id.testBtn, "field 'testBtn' and " +
                "method 'onClick'");
        target.testBtn = (Button) finder.castView(view, R.id.testBtn, "field 'testBtn'");
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
    }

    public void reset(T target) {
        target.pullToRefreshListView = null;
        target.testBtn = null;
    }
}
