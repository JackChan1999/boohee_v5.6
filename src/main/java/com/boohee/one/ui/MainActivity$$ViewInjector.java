package com.boohee.one.ui;

import android.support.design.widget.TabLayout;
import android.view.View;
import android.widget.ImageView;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

import com.boohee.one.R;

public class MainActivity$$ViewInjector<T extends MainActivity> implements Injector<T> {
    public void inject(Finder finder, T target, Object source) {
        target.viewTabs = (TabLayout) finder.castView((View) finder.findRequiredView(source, R.id
                .view_tabs, "field 'viewTabs'"), R.id.view_tabs, "field 'viewTabs'");
        target.divider = (ImageView) finder.castView((View) finder.findRequiredView(source, R.id
                .divider, "field 'divider'"), R.id.divider, "field 'divider'");
    }

    public void reset(T target) {
        target.viewTabs = null;
        target.divider = null;
    }
}
