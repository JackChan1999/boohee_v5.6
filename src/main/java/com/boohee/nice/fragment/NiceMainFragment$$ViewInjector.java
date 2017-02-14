package com.boohee.nice.fragment;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;
import butterknife.internal.DebouncingOnClickListener;

import com.boohee.one.R;

public class NiceMainFragment$$ViewInjector<T extends NiceMainFragment> implements Injector<T> {
    public void inject(Finder finder, final T target, Object source) {
        target.tvNiceTitle = (TextView) finder.castView((View) finder.findRequiredView(source, R
                .id.tv_nice_title, "field 'tvNiceTitle'"), R.id.tv_nice_title, "field " +
                "'tvNiceTitle'");
        target.tvNiceDay = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_nice_day, "field 'tvNiceDay'"), R.id.tv_nice_day, "field 'tvNiceDay'");
        target.tvNiceBegin = (TextView) finder.castView((View) finder.findRequiredView(source, R
                .id.tv_nice_begin, "field 'tvNiceBegin'"), R.id.tv_nice_begin, "field " +
                "'tvNiceBegin'");
        target.tvNiceEnd = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_nice_end, "field 'tvNiceEnd'"), R.id.tv_nice_end, "field 'tvNiceEnd'");
        target.pbNiceProgress = (ProgressBar) finder.castView((View) finder.findRequiredView
                (source, R.id.pb_nice_progress, "field 'pbNiceProgress'"), R.id.pb_nice_progress,
                "field 'pbNiceProgress'");
        target.tvAdviserDesc = (TextView) finder.castView((View) finder.findRequiredView(source,
                R.id.tv_adviser_desc, "field 'tvAdviserDesc'"), R.id.tv_adviser_desc, "field " +
                "'tvAdviserDesc'");
        View view = (View) finder.findRequiredView(source, R.id.rl_nice_top, "field 'rlNiceTop' " +
                "and method 'onClick'");
        target.rlNiceTop = (RelativeLayout) finder.castView(view, R.id.rl_nice_top, "field " +
                "'rlNiceTop'");
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        target.tvReportTitle = (TextView) finder.castView((View) finder.findRequiredView(source,
                R.id.tv_nice_report_title, "field 'tvReportTitle'"), R.id.tv_nice_report_title,
                "field 'tvReportTitle'");
        target.tvRenew = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_nice_renew, "field 'tvRenew'"), R.id.tv_nice_renew, "field 'tvRenew'");
        ((View) finder.findRequiredView(source, R.id.rl_nice_report, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        ((View) finder.findRequiredView(source, R.id.rl_nice_plan, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        ((View) finder.findRequiredView(source, R.id.ll_nice_adviser, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        ((View) finder.findRequiredView(source, R.id.ll_nice_diet, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        ((View) finder.findRequiredView(source, R.id.ll_nice_sport, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        ((View) finder.findRequiredView(source, R.id.ll_nice_knowledge, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
    }

    public void reset(T target) {
        target.tvNiceTitle = null;
        target.tvNiceDay = null;
        target.tvNiceBegin = null;
        target.tvNiceEnd = null;
        target.pbNiceProgress = null;
        target.tvAdviserDesc = null;
        target.rlNiceTop = null;
        target.tvReportTitle = null;
        target.tvRenew = null;
    }
}
