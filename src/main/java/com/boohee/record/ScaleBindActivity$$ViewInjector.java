package com.boohee.record;

import android.view.View;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

import com.boohee.one.R;
import com.skyfishjy.library.RippleBackground;

public class ScaleBindActivity$$ViewInjector<T extends ScaleBindActivity> implements Injector<T> {
    public void inject(Finder finder, T target, Object source) {
        target.ripple = (RippleBackground) finder.castView((View) finder.findRequiredView(source,
                R.id.ripple, "field 'ripple'"), R.id.ripple, "field 'ripple'");
    }

    public void reset(T target) {
        target.ripple = null;
    }
}
