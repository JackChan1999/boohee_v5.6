package com.boohee.one.ui.fragment;

import android.support.design.widget.FloatingActionButton;
import android.view.View;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;
import butterknife.internal.DebouncingOnClickListener;

import com.boohee.one.R;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class ShopMainFragment$$ViewInjector<T extends ShopMainFragment> implements Injector<T> {
    public void inject(Finder finder, final T target, Object source) {
        target.mPullListView = (PullToRefreshListView) finder.castView((View) finder
                .findRequiredView(source, R.id.listview, "field 'mPullListView'"), R.id.listview,
                "field 'mPullListView'");
        View view = (View) finder.findRequiredView(source, R.id.fab_button, "field 'fabButton' " +
                "and method 'onClick'");
        target.fabButton = (FloatingActionButton) finder.castView(view, R.id.fab_button, "field " +
                "'fabButton'");
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
    }

    public void reset(T target) {
        target.mPullListView = null;
        target.fabButton = null;
    }
}
