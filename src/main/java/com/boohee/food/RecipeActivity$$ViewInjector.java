package com.boohee.food;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

import com.boohee.one.R;

public class RecipeActivity$$ViewInjector<T extends RecipeActivity> implements Injector<T> {
    public void inject(Finder finder, T target, Object source) {
        target.ivRecipe = (ImageView) finder.castView((View) finder.findRequiredView(source, R.id
                .iv_recipe, "field 'ivRecipe'"), R.id.iv_recipe, "field 'ivRecipe'");
        target.tvTips = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_tips, "field 'tvTips'"), R.id.tv_tips, "field 'tvTips'");
        target.llCondiments = (LinearLayout) finder.castView((View) finder.findRequiredView
                (source, R.id.ll_condiments, "field 'llCondiments'"), R.id.ll_condiments, "field " +
                "'llCondiments'");
        target.llSteps = (LinearLayout) finder.castView((View) finder.findRequiredView(source, R
                .id.ll_steps, "field 'llSteps'"), R.id.ll_steps, "field 'llSteps'");
    }

    public void reset(T target) {
        target.ivRecipe = null;
        target.tvTips = null;
        target.llCondiments = null;
        target.llSteps = null;
    }
}
