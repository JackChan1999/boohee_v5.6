package com.boohee.one.ui.fragment;

import android.view.View;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

import com.boohee.one.R;
import com.boohee.widgets.ProgressWheel;

import uk.co.senab.photoview.PhotoView;

public class PhotoBrowserFragment$$ViewInjector<T extends PhotoBrowserFragment> implements
        Injector<T> {
    public void inject(Finder finder, T target, Object source) {
        target.ivPhoto = (PhotoView) finder.castView((View) finder.findRequiredView(source, R.id
                .iv_photo, "field 'ivPhoto'"), R.id.iv_photo, "field 'ivPhoto'");
        target.progressWheel = (ProgressWheel) finder.castView((View) finder.findRequiredView
                (source, R.id.progressWheel, "field 'progressWheel'"), R.id.progressWheel, "field" +
                " 'progressWheel'");
    }

    public void reset(T target) {
        target.ivPhoto = null;
        target.progressWheel = null;
    }
}
