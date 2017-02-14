package com.boohee.record;

import android.view.View;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.ViewFlipper;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;
import butterknife.internal.DebouncingOnClickListener;

import com.boohee.one.R;

public class DimensionRecordActivity$$ViewInjector<T extends DimensionRecordActivity> implements
        Injector<T> {
    public void inject(Finder finder, final T target, Object source) {
        View view = (View) finder.findRequiredView(source, R.id.rbtn_record_waist, "field " +
                "'rbtn_record_waist' and method 'onClick'");
        target.rbtn_record_waist = (RadioButton) finder.castView(view, R.id.rbtn_record_waist,
                "field 'rbtn_record_waist'");
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        view = (View) finder.findRequiredView(source, R.id.rbtn_record_brass, "field " +
                "'rbtn_record_brass' and method 'onClick'");
        target.rbtn_record_brass = (RadioButton) finder.castView(view, R.id.rbtn_record_brass,
                "field 'rbtn_record_brass'");
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        view = (View) finder.findRequiredView(source, R.id.rbtn_record_hip, "field " +
                "'rbtn_record_hip' and method 'onClick'");
        target.rbtn_record_hip = (RadioButton) finder.castView(view, R.id.rbtn_record_hip, "field" +
                " 'rbtn_record_hip'");
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        view = (View) finder.findRequiredView(source, R.id.rbtn_record_arm, "field " +
                "'rbtn_record_arm' and method 'onClick'");
        target.rbtn_record_arm = (RadioButton) finder.castView(view, R.id.rbtn_record_arm, "field" +
                " 'rbtn_record_arm'");
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        view = (View) finder.findRequiredView(source, R.id.rbtn_record_thigh, "field " +
                "'rbtn_record_thigh' and method 'onClick'");
        target.rbtn_record_thigh = (RadioButton) finder.castView(view, R.id.rbtn_record_thigh,
                "field 'rbtn_record_thigh'");
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        view = (View) finder.findRequiredView(source, R.id.rbtn_record_shank, "field " +
                "'rbtn_record_shank' and method 'onClick'");
        target.rbtn_record_shank = (RadioButton) finder.castView(view, R.id.rbtn_record_shank,
                "field 'rbtn_record_shank'");
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        target.flipper = (ViewFlipper) finder.castView((View) finder.findRequiredView(source, R
                .id.flipper, "field 'flipper'"), R.id.flipper, "field 'flipper'");
        target.calendarGrid = (GridView) finder.castView((View) finder.findRequiredView(source, R
                .id.calendar, "field 'calendarGrid'"), R.id.calendar, "field 'calendarGrid'");
    }

    public void reset(T target) {
        target.rbtn_record_waist = null;
        target.rbtn_record_brass = null;
        target.rbtn_record_hip = null;
        target.rbtn_record_arm = null;
        target.rbtn_record_thigh = null;
        target.rbtn_record_shank = null;
        target.flipper = null;
        target.calendarGrid = null;
    }
}
