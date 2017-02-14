package com.boohee.one.ui;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

import com.boohee.one.R;

public class NineGridGalleryActivity$$ViewInjector<T extends NineGridGalleryActivity> implements
        Injector<T> {
    public void inject(Finder finder, T target, Object source) {
        target.viewpager = (ViewPager) finder.castView((View) finder.findRequiredView(source, R
                .id.viewpager, "field 'viewpager'"), R.id.viewpager, "field 'viewpager'");
        target.tvIndex = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_index, "field 'tvIndex'"), R.id.tv_index, "field 'tvIndex'");
        target.rlGallery = (RelativeLayout) finder.castView((View) finder.findRequiredView
                (source, R.id.rl_gallery, "field 'rlGallery'"), R.id.rl_gallery, "field " +
                "'rlGallery'");
    }

    public void reset(T target) {
        target.viewpager = null;
        target.tvIndex = null;
        target.rlGallery = null;
    }
}
