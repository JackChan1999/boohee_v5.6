package com.boohee.myview;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

import com.boohee.one.R;

public class ChangeProfileItemView$$ViewInjector<T extends ChangeProfileItemView> implements
        Injector<T> {
    public void inject(Finder finder, T target, Object source) {
        target.tvTitle = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_title, "field 'tvTitle'"), R.id.tv_title, "field 'tvTitle'");
        target.viewEdit = (ImageView) finder.castView((View) finder.findRequiredView(source, R.id
                .view_edit, "field 'viewEdit'"), R.id.view_edit, "field 'viewEdit'");
        target.tvValue = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_value, "field 'tvValue'"), R.id.tv_value, "field 'tvValue'");
        target.viewContent = (LinearLayout) finder.castView((View) finder.findRequiredView
                (source, R.id.view_content, "field 'viewContent'"), R.id.view_content, "field " +
                "'viewContent'");
    }

    public void reset(T target) {
        target.tvTitle = null;
        target.viewEdit = null;
        target.tvValue = null;
        target.viewContent = null;
    }
}
