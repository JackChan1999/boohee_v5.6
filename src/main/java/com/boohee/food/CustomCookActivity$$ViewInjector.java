package com.boohee.food;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

import com.boohee.one.R;
import com.boohee.widgets.RoundedCornersImage;

public class CustomCookActivity$$ViewInjector<T extends CustomCookActivity> implements Injector<T> {
    public void inject(Finder finder, T target, Object source) {
        target.tvCookName = (TextView) finder.castView((View) finder.findRequiredView(source, R
                .id.et_cook_name, "field 'tvCookName'"), R.id.et_cook_name, "field 'tvCookName'");
        target.ivCookImg = (RoundedCornersImage) finder.castView((View) finder.findRequiredView
                (source, R.id.iv_cook_img, "field 'ivCookImg'"), R.id.iv_cook_img, "field " +
                "'ivCookImg'");
        target.rlCookPhoto = (RelativeLayout) finder.castView((View) finder.findRequiredView
                (source, R.id.rl_cook_photo, "field 'rlCookPhoto'"), R.id.rl_cook_photo, "field " +
                "'rlCookPhoto'");
        target.llFoodMaterial = (LinearLayout) finder.castView((View) finder.findRequiredView
                (source, R.id.ll_food_material, "field 'llFoodMaterial'"), R.id.ll_food_material,
                "field 'llFoodMaterial'");
        target.tvCalory = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_calory, "field 'tvCalory'"), R.id.tv_calory, "field 'tvCalory'");
    }

    public void reset(T target) {
        target.tvCookName = null;
        target.ivCookImg = null;
        target.rlCookPhoto = null;
        target.llFoodMaterial = null;
        target.tvCalory = null;
    }
}
