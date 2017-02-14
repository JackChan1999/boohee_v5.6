package com.boohee.food;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

import com.boohee.one.R;

public class AddCustomSportActivity$$ViewInjector<T extends AddCustomSportActivity> implements
        Injector<T> {
    public void inject(Finder finder, T target, Object source) {
        target.et_food_name = (EditText) finder.castView((View) finder.findRequiredView(source, R
                .id.et_food_name, "field 'et_food_name'"), R.id.et_food_name, "field " +
                "'et_food_name'");
        target.et_food_num = (EditText) finder.castView((View) finder.findRequiredView(source, R
                .id.et_food_num, "field 'et_food_num'"), R.id.et_food_num, "field 'et_food_num'");
        target.tv_food_unit = (TextView) finder.castView((View) finder.findRequiredView(source, R
                .id.tv_food_unit, "field 'tv_food_unit'"), R.id.tv_food_unit, "field " +
                "'tv_food_unit'");
        target.et_food_calory = (EditText) finder.castView((View) finder.findRequiredView(source,
                R.id.et_food_calory, "field 'et_food_calory'"), R.id.et_food_calory, "field " +
                "'et_food_calory'");
    }

    public void reset(T target) {
        target.et_food_name = null;
        target.et_food_num = null;
        target.tv_food_unit = null;
        target.et_food_calory = null;
    }
}
