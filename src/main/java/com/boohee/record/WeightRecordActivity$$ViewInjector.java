package com.boohee.record;

import android.view.View;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;
import butterknife.internal.DebouncingOnClickListener;

import com.boohee.one.R;

public class WeightRecordActivity$$ViewInjector<T extends WeightRecordActivity> implements
        Injector<T> {
    public void inject(Finder finder, final T target, Object source) {
        target.flipper = (ViewFlipper) finder.castView((View) finder.findRequiredView(source, R
                .id.flipper, "field 'flipper'"), R.id.flipper, "field 'flipper'");
        target.calendarGrid = (GridView) finder.castView((View) finder.findRequiredView(source, R
                .id.calendar, "field 'calendarGrid'"), R.id.calendar, "field 'calendarGrid'");
        target.tvWeightCount = (TextView) finder.castView((View) finder.findRequiredView(source,
                R.id.tv_weight_count, "field 'tvWeightCount'"), R.id.tv_weight_count, "field " +
                "'tvWeightCount'");
        target.tvPhotos = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_photos, "field 'tvPhotos'"), R.id.tv_photos, "field 'tvPhotos'");
        target.tvStatus = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_status, "field 'tvStatus'"), R.id.tv_status, "field 'tvStatus'");
        target.tvStatusTitle = (TextView) finder.castView((View) finder.findRequiredView(source,
                R.id.tv_status_title, "field 'tvStatusTitle'"), R.id.tv_status_title, "field " +
                "'tvStatusTitle'");
        target.rlScale = (View) finder.findRequiredView(source, R.id.rl_scale, "field 'rlScale'");
        target.tvScaleName = (TextView) finder.castView((View) finder.findRequiredView(source, R
                .id.tv_scale_name, "field 'tvScaleName'"), R.id.tv_scale_name, "field " +
                "'tvScaleName'");
        target.tvScaleHint = (TextView) finder.castView((View) finder.findRequiredView(source, R
                .id.tv_scale_hint, "field 'tvScaleHint'"), R.id.tv_scale_hint, "field " +
                "'tvScaleHint'");
        target.tvScaleGo = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_scale_go, "field 'tvScaleGo'"), R.id.tv_scale_go, "field 'tvScaleGo'");
        ((View) finder.findRequiredView(source, R.id.ll_weight_curve, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        ((View) finder.findRequiredView(source, R.id.ll_weight_photos, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        ((View) finder.findRequiredView(source, R.id.ll_status, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
    }

    public void reset(T target) {
        target.flipper = null;
        target.calendarGrid = null;
        target.tvWeightCount = null;
        target.tvPhotos = null;
        target.tvStatus = null;
        target.tvStatusTitle = null;
        target.rlScale = null;
        target.tvScaleName = null;
        target.tvScaleHint = null;
        target.tvScaleGo = null;
    }
}
