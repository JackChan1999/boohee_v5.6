package com.boohee.account;

import android.view.View;
import android.widget.TextView;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;
import butterknife.internal.DebouncingOnClickListener;

import com.boohee.myview.BooheeRulerView;
import com.boohee.one.R;

public class ProfileInitFiveFragment$$ViewInjector<T extends ProfileInitFiveFragment> implements
        Injector<T> {
    public void inject(Finder finder, final T target, Object source) {
        target.tvWeek = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_week, "field 'tvWeek'"), R.id.tv_week, "field 'tvWeek'");
        target.date = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .date, "field 'date'"), R.id.date, "field 'date'");
        target.tvLose = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_lose, "field 'tvLose'"), R.id.tv_lose, "field 'tvLose'");
        target.rvTargetWeight = (BooheeRulerView) finder.castView((View) finder.findRequiredView
                (source, R.id.rv_target_weight, "field 'rvTargetWeight'"), R.id.rv_target_weight,
                "field 'rvTargetWeight'");
        target.tvTooLow = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_too_low, "field 'tvTooLow'"), R.id.tv_too_low, "field 'tvTooLow'");
        View view = (View) finder.findRequiredView(source, R.id.btn_next, "field 'btnNext' and " +
                "method 'onClick'");
        target.btnNext = (TextView) finder.castView(view, R.id.btn_next, "field 'btnNext'");
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
    }

    public void reset(T target) {
        target.tvWeek = null;
        target.date = null;
        target.tvLose = null;
        target.rvTargetWeight = null;
        target.tvTooLow = null;
        target.btnNext = null;
    }
}
