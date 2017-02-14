package com.boohee.one.pedometer;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

import com.boohee.one.R;

public class StepCounterActivity$StepRecyclerAdapter$ViewHolder$$ViewInjector<T extends
        ViewHolder> implements Injector<T> {
    public void inject(Finder finder, T target, Object source) {
        target.ivStep = (ImageView) finder.castView((View) finder.findRequiredView(source, R.id
                .iv_step, "field 'ivStep'"), R.id.iv_step, "field 'ivStep'");
        target.tvFruitCount = (TextView) finder.castView((View) finder.findRequiredView(source, R
                .id.tv_fruit_count, "field 'tvFruitCount'"), R.id.tv_fruit_count, "field " +
                "'tvFruitCount'");
        target.tvCalory = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_calory, "field 'tvCalory'"), R.id.tv_calory, "field 'tvCalory'");
        target.llItem = (LinearLayout) finder.castView((View) finder.findRequiredView(source, R
                .id.ll_item, "field 'llItem'"), R.id.ll_item, "field 'llItem'");
    }

    public void reset(T target) {
        target.ivStep = null;
        target.tvFruitCount = null;
        target.tvCalory = null;
        target.llItem = null;
    }
}
