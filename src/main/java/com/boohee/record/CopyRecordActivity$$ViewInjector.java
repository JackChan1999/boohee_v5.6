package com.boohee.record;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;
import butterknife.internal.DebouncingOnClickListener;

import com.boohee.one.R;

public class CopyRecordActivity$$ViewInjector<T extends CopyRecordActivity> implements Injector<T> {
    public void inject(Finder finder, final T target, Object source) {
        target.ll_card_breakfast = (LinearLayout) finder.castView((View) finder.findRequiredView
                (source, R.id.ll_card_breakfast, "field 'll_card_breakfast'"), R.id
                .ll_card_breakfast, "field 'll_card_breakfast'");
        target.ll_card_snacks_breakfast = (LinearLayout) finder.castView((View) finder
                .findRequiredView(source, R.id.ll_card_snacks_breakfast, "field " +
                        "'ll_card_snacks_breakfast'"), R.id.ll_card_snacks_breakfast, "field " +
                "'ll_card_snacks_breakfast'");
        target.ll_card_lunch = (LinearLayout) finder.castView((View) finder.findRequiredView
                (source, R.id.ll_card_lunch, "field 'll_card_lunch'"), R.id.ll_card_lunch, "field" +
                " 'll_card_lunch'");
        target.ll_card_snacks_lunch = (LinearLayout) finder.castView((View) finder
                .findRequiredView(source, R.id.ll_card_snacks_lunch, "field " +
                        "'ll_card_snacks_lunch'"), R.id.ll_card_snacks_lunch, "field " +
                "'ll_card_snacks_lunch'");
        target.ll_card_dinner = (LinearLayout) finder.castView((View) finder.findRequiredView
                (source, R.id.ll_card_dinner, "field 'll_card_dinner'"), R.id.ll_card_dinner,
                "field 'll_card_dinner'");
        target.ll_card_snacks_dinner = (LinearLayout) finder.castView((View) finder
                .findRequiredView(source, R.id.ll_card_snacks_dinner, "field " +
                        "'ll_card_snacks_dinner'"), R.id.ll_card_snacks_dinner, "field " +
                "'ll_card_snacks_dinner'");
        target.ll_card_sport = (LinearLayout) finder.castView((View) finder.findRequiredView
                (source, R.id.ll_card_sport, "field 'll_card_sport'"), R.id.ll_card_sport, "field" +
                " 'll_card_sport'");
        target.tb_check_all = (ToggleButton) finder.castView((View) finder.findRequiredView
                (source, R.id.tb_check_all, "field 'tb_check_all'"), R.id.tb_check_all, "field " +
                "'tb_check_all'");
        ((View) finder.findRequiredView(source, R.id.ll_check_all, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        ((View) finder.findRequiredView(source, R.id.rl_copy, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
    }

    public void reset(T target) {
        target.ll_card_breakfast = null;
        target.ll_card_snacks_breakfast = null;
        target.ll_card_lunch = null;
        target.ll_card_snacks_lunch = null;
        target.ll_card_dinner = null;
        target.ll_card_snacks_dinner = null;
        target.ll_card_sport = null;
        target.tb_check_all = null;
    }
}
