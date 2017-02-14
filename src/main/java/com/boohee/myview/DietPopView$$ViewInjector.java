package com.boohee.myview;

import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;
import butterknife.internal.DebouncingOnClickListener;

import com.boohee.one.R;

public class DietPopView$$ViewInjector<T extends DietPopView> implements Injector<T> {
    public void inject(Finder finder, final T target, Object source) {
        target.tvDate = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_date, "field 'tvDate'"), R.id.tv_date, "field 'tvDate'");
        target.calendarGrid = (GridView) finder.castView((View) finder.findRequiredView(source, R
                .id.calendar, "field 'calendarGrid'"), R.id.calendar, "field 'calendarGrid'");
        target.flipper = (ViewFlipper) finder.castView((View) finder.findRequiredView(source, R
                .id.flipper, "field 'flipper'"), R.id.flipper, "field 'flipper'");
        target.bottomShadow = (View) finder.findRequiredView(source, R.id.bottom_shadow, "field " +
                "'bottomShadow'");
        target.bt_today = (Button) finder.castView((View) finder.findRequiredView(source, R.id
                .bt_today, "field 'bt_today'"), R.id.bt_today, "field 'bt_today'");
        ((View) finder.findRequiredView(source, R.id.rl_left, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        ((View) finder.findRequiredView(source, R.id.rl_right, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
    }

    public void reset(T target) {
        target.tvDate = null;
        target.calendarGrid = null;
        target.flipper = null;
        target.bottomShadow = null;
        target.bt_today = null;
    }
}
