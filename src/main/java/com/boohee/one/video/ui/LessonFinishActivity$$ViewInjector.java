package com.boohee.one.video.ui;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;
import butterknife.internal.DebouncingOnClickListener;

import com.boohee.one.R;

public class LessonFinishActivity$$ViewInjector<T extends LessonFinishActivity> implements
        Injector<T> {
    public void inject(Finder finder, final T target, Object source) {
        target.ivFinish = (ImageView) finder.castView((View) finder.findRequiredView(source, R.id
                .iv_finish, "field 'ivFinish'"), R.id.iv_finish, "field 'ivFinish'");
        target.tvProgress = (TextView) finder.castView((View) finder.findRequiredView(source, R
                .id.tv_progress, "field 'tvProgress'"), R.id.tv_progress, "field 'tvProgress'");
        target.lessonProgress = (ProgressBar) finder.castView((View) finder.findRequiredView
                (source, R.id.lesson_progress, "field 'lessonProgress'"), R.id.lesson_progress,
                "field 'lessonProgress'");
        target.progressLayout = (LinearLayout) finder.castView((View) finder.findRequiredView
                (source, R.id.progress_layout, "field 'progressLayout'"), R.id.progress_layout,
                "field 'progressLayout'");
        target.tvTodayCost = (TextView) finder.castView((View) finder.findRequiredView(source, R
                .id.tv_today_cost, "field 'tvTodayCost'"), R.id.tv_today_cost, "field " +
                "'tvTodayCost'");
        target.tvTomorrow = (TextView) finder.castView((View) finder.findRequiredView(source, R
                .id.tv_tomorrow, "field 'tvTomorrow'"), R.id.tv_tomorrow, "field 'tvTomorrow'");
        View view = (View) finder.findRequiredView(source, R.id.btn_question, "field " +
                "'btnQuestion' and method 'onClick'");
        target.btnQuestion = (ImageView) finder.castView(view, R.id.btn_question, "field " +
                "'btnQuestion'");
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        target.tvDiamond = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_diamond, "field 'tvDiamond'"), R.id.tv_diamond, "field 'tvDiamond'");
        target.diamondLayout = (LinearLayout) finder.castView((View) finder.findRequiredView
                (source, R.id.diamond_layout, "field 'diamondLayout'"), R.id.diamond_layout,
                "field 'diamondLayout'");
        view = (View) finder.findRequiredView(source, R.id.btn_go_sport_plan, "field " +
                "'btnGoSportPlan' and method 'onClick'");
        target.btnGoSportPlan = (TextView) finder.castView(view, R.id.btn_go_sport_plan, "field " +
                "'btnGoSportPlan'");
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        view = (View) finder.findRequiredView(source, R.id.btn_post_status, "field " +
                "'btnPostStatus' and method 'onClick'");
        target.btnPostStatus = (TextView) finder.castView(view, R.id.btn_post_status, "field " +
                "'btnPostStatus'");
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        target.tvUnfinshCount = (TextView) finder.castView((View) finder.findRequiredView(source,
                R.id.tv_unfinsh_count, "field 'tvUnfinshCount'"), R.id.tv_unfinsh_count, "field " +
                "'tvUnfinshCount'");
    }

    public void reset(T target) {
        target.ivFinish = null;
        target.tvProgress = null;
        target.lessonProgress = null;
        target.progressLayout = null;
        target.tvTodayCost = null;
        target.tvTomorrow = null;
        target.btnQuestion = null;
        target.tvDiamond = null;
        target.diamondLayout = null;
        target.btnGoSportPlan = null;
        target.btnPostStatus = null;
        target.tvUnfinshCount = null;
    }
}
