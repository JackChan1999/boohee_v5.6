package com.boohee.account;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;
import butterknife.internal.DebouncingOnClickListener;

import com.boohee.myview.BooheeRulerView;
import com.boohee.one.R;

public class ProfileInitThreeFragment$$ViewInjector<T extends ProfileInitThreeFragment>
        implements Injector<T> {
    public void inject(Finder finder, final T target, Object source) {
        target.tvWeight = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_weight, "field 'tvWeight'"), R.id.tv_weight, "field 'tvWeight'");
        target.rvWeight = (BooheeRulerView) finder.castView((View) finder.findRequiredView
                (source, R.id.rv_weight, "field 'rvWeight'"), R.id.rv_weight, "field 'rvWeight'");
        target.rbTargetLoseWeight = (RadioButton) finder.castView((View) finder.findRequiredView
                (source, R.id.rb_target_lose_weight, "field 'rbTargetLoseWeight'"), R.id
                .rb_target_lose_weight, "field 'rbTargetLoseWeight'");
        target.rbTargetKeep = (RadioButton) finder.castView((View) finder.findRequiredView
                (source, R.id.rb_target_keep, "field 'rbTargetKeep'"), R.id.rb_target_keep,
                "field 'rbTargetKeep'");
        target.rgTarget = (RadioGroup) finder.castView((View) finder.findRequiredView(source, R
                .id.rg_target, "field 'rgTarget'"), R.id.rg_target, "field 'rgTarget'");
        View view = (View) finder.findRequiredView(source, R.id.btn_next, "field 'btnNext' and " +
                "method 'onClick'");
        target.btnNext = (TextView) finder.castView(view, R.id.btn_next, "field 'btnNext'");
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        target.tvSuggest = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_suggest, "field 'tvSuggest'"), R.id.tv_suggest, "field 'tvSuggest'");
        target.pickerLayout = (LinearLayout) finder.castView((View) finder.findRequiredView
                (source, R.id.picker_layout, "field 'pickerLayout'"), R.id.picker_layout, "field " +
                "'pickerLayout'");
        target.tvBeginDate = (TextView) finder.castView((View) finder.findRequiredView(source, R
                .id.tv_begin_date, "field 'tvBeginDate'"), R.id.tv_begin_date, "field " +
                "'tvBeginDate'");
        target.rlDate = (RelativeLayout) finder.castView((View) finder.findRequiredView(source, R
                .id.rl_date, "field 'rlDate'"), R.id.rl_date, "field 'rlDate'");
        ((View) finder.findRequiredView(source, R.id.ll_content, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        ((View) finder.findRequiredView(source, R.id.btn_pick_date, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
    }

    public void reset(T target) {
        target.tvWeight = null;
        target.rvWeight = null;
        target.rbTargetLoseWeight = null;
        target.rbTargetKeep = null;
        target.rgTarget = null;
        target.btnNext = null;
        target.tvSuggest = null;
        target.pickerLayout = null;
        target.tvBeginDate = null;
        target.rlDate = null;
    }
}
