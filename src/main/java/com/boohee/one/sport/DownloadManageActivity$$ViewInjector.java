package com.boohee.one.sport;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

import com.boohee.one.R;

public class DownloadManageActivity$$ViewInjector<T extends DownloadManageActivity> implements
        Injector<T> {
    public void inject(Finder finder, T target, Object source) {
        target.recyclerView = (RecyclerView) finder.castView((View) finder.findRequiredView
                (source, R.id.recycler_view, "field 'recyclerView'"), R.id.recycler_view, "field " +
                "'recyclerView'");
        target.tvDelete = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .delete, "field 'tvDelete'"), R.id.delete, "field 'tvDelete'");
        target.cbSelectAll = (CheckBox) finder.castView((View) finder.findRequiredView(source, R
                .id.select_all, "field 'cbSelectAll'"), R.id.select_all, "field 'cbSelectAll'");
        target.selectLine = (View) finder.findRequiredView(source, R.id.select_line, "field " +
                "'selectLine'");
    }

    public void reset(T target) {
        target.recyclerView = null;
        target.tvDelete = null;
        target.cbSelectAll = null;
        target.selectLine = null;
    }
}
