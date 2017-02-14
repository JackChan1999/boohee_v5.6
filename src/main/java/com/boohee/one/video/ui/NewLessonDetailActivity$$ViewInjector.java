package com.boohee.one.video.ui;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;
import butterknife.internal.DebouncingOnClickListener;

import com.boohee.one.R;

public class NewLessonDetailActivity$$ViewInjector<T extends NewLessonDetailActivity> implements
        Injector<T> {
    public void inject(Finder finder, final T target, Object source) {
        target.layout = (View) finder.findRequiredView(source, R.id.layout, "field 'layout'");
        target.ivTop = (ImageView) finder.castView((View) finder.findRequiredView(source, R.id
                .iv_top, "field 'ivTop'"), R.id.iv_top, "field 'ivTop'");
        target.tvLessonTime = (TextView) finder.castView((View) finder.findRequiredView(source, R
                .id.tv_lesson_time, "field 'tvLessonTime'"), R.id.tv_lesson_time, "field " +
                "'tvLessonTime'");
        target.llLessonTime = (LinearLayout) finder.castView((View) finder.findRequiredView
                (source, R.id.ll_lesson_time, "field 'llLessonTime'"), R.id.ll_lesson_time,
                "field 'llLessonTime'");
        target.tvTitle = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_title, "field 'tvTitle'"), R.id.tv_title, "field 'tvTitle'");
        View view = (View) finder.findRequiredView(source, R.id.iv_question, "field 'ivQuestion' " +
                "and method 'onClick'");
        target.ivQuestion = (ImageView) finder.castView(view, R.id.iv_question, "field " +
                "'ivQuestion'");
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        target.tvLessonCalory = (TextView) finder.castView((View) finder.findRequiredView(source,
                R.id.tv_lesson_calory, "field 'tvLessonCalory'"), R.id.tv_lesson_calory, "field " +
                "'tvLessonCalory'");
        target.tvLessonNumber = (TextView) finder.castView((View) finder.findRequiredView(source,
                R.id.tv_lesson_number, "field 'tvLessonNumber'"), R.id.tv_lesson_number, "field " +
                "'tvLessonNumber'");
        target.llLessonNumber = (LinearLayout) finder.castView((View) finder.findRequiredView
                (source, R.id.ll_lesson_number, "field 'llLessonNumber'"), R.id.ll_lesson_number,
                "field 'llLessonNumber'");
        target.tvMentionDes = (TextView) finder.castView((View) finder.findRequiredView(source, R
                .id.tv_mention_des, "field 'tvMentionDes'"), R.id.tv_mention_des, "field " +
                "'tvMentionDes'");
        target.tvWarmUp = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_warm_up, "field 'tvWarmUp'"), R.id.tv_warm_up, "field 'tvWarmUp'");
        target.recyclerViewWarmUp = (RecyclerView) finder.castView((View) finder.findRequiredView
                (source, R.id.recycler_view_warm_up, "field 'recyclerViewWarmUp'"), R.id
                .recycler_view_warm_up, "field 'recyclerViewWarmUp'");
        target.tvTrain = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_train, "field 'tvTrain'"), R.id.tv_train, "field 'tvTrain'");
        target.recyclerViewTrain = (RecyclerView) finder.castView((View) finder.findRequiredView
                (source, R.id.recycler_view_train, "field 'recyclerViewTrain'"), R.id
                .recycler_view_train, "field 'recyclerViewTrain'");
        target.tvStart = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_start, "field 'tvStart'"), R.id.tv_start, "field 'tvStart'");
        target.progressBarDownload = (ProgressBar) finder.castView((View) finder.findRequiredView
                (source, R.id.progress_bar_download, "field 'progressBarDownload'"), R.id
                .progress_bar_download, "field 'progressBarDownload'");
        target.tvDownloadStatus = (TextView) finder.castView((View) finder.findRequiredView
                (source, R.id.tv_download_status, "field 'tvDownloadStatus'"), R.id
                .tv_download_status, "field 'tvDownloadStatus'");
        view = (View) finder.findRequiredView(source, R.id.bottom_layout, "field 'bottomLayout' " +
                "and method 'onClick'");
        target.bottomLayout = (RelativeLayout) finder.castView(view, R.id.bottom_layout, "field " +
                "'bottomLayout'");
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
    }

    public void reset(T target) {
        target.layout = null;
        target.ivTop = null;
        target.tvLessonTime = null;
        target.llLessonTime = null;
        target.tvTitle = null;
        target.ivQuestion = null;
        target.tvLessonCalory = null;
        target.tvLessonNumber = null;
        target.llLessonNumber = null;
        target.tvMentionDes = null;
        target.tvWarmUp = null;
        target.recyclerViewWarmUp = null;
        target.tvTrain = null;
        target.recyclerViewTrain = null;
        target.tvStart = null;
        target.progressBarDownload = null;
        target.tvDownloadStatus = null;
        target.bottomLayout = null;
    }
}
