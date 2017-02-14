package com.boohee.food;

import android.view.View;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;
import butterknife.internal.DebouncingOnClickListener;

import com.boohee.one.R;
import com.boohee.widgets.BooheeListView;

public class ListUploadActivity$$ViewInjector<T extends ListUploadActivity> implements Injector<T> {
    public void inject(Finder finder, final T target, Object source) {
        target.blvContent = (BooheeListView) finder.castView((View) finder.findRequiredView
                (source, R.id.blv_content, "field 'blvContent'"), R.id.blv_content, "field " +
                "'blvContent'");
        ((View) finder.findRequiredView(source, R.id.btn_add_custom, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
    }

    public void reset(T target) {
        target.blvContent = null;
    }
}
