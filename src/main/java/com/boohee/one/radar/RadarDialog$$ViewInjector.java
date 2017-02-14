package com.boohee.one.radar;

import android.view.View;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;
import butterknife.internal.DebouncingOnClickListener;

import com.boohee.one.R;

public class RadarDialog$$ViewInjector<T extends RadarDialog> implements Injector<T> {
    public void inject(Finder finder, final T target, Object source) {
        ((View) finder.findRequiredView(source, R.id.known, "method 'close'")).setOnClickListener
                (new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.close();
            }
        });
    }

    public void reset(T t) {
    }
}
