package com.boohee.record;

import android.view.View;
import android.widget.ListView;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

import com.boohee.one.R;

public class HistoryRecordActivity$$ViewInjector<T extends HistoryRecordActivity> implements
        Injector<T> {
    public void inject(Finder finder, T target, Object source) {
        target.listView = (ListView) finder.castView((View) finder.findRequiredView(source, R.id
                .listview, "field 'listView'"), R.id.listview, "field 'listView'");
    }

    public void reset(T target) {
        target.listView = null;
    }
}
