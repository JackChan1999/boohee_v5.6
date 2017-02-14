package com.boohee.one.video.ui;

import android.view.View;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

import com.boohee.one.R;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class AddSpecialLessonActivity$$ViewInjector<T extends AddSpecialLessonActivity>
        implements Injector<T> {
    public void inject(Finder finder, T target, Object source) {
        target.pullToRefresh = (PullToRefreshListView) finder.castView((View) finder
                .findRequiredView(source, R.id.pull_to_refresh, "field 'pullToRefresh'"), R.id
                .pull_to_refresh, "field 'pullToRefresh'");
    }

    public void reset(T target) {
        target.pullToRefresh = null;
    }
}
