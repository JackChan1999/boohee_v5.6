package com.boohee.food;

import android.view.View;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;
import butterknife.internal.DebouncingOnClickListener;

import com.boohee.one.R;
import com.boohee.widgets.BooheeListView;
import com.boohee.widgets.BooheeRippleLayout;

public class CollectionActivity$$ViewInjector<T extends CollectionActivity> implements Injector<T> {
    public void inject(Finder finder, final T target, Object source) {
        target.blvContent = (BooheeListView) finder.castView((View) finder.findRequiredView
                (source, R.id.blv_content, "field 'blvContent'"), R.id.blv_content, "field " +
                "'blvContent'");
        target.viewOperate = (View) finder.findRequiredView(source, R.id.view_operate, "field " +
                "'viewOperate'");
        target.rippleLayout = (BooheeRippleLayout) finder.castView((View) finder.findRequiredView
                (source, R.id.ripple, "field 'rippleLayout'"), R.id.ripple, "field 'rippleLayout'");
        ((View) finder.findRequiredView(source, R.id.bt_delete, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        ((View) finder.findRequiredView(source, R.id.tv_all, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        ((View) finder.findRequiredView(source, R.id.btn_add_custom, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
    }

    public void reset(T target) {
        target.blvContent = null;
        target.viewOperate = null;
        target.rippleLayout = null;
    }
}
