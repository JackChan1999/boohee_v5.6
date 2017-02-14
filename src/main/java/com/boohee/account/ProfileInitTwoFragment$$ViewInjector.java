package com.boohee.account;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;
import butterknife.internal.DebouncingOnClickListener;

import com.boohee.one.R;

public class ProfileInitTwoFragment$$ViewInjector<T extends ProfileInitTwoFragment> implements
        Injector<T> {
    public void inject(Finder finder, final T target, Object source) {
        View view = (View) finder.findRequiredView(source, R.id.btn_next, "field 'btnNext' and " +
                "method 'onClick'");
        target.btnNext = (TextView) finder.castView(view, R.id.btn_next, "field 'btnNext'");
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        target.pickerLayout = (LinearLayout) finder.castView((View) finder.findRequiredView
                (source, R.id.picker_layout, "field 'pickerLayout'"), R.id.picker_layout, "field " +
                "'pickerLayout'");
        target.tvBirth = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_birth, "field 'tvBirth'"), R.id.tv_birth, "field 'tvBirth'");
        target.tvNoUse = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_no_use, "field 'tvNoUse'"), R.id.tv_no_use, "field 'tvNoUse'");
    }

    public void reset(T target) {
        target.btnNext = null;
        target.pickerLayout = null;
        target.tvBirth = null;
        target.tvNoUse = null;
    }
}
