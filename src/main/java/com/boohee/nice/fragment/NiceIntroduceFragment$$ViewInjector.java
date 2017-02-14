package com.boohee.nice.fragment;

import android.view.View;
import android.webkit.WebView;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;
import butterknife.internal.DebouncingOnClickListener;

import com.boohee.one.R;

public class NiceIntroduceFragment$$ViewInjector<T extends NiceIntroduceFragment> implements
        Injector<T> {
    public void inject(Finder finder, final T target, Object source) {
        target.webview = (WebView) finder.castView((View) finder.findRequiredView(source, R.id
                .webview, "field 'webview'"), R.id.webview, "field 'webview'");
        ((View) finder.findRequiredView(source, R.id.view_contact_us, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        ((View) finder.findRequiredView(source, R.id.btn_buy_immediately, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
    }

    public void reset(T target) {
        target.webview = null;
    }
}
