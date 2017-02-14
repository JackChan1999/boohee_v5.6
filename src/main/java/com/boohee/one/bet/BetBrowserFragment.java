package com.boohee.one.bet;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.boohee.one.R;
import com.boohee.one.ui.BrowserActivity;
import com.boohee.one.ui.fragment.BaseFragment;
import com.boohee.utility.BooheeScheme;
import com.boohee.utility.TimeLinePatterns;
import com.boohee.utils.Helper;
import com.boohee.utils.UrlUtils;
import com.tencent.tinker.loader.shareutil.ShareConstants;

import de.greenrobot.event.EventBus;

public class BetBrowserFragment extends BaseFragment {
    private static final int    FILECHOOSER_RESULTCODE = 1;
    public static final  String OUT_LINK_URL           = "http://lina.elementfresh" +
            ".com/boohee201508";
    public static final  String REFFRESH               = "refresh";
    public static final  String TITLE                  = "title";
    public static final  String URL                    = "url";
    protected ProgressBar        mProgressBar;
    protected String             mTitle;
    private   ValueCallback<Uri> mUploadMessage;
    protected String             mUrl;
    protected String             originUrl;
    protected WebView            webView;

    public class MyWebClient extends WebChromeClient {
        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
            BetBrowserFragment.this.mUploadMessage = uploadMsg;
            Intent i = new Intent("android.intent.action.GET_CONTENT");
            i.addCategory("android.intent.category.OPENABLE");
            i.setType("image/*");
            BetBrowserFragment.this.startActivityForResult(Intent.createChooser(i, "File " +
                    "Chooser"), 1);
        }

        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
            BetBrowserFragment.this.mUploadMessage = uploadMsg;
            Intent i = new Intent("android.intent.action.GET_CONTENT");
            i.addCategory("android.intent.category.OPENABLE");
            i.setType("*/*");
            BetBrowserFragment.this.startActivityForResult(Intent.createChooser(i, "File " +
                    "Browser"), 1);
        }

        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String
                capture) {
            BetBrowserFragment.this.mUploadMessage = uploadMsg;
            Intent i = new Intent("android.intent.action.GET_CONTENT");
            i.addCategory("android.intent.category.OPENABLE");
            i.setType("image/*");
            BetBrowserFragment.this.startActivityForResult(Intent.createChooser(i, "File " +
                    "Chooser"), 1);
        }

        public void onProgressChanged(WebView view, int progress) {
            BetBrowserFragment.this.mProgressBar.setVisibility(0);
            BetBrowserFragment.this.mProgressBar.setProgress(progress);
            if (progress == 100) {
                BetBrowserFragment.this.mProgressBar.setVisibility(8);
            }
        }

        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
        }
    }

    public static BetBrowserFragment newInstance(String originUrl) {
        BetBrowserFragment fragment = new BetBrowserFragment();
        Bundle bundle = new Bundle();
        bundle.putString("url", originUrl);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        return LayoutInflater.from(getActivity()).inflate(R.layout.fh, null);
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        EventBus.getDefault().register(this);
        if (getArguments() != null) {
            this.originUrl = getArguments().getString("url");
        }
        this.mUrl = UrlUtils.handleUrl(this.originUrl);
        if (TextUtils.isEmpty(this.mUrl)) {
            Helper.showToast((CharSequence) "无效的链接");
            return;
        }
        initView();
        this.webView.loadUrl(this.mUrl);
    }

    public void onEvent(String event) {
        if (TextUtils.equals(event, REFFRESH) && this.webView != null) {
            this.webView.loadUrl(this.mUrl);
        }
    }

    private void initView() {
        this.mProgressBar = (ProgressBar) getView().findViewById(R.id.progress_bar);
        this.webView = (WebView) getView().findViewById(R.id.wv_content);
        this.webView.getSettings().setJavaScriptEnabled(true);
        this.webView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.NORMAL);
        this.webView.getSettings().setPluginState(PluginState.ON);
        this.webView.getSettings().setUserAgentString(this.webView.getSettings()
                .getUserAgentString() + " App/boohee");
        this.webView.getSettings().setDomStorageEnabled(true);
        this.webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (!TextUtils.isEmpty(url)) {
                    Uri uri = Uri.parse(url);
                    Intent intent;
                    if (url.contains(ShareConstants.PATCH_SUFFIX) || url.contains("http://lina" +
                            ".elementfresh.com/boohee201508")) {
                        try {
                            intent = new Intent();
                            intent.setAction("android.intent.action.VIEW");
                            intent.setData(uri);
                            BetBrowserFragment.this.startActivity(intent);
                        } catch (Exception e) {
                        }
                    } else if (url.contains(TimeLinePatterns.WEB_SCHEME) || url.contains
                            ("https://")) {
                        BrowserActivity.comeOnBaby(BetBrowserFragment.this.getActivity(), "", url);
                    } else if (!(BooheeScheme.handleUrl(BetBrowserFragment.this.getActivity(),
                            url) || uri == null)) {
                        try {
                            intent = new Intent();
                            intent.setAction("android.intent.action.VIEW");
                            intent.setData(uri);
                            BetBrowserFragment.this.startActivity(intent);
                        } catch (Exception e2) {
                        }
                    }
                }
                return true;
            }

            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                BetBrowserFragment.this.mProgressBar.setVisibility(0);
            }

            public void onPageFinished(WebView view, String url) {
                BetBrowserFragment.this.mProgressBar.setVisibility(8);
            }

            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }
        });
        this.webView.setWebChromeClient(new MyWebClient());
    }

    public void onPause() {
        super.onPause();
        if (this.webView != null) {
            this.webView.onPause();
        }
    }

    public void onStop() {
        super.onStop();
        if (this.webView != null) {
            this.webView.stopLoading();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 1 && this.mUploadMessage != null) {
            Uri result = (intent == null || resultCode != -1) ? null : intent.getData();
            this.mUploadMessage.onReceiveValue(result);
            this.mUploadMessage = null;
        }
    }

    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        if (this.webView != null) {
            this.webView.removeAllViews();
            this.webView.destroy();
        }
        super.onDestroy();
    }

    public void reload() {
        if (this.webView != null && !TextUtils.isEmpty(this.mUrl)) {
            this.webView.loadUrl(this.mUrl);
        }
    }
}
