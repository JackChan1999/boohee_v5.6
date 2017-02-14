package com.boohee.one.ui.fragment;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

import com.boohee.one.R;
import com.boohee.widgets.HorizontalListView;

public class ChartletFragment$$ViewInjector<T extends ChartletFragment> implements Injector<T> {
    public void inject(Finder finder, T target, Object source) {
        target.preImage = (ImageView) finder.castView((View) finder.findRequiredView(source, R.id
                .chartletImageView, "field 'preImage'"), R.id.chartletImageView, "field " +
                "'preImage'");
        target.iconListView = (HorizontalListView) finder.castView((View) finder.findRequiredView
                (source, R.id.charletListView, "field 'iconListView'"), R.id.charletListView,
                "field 'iconListView'");
        target.parentLayout = (FrameLayout) finder.castView((View) finder.findRequiredView
                (source, R.id.chartlet_parent, "field 'parentLayout'"), R.id.chartlet_parent,
                "field 'parentLayout'");
    }

    public void reset(T target) {
        target.preImage = null;
        target.iconListView = null;
        target.parentLayout = null;
    }
}
