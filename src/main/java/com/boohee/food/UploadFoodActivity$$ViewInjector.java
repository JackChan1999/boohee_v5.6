package com.boohee.food;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;
import butterknife.internal.DebouncingOnClickListener;

import com.boohee.one.R;

public class UploadFoodActivity$$ViewInjector<T extends UploadFoodActivity> implements Injector<T> {
    public void inject(Finder finder, final T target, Object source) {
        target.etBrand = (EditText) finder.castView((View) finder.findRequiredView(source, R.id
                .et_food_brand, "field 'etBrand'"), R.id.et_food_brand, "field 'etBrand'");
        target.etFoodName = (EditText) finder.castView((View) finder.findRequiredView(source, R
                .id.et_food_name, "field 'etFoodName'"), R.id.et_food_name, "field 'etFoodName'");
        target.rlFoodFront = (RelativeLayout) finder.castView((View) finder.findRequiredView
                (source, R.id.rl_front, "field 'rlFoodFront'"), R.id.rl_front, "field " +
                "'rlFoodFront'");
        target.rlFoodBack = (RelativeLayout) finder.castView((View) finder.findRequiredView
                (source, R.id.rl_back, "field 'rlFoodBack'"), R.id.rl_back, "field 'rlFoodBack'");
        target.foodFrontImg = (ImageView) finder.castView((View) finder.findRequiredView(source,
                R.id.iv_front_img, "field 'foodFrontImg'"), R.id.iv_front_img, "field " +
                "'foodFrontImg'");
        target.foodBackImg = (ImageView) finder.castView((View) finder.findRequiredView(source, R
                .id.iv_back_img, "field 'foodBackImg'"), R.id.iv_back_img, "field 'foodBackImg'");
        target.tvCode = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_food_code, "field 'tvCode'"), R.id.tv_food_code, "field 'tvCode'");
        ((View) finder.findRequiredView(source, R.id.rl_food_code, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        ((View) finder.findRequiredView(source, R.id.rl_food_front, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        ((View) finder.findRequiredView(source, R.id.rl_food_back, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        ((View) finder.findRequiredView(source, R.id.iv_front_delete, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        ((View) finder.findRequiredView(source, R.id.iv_back_delete, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        ((View) finder.findRequiredView(source, R.id.ll_brand, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        ((View) finder.findRequiredView(source, R.id.ll_food_name, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
    }

    public void reset(T target) {
        target.etBrand = null;
        target.etFoodName = null;
        target.rlFoodFront = null;
        target.rlFoodBack = null;
        target.foodFrontImg = null;
        target.foodBackImg = null;
        target.tvCode = null;
    }
}
