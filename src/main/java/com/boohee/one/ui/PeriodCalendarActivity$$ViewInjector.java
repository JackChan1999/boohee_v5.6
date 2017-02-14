package com.boohee.one.ui;

import android.view.View;
import android.widget.GridView;
import android.widget.ViewFlipper;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

import com.boohee.one.R;

public class PeriodCalendarActivity$$ViewInjector<T extends PeriodCalendarActivity> implements
        Injector<T> {
    public void inject(Finder finder, T target, Object source) {
        target.flipper = (ViewFlipper) finder.castView((View) finder.findRequiredView(source, R
                .id.flipper, "field 'flipper'"), R.id.flipper, "field 'flipper'");
        target.calendarGrid = (GridView) finder.castView((View) finder.findRequiredView(source, R
                .id.calendar, "field 'calendarGrid'"), R.id.calendar, "field 'calendarGrid'");
    }

    public void reset(T target) {
        target.flipper = null;
        target.calendarGrid = null;
    }
}
