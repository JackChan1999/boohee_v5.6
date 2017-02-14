package com.boohee.one.ui.fragment;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;
import butterknife.internal.DebouncingOnClickListener;

import com.boohee.one.R;
import com.booheee.view.keyboard.SportKeyboard;

public class AddSportFragment$$ViewInjector<T extends AddSportFragment> implements Injector<T> {
    public void inject(Finder finder, final T target, Object source) {
        target.iv_sport = (ImageView) finder.castView((View) finder.findRequiredView(source, R.id
                .iv_sport, "field 'iv_sport'"), R.id.iv_sport, "field 'iv_sport'");
        target.txt_name = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .txt_name, "field 'txt_name'"), R.id.txt_name, "field 'txt_name'");
        target.txt_title = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .txt_title, "field 'txt_title'"), R.id.txt_title, "field 'txt_title'");
        target.txt_calory = (TextView) finder.castView((View) finder.findRequiredView(source, R
                .id.txt_calory, "field 'txt_calory'"), R.id.txt_calory, "field 'txt_calory'");
        View view = (View) finder.findRequiredView(source, R.id.txt_delete, "field 'txt_delete' " +
                "and method 'onClick'");
        target.txt_delete = (TextView) finder.castView(view, R.id.txt_delete, "field 'txt_delete'");
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        target.sport_keyboard = (SportKeyboard) finder.castView((View) finder.findRequiredView
                (source, R.id.sport_keyboard, "field 'sport_keyboard'"), R.id.sport_keyboard,
                "field 'sport_keyboard'");
        ((View) finder.findRequiredView(source, R.id.txt_cancel, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        ((View) finder.findRequiredView(source, R.id.txt_commit, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
    }

    public void reset(T target) {
        target.iv_sport = null;
        target.txt_name = null;
        target.txt_title = null;
        target.txt_calory = null;
        target.txt_delete = null;
        target.sport_keyboard = null;
    }
}
