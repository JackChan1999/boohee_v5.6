package com.boohee.one.ui.fragment;

import android.view.View;
import android.widget.TextView;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;
import butterknife.internal.DebouncingOnClickListener;

import com.boohee.one.R;

import kankan.wheel.widget.WheelView;

public class ChangeDateAndTimeTypeFragment$$ViewInjector<T extends ChangeDateAndTimeTypeFragment>
        implements Injector<T> {
    public void inject(Finder finder, final T target, Object source) {
        target.tvTitle = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_title, "field 'tvTitle'"), R.id.tv_title, "field 'tvTitle'");
        target.wheelDate = (WheelView) finder.castView((View) finder.findRequiredView(source, R
                .id.wheel_date, "field 'wheelDate'"), R.id.wheel_date, "field 'wheelDate'");
        target.wheelTimeType = (WheelView) finder.castView((View) finder.findRequiredView(source,
                R.id.wheel_time_type, "field 'wheelTimeType'"), R.id.wheel_time_type, "field " +
                "'wheelTimeType'");
        View view = (View) finder.findRequiredView(source, R.id.tv_confirm, "field 'tvConfirm' " +
                "and method 'onClick'");
        target.tvConfirm = (TextView) finder.castView(view, R.id.tv_confirm, "field 'tvConfirm'");
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
    }

    public void reset(T target) {
        target.tvTitle = null;
        target.wheelDate = null;
        target.wheelTimeType = null;
        target.tvConfirm = null;
    }
}
