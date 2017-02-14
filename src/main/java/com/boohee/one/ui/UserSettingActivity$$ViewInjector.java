package com.boohee.one.ui;

import android.view.View;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;
import butterknife.internal.DebouncingOnClickListener;

import com.boohee.myview.SettingItemView;
import com.boohee.one.R;

public class UserSettingActivity$$ViewInjector<T extends UserSettingActivity> implements
        Injector<T> {
    public void inject(Finder finder, final T target, Object source) {
        View view = (View) finder.findRequiredView(source, R.id.clv_clean_cache, "field " +
                "'cacheLine' and method 'onClick'");
        target.cacheLine = (SettingItemView) finder.castView(view, R.id.clv_clean_cache, "field " +
                "'cacheLine'");
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        view = (View) finder.findRequiredView(source, R.id.clv_order, "field 'clv_order' and " +
                "method 'onClick'");
        target.clv_order = (SettingItemView) finder.castView(view, R.id.clv_order, "field " +
                "'clv_order'");
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        view = (View) finder.findRequiredView(source, R.id.clv_coupon, "field 'clv_coupon' and " +
                "method 'onClick'");
        target.clv_coupon = (SettingItemView) finder.castView(view, R.id.clv_coupon, "field " +
                "'clv_coupon'");
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        view = (View) finder.findRequiredView(source, R.id.clv_about_boohee, "field 'aboutLine' " +
                "and method 'onClick'");
        target.aboutLine = (SettingItemView) finder.castView(view, R.id.clv_about_boohee, "field " +
                "'aboutLine'");
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        view = (View) finder.findRequiredView(source, R.id.clv_change_environment, "field " +
                "'changeEnvironment' and method 'onClick'");
        target.changeEnvironment = (SettingItemView) finder.castView(view, R.id
                .clv_change_environment, "field 'changeEnvironment'");
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        view = (View) finder.findRequiredView(source, R.id.clv_step, "field 'clvStep' and method " +
                "'onClick'");
        target.clvStep = (SettingItemView) finder.castView(view, R.id.clv_step, "field 'clvStep'");
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        ((View) finder.findRequiredView(source, R.id.clv_mi_band, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        ((View) finder.findRequiredView(source, R.id.clv_address, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        ((View) finder.findRequiredView(source, R.id.clv_account_setting, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        ((View) finder.findRequiredView(source, R.id.clv_score, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        ((View) finder.findRequiredView(source, R.id.clv_share, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        ((View) finder.findRequiredView(source, R.id.logoutBtn, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        ((View) finder.findRequiredView(source, R.id.clv_question, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        ((View) finder.findRequiredView(source, R.id.clv_estimate, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
    }

    public void reset(T target) {
        target.cacheLine = null;
        target.clv_order = null;
        target.clv_coupon = null;
        target.aboutLine = null;
        target.changeEnvironment = null;
        target.clvStep = null;
    }
}
