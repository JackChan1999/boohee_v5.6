package com.boohee.food;

import android.view.View;
import android.widget.TextView;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

import com.boohee.one.R;

public class LightIntroduceActivity$$ViewInjector<T extends LightIntroduceActivity> implements
        Injector<T> {
    public void inject(Finder finder, T target, Object source) {
        target.tvGreen = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_green, "field 'tvGreen'"), R.id.tv_green, "field 'tvGreen'");
        target.tvYellow = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_yellow, "field 'tvYellow'"), R.id.tv_yellow, "field 'tvYellow'");
        target.tvRed = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_red, "field 'tvRed'"), R.id.tv_red, "field 'tvRed'");
    }

    public void reset(T target) {
        target.tvGreen = null;
        target.tvYellow = null;
        target.tvRed = null;
    }
}
