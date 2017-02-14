package com.boohee.one.video.ui;

import android.view.View;
import android.widget.TextView;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;
import butterknife.internal.DebouncingOnClickListener;

import com.boohee.myview.CommonLineView;
import com.boohee.one.R;
import com.boohee.widgets.AnimCheckBox;

public class SportSettingActivity$$ViewInjector<T extends SportSettingActivity> implements
        Injector<T> {
    public void inject(Finder finder, final T target, Object source) {
        target.tvTime = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .remind_time, "field 'tvTime'"), R.id.remind_time, "field 'tvTime'");
        target.checkBox = (AnimCheckBox) finder.castView((View) finder.findRequiredView(source, R
                .id.check_box, "field 'checkBox'"), R.id.check_box, "field 'checkBox'");
        View view = (View) finder.findRequiredView(source, R.id.clv_clean_cache, "field " +
                "'clvCleanCache' and method 'onClick'");
        target.clvCleanCache = (CommonLineView) finder.castView(view, R.id.clv_clean_cache,
                "field 'clvCleanCache'");
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        ((View) finder.findRequiredView(source, R.id.clv_sport_test, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        ((View) finder.findRequiredView(source, R.id.rl_remind, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        ((View) finder.findRequiredView(source, R.id.clv_sport_history, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
    }

    public void reset(T target) {
        target.tvTime = null;
        target.checkBox = null;
        target.clvCleanCache = null;
    }
}
