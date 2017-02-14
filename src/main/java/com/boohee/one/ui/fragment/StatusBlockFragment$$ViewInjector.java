package com.boohee.one.ui.fragment;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;
import butterknife.internal.DebouncingOnClickListener;

import com.boohee.one.R;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;

public class StatusBlockFragment$$ViewInjector<T extends StatusBlockFragment> implements
        Injector<T> {
    public void inject(Finder finder, final T target, Object source) {
        View view = (View) finder.findRequiredView(source, R.id.btn_check_in, "field 'checkInBtn'" +
                " and method 'onClick'");
        target.checkInBtn = (TextView) finder.castView(view, R.id.btn_check_in, "field " +
                "'checkInBtn'");
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        target.checkInMsgText = (TextView) finder.castView((View) finder.findRequiredView(source,
                R.id.tv_check_in_msg, "field 'checkInMsgText'"), R.id.tv_check_in_msg, "field " +
                "'checkInMsgText'");
        target.contentLayout = (LinearLayout) finder.castView((View) finder.findRequiredView
                (source, R.id.ll_content, "field 'contentLayout'"), R.id.ll_content, "field " +
                "'contentLayout'");
        view = (View) finder.findRequiredView(source, R.id.ll_chat, "field 'chatLayout' and " +
                "method 'onClick'");
        target.chatLayout = (LinearLayout) finder.castView(view, R.id.ll_chat, "field " +
                "'chatLayout'");
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        target.friendsBadge = (TextView) finder.castView((View) finder.findRequiredView(source, R
                .id.tv_friends_badge, "field 'friendsBadge'"), R.id.tv_friends_badge, "field " +
                "'friendsBadge'");
        view = (View) finder.findRequiredView(source, R.id.ll_collect, "field 'collectLayout' and" +
                " method 'onClick'");
        target.collectLayout = (LinearLayout) finder.castView(view, R.id.ll_collect, "field " +
                "'collectLayout'");
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        target.hotBadge = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_hot_badge, "field 'hotBadge'"), R.id.tv_hot_badge, "field 'hotBadge'");
        target.chatBadge = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_chat_badge, "field 'chatBadge'"), R.id.tv_chat_badge, "field 'chatBadge'");
        target.scrollView = (PullToRefreshScrollView) finder.castView((View) finder
                .findRequiredView(source, R.id.scrollview, "field 'scrollView'"), R.id
                .scrollview, "field 'scrollView'");
        ((View) finder.findRequiredView(source, R.id.ll_check_in, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        ((View) finder.findRequiredView(source, R.id.ll_friends, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        ((View) finder.findRequiredView(source, R.id.ll_hot, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
    }

    public void reset(T target) {
        target.checkInBtn = null;
        target.checkInMsgText = null;
        target.contentLayout = null;
        target.chatLayout = null;
        target.friendsBadge = null;
        target.collectLayout = null;
        target.hotBadge = null;
        target.chatBadge = null;
        target.scrollView = null;
    }
}
