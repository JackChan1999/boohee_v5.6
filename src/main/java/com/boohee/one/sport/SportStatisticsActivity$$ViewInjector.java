package com.boohee.one.sport;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

import com.boohee.one.R;

public class SportStatisticsActivity$$ViewInjector<T extends SportStatisticsActivity> implements
        Injector<T> {
    public void inject(Finder finder, T target, Object source) {
        target.ivBg = (ImageView) finder.castView((View) finder.findRequiredView(source, R.id
                .iv_bg, "field 'ivBg'"), R.id.iv_bg, "field 'ivBg'");
        target.ivFinish = (ImageView) finder.castView((View) finder.findRequiredView(source, R.id
                .iv_finish, "field 'ivFinish'"), R.id.iv_finish, "field 'ivFinish'");
        target.tvPeriod = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_period, "field 'tvPeriod'"), R.id.tv_period, "field 'tvPeriod'");
        target.tvCourseProgress = (TextView) finder.castView((View) finder.findRequiredView
                (source, R.id.tv_course_progress, "field 'tvCourseProgress'"), R.id
                .tv_course_progress, "field 'tvCourseProgress'");
        target.tvDuration = (TextView) finder.castView((View) finder.findRequiredView(source, R
                .id.tv_duration, "field 'tvDuration'"), R.id.tv_duration, "field 'tvDuration'");
        target.tvKcal = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_kcal, "field 'tvKcal'"), R.id.tv_kcal, "field 'tvKcal'");
    }

    public void reset(T target) {
        target.ivBg = null;
        target.ivFinish = null;
        target.tvPeriod = null;
        target.tvCourseProgress = null;
        target.tvDuration = null;
        target.tvKcal = null;
    }
}
