package com.boohee.one.radar;

import android.view.View;
import android.widget.TextView;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

import com.boohee.one.R;

public class SportLayout$$ViewInjector<T extends SportLayout> implements Injector<T> {
    public void inject(Finder finder, T target, Object source) {
        target.emptySport = (TextView) finder.castView((View) finder.findRequiredView(source, R
                .id.empty_sport, "field 'emptySport'"), R.id.empty_sport, "field 'emptySport'");
    }

    public void reset(T target) {
        target.emptySport = null;
    }
}
