package com.boohee.account;

import android.view.View;
import android.widget.TextView;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;
import butterknife.internal.DebouncingOnClickListener;

import com.boohee.myview.BooheeRulerView;
import com.boohee.one.R;

public class ProfileInitFourFragment$$ViewInjector<T extends ProfileInitFourFragment> implements
        Injector<T> {
    public void inject(Finder finder, final T target, Object source) {
        target.tvSuggestWeight = (TextView) finder.castView((View) finder.findRequiredView
                (source, R.id.tv_suggest_weight, "field 'tvSuggestWeight'"), R.id
                .tv_suggest_weight, "field 'tvSuggestWeight'");
        target.tvTargetWeight = (TextView) finder.castView((View) finder.findRequiredView(source,
                R.id.tv_target_weight, "field 'tvTargetWeight'"), R.id.tv_target_weight, "field " +
                "'tvTargetWeight'");
        target.rvTargetWeight = (BooheeRulerView) finder.castView((View) finder.findRequiredView
                (source, R.id.rv_target_weight, "field 'rvTargetWeight'"), R.id.rv_target_weight,
                "field 'rvTargetWeight'");
        View view = (View) finder.findRequiredView(source, R.id.btn_next, "field 'btnNext' and " +
                "method 'onClick'");
        target.btnNext = (TextView) finder.castView(view, R.id.btn_next, "field 'btnNext'");
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        target.tvTooLow = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_too_low, "field 'tvTooLow'"), R.id.tv_too_low, "field 'tvTooLow'");
    }

    public void reset(T target) {
        target.tvSuggestWeight = null;
        target.tvTargetWeight = null;
        target.rvTargetWeight = null;
        target.btnNext = null;
        target.tvTooLow = null;
    }
}
