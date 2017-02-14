package com.boohee.one.video.ui;

import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;
import butterknife.internal.DebouncingOnClickListener;

import com.boohee.one.R;
import com.boohee.one.video.view.ProgressBarHintView;

public class VideoPlayActivity$$ViewInjector<T extends VideoPlayActivity> implements Injector<T> {
    public void inject(Finder finder, final T target, Object source) {
        target.rootVideo = (RelativeLayout) finder.castView((View) finder.findRequiredView
                (source, R.id.root_video, "field 'rootVideo'"), R.id.root_video, "field " +
                "'rootVideo'");
        target.videoView = (VideoView) finder.castView((View) finder.findRequiredView(source, R
                .id.video_view, "field 'videoView'"), R.id.video_view, "field 'videoView'");
        View view = (View) finder.findRequiredView(source, R.id.controller_view, "field " +
                "'controllerView' and method 'onClick'");
        target.controllerView = (RelativeLayout) finder.castView(view, R.id.controller_view,
                "field 'controllerView'");
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        target.tvTotalTime = (Chronometer) finder.castView((View) finder.findRequiredView(source,
                R.id.tv_total_time, "field 'tvTotalTime'"), R.id.tv_total_time, "field " +
                "'tvTotalTime'");
        target.progressBar = (ProgressBar) finder.castView((View) finder.findRequiredView(source,
                R.id.progress_bar, "field 'progressBar'"), R.id.progress_bar, "field " +
                "'progressBar'");
        target.pauseLayout = (RelativeLayout) finder.castView((View) finder.findRequiredView
                (source, R.id.pause_layout, "field 'pauseLayout'"), R.id.pause_layout, "field " +
                "'pauseLayout'");
        target.tvMentionTime = (Chronometer) finder.castView((View) finder.findRequiredView
                (source, R.id.tv_mention_time, "field 'tvMentionTime'"), R.id.tv_mention_time,
                "field 'tvMentionTime'");
        target.tvMentionCount = (TextView) finder.castView((View) finder.findRequiredView(source,
                R.id.tv_mention_count, "field 'tvMentionCount'"), R.id.tv_mention_count, "field " +
                "'tvMentionCount'");
        target.progressBarDivider = (ProgressBarHintView) finder.castView((View) finder
                .findRequiredView(source, R.id.progress_bar_divider, "field " +
                        "'progressBarDivider'"), R.id.progress_bar_divider, "field " +
                "'progressBarDivider'");
        target.tvName = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_name, "field 'tvName'"), R.id.tv_name, "field 'tvName'");
        target.tvEssentials1 = (TextView) finder.castView((View) finder.findRequiredView(source,
                R.id.tv_essentials_1, "field 'tvEssentials1'"), R.id.tv_essentials_1, "field " +
                "'tvEssentials1'");
        target.tvEssentials2 = (TextView) finder.castView((View) finder.findRequiredView(source,
                R.id.tv_essentials_2, "field 'tvEssentials2'"), R.id.tv_essentials_2, "field " +
                "'tvEssentials2'");
        target.tvEssentials3 = (TextView) finder.castView((View) finder.findRequiredView(source,
                R.id.tv_essentials_3, "field 'tvEssentials3'"), R.id.tv_essentials_3, "field " +
                "'tvEssentials3'");
        target.tvCommonError = (TextView) finder.castView((View) finder.findRequiredView(source,
                R.id.tv_common_error, "field 'tvCommonError'"), R.id.tv_common_error, "field " +
                "'tvCommonError'");
        target.tvBreath = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_breath, "field 'tvBreath'"), R.id.tv_breath, "field 'tvBreath'");
        target.essentials1Layout = (LinearLayout) finder.castView((View) finder.findRequiredView
                (source, R.id.essentials_1_layout, "field 'essentials1Layout'"), R.id
                .essentials_1_layout, "field 'essentials1Layout'");
        target.essentials2Layout = (LinearLayout) finder.castView((View) finder.findRequiredView
                (source, R.id.essentials_2_layout, "field 'essentials2Layout'"), R.id
                .essentials_2_layout, "field 'essentials2Layout'");
        target.essentials3Layout = (LinearLayout) finder.castView((View) finder.findRequiredView
                (source, R.id.essentials_3_layout, "field 'essentials3Layout'"), R.id
                .essentials_3_layout, "field 'essentials3Layout'");
        target.commonErrorLayout = (LinearLayout) finder.castView((View) finder.findRequiredView
                (source, R.id.common_error_layout, "field 'commonErrorLayout'"), R.id
                .common_error_layout, "field 'commonErrorLayout'");
        target.breathLayout = (LinearLayout) finder.castView((View) finder.findRequiredView
                (source, R.id.breath_layout, "field 'breathLayout'"), R.id.breath_layout, "field " +
                "'breathLayout'");
        target.tvMentionTitle = (TextView) finder.castView((View) finder.findRequiredView(source,
                R.id.tv_mention_title, "field 'tvMentionTitle'"), R.id.tv_mention_title, "field " +
                "'tvMentionTitle'");
        view = (View) finder.findRequiredView(source, R.id.btn_close_bgm, "field 'btnCloseBgm' " +
                "and method 'onClick'");
        target.btnCloseBgm = (ImageView) finder.castView(view, R.id.btn_close_bgm, "field " +
                "'btnCloseBgm'");
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        view = (View) finder.findRequiredView(source, R.id.tv_bgm_fit, "field 'tvBgmFit' and " +
                "method 'onClick'");
        target.tvBgmFit = (TextView) finder.castView(view, R.id.tv_bgm_fit, "field 'tvBgmFit'");
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        view = (View) finder.findRequiredView(source, R.id.tv_bgm_up, "field 'tvBgmUp' and method" +
                " 'onClick'");
        target.tvBgmUp = (TextView) finder.castView(view, R.id.tv_bgm_up, "field 'tvBgmUp'");
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        target.bgmControlLayout = (LinearLayout) finder.castView((View) finder.findRequiredView
                (source, R.id.bgm_control_layout, "field 'bgmControlLayout'"), R.id
                .bgm_control_layout, "field 'bgmControlLayout'");
        target.tvMoreBgm = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_more_bgm, "field 'tvMoreBgm'"), R.id.tv_more_bgm, "field 'tvMoreBgm'");
        target.tvBgmState = (TextView) finder.castView((View) finder.findRequiredView(source, R
                .id.tv_bgm_state, "field 'tvBgmState'"), R.id.tv_bgm_state, "field 'tvBgmState'");
        view = (View) finder.findRequiredView(source, R.id.btn_open_bgm_control, "field " +
                "'btnOpenBgmControl' and method 'onClick'");
        target.btnOpenBgmControl = (ImageView) finder.castView(view, R.id.btn_open_bgm_control,
                "field 'btnOpenBgmControl'");
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        target.tvGroupCount = (TextView) finder.castView((View) finder.findRequiredView(source, R
                .id.tv_group_count, "field 'tvGroupCount'"), R.id.tv_group_count, "field " +
                "'tvGroupCount'");
        ((View) finder.findRequiredView(source, R.id.btn_close, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        ((View) finder.findRequiredView(source, R.id.btn_prev, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        ((View) finder.findRequiredView(source, R.id.btn_next, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        ((View) finder.findRequiredView(source, R.id.btn_pause, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        ((View) finder.findRequiredView(source, R.id.btn_resume, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        ((View) finder.findRequiredView(source, R.id.btn_close_bgm_control, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
    }

    public void reset(T target) {
        target.rootVideo = null;
        target.videoView = null;
        target.controllerView = null;
        target.tvTotalTime = null;
        target.progressBar = null;
        target.pauseLayout = null;
        target.tvMentionTime = null;
        target.tvMentionCount = null;
        target.progressBarDivider = null;
        target.tvName = null;
        target.tvEssentials1 = null;
        target.tvEssentials2 = null;
        target.tvEssentials3 = null;
        target.tvCommonError = null;
        target.tvBreath = null;
        target.essentials1Layout = null;
        target.essentials2Layout = null;
        target.essentials3Layout = null;
        target.commonErrorLayout = null;
        target.breathLayout = null;
        target.tvMentionTitle = null;
        target.btnCloseBgm = null;
        target.tvBgmFit = null;
        target.tvBgmUp = null;
        target.bgmControlLayout = null;
        target.tvMoreBgm = null;
        target.tvBgmState = null;
        target.btnOpenBgmControl = null;
        target.tvGroupCount = null;
    }
}
