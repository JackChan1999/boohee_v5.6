package com.boohee.one.ui;

import android.view.View;
import android.widget.LinearLayout;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

import com.boohee.one.R;

public class SuccessStoryTagActivity$$ViewInjector<T extends SuccessStoryTagActivity> implements
        Injector<T> {
    public void inject(Finder finder, T target, Object source) {
        target.llContent = (LinearLayout) finder.castView((View) finder.findRequiredView(source,
                R.id.ll_content, "field 'llContent'"), R.id.ll_content, "field 'llContent'");
    }

    public void reset(T target) {
        target.llContent = null;
    }
}
