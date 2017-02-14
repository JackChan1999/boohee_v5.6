package com.boohee.one.bet;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;
import butterknife.internal.DebouncingOnClickListener;

import com.boohee.one.R;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.viewpagerindicator.LinePageIndicator;

public class BetFragment$$ViewInjector<T extends BetFragment> implements Injector<T> {
    public void inject(Finder finder, final T target, Object source) {
        target.ivBetTop = (ImageView) finder.castView((View) finder.findRequiredView(source, R.id
                .iv_bet_top, "field 'ivBetTop'"), R.id.iv_bet_top, "field 'ivBetTop'");
        target.ivBetDes = (ImageView) finder.castView((View) finder.findRequiredView(source, R.id
                .iv_bet_des, "field 'ivBetDes'"), R.id.iv_bet_des, "field 'ivBetDes'");
        target.llContent = (LinearLayout) finder.castView((View) finder.findRequiredView(source,
                R.id.ll_content, "field 'llContent'"), R.id.ll_content, "field 'llContent'");
        target.scrollview = (PullToRefreshScrollView) finder.castView((View) finder
                .findRequiredView(source, R.id.scrollview, "field 'scrollview'"), R.id
                .scrollview, "field 'scrollview'");
        target.viewpagerTips = (ViewPager) finder.castView((View) finder.findRequiredView(source,
                R.id.viewpager_tips, "field 'viewpagerTips'"), R.id.viewpager_tips, "field " +
                "'viewpagerTips'");
        target.flTips = (FrameLayout) finder.castView((View) finder.findRequiredView(source, R.id
                .fl_tips, "field 'flTips'"), R.id.fl_tips, "field 'flTips'");
        target.tipsIndicator = (LinePageIndicator) finder.castView((View) finder.findRequiredView
                (source, R.id.tips_indicator, "field 'tipsIndicator'"), R.id.tips_indicator,
                "field 'tipsIndicator'");
        target.teamContent = (LinearLayout) finder.castView((View) finder.findRequiredView
                (source, R.id.team_content, "field 'teamContent'"), R.id.team_content, "field " +
                "'teamContent'");
        target.llBets = (LinearLayout) finder.castView((View) finder.findRequiredView(source, R
                .id.ll_bets, "field 'llBets'"), R.id.ll_bets, "field 'llBets'");
        target.llTeam = (LinearLayout) finder.castView((View) finder.findRequiredView(source, R
                .id.ll_team, "field 'llTeam'"), R.id.ll_team, "field 'llTeam'");
        ((View) finder.findRequiredView(source, R.id.ll_bet_des, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        ((View) finder.findRequiredView(source, R.id.btn_all_bet, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        ((View) finder.findRequiredView(source, R.id.btn_all_team, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        ((View) finder.findRequiredView(source, R.id.view_become_leader, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
    }

    public void reset(T target) {
        target.ivBetTop = null;
        target.ivBetDes = null;
        target.llContent = null;
        target.scrollview = null;
        target.viewpagerTips = null;
        target.flTips = null;
        target.tipsIndicator = null;
        target.teamContent = null;
        target.llBets = null;
        target.llTeam = null;
    }
}
