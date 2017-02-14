package com.boohee.one.ui.fragment;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;
import butterknife.internal.DebouncingOnClickListener;

import com.boohee.one.R;

public class WeightImageFragment$$ViewInjector<T extends WeightImageFragment> implements
        Injector<T> {
    public void inject(Finder finder, final T target, Object source) {
        target.tv_date = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_date, "field 'tv_date'"), R.id.tv_date, "field 'tv_date'");
        target.tv_weight = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_weight, "field 'tv_weight'"), R.id.tv_weight, "field 'tv_weight'");
        target.tv_bmi = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_bmi, "field 'tv_bmi'"), R.id.tv_bmi, "field 'tv_bmi'");
        View view = (View) finder.findRequiredView(source, R.id.iv_weight, "field 'iv_weight' and" +
                " method 'onClick'");
        target.iv_weight = (ImageView) finder.castView(view, R.id.iv_weight, "field 'iv_weight'");
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
    }

    public void reset(T target) {
        target.tv_date = null;
        target.tv_weight = null;
        target.tv_bmi = null;
        target.iv_weight = null;
    }
}
