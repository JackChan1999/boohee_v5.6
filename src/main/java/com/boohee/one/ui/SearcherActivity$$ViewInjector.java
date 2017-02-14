package com.boohee.one.ui;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;
import butterknife.internal.DebouncingOnClickListener;

import com.boohee.one.R;

public class SearcherActivity$$ViewInjector<T extends SearcherActivity> implements Injector<T> {
    public void inject(Finder finder, final T target, Object source) {
        target.viewWelcome = (LinearLayout) finder.castView((View) finder.findRequiredView
                (source, R.id.view_welcome, "field 'viewWelcome'"), R.id.view_welcome, "field " +
                "'viewWelcome'");
        target.viewRecommend = (LinearLayout) finder.castView((View) finder.findRequiredView
                (source, R.id.view_recommend, "field 'viewRecommend'"), R.id.view_recommend,
                "field 'viewRecommend'");
        target.rvRecommend = (RecyclerView) finder.castView((View) finder.findRequiredView
                (source, R.id.rv_recommend, "field 'rvRecommend'"), R.id.rv_recommend, "field " +
                "'rvRecommend'");
        target.viewSearch = (FrameLayout) finder.castView((View) finder.findRequiredView(source,
                R.id.view_search, "field 'viewSearch'"), R.id.view_search, "field 'viewSearch'");
        target.viewContent = (View) finder.findRequiredView(source, R.id.view_content, "field " +
                "'viewContent'");
        target.rvSearch = (RecyclerView) finder.castView((View) finder.findRequiredView(source, R
                .id.rv_search, "field 'rvSearch'"), R.id.rv_search, "field 'rvSearch'");
        View view = (View) finder.findRequiredView(source, R.id.view_load_more, "field " +
                "'viewLoadMore' and method 'onClick'");
        target.viewLoadMore = view;
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        target.viewNoResult = (TextView) finder.castView((View) finder.findRequiredView(source, R
                .id.view_no_result, "field 'viewNoResult'"), R.id.view_no_result, "field " +
                "'viewNoResult'");
        target.viewSearchAlert = (LinearLayout) finder.castView((View) finder.findRequiredView
                (source, R.id.view_search_alert, "field 'viewSearchAlert'"), R.id
                .view_search_alert, "field 'viewSearchAlert'");
        view = (View) finder.findRequiredView(source, R.id.tv_search_alert, "field " +
                "'tvSearchAlert' and method 'onClick'");
        target.tvSearchAlert = (TextView) finder.castView(view, R.id.tv_search_alert, "field " +
                "'tvSearchAlert'");
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        ((View) finder.findRequiredView(source, R.id.view_search_topic, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        ((View) finder.findRequiredView(source, R.id.view_search_article, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        ((View) finder.findRequiredView(source, R.id.view_search_user, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
    }

    public void reset(T target) {
        target.viewWelcome = null;
        target.viewRecommend = null;
        target.rvRecommend = null;
        target.viewSearch = null;
        target.viewContent = null;
        target.rvSearch = null;
        target.viewLoadMore = null;
        target.viewNoResult = null;
        target.viewSearchAlert = null;
        target.tvSearchAlert = null;
    }
}
