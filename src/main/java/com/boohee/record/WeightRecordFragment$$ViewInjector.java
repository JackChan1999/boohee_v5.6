package com.boohee.record;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;
import butterknife.internal.DebouncingOnClickListener;

import com.boohee.myview.BooheeRulerView;
import com.boohee.one.R;

public class WeightRecordFragment$$ViewInjector<T extends WeightRecordFragment> implements
        Injector<T> {
    public void inject(Finder finder, final T target, Object source) {
        target.ruler = (BooheeRulerView) finder.castView((View) finder.findRequiredView(source, R
                .id.ruler, "field 'ruler'"), R.id.ruler, "field 'ruler'");
        target.tvWeight = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_weight, "field 'tvWeight'"), R.id.tv_weight, "field 'tvWeight'");
        View view = (View) finder.findRequiredView(source, R.id.tv_delete, "field 'tvDelete' and " +
                "method 'onClick'");
        target.tvDelete = (TextView) finder.castView(view, R.id.tv_delete, "field 'tvDelete'");
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        view = (View) finder.findRequiredView(source, R.id.ripple_cancel, "field 'rippleCancle' " +
                "and method 'onClick'");
        target.rippleCancle = view;
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        view = (View) finder.findRequiredView(source, R.id.ripple_send, "field 'rippleSend' and " +
                "method 'onClick'");
        target.rippleSend = view;
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        target.tvDate = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_date, "field 'tvDate'"), R.id.tv_date, "field 'tvDate'");
        view = (View) finder.findRequiredView(source, R.id.iv_camera, "field 'ivCamera' and " +
                "method 'onClick'");
        target.ivCamera = view;
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        target.ivImage = (ImageView) finder.castView((View) finder.findRequiredView(source, R.id
                .iv_image, "field 'ivImage'"), R.id.iv_image, "field 'ivImage'");
    }

    public void reset(T target) {
        target.ruler = null;
        target.tvWeight = null;
        target.tvDelete = null;
        target.rippleCancle = null;
        target.rippleSend = null;
        target.tvDate = null;
        target.ivCamera = null;
        target.ivImage = null;
    }
}
