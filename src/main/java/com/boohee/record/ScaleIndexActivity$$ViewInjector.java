package com.boohee.record;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

import com.boohee.one.R;

public class ScaleIndexActivity$$ViewInjector<T extends ScaleIndexActivity> implements Injector<T> {
    public void inject(Finder finder, T target, Object source) {
        target.recyclerView = (RecyclerView) finder.castView((View) finder.findRequiredView
                (source, R.id.recycler, "field 'recyclerView'"), R.id.recycler, "field " +
                "'recyclerView'");
    }

    public void reset(T target) {
        target.recyclerView = null;
    }
}
