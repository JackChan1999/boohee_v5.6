package com.boohee.one.ui;

import android.view.View;
import android.widget.LinearLayout;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

import com.boohee.myview.WeekView;
import com.boohee.one.R;

public class DietPlanActivity$$ViewInjector<T extends DietPlanActivity> implements Injector<T> {
    public void inject(Finder finder, T target, Object source) {
        target.llPlan = (LinearLayout) finder.castView((View) finder.findRequiredView(source, R
                .id.ll_plan, "field 'llPlan'"), R.id.ll_plan, "field 'llPlan'");
        target.llWeek = (WeekView) finder.castView((View) finder.findRequiredView(source, R.id
                .ll_week, "field 'llWeek'"), R.id.ll_week, "field 'llWeek'");
        target.divider = (View) finder.findRequiredView(source, R.id.divider, "field 'divider'");
        target.rippleReset = (View) finder.findRequiredView(source, R.id.ripple_reset, "field " +
                "'rippleReset'");
        target.rippleCopy = (View) finder.findRequiredView(source, R.id.ripple_copy, "field " +
                "'rippleCopy'");
        target.llBottom = (LinearLayout) finder.castView((View) finder.findRequiredView(source, R
                .id.ll_bottom, "field 'llBottom'"), R.id.ll_bottom, "field 'llBottom'");
    }

    public void reset(T target) {
        target.llPlan = null;
        target.llWeek = null;
        target.divider = null;
        target.rippleReset = null;
        target.rippleCopy = null;
        target.llBottom = null;
    }
}
