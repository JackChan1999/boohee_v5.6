package com.boohee.record;

import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;
import butterknife.internal.DebouncingOnClickListener;

import com.boohee.one.R;

public class ScaleRecordFragment$$ViewInjector<T extends ScaleRecordFragment> implements
        Injector<T> {
    public void inject(Finder finder, final T target, Object source) {
        target.gv = (GridView) finder.castView((View) finder.findRequiredView(source, R.id.gv,
                "field 'gv'"), R.id.gv, "field 'gv'");
        View view = (View) finder.findRequiredView(source, R.id.iv_camera, "field 'ivCamera' and " +
                "method 'onClick'");
        target.ivCamera = view;
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        target.ivImage = (ImageView) finder.castView((View) finder.findRequiredView(source, R.id
                .iv_image, "field 'ivImage'"), R.id.iv_image, "field 'ivImage'");
        target.tvWeight = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_weight, "field 'tvWeight'"), R.id.tv_weight, "field 'tvWeight'");
        target.tvDate = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_date, "field 'tvDate'"), R.id.tv_date, "field 'tvDate'");
        view = (View) finder.findRequiredView(source, R.id.tv_delete, "field 'tvDelete' and " +
                "method 'onClick'");
        target.tvDelete = (TextView) finder.castView(view, R.id.tv_delete, "field 'tvDelete'");
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        target.ivRunning = (ImageView) finder.castView((View) finder.findRequiredView(source, R
                .id.iv_running, "field 'ivRunning'"), R.id.iv_running, "field 'ivRunning'");
        target.ivAchieve = (ImageView) finder.castView((View) finder.findRequiredView(source, R
                .id.iv_achieve, "field 'ivAchieve'"), R.id.iv_achieve, "field 'ivAchieve'");
        target.tvSend = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_send, "field 'tvSend'"), R.id.tv_send, "field 'tvSend'");
        ((View) finder.findRequiredView(source, R.id.ripple_cancel, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        ((View) finder.findRequiredView(source, R.id.ripple_send, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
    }

    public void reset(T target) {
        target.gv = null;
        target.ivCamera = null;
        target.ivImage = null;
        target.tvWeight = null;
        target.tvDate = null;
        target.tvDelete = null;
        target.ivRunning = null;
        target.ivAchieve = null;
        target.tvSend = null;
    }
}
