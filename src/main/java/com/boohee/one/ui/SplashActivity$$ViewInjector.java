package com.boohee.one.ui;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

import com.boohee.one.R;

public class SplashActivity$$ViewInjector<T extends SplashActivity> implements Injector<T> {
    public void inject(Finder finder, T target, Object source) {
        target.imgStartLogo = (ImageView) finder.castView((View) finder.findRequiredView(source,
                R.id.img_start_logo, "field 'imgStartLogo'"), R.id.img_start_logo, "field " +
                "'imgStartLogo'");
        target.tvTime = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_time, "field 'tvTime'"), R.id.tv_time, "field 'tvTime'");
        target.ivAdContent = (ImageView) finder.castView((View) finder.findRequiredView(source, R
                .id.ivAdContent, "field 'ivAdContent'"), R.id.ivAdContent, "field 'ivAdContent'");
        target.ivThirdLogo = (ImageView) finder.castView((View) finder.findRequiredView(source, R
                .id.iv_third_logo, "field 'ivThirdLogo'"), R.id.iv_third_logo, "field " +
                "'ivThirdLogo'");
        target.tvAdTitle = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_ad_title, "field 'tvAdTitle'"), R.id.tv_ad_title, "field 'tvAdTitle'");
    }

    public void reset(T target) {
        target.imgStartLogo = null;
        target.tvTime = null;
        target.ivAdContent = null;
        target.ivThirdLogo = null;
        target.tvAdTitle = null;
    }
}
