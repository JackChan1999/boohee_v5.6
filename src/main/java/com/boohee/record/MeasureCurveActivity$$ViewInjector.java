package com.boohee.record;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

import com.boohee.one.R;

import lecho.lib.hellocharts.view.LineChartView;

public class MeasureCurveActivity$$ViewInjector<T extends MeasureCurveActivity> implements
        Injector<T> {
    public void inject(Finder finder, T target, Object source) {
        target.lineChart = (LineChartView) finder.castView((View) finder.findRequiredView(source,
                R.id.linechart, "field 'lineChart'"), R.id.linechart, "field 'lineChart'");
        target.rg_weight = (RadioGroup) finder.castView((View) finder.findRequiredView(source, R
                .id.rg_weight, "field 'rg_weight'"), R.id.rg_weight, "field 'rg_weight'");
        target.spinner = (Spinner) finder.castView((View) finder.findRequiredView(source, R.id
                .spinner, "field 'spinner'"), R.id.spinner, "field 'spinner'");
        target.ll_content = (LinearLayout) finder.castView((View) finder.findRequiredView(source,
                R.id.ll_content, "field 'll_content'"), R.id.ll_content, "field 'll_content'");
    }

    public void reset(T target) {
        target.lineChart = null;
        target.rg_weight = null;
        target.spinner = null;
        target.ll_content = null;
    }
}
