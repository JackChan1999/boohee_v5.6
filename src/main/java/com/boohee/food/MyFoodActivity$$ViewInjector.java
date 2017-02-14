package com.boohee.food;

import android.view.View;
import android.widget.TextView;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;
import butterknife.internal.DebouncingOnClickListener;

import com.boohee.one.R;

public class MyFoodActivity$$ViewInjector<T extends MyFoodActivity> implements Injector<T> {
    public void inject(Finder finder, final T target, Object source) {
        target.collectNum = (TextView) finder.castView((View) finder.findRequiredView(source, R
                .id.tv_collect_food, "field 'collectNum'"), R.id.tv_collect_food, "field " +
                "'collectNum'");
        target.customNum = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_custom_food, "field 'customNum'"), R.id.tv_custom_food, "field 'customNum'");
        target.uploadNum = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_upload_food, "field 'uploadNum'"), R.id.tv_upload_food, "field 'uploadNum'");
        target.customCook = (TextView) finder.castView((View) finder.findRequiredView(source, R
                .id.tv_custom_cook, "field 'customCook'"), R.id.tv_custom_cook, "field " +
                "'customCook'");
        ((View) finder.findRequiredView(source, R.id.rl_collect_food, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        ((View) finder.findRequiredView(source, R.id.rl_custom_food, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        ((View) finder.findRequiredView(source, R.id.ll_upload_food, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        ((View) finder.findRequiredView(source, R.id.rl_add_custom_food, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        ((View) finder.findRequiredView(source, R.id.rl_upload_food, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        ((View) finder.findRequiredView(source, R.id.rl_custom_cook, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        ((View) finder.findRequiredView(source, R.id.ll_custom_cook, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
    }

    public void reset(T target) {
        target.collectNum = null;
        target.customNum = null;
        target.uploadNum = null;
        target.customCook = null;
    }
}
