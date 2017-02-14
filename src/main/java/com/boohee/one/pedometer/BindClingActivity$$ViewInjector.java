package com.boohee.one.pedometer;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;
import butterknife.internal.DebouncingOnClickListener;

import com.boohee.one.R;

public class BindClingActivity$$ViewInjector<T extends BindClingActivity> implements Injector<T> {
    public void inject(Finder finder, final T target, Object source) {
        target.editCling = (EditText) finder.castView((View) finder.findRequiredView(source, R.id
                .edit_cling, "field 'editCling'"), R.id.edit_cling, "field 'editCling'");
        View view = (View) finder.findRequiredView(source, R.id.btn_bind, "field 'btnBind' and " +
                "method 'onClick'");
        target.btnBind = (TextView) finder.castView(view, R.id.btn_bind, "field 'btnBind'");
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        target.divider = (View) finder.findRequiredView(source, R.id.divider, "field 'divider'");
    }

    public void reset(T target) {
        target.editCling = null;
        target.btnBind = null;
        target.divider = null;
    }
}
