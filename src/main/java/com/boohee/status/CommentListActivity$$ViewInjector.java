package com.boohee.status;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;
import butterknife.internal.DebouncingOnClickListener;
import cn.dreamtobe.kpswitch.widget.KPSwitchPanelLinearLayout;

import com.boohee.one.R;
import com.viewpagerindicator.CirclePageIndicator;

public class CommentListActivity$$ViewInjector<T extends CommentListActivity> implements
        Injector<T> {
    public void inject(Finder finder, final T target, Object source) {
        View view = (View) finder.findRequiredView(source, R.id.status_post_text_emojiBtn, "field" +
                " 'btnEmoji' and method 'onClick'");
        target.btnEmoji = (ImageButton) finder.castView(view, R.id.status_post_text_emojiBtn,
                "field 'btnEmoji'");
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        target.viewPagerEmoji = (ViewPager) finder.castView((View) finder.findRequiredView
                (source, R.id.view_pager_emoji, "field 'viewPagerEmoji'"), R.id.view_pager_emoji,
                "field 'viewPagerEmoji'");
        target.indicatorEmoji = (CirclePageIndicator) finder.castView((View) finder
                .findRequiredView(source, R.id.indicator_emoji, "field 'indicatorEmoji'"), R.id
                .indicator_emoji, "field 'indicatorEmoji'");
        target.lyEmoji = (KPSwitchPanelLinearLayout) finder.castView((View) finder
                .findRequiredView(source, R.id.ly_emoji, "field 'lyEmoji'"), R.id.ly_emoji,
                "field 'lyEmoji'");
        ((View) finder.findRequiredView(source, R.id.btn_comment, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
    }

    public void reset(T target) {
        target.btnEmoji = null;
        target.viewPagerEmoji = null;
        target.indicatorEmoji = null;
        target.lyEmoji = null;
    }
}
