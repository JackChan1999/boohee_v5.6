package com.boohee.one.video.ui;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;
import butterknife.internal.DebouncingOnClickListener;

import com.boohee.one.R;
import com.boohee.widgets.ProgressWheel;

public class MentionPreviewActivity$$ViewInjector<T extends MentionPreviewActivity> implements
        Injector<T> {
    public void inject(Finder finder, final T target, Object source) {
        target.progressBar = (ProgressWheel) finder.castView((View) finder.findRequiredView
                (source, R.id.progress_bar, "field 'progressBar'"), R.id.progress_bar, "field " +
                "'progressBar'");
        target.progressLayout = (RelativeLayout) finder.castView((View) finder.findRequiredView
                (source, R.id.progress_layout, "field 'progressLayout'"), R.id.progress_layout,
                "field 'progressLayout'");
        View view = (View) finder.findRequiredView(source, R.id.btn_close, "field 'btnClose' and " +
                "method 'onClick'");
        target.btnClose = (ImageView) finder.castView(view, R.id.btn_close, "field 'btnClose'");
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        target.videoView = (VideoView) finder.castView((View) finder.findRequiredView(source, R
                .id.video_view, "field 'videoView'"), R.id.video_view, "field 'videoView'");
        target.viewPager = (ViewPager) finder.castView((View) finder.findRequiredView(source, R
                .id.view_pager, "field 'viewPager'"), R.id.view_pager, "field 'viewPager'");
        target.tvIndex = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_index, "field 'tvIndex'"), R.id.tv_index, "field 'tvIndex'");
    }

    public void reset(T target) {
        target.progressBar = null;
        target.progressLayout = null;
        target.btnClose = null;
        target.videoView = null;
        target.viewPager = null;
        target.tvIndex = null;
    }
}
