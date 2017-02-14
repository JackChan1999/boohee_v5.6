package com.boohee.one.sport;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;
import butterknife.internal.DebouncingOnClickListener;

import com.boohee.one.R;
import com.boohee.one.sport.view.SportProgress;

public class SportCourseActivity$$ViewInjector<T extends SportCourseActivity> implements
        Injector<T> {
    public void inject(Finder finder, final T target, Object source) {
        target.iv_header_bg = (ImageView) finder.castView((View) finder.findRequiredView(source,
                R.id.iv_header_bg, "field 'iv_header_bg'"), R.id.iv_header_bg, "field " +
                "'iv_header_bg'");
        target.tv_course_finished = (TextView) finder.castView((View) finder.findRequiredView
                (source, R.id.tv_course_finished, "field 'tv_course_finished'"), R.id
                .tv_course_finished, "field 'tv_course_finished'");
        target.tv_course_count = (TextView) finder.castView((View) finder.findRequiredView
                (source, R.id.tv_course_count, "field 'tv_course_count'"), R.id.tv_course_count,
                "field 'tv_course_count'");
        View view = (View) finder.findRequiredView(source, R.id.view_course_finish, "field " +
                "'view_course_finish' and method 'onClick'");
        target.view_course_finish = view;
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        target.viewCourseProgress = (SportProgress) finder.castView((View) finder
                .findRequiredView(source, R.id.view_course_progress, "field " +
                        "'viewCourseProgress'"), R.id.view_course_progress, "field " +
                "'viewCourseProgress'");
        target.tablayout = (TabLayout) finder.castView((View) finder.findRequiredView(source, R
                .id.tablayout, "field 'tablayout'"), R.id.tablayout, "field 'tablayout'");
        target.viewpager = (ViewPager) finder.castView((View) finder.findRequiredView(source, R
                .id.viewpager, "field 'viewpager'"), R.id.viewpager, "field 'viewpager'");
        ((View) finder.findRequiredView(source, R.id.view_course_record, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
    }

    public void reset(T target) {
        target.iv_header_bg = null;
        target.tv_course_finished = null;
        target.tv_course_count = null;
        target.view_course_finish = null;
        target.viewCourseProgress = null;
        target.tablayout = null;
        target.viewpager = null;
    }
}
