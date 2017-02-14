package com.boohee.one.ui;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

import com.boohee.one.R;

public class SearcherOtherActivity$$ViewInjector<T extends SearcherOtherActivity> implements
        Injector<T> {
    public void inject(Finder finder, T target, Object source) {
        target.rvSearch = (RecyclerView) finder.castView((View) finder.findRequiredView(source, R
                .id.rv_search, "field 'rvSearch'"), R.id.rv_search, "field 'rvSearch'");
        target.viewNoResult = (TextView) finder.castView((View) finder.findRequiredView(source, R
                .id.view_no_result, "field 'viewNoResult'"), R.id.view_no_result, "field " +
                "'viewNoResult'");
    }

    public void reset(T target) {
        target.rvSearch = null;
        target.viewNoResult = null;
    }
}
