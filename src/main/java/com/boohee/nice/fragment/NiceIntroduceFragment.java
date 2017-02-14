package com.boohee.nice.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.boohee.apn.ApnActivity;
import com.boohee.main.FeedBackSwitcher;
import com.boohee.nice.NiceSellActivity;
import com.boohee.one.R;
import com.boohee.one.http.client.BooheeClient;
import com.boohee.one.ui.fragment.BaseFragment;
import com.boohee.utility.BooheeScheme;
import com.boohee.utils.MeiQiaHelper;
import com.boohee.utils.UrlUtils;

public class NiceIntroduceFragment extends BaseFragment {
    private static final String NICE_URL = "/api/v1/services/intro";
    @InjectView(2131427351)
    WebView webview;

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.g6, container, false);
        ButterKnife.inject((Object) this, view);
        return view;
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initWebView();
    }

    @SuppressLint({"SetJavaScriptEnabled"})
    private void initWebView() {
        this.webview.getSettings().setJavaScriptEnabled(true);
        this.webview.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
        this.webview.setVerticalScrollBarEnabled(false);
        this.webview.setVerticalScrollbarOverlay(false);
        this.webview.setHorizontalScrollBarEnabled(false);
        this.webview.setHorizontalScrollbarOverlay(false);
        this.webview.loadUrl(UrlUtils.handleUrl(BooheeClient.build(BooheeClient.ONE)
                .getDefaultURL(NICE_URL)));
        this.webview.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (TextUtils.isEmpty(url)) {
                    return false;
                }
                BooheeScheme.handleUrl(NiceIntroduceFragment.this.getActivity(), url);
                return true;
            }
        });
    }

    public static NiceIntroduceFragment newInstance() {
        return new NiceIntroduceFragment();
    }

    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick({2131428286, 2131428287})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.view_contact_us:
                if (FeedBackSwitcher.isFeedbackTime()) {
                    MeiQiaHelper.startChat(getActivity());
                    return;
                } else {
                    ApnActivity.comeOnBaby(getActivity(), true);
                    return;
                }
            case R.id.btn_buy_immediately:
                NiceSellActivity.startActivity(getActivity(), NiceSellActivity.NICE_SERVICE);
                return;
            default:
                return;
        }
    }
}
