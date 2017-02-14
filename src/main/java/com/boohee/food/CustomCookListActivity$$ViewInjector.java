package com.boohee.food;

import android.view.View;
import android.widget.TextView;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;
import butterknife.internal.DebouncingOnClickListener;

import com.boohee.one.R;
import com.boohee.widgets.BooheeListView;
import com.boohee.widgets.BooheeRippleLayout;

public class CustomCookListActivity$$ViewInjector<T extends CustomCookListActivity> implements
        Injector<T> {
    public void inject(Finder finder, final T target, Object source) {
        target.blvContent = (BooheeListView) finder.castView((View) finder.findRequiredView
                (source, R.id.blv_content, "field 'blvContent'"), R.id.blv_content, "field " +
                "'blvContent'");
        target.viewOperate = (View) finder.findRequiredView(source, R.id.view_operate, "field " +
                "'viewOperate'");
        target.tvAddCustom = (TextView) finder.castView((View) finder.findRequiredView(source, R
                .id.tv_add_custom, "field 'tvAddCustom'"), R.id.tv_add_custom, "field " +
                "'tvAddCustom'");
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
        target.tvAddCustom = null;
        target.rippleLayout = null;
    }
}
