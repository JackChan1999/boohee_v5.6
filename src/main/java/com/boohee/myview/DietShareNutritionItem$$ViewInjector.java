package com.boohee.myview;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

import com.boohee.one.R;

public class DietShareNutritionItem$$ViewInjector<T extends DietShareNutritionItem> implements
        Injector<T> {
    public void inject(Finder finder, T target, Object source) {
        target.tvIngredient = (TextView) finder.castView((View) finder.findRequiredView(source, R
                .id.tv_ingredient, "field 'tvIngredient'"), R.id.tv_ingredient, "field " +
                "'tvIngredient'");
        target.tvContent = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_content, "field 'tvContent'"), R.id.tv_content, "field 'tvContent'");
        target.tvPercent = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_percent, "field 'tvPercent'"), R.id.tv_percent, "field 'tvPercent'");
        target.tvIndicate = (TextView) finder.castView((View) finder.findRequiredView(source, R
                .id.tv_indicate, "field 'tvIndicate'"), R.id.tv_indicate, "field 'tvIndicate'");
        target.viewIndicate = (ImageView) finder.castView((View) finder.findRequiredView(source,
                R.id.view_indicate, "field 'viewIndicate'"), R.id.view_indicate, "field " +
                "'viewIndicate'");
    }

    public void reset(T target) {
        target.tvIngredient = null;
        target.tvContent = null;
        target.tvPercent = null;
        target.tvIndicate = null;
        target.viewIndicate = null;
    }
}
