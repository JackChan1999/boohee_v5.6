package com.boohee.one.ui.fragment;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;
import butterknife.internal.DebouncingOnClickListener;

import com.boohee.one.R;
import com.booheee.view.keyboard.DietKeyboard;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddMaterialFragment$$ViewInjector<T extends AddMaterialFragment> implements
        Injector<T> {
    public void inject(Finder finder, final T target, Object source) {
        target.txt_title = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .txt_title, "field 'txt_title'"), R.id.txt_title, "field 'txt_title'");
        target.txt_name = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .txt_name, "field 'txt_name'"), R.id.txt_name, "field 'txt_name'");
        target.civ_title = (CircleImageView) finder.castView((View) finder.findRequiredView
                (source, R.id.civ_title, "field 'civ_title'"), R.id.civ_title, "field 'civ_title'");
        target.txt_calory = (TextView) finder.castView((View) finder.findRequiredView(source, R
                .id.txt_calory, "field 'txt_calory'"), R.id.txt_calory, "field 'txt_calory'");
        target.tv_light = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_light, "field 'tv_light'"), R.id.tv_light, "field 'tv_light'");
        target.diet_keyboard = (DietKeyboard) finder.castView((View) finder.findRequiredView
                (source, R.id.diet_keyboard, "field 'diet_keyboard'"), R.id.diet_keyboard, "field" +
                " 'diet_keyboard'");
        target.iv_light = (ImageView) finder.castView((View) finder.findRequiredView(source, R.id
                .iv_light, "field 'iv_light'"), R.id.iv_light, "field 'iv_light'");
        target.toggle_collect = (ToggleButton) finder.castView((View) finder.findRequiredView
                (source, R.id.toggle_collect, "field 'toggle_collect'"), R.id.toggle_collect,
                "field 'toggle_collect'");
        target.tv_collect = (TextView) finder.castView((View) finder.findRequiredView(source, R
                .id.tv_collect, "field 'tv_collect'"), R.id.tv_collect, "field 'tv_collect'");
        View view = (View) finder.findRequiredView(source, R.id.ll_collect, "field 'll_collect' " +
                "and method 'onClick'");
        target.ll_collect = (LinearLayout) finder.castView(view, R.id.ll_collect, "field " +
                "'ll_collect'");
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
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
        ((View) finder.findRequiredView(source, R.id.ll_light, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        ((View) finder.findRequiredView(source, R.id.ll_detail, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        ((View) finder.findRequiredView(source, R.id.ll_estimate, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
    }

    public void reset(T target) {
        target.txt_title = null;
        target.txt_name = null;
        target.civ_title = null;
        target.txt_calory = null;
        target.tv_light = null;
        target.diet_keyboard = null;
        target.iv_light = null;
        target.toggle_collect = null;
        target.tv_collect = null;
        target.ll_collect = null;
    }
}
