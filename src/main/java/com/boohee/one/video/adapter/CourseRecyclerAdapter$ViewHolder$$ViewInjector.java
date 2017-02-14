package com.boohee.one.video.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

import com.boohee.one.R;
import com.boohee.one.video.adapter.CourseRecyclerAdapter.ViewHolder;

public class CourseRecyclerAdapter$ViewHolder$$ViewInjector<T extends ViewHolder> implements
        Injector<T> {
    public void inject(Finder finder, T target, Object source) {
        target.tvTime = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_time, "field 'tvTime'"), R.id.tv_time, "field 'tvTime'");
        target.tvCourseName = (TextView) finder.castView((View) finder.findRequiredView(source, R
                .id.tv_course_name, "field 'tvCourseName'"), R.id.tv_course_name, "field " +
                "'tvCourseName'");
        target.ivCourseStatus = (ImageView) finder.castView((View) finder.findRequiredView
                (source, R.id.iv_course_status, "field 'ivCourseStatus'"), R.id.iv_course_status,
                "field 'ivCourseStatus'");
        target.dailyCourseStatusLayout = (RelativeLayout) finder.castView((View) finder
                .findRequiredView(source, R.id.daily_course_status_layout, "field " +
                        "'dailyCourseStatusLayout'"), R.id.daily_course_status_layout, "field " +
                "'dailyCourseStatusLayout'");
        target.dailyCourseContainer = (RelativeLayout) finder.castView((View) finder
                .findRequiredView(source, R.id.daily_course_container, "field " +
                        "'dailyCourseContainer'"), R.id.daily_course_container, "field " +
                "'dailyCourseContainer'");
        target.bgLayout = (RelativeLayout) finder.castView((View) finder.findRequiredView(source,
                R.id.bg_layout, "field 'bgLayout'"), R.id.bg_layout, "field 'bgLayout'");
        target.tvBig = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_big, "field 'tvBig'"), R.id.tv_big, "field 'tvBig'");
    }

    public void reset(T target) {
        target.tvTime = null;
        target.tvCourseName = null;
        target.ivCourseStatus = null;
        target.dailyCourseStatusLayout = null;
        target.dailyCourseContainer = null;
        target.bgLayout = null;
        target.tvBig = null;
    }
}
