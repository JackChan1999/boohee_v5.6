package com.boohee.myview;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

import com.boohee.one.R;

public class SettingItemView$$ViewInjector<T extends SettingItemView> implements Injector<T> {
    public void inject(Finder finder, T target, Object source) {
        target.view_top_divider = (View) finder.findRequiredView(source, R.id.view_top_divider,
                "field 'view_top_divider'");
        target.iv_icon = (ImageView) finder.castView((View) finder.findRequiredView(source, R.id
                .iv_icon, "field 'iv_icon'"), R.id.iv_icon, "field 'iv_icon'");
        target.tv_title = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_title, "field 'tv_title'"), R.id.tv_title, "field 'tv_title'");
        target.tv_subtitle = (TextView) finder.castView((View) finder.findRequiredView(source, R
                .id.tv_subtitle, "field 'tv_subtitle'"), R.id.tv_subtitle, "field 'tv_subtitle'");
        target.tv_indicate = (TextView) finder.castView((View) finder.findRequiredView(source, R
                .id.tv_indicate, "field 'tv_indicate'"), R.id.tv_indicate, "field 'tv_indicate'");
        target.view_indicate = (ImageView) finder.castView((View) finder.findRequiredView(source,
                R.id.view_indicate, "field 'view_indicate'"), R.id.view_indicate, "field " +
                "'view_indicate'");
        target.view_bottom_divider = (View) finder.findRequiredView(source, R.id
                .view_bottom_divider, "field 'view_bottom_divider'");
    }

    public void reset(T target) {
        target.view_top_divider = null;
        target.iv_icon = null;
        target.tv_title = null;
        target.tv_subtitle = null;
        target.tv_indicate = null;
        target.view_indicate = null;
        target.view_bottom_divider = null;
    }
}
