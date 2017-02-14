package com.boohee.one.ui;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;
import butterknife.internal.DebouncingOnClickListener;

import com.boohee.circleview.CircleIndicator;
import com.boohee.circleview.CircleProgress;
import com.boohee.circleview.LineIndicator;
import com.boohee.one.R;

public class WeightStatusActivity$$ViewInjector<T extends WeightStatusActivity> implements
        Injector<T> {
    public void inject(Finder finder, final T target, Object source) {
        target.liWeightProgress = (LineIndicator) finder.castView((View) finder.findRequiredView
                (source, R.id.li_weight_progress, "field 'liWeightProgress'"), R.id
                .li_weight_progress, "field 'liWeightProgress'");
        target.ciBmi = (CircleIndicator) finder.castView((View) finder.findRequiredView(source, R
                .id.ci_bmi, "field 'ciBmi'"), R.id.ci_bmi, "field 'ciBmi'");
        target.ciBodyFatRate = (CircleIndicator) finder.castView((View) finder.findRequiredView
                (source, R.id.ci_body_fat_rate, "field 'ciBodyFatRate'"), R.id.ci_body_fat_rate,
                "field 'ciBodyFatRate'");
        target.cpBodyAge = (CircleProgress) finder.castView((View) finder.findRequiredView
                (source, R.id.cp_body_age, "field 'cpBodyAge'"), R.id.cp_body_age, "field " +
                "'cpBodyAge'");
        target.viewHeader = (LinearLayout) finder.castView((View) finder.findRequiredView(source,
                R.id.view_header, "field 'viewHeader'"), R.id.view_header, "field 'viewHeader'");
        target.tvLoseWeight = (TextView) finder.castView((View) finder.findRequiredView(source, R
                .id.tv_lose_weight, "field 'tvLoseWeight'"), R.id.tv_lose_weight, "field " +
                "'tvLoseWeight'");
        target.viewContentReset = (LinearLayout) finder.castView((View) finder.findRequiredView
                (source, R.id.view_content_reset, "field 'viewContentReset'"), R.id
                .view_content_reset, "field 'viewContentReset'");
        target.viewContent = (LinearLayout) finder.castView((View) finder.findRequiredView
                (source, R.id.view_content, "field 'viewContent'"), R.id.view_content, "field " +
                "'viewContent'");
        ((View) finder.findRequiredView(source, R.id.bt_reset_1, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        ((View) finder.findRequiredView(source, R.id.bt_reset_2, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
    }

    public void reset(T target) {
        target.liWeightProgress = null;
        target.ciBmi = null;
        target.ciBodyFatRate = null;
        target.cpBodyAge = null;
        target.viewHeader = null;
        target.tvLoseWeight = null;
        target.viewContentReset = null;
        target.viewContent = null;
    }
}
