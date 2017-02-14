package com.boohee.food;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;
import butterknife.internal.DebouncingOnClickListener;

import com.boohee.one.R;

import uk.co.senab.photoview.PhotoView;

public class EditCameraRecordActivity$$ViewInjector<T extends EditCameraRecordActivity>
        implements Injector<T> {
    public void inject(Finder finder, final T target, Object source) {
        target.iv_photo = (PhotoView) finder.castView((View) finder.findRequiredView(source, R.id
                .iv_photo, "field 'iv_photo'"), R.id.iv_photo, "field 'iv_photo'");
        target.tv_message = (TextView) finder.castView((View) finder.findRequiredView(source, R
                .id.tv_message, "field 'tv_message'"), R.id.tv_message, "field 'tv_message'");
        target.tv_calory = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_calory, "field 'tv_calory'"), R.id.tv_calory, "field 'tv_calory'");
        target.tv_name = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_name, "field 'tv_name'"), R.id.tv_name, "field 'tv_name'");
        target.tv_unit = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_unit, "field 'tv_unit'"), R.id.tv_unit, "field 'tv_unit'");
        target.ll_bingo_estimate = (LinearLayout) finder.castView((View) finder.findRequiredView
                (source, R.id.ll_bingo_estimate, "field 'll_bingo_estimate'"), R.id
                .ll_bingo_estimate, "field 'll_bingo_estimate'");
        target.view_divide_name = (View) finder.findRequiredView(source, R.id.view_divide_name,
                "field 'view_divide_name'");
        target.view_divide_estimate = (View) finder.findRequiredView(source, R.id
                .view_divide_estimate, "field 'view_divide_estimate'");
        target.view_divide_messsage = (View) finder.findRequiredView(source, R.id
                .view_divide_messsage, "field 'view_divide_messsage'");
        ((View) finder.findRequiredView(source, R.id.rl_delete, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
    }

    public void reset(T target) {
        target.iv_photo = null;
        target.tv_message = null;
        target.tv_calory = null;
        target.tv_name = null;
        target.tv_unit = null;
        target.ll_bingo_estimate = null;
        target.view_divide_name = null;
        target.view_divide_estimate = null;
        target.view_divide_messsage = null;
    }
}
