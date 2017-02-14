package com.boohee.record;

import android.view.View;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

import com.boohee.one.R;

import lecho.lib.hellocharts.view.ColumnChartView;

public class DietChartActivity$$ViewInjector<T extends DietChartActivity> implements Injector<T> {
    public void inject(Finder finder, T target, Object source) {
        target.chart = (ColumnChartView) finder.castView((View) finder.findRequiredView(source, R
                .id.column_chart_view, "field 'chart'"), R.id.column_chart_view, "field 'chart'");
    }

    public void reset(T target) {
        target.chart = null;
    }
}
