package com.boohee.one.ui.fragment;

import android.view.View;
import android.widget.TextView;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;
import butterknife.internal.DebouncingOnClickListener;

import com.boohee.one.R;
import com.booheee.view.keyboard.CustomSportKeyboard;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddCustomSportFragment$$ViewInjector<T extends AddCustomSportFragment> implements
        Injector<T> {
    public void inject(Finder finder, final T target, Object source) {
        target.civ_title = (CircleImageView) finder.castView((View) finder.findRequiredView
                (source, R.id.civ_title, "field 'civ_title'"), R.id.civ_title, "field 'civ_title'");
        target.txt_name = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .txt_name, "field 'txt_name'"), R.id.txt_name, "field 'txt_name'");
        target.txt_title = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .txt_title, "field 'txt_title'"), R.id.txt_title, "field 'txt_title'");
        target.txt_calory = (TextView) finder.castView((View) finder.findRequiredView(source, R
                .id.txt_calory, "field 'txt_calory'"), R.id.txt_calory, "field 'txt_calory'");
        target.txt_unit = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .txt_unit, "field 'txt_unit'"), R.id.txt_unit, "field 'txt_unit'");
        View view = (View) finder.findRequiredView(source, R.id.txt_delete, "field 'txt_delete' " +
                "and method 'onClick'");
        target.txt_delete = (TextView) finder.castView(view, R.id.txt_delete, "field 'txt_delete'");
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        target.custom_sport_keyboard = (CustomSportKeyboard) finder.castView((View) finder
                .findRequiredView(source, R.id.custom_sport_keyboard, "field " +
                        "'custom_sport_keyboard'"), R.id.custom_sport_keyboard, "field " +
                "'custom_sport_keyboard'");
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
        target.civ_title = null;
        target.txt_name = null;
        target.txt_title = null;
        target.txt_calory = null;
        target.txt_unit = null;
        target.txt_delete = null;
        target.custom_sport_keyboard = null;
    }
}
