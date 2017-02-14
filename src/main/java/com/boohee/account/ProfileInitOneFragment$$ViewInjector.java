package com.boohee.account;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;
import butterknife.internal.DebouncingOnClickListener;

import com.boohee.myview.BooheeRulerView;
import com.boohee.one.R;

public class ProfileInitOneFragment$$ViewInjector<T extends ProfileInitOneFragment> implements
        Injector<T> {
    public void inject(Finder finder, final T target, Object source) {
        target.ivMale = (ImageView) finder.castView((View) finder.findRequiredView(source, R.id
                .iv_male, "field 'ivMale'"), R.id.iv_male, "field 'ivMale'");
        target.tvMale = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_male, "field 'tvMale'"), R.id.tv_male, "field 'tvMale'");
        View view = (View) finder.findRequiredView(source, R.id.ll_male, "field 'llMale' and " +
                "method 'onClick'");
        target.llMale = (LinearLayout) finder.castView(view, R.id.ll_male, "field 'llMale'");
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        target.ivFemale = (ImageView) finder.castView((View) finder.findRequiredView(source, R.id
                .iv_female, "field 'ivFemale'"), R.id.iv_female, "field 'ivFemale'");
        target.tvFemale = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_female, "field 'tvFemale'"), R.id.tv_female, "field 'tvFemale'");
        view = (View) finder.findRequiredView(source, R.id.ll_female, "field 'llFemale' and " +
                "method 'onClick'");
        target.llFemale = (LinearLayout) finder.castView(view, R.id.ll_female, "field 'llFemale'");
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        target.tvHeight = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_height, "field 'tvHeight'"), R.id.tv_height, "field 'tvHeight'");
        target.rvHeight = (BooheeRulerView) finder.castView((View) finder.findRequiredView
                (source, R.id.rv_height, "field 'rvHeight'"), R.id.rv_height, "field 'rvHeight'");
        ((View) finder.findRequiredView(source, R.id.btn_next, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
    }

    public void reset(T target) {
        target.ivMale = null;
        target.tvMale = null;
        target.llMale = null;
        target.ivFemale = null;
        target.tvFemale = null;
        target.llFemale = null;
        target.tvHeight = null;
        target.rvHeight = null;
    }
}
