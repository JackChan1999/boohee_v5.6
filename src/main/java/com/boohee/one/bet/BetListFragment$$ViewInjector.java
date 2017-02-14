package com.boohee.one.bet;

import android.view.View;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

import com.boohee.one.R;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class BetListFragment$$ViewInjector<T extends BetListFragment> implements Injector<T> {
    public void inject(Finder finder, T target, Object source) {
        target.listview = (PullToRefreshListView) finder.castView((View) finder.findRequiredView
                (source, R.id.listview, "field 'listview'"), R.id.listview, "field 'listview'");
    }

    public void reset(T target) {
        target.listview = null;
    }
}
