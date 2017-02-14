package com.boohee.one.video.ui;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;
import butterknife.internal.DebouncingOnClickListener;

import com.boohee.one.R;

public class SpecialLessonDetailActivity$$ViewInjector<T extends SpecialLessonDetailActivity>
        implements Injector<T> {
    public void inject(Finder finder, final T target, Object source) {
        target.ivTop = (ImageView) finder.castView((View) finder.findRequiredView(source, R.id
                .iv_top, "field 'ivTop'"), R.id.iv_top, "field 'ivTop'");
        target.tvName = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_name, "field 'tvName'"), R.id.tv_name, "field 'tvName'");
        target.tvCalory = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_calory, "field 'tvCalory'"), R.id.tv_calory, "field 'tvCalory'");
        target.tvTime = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_time, "field 'tvTime'"), R.id.tv_time, "field 'tvTime'");
        target.tvMentionDes = (TextView) finder.castView((View) finder.findRequiredView(source, R
                .id.tv_mention_des, "field 'tvMentionDes'"), R.id.tv_mention_des, "field " +
                "'tvMentionDes'");
        target.recyclerView = (RecyclerView) finder.castView((View) finder.findRequiredView
                (source, R.id.recycler_view, "field 'recyclerView'"), R.id.recycler_view, "field " +
                "'recyclerView'");
        target.tvStart = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_start, "field 'tvStart'"), R.id.tv_start, "field 'tvStart'");
        target.progressBarDownload = (ProgressBar) finder.castView((View) finder.findRequiredView
                (source, R.id.progress_bar_download, "field 'progressBarDownload'"), R.id
                .progress_bar_download, "field 'progressBarDownload'");
        target.tvDownloadStatus = (TextView) finder.castView((View) finder.findRequiredView
                (source, R.id.tv_download_status, "field 'tvDownloadStatus'"), R.id
                .tv_download_status, "field 'tvDownloadStatus'");
        View view = (View) finder.findRequiredView(source, R.id.bottom_layout, "field " +
                "'bottomLayout' and method 'onClick'");
        target.bottomLayout = (RelativeLayout) finder.castView(view, R.id.bottom_layout, "field " +
                "'bottomLayout'");
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
    }

    public void reset(T target) {
        target.ivTop = null;
        target.tvName = null;
        target.tvCalory = null;
        target.tvTime = null;
        target.tvMentionDes = null;
        target.recyclerView = null;
        target.tvStart = null;
        target.progressBarDownload = null;
        target.tvDownloadStatus = null;
        target.bottomLayout = null;
    }
}
