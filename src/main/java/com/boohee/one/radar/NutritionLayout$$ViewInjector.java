package com.boohee.one.radar;

import android.view.View;
import android.widget.TextView;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

import com.boohee.one.R;

public class NutritionLayout$$ViewInjector<T extends NutritionLayout> implements Injector<T> {
    public void inject(Finder finder, T target, Object source) {
        target.emptyStructure = (TextView) finder.castView((View) finder.findRequiredView(source,
                R.id.empty_nutrition_structure, "field 'emptyStructure'"), R.id
                .empty_nutrition_structure, "field 'emptyStructure'");
    }

    public void reset(T target) {
        target.emptyStructure = null;
    }
}
