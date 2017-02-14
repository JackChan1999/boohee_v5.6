package com.boohee.food;

import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

import com.boohee.one.R;

public class FoodErrorActivity$$ViewInjector<T extends FoodErrorActivity> implements Injector<T> {
    public void inject(Finder finder, T target, Object source) {
        target.edit = (EditText) finder.castView((View) finder.findRequiredView(source, R.id
                .edit, "field 'edit'"), R.id.edit, "field 'edit'");
        target.btn = (TextView) finder.castView((View) finder.findRequiredView(source, R.id.btn,
                "field 'btn'"), R.id.btn, "field 'btn'");
        target.activityFoodError = (LinearLayout) finder.castView((View) finder.findRequiredView
                (source, R.id.activity_food_error, "field 'activityFoodError'"), R.id
                .activity_food_error, "field 'activityFoodError'");
    }

    public void reset(T target) {
        target.edit = null;
        target.btn = null;
        target.activityFoodError = null;
    }
}
