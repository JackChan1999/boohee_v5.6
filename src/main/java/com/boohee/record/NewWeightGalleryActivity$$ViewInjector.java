package com.boohee.record;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;
import butterknife.internal.DebouncingOnClickListener;

import com.boohee.one.R;

public class NewWeightGalleryActivity$$ViewInjector<T extends NewWeightGalleryActivity>
        implements Injector<T> {
    public void inject(Finder finder, final T target, Object source) {
        target.viewpager = (ViewPager) finder.castView((View) finder.findRequiredView(source, R
                .id.viewpager, "field 'viewpager'"), R.id.viewpager, "field 'viewpager'");
        target.tvWeight = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_weight, "field 'tvWeight'"), R.id.tv_weight, "field 'tvWeight'");
        target.tvTime = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_time, "field 'tvTime'"), R.id.tv_time, "field 'tvTime'");
        View view = (View) finder.findRequiredView(source, R.id.bt_downlaod, "field 'btDownload' " +
                "and method 'onClick'");
        target.btDownload = (ImageButton) finder.castView(view, R.id.bt_downlaod, "field " +
                "'btDownload'");
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        view = (View) finder.findRequiredView(source, R.id.bt_delete, "field 'btDelete' and " +
                "method 'onClick'");
        target.btDelete = (ImageButton) finder.castView(view, R.id.bt_delete, "field 'btDelete'");
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
    }

    public void reset(T target) {
        target.viewpager = null;
        target.tvWeight = null;
        target.tvTime = null;
        target.btDownload = null;
        target.btDelete = null;
    }
}
