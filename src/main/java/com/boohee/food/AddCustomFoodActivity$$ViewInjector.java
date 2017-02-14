package com.boohee.food;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;
import butterknife.internal.DebouncingOnClickListener;

import com.boohee.one.R;
import com.boohee.widgets.RoundedCornersImage;

public class AddCustomFoodActivity$$ViewInjector<T extends AddCustomFoodActivity> implements
        Injector<T> {
    public void inject(Finder finder, final T target, Object source) {
        target.et_food_name = (EditText) finder.castView((View) finder.findRequiredView(source, R
                .id.et_food_name, "field 'et_food_name'"), R.id.et_food_name, "field " +
                "'et_food_name'");
        target.et_food_num = (EditText) finder.castView((View) finder.findRequiredView(source, R
                .id.et_food_num, "field 'et_food_num'"), R.id.et_food_num, "field 'et_food_num'");
        target.et_food_unit = (EditText) finder.castView((View) finder.findRequiredView(source, R
                .id.et_food_unit, "field 'et_food_unit'"), R.id.et_food_unit, "field " +
                "'et_food_unit'");
        target.et_food_calory = (EditText) finder.castView((View) finder.findRequiredView(source,
                R.id.et_food_calory, "field 'et_food_calory'"), R.id.et_food_calory, "field " +
                "'et_food_calory'");
        target.ivFoodImg = (RoundedCornersImage) finder.castView((View) finder.findRequiredView
                (source, R.id.iv_food_img, "field 'ivFoodImg'"), R.id.iv_food_img, "field " +
                "'ivFoodImg'");
        View view = (View) finder.findRequiredView(source, R.id.iv_delete, "field 'ivDelete' and " +
                "method 'onClick'");
        target.ivDelete = (ImageView) finder.castView(view, R.id.iv_delete, "field 'ivDelete'");
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        view = (View) finder.findRequiredView(source, R.id.rl_food_photo, "field 'rlFoodPhoto' " +
                "and method 'onClick'");
        target.rlFoodPhoto = (RelativeLayout) finder.castView(view, R.id.rl_food_photo, "field " +
                "'rlFoodPhoto'");
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        target.rlFoodImg = (RelativeLayout) finder.castView((View) finder.findRequiredView
                (source, R.id.rl_food, "field 'rlFoodImg'"), R.id.rl_food, "field 'rlFoodImg'");
        target.et_protein_num = (EditText) finder.castView((View) finder.findRequiredView(source,
                R.id.et_protein_num, "field 'et_protein_num'"), R.id.et_protein_num, "field " +
                "'et_protein_num'");
        target.et_fat_num = (EditText) finder.castView((View) finder.findRequiredView(source, R
                .id.et_fat_num, "field 'et_fat_num'"), R.id.et_fat_num, "field 'et_fat_num'");
        target.et_carbohydrate_num = (EditText) finder.castView((View) finder.findRequiredView
                (source, R.id.et_carbohydrate_num, "field 'et_carbohydrate_num'"), R.id
                .et_carbohydrate_num, "field 'et_carbohydrate_num'");
    }

    public void reset(T target) {
        target.et_food_name = null;
        target.et_food_num = null;
        target.et_food_unit = null;
        target.et_food_calory = null;
        target.ivFoodImg = null;
        target.ivDelete = null;
        target.rlFoodPhoto = null;
        target.rlFoodImg = null;
        target.et_protein_num = null;
        target.et_fat_num = null;
        target.et_carbohydrate_num = null;
    }
}
