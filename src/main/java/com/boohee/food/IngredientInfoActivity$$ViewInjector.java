package com.boohee.food;

import android.view.View;
import android.widget.ListView;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

import com.boohee.one.R;

public class IngredientInfoActivity$$ViewInjector<T extends IngredientInfoActivity> implements
        Injector<T> {
    public void inject(Finder finder, T target, Object source) {
        target.lvMain = (ListView) finder.castView((View) finder.findRequiredView(source, R.id
                .lv_main, "field 'lvMain'"), R.id.lv_main, "field 'lvMain'");
    }

    public void reset(T target) {
        target.lvMain = null;
    }
}
