package com.boohee.one.ui.fragment;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import butterknife.ButterKnife;
import butterknife.InjectView;

import com.boohee.myview.ViewPagerHeaderScroll.delegate.ScrollViewDelegate;
import com.boohee.myview.ViewPagerHeaderScroll.fragment.BaseViewPagerFragment;
import com.boohee.one.R;
import com.boohee.utility.BooheeScheme;

public class TimeLineBrowserFragment extends BaseViewPagerFragment {
    @InjectView(2131428207)
    ProgressBar mPbLoading;
    private String mUrl;
    @InjectView(2131428361)
    WebView mWebView;
    private ScrollViewDelegate scrollViewDelegate = new ScrollViewDelegate();

    private class ChromeClient extends WebChromeClient {
        private ChromeClient() {
        }

        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            if (TimeLineBrowserFragment.this.mPbLoading != null) {
                TimeLineBrowserFragment.this.mPbLoading.setProgress(newProgress);
            }
        }
    }

    private class WebClient extends WebViewClient {
        private WebClient() {
        }

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            BooheeScheme.handleUrl(TimeLineBrowserFragment.this.getActivity(), url);
            return true;
        }

        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            if (TimeLineBrowserFragment.this.mPbLoading != null) {
                TimeLineBrowserFragment.this.mPbLoading.setVisibility(0);
            }
        }

        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (TimeLineBrowserFragment.this.mPbLoading != null) {
                TimeLineBrowserFragment.this.mPbLoading.setVisibility(8);
            }
        }
    }

    public static TimeLineBrowserFragment newInstance(String url) {
        TimeLineBrowserFragment frag = new TimeLineBrowserFragment();
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        frag.setArguments(bundle);
        return frag;
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mUrl = getArguments().getString("url");
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            this.mUrl = savedInstanceState.getString("url");
        }
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.gs, container, false);
        ButterKnife.inject((Object) this, view);
        return view;
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.mWebView.setWebViewClient(new WebClient());
        this.mWebView.setWebChromeClient(new ChromeClient());
        this.mWebView.getSettings().setJavaScriptEnabled(true);
    }

    public void onSaveInstanceState(Bundle outState) {
        if (outState == null) {
            outState = new Bundle();
        }
        outState.putString("url", this.mUrl);
        super.onSaveInstanceState(outState);
    }

    public void onDestroy() {
        super.onDestroy();
        this.mWebView.stopLoading();
    }

    public void loadWeb() {
        if (this.mWebView != null && !TextUtils.isEmpty(this.mUrl)) {
            this.mWebView.loadUrl(this.mUrl);
        }
    }

    public boolean isViewBeingDragged(MotionEvent event) {
        return true;
    }
}
