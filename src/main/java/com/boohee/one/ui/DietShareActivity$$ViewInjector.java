package com.boohee.one.ui;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;
import butterknife.internal.DebouncingOnClickListener;

import com.boohee.one.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class DietShareActivity$$ViewInjector<T extends DietShareActivity> implements Injector<T> {
    public void inject(Finder finder, final T target, Object source) {
        target.tvDate = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_date, "field 'tvDate'"), R.id.tv_date, "field 'tvDate'");
        target.tvPlan = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_plan, "field 'tvPlan'"), R.id.tv_plan, "field 'tvPlan'");
        target.tvEating = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_eating, "field 'tvEating'"), R.id.tv_eating, "field 'tvEating'");
        target.tvActivity = (TextView) finder.castView((View) finder.findRequiredView(source, R
                .id.tv_activity, "field 'tvActivity'"), R.id.tv_activity, "field 'tvActivity'");
        target.viewMetabolism = (LinearLayout) finder.castView((View) finder.findRequiredView
                (source, R.id.view_metabolism, "field 'viewMetabolism'"), R.id.view_metabolism,
                "field 'viewMetabolism'");
        target.viewSlogan = (ImageView) finder.castView((View) finder.findRequiredView(source, R
                .id.view_slogan, "field 'viewSlogan'"), R.id.view_slogan, "field 'viewSlogan'");
        target.viewDietEat = (LinearLayout) finder.castView((View) finder.findRequiredView
                (source, R.id.view_diet_eat, "field 'viewDietEat'"), R.id.view_diet_eat, "field " +
                "'viewDietEat'");
        target.viewDietNuturitions = (LinearLayout) finder.castView((View) finder
                .findRequiredView(source, R.id.view_diet_nuturitions, "field " +
                        "'viewDietNuturitions'"), R.id.view_diet_nuturitions, "field " +
                "'viewDietNuturitions'");
        target.viewContent = (ScrollView) finder.castView((View) finder.findRequiredView(source,
                R.id.view_content, "field 'viewContent'"), R.id.view_content, "field " +
                "'viewContent'");
        target.avatar = (CircleImageView) finder.castView((View) finder.findRequiredView(source,
                R.id.avatar, "field 'avatar'"), R.id.avatar, "field 'avatar'");
        ((View) finder.findRequiredView(source, R.id.bt_share_boohee, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        ((View) finder.findRequiredView(source, R.id.bt_share_sns, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
    }

    public void reset(T target) {
        target.tvDate = null;
        target.tvPlan = null;
        target.tvEating = null;
        target.tvActivity = null;
        target.viewMetabolism = null;
        target.viewSlogan = null;
        target.viewDietEat = null;
        target.viewDietNuturitions = null;
        target.viewContent = null;
        target.avatar = null;
    }
}
