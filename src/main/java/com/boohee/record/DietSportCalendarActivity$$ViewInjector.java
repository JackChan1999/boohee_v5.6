package com.boohee.record;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;
import butterknife.internal.DebouncingOnClickListener;

import com.boohee.myview.DietPopView;
import com.boohee.one.R;

public class DietSportCalendarActivity$$ViewInjector<T extends DietSportCalendarActivity>
        implements Injector<T> {
    public void inject(Finder finder, final T target, Object source) {
        target.ll_top_layout = (LinearLayout) finder.castView((View) finder.findRequiredView
                (source, R.id.ll_top_layout, "field 'll_top_layout'"), R.id.ll_top_layout, "field" +
                " 'll_top_layout'");
        target.ll_record = (LinearLayout) finder.castView((View) finder.findRequiredView(source,
                R.id.ll_record, "field 'll_record'"), R.id.ll_record, "field 'll_record'");
        target.ll_record_null = (LinearLayout) finder.castView((View) finder.findRequiredView
                (source, R.id.ll_record_null, "field 'll_record_null'"), R.id.ll_record_null,
                "field 'll_record_null'");
        View view = (View) finder.findRequiredView(source, R.id.ll_nav_snacks, "field " +
                "'ll_nav_snacks' and method 'onClick'");
        target.ll_nav_snacks = (LinearLayout) finder.castView(view, R.id.ll_nav_snacks, "field " +
                "'ll_nav_snacks'");
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
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
        target.ll_calory_distribute = (LinearLayout) finder.castView((View) finder
                .findRequiredView(source, R.id.ll_calory_distribute, "field " +
                        "'ll_calory_distribute'"), R.id.ll_calory_distribute, "field " +
                "'ll_calory_distribute'");
        target.ll_today_analysis = (LinearLayout) finder.castView((View) finder.findRequiredView
                (source, R.id.ll_today_analysis, "field 'll_today_analysis'"), R.id
                .ll_today_analysis, "field 'll_today_analysis'");
        target.dietPopView = (DietPopView) finder.castView((View) finder.findRequiredView(source,
                R.id.diet_pop_view, "field 'dietPopView'"), R.id.diet_pop_view, "field " +
                "'dietPopView'");
        view = (View) finder.findRequiredView(source, R.id.btn_today_analysis, "field " +
                "'btnTodayAnalysis' and method 'onClick'");
        target.btnTodayAnalysis = (Button) finder.castView(view, R.id.btn_today_analysis, "field " +
                "'btnTodayAnalysis'");
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        ((View) finder.findRequiredView(source, R.id.ll_nav_breakfast, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        ((View) finder.findRequiredView(source, R.id.ll_nav_lunch, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        ((View) finder.findRequiredView(source, R.id.ll_nav_dinner, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        ((View) finder.findRequiredView(source, R.id.ll_nav_sport, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
    }

    public void reset(T target) {
        target.ll_top_layout = null;
        target.ll_record = null;
        target.ll_record_null = null;
        target.ll_nav_snacks = null;
        target.ll_card_breakfast = null;
        target.ll_card_snacks_breakfast = null;
        target.ll_card_lunch = null;
        target.ll_card_snacks_lunch = null;
        target.ll_card_dinner = null;
        target.ll_card_snacks_dinner = null;
        target.ll_card_sport = null;
        target.ll_calory_distribute = null;
        target.ll_today_analysis = null;
        target.dietPopView = null;
        target.btnTodayAnalysis = null;
    }
}
