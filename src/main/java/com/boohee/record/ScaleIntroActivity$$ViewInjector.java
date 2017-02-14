package com.boohee.record;

import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

import com.boohee.one.R;

public class ScaleIntroActivity$$ViewInjector<T extends ScaleIntroActivity> implements Injector<T> {
    public void inject(Finder finder, T target, Object source) {
        target.tvBind = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_bind, "field 'tvBind'"), R.id.tv_bind, "field 'tvBind'");
        target.webView = (WebView) finder.castView((View) finder.findRequiredView(source, R.id
                .web, "field 'webView'"), R.id.web, "field 'webView'");
    }

    public void reset(T target) {
        target.tvBind = null;
        target.webView = null;
    }
}
