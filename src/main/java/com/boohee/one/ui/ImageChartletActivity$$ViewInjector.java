package com.boohee.one.ui;

import android.view.View;
import android.widget.TextView;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;
import butterknife.internal.DebouncingOnClickListener;

import com.boohee.one.R;

public class ImageChartletActivity$$ViewInjector<T extends ImageChartletActivity> implements
        Injector<T> {
    public void inject(Finder finder, final T target, Object source) {
        target.v_line = (View) finder.findRequiredView(source, R.id.v_line, "field 'v_line'");
        View view = (View) finder.findRequiredView(source, R.id.tv_tab_encourage, "field " +
                "'currentTab' and method 'onTabClick'");
        target.currentTab = (TextView) finder.castView(view, R.id.tv_tab_encourage, "field " +
                "'currentTab'");
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onTabClick(p0);
            }
        });
        ((View) finder.findRequiredView(source, R.id.tv_tab_eat, "method 'onTabClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onTabClick(p0);
            }
        });
        ((View) finder.findRequiredView(source, R.id.tv_tab_qute, "method 'onTabClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onTabClick(p0);
            }
        });
    }

    public void reset(T target) {
        target.v_line = null;
        target.currentTab = null;
    }
}
