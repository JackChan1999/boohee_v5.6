package com.boohee.record;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;
import butterknife.internal.DebouncingOnClickListener;

import com.boohee.one.R;

public class WeightCompareActivity$$ViewInjector<T extends WeightCompareActivity> implements
        Injector<T> {
    public void inject(Finder finder, final T target, Object source) {
        target.view_compare = (LinearLayout) finder.castView((View) finder.findRequiredView
                (source, R.id.view_compare, "field 'view_compare'"), R.id.view_compare, "field " +
                "'view_compare'");
        target.view_photo_before = (ImageView) finder.castView((View) finder.findRequiredView
                (source, R.id.view_photo_before, "field 'view_photo_before'"), R.id
                .view_photo_before, "field 'view_photo_before'");
        target.view_photo_now = (ImageView) finder.castView((View) finder.findRequiredView
                (source, R.id.view_photo_now, "field 'view_photo_now'"), R.id.view_photo_now,
                "field 'view_photo_now'");
        target.tv_day = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_day, "field 'tv_day'"), R.id.tv_day, "field 'tv_day'");
        target.tv_reduce = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_reduce, "field 'tv_reduce'"), R.id.tv_reduce, "field 'tv_reduce'");
        target.tv_state = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_state, "field 'tv_state'"), R.id.tv_state, "field 'tv_state'");
        View view = (View) finder.findRequiredView(source, R.id.bt_save_compare, "field " +
                "'bt_save_compare' and method 'onClick'");
        target.bt_save_compare = (Button) finder.castView(view, R.id.bt_save_compare, "field " +
                "'bt_save_compare'");
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        ((View) finder.findRequiredView(source, R.id.bt_share_compare, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
    }

    public void reset(T target) {
        target.view_compare = null;
        target.view_photo_before = null;
        target.view_photo_now = null;
        target.tv_day = null;
        target.tv_reduce = null;
        target.tv_state = null;
        target.bt_save_compare = null;
    }
}
