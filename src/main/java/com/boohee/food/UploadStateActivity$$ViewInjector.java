package com.boohee.food;

import android.view.View;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

import com.boohee.one.R;

public class UploadStateActivity$$ViewInjector<T extends UploadStateActivity> implements
        Injector<T> {
    public void inject(Finder finder, T target, Object source) {
        target.brandItem = (View) finder.findRequiredView(source, R.id.view_item_brand, "field " +
                "'brandItem'");
        target.aliasItem = (View) finder.findRequiredView(source, R.id.view_item_alias, "field " +
                "'aliasItem'");
    }

    public void reset(T target) {
        target.brandItem = null;
        target.aliasItem = null;
    }
}
