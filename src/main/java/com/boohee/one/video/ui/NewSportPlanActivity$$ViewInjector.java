package com.boohee.one.video.ui;

import android.support.design.widget.TabLayout;
import android.view.View;
import android.widget.TextView;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

import com.boohee.one.R;

public class NewSportPlanActivity$$ViewInjector<T extends NewSportPlanActivity> implements
        Injector<T> {
    public void inject(Finder finder, T target, Object source) {
        target.tabLayout = (TabLayout) finder.castView((View) finder.findRequiredView(source, R
                .id.tab_layout, "field 'tabLayout'"), R.id.tab_layout, "field 'tabLayout'");
        target.tvTotalCalory = (TextView) finder.castView((View) finder.findRequiredView(source,
                R.id.tv_total_calory, "field 'tvTotalCalory'"), R.id.tv_total_calory, "field " +
                "'tvTotalCalory'");
        target.tvTrainTime = (TextView) finder.castView((View) finder.findRequiredView(source, R
                .id.tv_train_time, "field 'tvTrainTime'"), R.id.tv_train_time, "field " +
                "'tvTrainTime'");
        target.tvContinueDay = (TextView) finder.castView((View) finder.findRequiredView(source,
                R.id.tv_continue_day, "field 'tvContinueDay'"), R.id.tv_continue_day, "field " +
                "'tvContinueDay'");
    }

    public void reset(T target) {
        target.tabLayout = null;
        target.tvTotalCalory = null;
        target.tvTrainTime = null;
        target.tvContinueDay = null;
    }
}
