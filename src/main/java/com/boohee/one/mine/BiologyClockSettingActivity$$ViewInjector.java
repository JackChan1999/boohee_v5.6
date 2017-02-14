package com.boohee.one.mine;

import android.view.View;
import android.widget.TextView;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;
import butterknife.internal.DebouncingOnClickListener;

import com.boohee.one.R;

public class BiologyClockSettingActivity$$ViewInjector<T extends BiologyClockSettingActivity>
        implements Injector<T> {
    public void inject(Finder finder, final T target, Object source) {
        target.tvCount = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .mc_count, "field 'tvCount'"), R.id.mc_count, "field 'tvCount'");
        target.tvDays = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .mc_days, "field 'tvDays'"), R.id.mc_days, "field 'tvDays'");
        target.tvCircle = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .mc_circle, "field 'tvCircle'"), R.id.mc_circle, "field 'tvCircle'");
        ((View) finder.findRequiredView(source, R.id.mc_circle_click, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        ((View) finder.findRequiredView(source, R.id.mc_days_click, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        ((View) finder.findRequiredView(source, R.id.mc_history, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
    }

    public void reset(T target) {
        target.tvCount = null;
        target.tvDays = null;
        target.tvCircle = null;
    }
}
