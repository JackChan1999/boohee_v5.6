package com.boohee.one.ui.fragment;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;
import butterknife.internal.DebouncingOnClickListener;

import com.boohee.myview.ArcProgressView;
import com.boohee.myview.DietRecordBar;
import com.boohee.myview.LineGraph;
import com.boohee.myview.ProgressLine;
import com.boohee.myview.PullToZoomScrollView;
import com.boohee.one.R;
import com.boohee.widgets.BooheeRippleLayout;

public class HomeNewFragment$$ViewInjector<T extends HomeNewFragment> implements Injector<T> {
    public void inject(Finder finder, final T target, Object source) {
        target.viewHeaderBG = (View) finder.findRequiredView(source, R.id.view_header_bg, "field " +
                "'viewHeaderBG'");
        target.ivTop = (ImageView) finder.castView((View) finder.findRequiredView(source, R.id
                .iv_top, "field 'ivTop'"), R.id.iv_top, "field 'ivTop'");
        target.layoutTop = (RelativeLayout) finder.castView((View) finder.findRequiredView
                (source, R.id.layout_top, "field 'layoutTop'"), R.id.layout_top, "field " +
                "'layoutTop'");
        target.layoutPullDown = (PullToZoomScrollView) finder.castView((View) finder
                .findRequiredView(source, R.id.layout_pull_down, "field 'layoutPullDown'"), R.id
                .layout_pull_down, "field 'layoutPullDown'");
        target.tvTips = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_tips, "field 'tvTips'"), R.id.tv_tips, "field 'tvTips'");
        target.rpCheckIn = (BooheeRippleLayout) finder.castView((View) finder.findRequiredView
                (source, R.id.rp_check_in, "field 'rpCheckIn'"), R.id.rp_check_in, "field " +
                "'rpCheckIn'");
        target.rlCheckIn = (RelativeLayout) finder.castView((View) finder.findRequiredView
                (source, R.id.rl_check_in, "field 'rlCheckIn'"), R.id.rl_check_in, "field " +
                "'rlCheckIn'");
        target.brlCheckinStatus = (BooheeRippleLayout) finder.castView((View) finder
                .findRequiredView(source, R.id.brl_checkin_status, "field 'brlCheckinStatus'"), R
                .id.brl_checkin_status, "field 'brlCheckinStatus'");
        View view = (View) finder.findRequiredView(source, R.id.tv_check_in, "field 'tvCheckIn' " +
                "and method 'onClick'");
        target.tvCheckIn = (TextView) finder.castView(view, R.id.tv_check_in, "field 'tvCheckIn'");
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        target.arcProgress = (ArcProgressView) finder.castView((View) finder.findRequiredView
                (source, R.id.arc_progress, "field 'arcProgress'"), R.id.arc_progress, "field " +
                "'arcProgress'");
        target.tvDes = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_des, "field 'tvDes'"), R.id.tv_des, "field 'tvDes'");
        target.tvWeight = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_weight, "field 'tvWeight'"), R.id.tv_weight, "field 'tvWeight'");
        target.tvProgress = (TextView) finder.castView((View) finder.findRequiredView(source, R
                .id.tv_progress, "field 'tvProgress'"), R.id.tv_progress, "field 'tvProgress'");
        target.rlProgress = (View) finder.findRequiredView(source, R.id.rl_progress, "field " +
                "'rlProgress'");
        target.tvCalorieStatus = (TextView) finder.castView((View) finder.findRequiredView
                (source, R.id.tv_calorie_status, "field 'tvCalorieStatus'"), R.id
                .tv_calorie_status, "field 'tvCalorieStatus'");
        target.tvCalorie = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_calorie, "field 'tvCalorie'"), R.id.tv_calorie, "field 'tvCalorie'");
        target.dietBar = (DietRecordBar) finder.castView((View) finder.findRequiredView(source, R
                .id.diet_bar, "field 'dietBar'"), R.id.diet_bar, "field 'dietBar'");
        target.tvLastWeight = (TextView) finder.castView((View) finder.findRequiredView(source, R
                .id.tv_latest_weight, "field 'tvLastWeight'"), R.id.tv_latest_weight, "field " +
                "'tvLastWeight'");
        target.weightGraph = (LineGraph) finder.castView((View) finder.findRequiredView(source, R
                .id.weight_graph, "field 'weightGraph'"), R.id.weight_graph, "field 'weightGraph'");
        target.tvStep = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_current_step, "field 'tvStep'"), R.id.tv_current_step, "field 'tvStep'");
        target.rlStep = (View) finder.findRequiredView(source, R.id.rl_step_parent, "field " +
                "'rlStep'");
        target.progressStep = (ProgressLine) finder.castView((View) finder.findRequiredView
                (source, R.id.progress_step, "field 'progressStep'"), R.id.progress_step, "field " +
                "'progressStep'");
        target.tvTargetStep = (TextView) finder.castView((View) finder.findRequiredView(source, R
                .id.tv_target_step, "field 'tvTargetStep'"), R.id.tv_target_step, "field " +
                "'tvTargetStep'");
        view = (View) finder.findRequiredView(source, R.id.rl_peroid, "field 'rlPeriod' and " +
                "method 'onClick'");
        target.rlPeriod = view;
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        target.tvPeroidDistance = (TextView) finder.castView((View) finder.findRequiredView
                (source, R.id.tv_peroid_distance, "field 'tvPeroidDistance'"), R.id
                .tv_peroid_distance, "field 'tvPeroidDistance'");
        target.dividerPeriod = (View) finder.findRequiredView(source, R.id.divider_period, "field" +
                " 'dividerPeriod'");
        target.tvCheckInStatusAlert = (TextView) finder.castView((View) finder.findRequiredView
                (source, R.id.tv_checkin_status_alert, "field 'tvCheckInStatusAlert'"), R.id
                .tv_checkin_status_alert, "field 'tvCheckInStatusAlert'");
        ((View) finder.findRequiredView(source, R.id.rl_checkin_status, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        ((View) finder.findRequiredView(source, R.id.rl_diet_sport, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        ((View) finder.findRequiredView(source, R.id.rl_weight, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        ((View) finder.findRequiredView(source, R.id.rl_measure, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        ((View) finder.findRequiredView(source, R.id.rl_step, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        ((View) finder.findRequiredView(source, R.id.tv_plan, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
    }

    public void reset(T target) {
        target.viewHeaderBG = null;
        target.ivTop = null;
        target.layoutTop = null;
        target.layoutPullDown = null;
        target.tvTips = null;
        target.rpCheckIn = null;
        target.rlCheckIn = null;
        target.brlCheckinStatus = null;
        target.tvCheckIn = null;
        target.arcProgress = null;
        target.tvDes = null;
        target.tvWeight = null;
        target.tvProgress = null;
        target.rlProgress = null;
        target.tvCalorieStatus = null;
        target.tvCalorie = null;
        target.dietBar = null;
        target.tvLastWeight = null;
        target.weightGraph = null;
        target.tvStep = null;
        target.rlStep = null;
        target.progressStep = null;
        target.tvTargetStep = null;
        target.rlPeriod = null;
        target.tvPeroidDistance = null;
        target.dividerPeriod = null;
        target.tvCheckInStatusAlert = null;
    }
}
