package com.boohee.one.ui.fragment;

import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

import com.boohee.one.R;

public class TimeLineBrowserFragment$$ViewInjector<T extends TimeLineBrowserFragment> implements
        Injector<T> {
    public void inject(Finder finder, T target, Object source) {
        target.mWebView = (WebView) finder.castView((View) finder.findRequiredView(source, R.id
                .web_view, "field 'mWebView'"), R.id.web_view, "field 'mWebView'");
        target.mPbLoading = (ProgressBar) finder.castView((View) finder.findRequiredView(source,
                R.id.pb_loading, "field 'mPbLoading'"), R.id.pb_loading, "field 'mPbLoading'");
    }

    public void reset(T target) {
        target.mWebView = null;
        target.mPbLoading = null;
    }
}
