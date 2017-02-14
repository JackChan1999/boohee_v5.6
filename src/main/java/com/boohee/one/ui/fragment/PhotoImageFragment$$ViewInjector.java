package com.boohee.one.ui.fragment;

import android.view.View;
import android.widget.Button;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

import com.boohee.one.R;
import com.boohee.widgets.ProgressWheel;

import uk.co.senab.photoview.PhotoView;

public class PhotoImageFragment$$ViewInjector<T extends PhotoImageFragment> implements Injector<T> {
    public void inject(Finder finder, T target, Object source) {
        target.ivPhoto = (PhotoView) finder.castView((View) finder.findRequiredView(source, R.id
                .iv_photo, "field 'ivPhoto'"), R.id.iv_photo, "field 'ivPhoto'");
        target.progressWheel = (ProgressWheel) finder.castView((View) finder.findRequiredView
                (source, R.id.progressWheel, "field 'progressWheel'"), R.id.progressWheel, "field" +
                " 'progressWheel'");
        target.btnSave = (Button) finder.castView((View) finder.findRequiredView(source, R.id
                .btn_save, "field 'btnSave'"), R.id.btn_save, "field 'btnSave'");
    }

    public void reset(T target) {
        target.ivPhoto = null;
        target.progressWheel = null;
        target.btnSave = null;
    }
}
