package com.boohee.myview;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

import com.boohee.one.R;

public class CommonLineView$$ViewInjector<T extends CommonLineView> implements Injector<T> {
    public void inject(Finder finder, T target, Object source) {
        target.ivIcon = (ImageView) finder.castView((View) finder.findRequiredView(source, R.id
                .iv_icon, "field 'ivIcon'"), R.id.iv_icon, "field 'ivIcon'");
        target.tvTitle = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_title, "field 'tvTitle'"), R.id.tv_title, "field 'tvTitle'");
        target.tvText = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_text, "field 'tvText'"), R.id.tv_text, "field 'tvText'");
    }

    public void reset(T target) {
        target.ivIcon = null;
        target.tvTitle = null;
        target.tvText = null;
    }
}
