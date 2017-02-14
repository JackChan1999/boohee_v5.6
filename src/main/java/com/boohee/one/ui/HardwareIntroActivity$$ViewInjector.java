package com.boohee.one.ui;

import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

import com.boohee.one.R;

public class HardwareIntroActivity$$ViewInjector<T extends HardwareIntroActivity> implements
        Injector<T> {
    public void inject(Finder finder, T target, Object source) {
        target.tvBind = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_bind, "field 'tvBind'"), R.id.tv_bind, "field 'tvBind'");
        target.tvBuy = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_buy, "field 'tvBuy'"), R.id.tv_buy, "field 'tvBuy'");
        target.webView = (WebView) finder.castView((View) finder.findRequiredView(source, R.id
                .web, "field 'webView'"), R.id.web, "field 'webView'");
    }

    public void reset(T target) {
        target.tvBind = null;
        target.tvBuy = null;
        target.webView = null;
    }
}
