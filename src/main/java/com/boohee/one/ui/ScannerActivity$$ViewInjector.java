package com.boohee.one.ui;

import android.view.View;
import android.widget.FrameLayout;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

import com.boohee.one.R;

public class ScannerActivity$$ViewInjector<T extends ScannerActivity> implements Injector<T> {
    public void inject(Finder finder, T target, Object source) {
        target.flScanner = (FrameLayout) finder.castView((View) finder.findRequiredView(source, R
                .id.fl_scanner, "field 'flScanner'"), R.id.fl_scanner, "field 'flScanner'");
    }

    public void reset(T target) {
        target.flScanner = null;
    }
}
