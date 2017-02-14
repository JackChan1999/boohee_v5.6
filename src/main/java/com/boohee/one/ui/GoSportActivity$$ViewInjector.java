package com.boohee.one.ui;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

import com.boohee.one.R;

public class GoSportActivity$$ViewInjector<T extends GoSportActivity> implements Injector<T> {
    public void inject(Finder finder, T target, Object source) {
        target.recyclerView = (RecyclerView) finder.castView((View) finder.findRequiredView
                (source, R.id.recycler_view, "field 'recyclerView'"), R.id.recycler_view, "field " +
                "'recyclerView'");
    }

    public void reset(T target) {
        target.recyclerView = null;
    }
}
