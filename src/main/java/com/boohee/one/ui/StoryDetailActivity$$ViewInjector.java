package com.boohee.one.ui;

import android.view.View;
import android.webkit.WebView;
import android.widget.CheckBox;
import android.widget.TextView;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;
import butterknife.internal.DebouncingOnClickListener;

import com.boohee.one.R;

public class StoryDetailActivity$$ViewInjector<T extends StoryDetailActivity> implements
        Injector<T> {
    public void inject(Finder finder, final T target, Object source) {
        target.webView = (WebView) finder.castView((View) finder.findRequiredView(source, R.id
                .wv_content, "field 'webView'"), R.id.wv_content, "field 'webView'");
        target.cbPraise = (CheckBox) finder.castView((View) finder.findRequiredView(source, R.id
                .cb_praise, "field 'cbPraise'"), R.id.cb_praise, "field 'cbPraise'");
        target.tvPraisePlus = (TextView) finder.castView((View) finder.findRequiredView(source, R
                .id.tv_praise_plus, "field 'tvPraisePlus'"), R.id.tv_praise_plus, "field " +
                "'tvPraisePlus'");
        target.tvComment = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_comment, "field 'tvComment'"), R.id.tv_comment, "field 'tvComment'");
        target.cbCollect = (CheckBox) finder.castView((View) finder.findRequiredView(source, R.id
                .cb_collect, "field 'cbCollect'"), R.id.cb_collect, "field 'cbCollect'");
        ((View) finder.findRequiredView(source, R.id.rl_praise, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        ((View) finder.findRequiredView(source, R.id.ll_comment, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        ((View) finder.findRequiredView(source, R.id.ll_collect, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
    }

    public void reset(T target) {
        target.webView = null;
        target.cbPraise = null;
        target.tvPraisePlus = null;
        target.tvComment = null;
        target.cbCollect = null;
    }
}
