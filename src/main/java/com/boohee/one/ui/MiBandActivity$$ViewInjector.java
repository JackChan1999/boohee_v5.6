package com.boohee.one.ui;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;
import butterknife.internal.DebouncingOnClickListener;

import com.boohee.one.R;

public class MiBandActivity$$ViewInjector<T extends MiBandActivity> implements Injector<T> {
    public void inject(Finder finder, final T target, Object source) {
        View view = (View) finder.findRequiredView(source, R.id.view_band, "field 'viewBand' and " +
                "method 'onClick'");
        target.viewBand = (Button) finder.castView(view, R.id.view_band, "field 'viewBand'");
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        view = (View) finder.findRequiredView(source, R.id.view_unband, "field 'viewUnband' and " +
                "method 'onClick'");
        target.viewUnband = (TextView) finder.castView(view, R.id.view_unband, "field " +
                "'viewUnband'");
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        target.viewStatus = (LinearLayout) finder.castView((View) finder.findRequiredView(source,
                R.id.view_status, "field 'viewStatus'"), R.id.view_status, "field 'viewStatus'");
    }

    public void reset(T target) {
        target.viewBand = null;
        target.viewUnband = null;
        target.viewStatus = null;
    }
}
