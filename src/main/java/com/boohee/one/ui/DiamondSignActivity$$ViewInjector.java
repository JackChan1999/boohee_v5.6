package com.boohee.one.ui;

import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;
import butterknife.internal.DebouncingOnClickListener;

import com.boohee.one.R;
import com.boohee.widgets.AnimCheckBox;

public class DiamondSignActivity$$ViewInjector<T extends DiamondSignActivity> implements
        Injector<T> {
    public void inject(Finder finder, final T target, Object source) {
        target.tvNumber = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_diamond_number, "field 'tvNumber'"), R.id.tv_diamond_number, "field " +
                "'tvNumber'");
        View view = (View) finder.findRequiredView(source, R.id.btn_go_exchange, "field " +
                "'exchangeBtn' and method 'onClick'");
        target.exchangeBtn = (Button) finder.castView(view, R.id.btn_go_exchange, "field " +
                "'exchangeBtn'");
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        target.tvDate = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .txt_date, "field 'tvDate'"), R.id.txt_date, "field 'tvDate'");
        view = (View) finder.findRequiredView(source, R.id.rl_left, "field 'leftDate' and method " +
                "'onClick'");
        target.leftDate = view;
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        view = (View) finder.findRequiredView(source, R.id.rl_right, "field 'rightDate' and " +
                "method 'onClick'");
        target.rightDate = view;
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        target.flipper = (ViewFlipper) finder.castView((View) finder.findRequiredView(source, R
                .id.flipper, "field 'flipper'"), R.id.flipper, "field 'flipper'");
        target.calendarGrid = (GridView) finder.castView((View) finder.findRequiredView(source, R
                .id.calendar, "field 'calendarGrid'"), R.id.calendar, "field 'calendarGrid'");
        target.tvContinueDay = (TextView) finder.castView((View) finder.findRequiredView(source,
                R.id.tv_continue_day, "field 'tvContinueDay'"), R.id.tv_continue_day, "field " +
                "'tvContinueDay'");
        target.tvAllDay = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_all_day, "field 'tvAllDay'"), R.id.tv_all_day, "field 'tvAllDay'");
        target.checkBox = (AnimCheckBox) finder.castView((View) finder.findRequiredView(source, R
                .id.check_box, "field 'checkBox'"), R.id.check_box, "field 'checkBox'");
        view = (View) finder.findRequiredView(source, R.id.btn_repair, "field 'repairBtn' and " +
                "method 'onClick'");
        target.repairBtn = (Button) finder.castView(view, R.id.btn_repair, "field 'repairBtn'");
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        ((View) finder.findRequiredView(source, R.id.ll_check_in, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
    }

    public void reset(T target) {
        target.tvNumber = null;
        target.exchangeBtn = null;
        target.tvDate = null;
        target.leftDate = null;
        target.rightDate = null;
        target.flipper = null;
        target.calendarGrid = null;
        target.tvContinueDay = null;
        target.tvAllDay = null;
        target.checkBox = null;
        target.repairBtn = null;
    }
}
