package com.boohee.food;

import android.view.View;
import android.widget.EditText;
import android.widget.ToggleButton;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;
import butterknife.internal.DebouncingOnClickListener;

import com.boohee.one.R;

import uk.co.senab.photoview.PhotoView;

public class AddCameraRecordActivity$$ViewInjector<T extends AddCameraRecordActivity> implements
        Injector<T> {
    public void inject(Finder finder, final T target, Object source) {
        target.toggle_bingo = (ToggleButton) finder.castView((View) finder.findRequiredView
                (source, R.id.toggle_bingo, "field 'toggle_bingo'"), R.id.toggle_bingo, "field " +
                "'toggle_bingo'");
        target.et_calory = (EditText) finder.castView((View) finder.findRequiredView(source, R.id
                .et_calory, "field 'et_calory'"), R.id.et_calory, "field 'et_calory'");
        target.et_name = (EditText) finder.castView((View) finder.findRequiredView(source, R.id
                .et_name, "field 'et_name'"), R.id.et_name, "field 'et_name'");
        target.iv_photo = (PhotoView) finder.castView((View) finder.findRequiredView(source, R.id
                .iv_photo, "field 'iv_photo'"), R.id.iv_photo, "field 'iv_photo'");
        ((View) finder.findRequiredView(source, R.id.ll_invite_bingo, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
    }

    public void reset(T target) {
        target.toggle_bingo = null;
        target.et_calory = null;
        target.et_name = null;
        target.iv_photo = null;
    }
}
