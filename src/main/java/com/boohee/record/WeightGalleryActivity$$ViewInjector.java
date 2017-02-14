package com.boohee.record;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

import com.boohee.one.R;

public class WeightGalleryActivity$$ViewInjector<T extends WeightGalleryActivity> implements
        Injector<T> {
    public void inject(Finder finder, T target, Object source) {
        target.viewpager = (ViewPager) finder.castView((View) finder.findRequiredView(source, R
                .id.viewpager, "field 'viewpager'"), R.id.viewpager, "field 'viewpager'");
        target.tv_index = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_index, "field 'tv_index'"), R.id.tv_index, "field 'tv_index'");
        target.rl_gallery = (RelativeLayout) finder.castView((View) finder.findRequiredView
                (source, R.id.rl_gallery, "field 'rl_gallery'"), R.id.rl_gallery, "field " +
                "'rl_gallery'");
    }

    public void reset(T target) {
        target.viewpager = null;
        target.tv_index = null;
        target.rl_gallery = null;
    }
}
