package com.boohee.one.ui.fragment;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;
import butterknife.internal.DebouncingOnClickListener;

import com.boohee.one.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeMineFragment$$ViewInjector<T extends HomeMineFragment> implements Injector<T> {
    public void inject(Finder finder, final T target, Object source) {
        target.ivWeekStatus = (ImageView) finder.castView((View) finder.findRequiredView(source,
                R.id.iv_week_status, "field 'ivWeekStatus'"), R.id.iv_week_status, "field " +
                "'ivWeekStatus'");
        target.ivAvatar = (CircleImageView) finder.castView((View) finder.findRequiredView
                (source, R.id.iv_avatar, "field 'ivAvatar'"), R.id.iv_avatar, "field 'ivAvatar'");
        target.tvPostCount = (TextView) finder.castView((View) finder.findRequiredView(source, R
                .id.tv_post_count, "field 'tvPostCount'"), R.id.tv_post_count, "field " +
                "'tvPostCount'");
        target.tvFollowingCount = (TextView) finder.castView((View) finder.findRequiredView
                (source, R.id.tv_following_count, "field 'tvFollowingCount'"), R.id
                .tv_following_count, "field 'tvFollowingCount'");
        target.tvFollowerCount = (TextView) finder.castView((View) finder.findRequiredView
                (source, R.id.tv_follower_count, "field 'tvFollowerCount'"), R.id
                .tv_follower_count, "field 'tvFollowerCount'");
        target.tvBadgeCount = (TextView) finder.castView((View) finder.findRequiredView(source, R
                .id.tv_badges_count, "field 'tvBadgeCount'"), R.id.tv_badges_count, "field " +
                "'tvBadgeCount'");
        ((View) finder.findRequiredView(source, R.id.view_profile, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        ((View) finder.findRequiredView(source, R.id.view_post, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        ((View) finder.findRequiredView(source, R.id.view_follower, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        ((View) finder.findRequiredView(source, R.id.view_following, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        ((View) finder.findRequiredView(source, R.id.view_health_report, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        ((View) finder.findRequiredView(source, R.id.view_favorite, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        ((View) finder.findRequiredView(source, R.id.view_food, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        ((View) finder.findRequiredView(source, R.id.view_diamond, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        ((View) finder.findRequiredView(source, R.id.view_setting, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        ((View) finder.findRequiredView(source, R.id.view_badge, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        ((View) finder.findRequiredView(source, R.id.view_hardware, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        ((View) finder.findRequiredView(source, R.id.view_nice, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        ((View) finder.findRequiredView(source, R.id.view_question, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        ((View) finder.findRequiredView(source, R.id.view_report, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
    }

    public void reset(T target) {
        target.ivWeekStatus = null;
        target.ivAvatar = null;
        target.tvPostCount = null;
        target.tvFollowingCount = null;
        target.tvFollowerCount = null;
        target.tvBadgeCount = null;
    }
}
