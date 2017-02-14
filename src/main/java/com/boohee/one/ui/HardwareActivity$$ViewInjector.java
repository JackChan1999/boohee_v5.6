package com.boohee.one.ui;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

import com.boohee.one.R;

public class HardwareActivity$$ViewInjector<T extends HardwareActivity> implements Injector<T> {
    public void inject(Finder finder, T target, Object source) {
        target.ivScale = (ImageView) finder.castView((View) finder.findRequiredView(source, R.id
                .iv_scale, "field 'ivScale'"), R.id.iv_scale, "field 'ivScale'");
        target.tvScaleName = (TextView) finder.castView((View) finder.findRequiredView(source, R
                .id.tv_scale_name, "field 'tvScaleName'"), R.id.tv_scale_name, "field " +
                "'tvScaleName'");
        target.tvScaleGo = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_scale_go, "field 'tvScaleGo'"), R.id.tv_scale_go, "field 'tvScaleGo'");
        target.ivCling = (ImageView) finder.castView((View) finder.findRequiredView(source, R.id
                .iv_cling, "field 'ivCling'"), R.id.iv_cling, "field 'ivCling'");
        target.tvClingName = (TextView) finder.castView((View) finder.findRequiredView(source, R
                .id.tv_cling_name, "field 'tvClingName'"), R.id.tv_cling_name, "field " +
                "'tvClingName'");
        target.tvClingGo = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_cling_go, "field 'tvClingGo'"), R.id.tv_cling_go, "field 'tvClingGo'");
        target.tvScaleStatus = (TextView) finder.castView((View) finder.findRequiredView(source,
                R.id.tv_scale_status, "field 'tvScaleStatus'"), R.id.tv_scale_status, "field " +
                "'tvScaleStatus'");
        target.tvClingStatus = (TextView) finder.castView((View) finder.findRequiredView(source,
                R.id.tv_cling_status, "field 'tvClingStatus'"), R.id.tv_cling_status, "field " +
                "'tvClingStatus'");
        target.rlScale = (RelativeLayout) finder.castView((View) finder.findRequiredView(source,
                R.id.rl_scale, "field 'rlScale'"), R.id.rl_scale, "field 'rlScale'");
    }

    public void reset(T target) {
        target.ivScale = null;
        target.tvScaleName = null;
        target.tvScaleGo = null;
        target.ivCling = null;
        target.tvClingName = null;
        target.tvClingGo = null;
        target.tvScaleStatus = null;
        target.tvClingStatus = null;
        target.rlScale = null;
    }
}
