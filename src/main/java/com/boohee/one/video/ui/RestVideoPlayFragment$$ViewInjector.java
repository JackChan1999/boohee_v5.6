package com.boohee.one.video.ui;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;
import butterknife.internal.DebouncingOnClickListener;

import com.boohee.one.R;
import com.boohee.widgets.ProgressWheel;

public class RestVideoPlayFragment$$ViewInjector<T extends RestVideoPlayFragment> implements
        Injector<T> {
    public void inject(Finder finder, final T target, Object source) {
        target.restVideoView = (VideoView) finder.castView((View) finder.findRequiredView(source,
                R.id.rest_video_view, "field 'restVideoView'"), R.id.rest_video_view, "field " +
                "'restVideoView'");
        target.progressBar = (ProgressWheel) finder.castView((View) finder.findRequiredView
                (source, R.id.progress_bar, "field 'progressBar'"), R.id.progress_bar, "field " +
                "'progressBar'");
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
        target.tvNextMention = (TextView) finder.castView((View) finder.findRequiredView(source,
                R.id.tv_next_mention, "field 'tvNextMention'"), R.id.tv_next_mention, "field " +
                "'tvNextMention'");
        ((View) finder.findRequiredView(source, R.id.btn_finish, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
    }

    public void reset(T target) {
        target.restVideoView = null;
        target.progressBar = null;
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
        target.tvNextMention = null;
    }
}
