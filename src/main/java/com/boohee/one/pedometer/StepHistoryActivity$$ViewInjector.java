package com.boohee.one.pedometer;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;
import butterknife.internal.DebouncingOnClickListener;

import com.boohee.one.R;

import kankan.wheel.widget.WheelView;
import lecho.lib.hellocharts.view.ColumnChartView;

public class StepHistoryActivity$$ViewInjector<T extends StepHistoryActivity> implements
        Injector<T> {
    public void inject(Finder finder, final T target, Object source) {
        target.columnChartView = (ColumnChartView) finder.castView((View) finder.findRequiredView
                (source, R.id.column_chart_view, "field 'columnChartView'"), R.id
                .column_chart_view, "field 'columnChartView'");
        target.tvMyTarget = (TextView) finder.castView((View) finder.findRequiredView(source, R
                .id.tv_my_target, "field 'tvMyTarget'"), R.id.tv_my_target, "field 'tvMyTarget'");
        View view = (View) finder.findRequiredView(source, R.id.rl_target, "field 'rlTarget' and " +
                "method 'onClick'");
        target.rlTarget = (RelativeLayout) finder.castView(view, R.id.rl_target, "field " +
                "'rlTarget'");
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        target.tvWeekSteps = (TextView) finder.castView((View) finder.findRequiredView(source, R
                .id.tv_week_steps, "field 'tvWeekSteps'"), R.id.tv_week_steps, "field " +
                "'tvWeekSteps'");
        target.tvMonthSteps = (TextView) finder.castView((View) finder.findRequiredView(source, R
                .id.tv_month_steps, "field 'tvMonthSteps'"), R.id.tv_month_steps, "field " +
                "'tvMonthSteps'");
        view = (View) finder.findRequiredView(source, R.id.tv_target_cancel, "field " +
                "'tvTargetCancel' and method 'onClick'");
        target.tvTargetCancel = (TextView) finder.castView(view, R.id.tv_target_cancel, "field " +
                "'tvTargetCancel'");
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        view = (View) finder.findRequiredView(source, R.id.tv_target_confirm, "field " +
                "'tvTargetConfirm' and method 'onClick'");
        target.tvTargetConfirm = (TextView) finder.castView(view, R.id.tv_target_confirm, "field " +
                "'tvTargetConfirm'");
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        target.wheelTarget = (WheelView) finder.castView((View) finder.findRequiredView(source, R
                .id.wheel_target, "field 'wheelTarget'"), R.id.wheel_target, "field 'wheelTarget'");
        target.llSetTarget = (LinearLayout) finder.castView((View) finder.findRequiredView
                (source, R.id.ll_set_target, "field 'llSetTarget'"), R.id.ll_set_target, "field " +
                "'llSetTarget'");
    }

    public void reset(T target) {
        target.columnChartView = null;
        target.tvMyTarget = null;
        target.rlTarget = null;
        target.tvWeekSteps = null;
        target.tvMonthSteps = null;
        target.tvTargetCancel = null;
        target.tvTargetConfirm = null;
        target.wheelTarget = null;
        target.llSetTarget = null;
    }
}
