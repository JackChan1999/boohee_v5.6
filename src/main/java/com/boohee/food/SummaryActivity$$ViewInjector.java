package com.boohee.food;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

import com.boohee.one.R;

public class SummaryActivity$$ViewInjector<T extends SummaryActivity> implements Injector<T> {
    public void inject(Finder finder, T target, Object source) {
        target.sv_main = (ScrollView) finder.castView((View) finder.findRequiredView(source, R.id
                .sv_main, "field 'sv_main'"), R.id.sv_main, "field 'sv_main'");
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
        target.tv_diet_calory = (TextView) finder.castView((View) finder.findRequiredView(source,
                R.id.tv_diet_calory, "field 'tv_diet_calory'"), R.id.tv_diet_calory, "field " +
                "'tv_diet_calory'");
        target.tv_sport_calory = (TextView) finder.castView((View) finder.findRequiredView
                (source, R.id.tv_sport_calory, "field 'tv_sport_calory'"), R.id.tv_sport_calory,
                "field 'tv_sport_calory'");
        target.tv_date = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_date, "field 'tv_date'"), R.id.tv_date, "field 'tv_date'");
    }

    public void reset(T target) {
        target.sv_main = null;
        target.ll_card_breakfast = null;
        target.ll_card_snacks_breakfast = null;
        target.ll_card_lunch = null;
        target.ll_card_snacks_lunch = null;
        target.ll_card_dinner = null;
        target.ll_card_snacks_dinner = null;
        target.ll_card_sport = null;
        target.tv_diet_calory = null;
        target.tv_sport_calory = null;
        target.tv_date = null;
    }
}
