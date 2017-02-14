package com.boohee.myview;

import android.view.View;
import android.widget.ToggleButton;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;
import butterknife.internal.DebouncingOnClickListener;

import com.boohee.one.R;

public class NiceCheckBox$$ViewInjector<T extends NiceCheckBox> implements Injector<T> {
    public void inject(Finder finder, final T target, Object source) {
        target.tbYes = (ToggleButton) finder.castView((View) finder.findRequiredView(source, R.id
                .tb_yes, "field 'tbYes'"), R.id.tb_yes, "field 'tbYes'");
        target.tbNo = (ToggleButton) finder.castView((View) finder.findRequiredView(source, R.id
                .tb_no, "field 'tbNo'"), R.id.tb_no, "field 'tbNo'");
        ((View) finder.findRequiredView(source, R.id.ll_yes, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        ((View) finder.findRequiredView(source, R.id.ll_no, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
    }

    public void reset(T target) {
        target.tbYes = null;
        target.tbNo = null;
    }
}
