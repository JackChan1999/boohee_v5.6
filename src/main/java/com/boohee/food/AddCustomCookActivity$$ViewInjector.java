package com.boohee.food;

import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;
import butterknife.internal.DebouncingOnClickListener;

import com.boohee.one.R;
import com.boohee.widgets.RoundedCornersImage;

public class AddCustomCookActivity$$ViewInjector<T extends AddCustomCookActivity> implements
        Injector<T> {
    public void inject(Finder finder, final T target, Object source) {
        target.etCookName = (EditText) finder.castView((View) finder.findRequiredView(source, R
                .id.et_cook_name, "field 'etCookName'"), R.id.et_cook_name, "field 'etCookName'");
        target.ivCookImg = (RoundedCornersImage) finder.castView((View) finder.findRequiredView
                (source, R.id.iv_cook_img, "field 'ivCookImg'"), R.id.iv_cook_img, "field " +
                "'ivCookImg'");
        target.llFoodMaterial = (LinearLayout) finder.castView((View) finder.findRequiredView
                (source, R.id.ll_food_material, "field 'llFoodMaterial'"), R.id.ll_food_material,
                "field 'llFoodMaterial'");
        target.tvCalory = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_calory, "field 'tvCalory'"), R.id.tv_calory, "field 'tvCalory'");
        target.rlCookImg = (RelativeLayout) finder.castView((View) finder.findRequiredView
                (source, R.id.rl_cook_img, "field 'rlCookImg'"), R.id.rl_cook_img, "field " +
                "'rlCookImg'");
        ((View) finder.findRequiredView(source, R.id.ll_cook_name, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        ((View) finder.findRequiredView(source, R.id.rl_cook_photo, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        ((View) finder.findRequiredView(source, R.id.ll_add_material, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        ((View) finder.findRequiredView(source, R.id.iv_cook_delete, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
    }

    public void reset(T target) {
        target.etCookName = null;
        target.ivCookImg = null;
        target.llFoodMaterial = null;
        target.tvCalory = null;
        target.rlCookImg = null;
    }
}
