package com.boohee.one.ui;

import android.view.View;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

import com.boohee.one.R;

import uk.co.senab.photoview.PhotoView;

public class DushouExampleActivity$$ViewInjector<T extends DushouExampleActivity> implements
        Injector<T> {
    public void inject(Finder finder, T target, Object source) {
        target.mIvPhoto = (PhotoView) finder.castView((View) finder.findRequiredView(source, R.id
                .iv_photo, "field 'mIvPhoto'"), R.id.iv_photo, "field 'mIvPhoto'");
    }

    public void reset(T target) {
        target.mIvPhoto = null;
    }
}
