package com.boohee.one.pedometer.v2;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;
import butterknife.internal.DebouncingOnClickListener;

import com.boohee.one.R;

public class StepGiftFragment$$ViewInjector<T extends StepGiftFragment> implements Injector<T> {
    public void inject(Finder finder, final T target, Object source) {
        target.mListView = (ListView) finder.castView((View) finder.findRequiredView(source, R.id
                .listview, "field 'mListView'"), R.id.listview, "field 'mListView'");
        target.viewContent = (View) finder.findRequiredView(source, R.id.view_content, "field " +
                "'viewContent'");
        View view = (View) finder.findRequiredView(source, R.id.bt_operate, "field 'btOperate' " +
                "and method 'onClick'");
        target.btOperate = (Button) finder.castView(view, R.id.bt_operate, "field 'btOperate'");
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        target.viewList = (LinearLayout) finder.castView((View) finder.findRequiredView(source, R
                .id.view_list, "field 'viewList'"), R.id.view_list, "field 'viewList'");
        ((View) finder.findRequiredView(source, R.id.bt_close, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
    }

    public void reset(T target) {
        target.mListView = null;
        target.viewContent = null;
        target.btOperate = null;
        target.viewList = null;
    }
}
