package com.boohee.one.sport;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

import com.boohee.one.R;

public class SportCourseHistoryActivity$$ViewInjector<T extends SportCourseHistoryActivity>
        implements Injector<T> {
    public void inject(Finder finder, T target, Object source) {
        target.mRecyclerView = (RecyclerView) finder.castView((View) finder.findRequiredView
                (source, R.id.recyclerview, "field 'mRecyclerView'"), R.id.recyclerview, "field " +
                "'mRecyclerView'");
        target.view_no_result = (View) finder.findRequiredView(source, R.id.view_no_result,
                "field 'view_no_result'");
    }

    public void reset(T target) {
        target.mRecyclerView = null;
        target.view_no_result = null;
    }
}
