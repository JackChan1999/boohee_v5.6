package com.boohee.one.ui;

import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;
import butterknife.internal.DebouncingOnClickListener;

import com.boohee.one.R;

public class WebViewPicUploadActivity$$ViewInjector<T extends WebViewPicUploadActivity>
        implements Injector<T> {
    public void inject(Finder finder, final T target, Object source) {
        target.mEtCurrentWeight = (EditText) finder.castView((View) finder.findRequiredView
                (source, R.id.et_current_weight, "field 'mEtCurrentWeight'"), R.id
                .et_current_weight, "field 'mEtCurrentWeight'");
        View view = (View) finder.findRequiredView(source, R.id.iv_weight, "field 'mIvWeight' and" +
                " method 'OnClick'");
        target.mIvWeight = (ImageView) finder.castView(view, R.id.iv_weight, "field 'mIvWeight'");
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.OnClick(p0);
            }
        });
        view = (View) finder.findRequiredView(source, R.id.iv_weight_delete, "field " +
                "'mIvWeightDelete' and method 'OnClick'");
        target.mIvWeightDelete = (ImageView) finder.castView(view, R.id.iv_weight_delete, "field " +
                "'mIvWeightDelete'");
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.OnClick(p0);
            }
        });
        view = (View) finder.findRequiredView(source, R.id.iv_whole, "field 'mIvWhole' and method" +
                " 'OnClick'");
        target.mIvWhole = (ImageView) finder.castView(view, R.id.iv_whole, "field 'mIvWhole'");
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.OnClick(p0);
            }
        });
        view = (View) finder.findRequiredView(source, R.id.iv_whole_delete, "field " +
                "'mIvWholeDelete' and method 'OnClick'");
        target.mIvWholeDelete = (ImageView) finder.castView(view, R.id.iv_whole_delete, "field " +
                "'mIvWholeDelete'");
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.OnClick(p0);
            }
        });
        target.mCbSyncPost = (CheckBox) finder.castView((View) finder.findRequiredView(source, R
                .id.cb_sync_post, "field 'mCbSyncPost'"), R.id.cb_sync_post, "field 'mCbSyncPost'");
        ((View) finder.findRequiredView(source, R.id.btn_commit, "method 'OnClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.OnClick(p0);
            }
        });
        ((View) finder.findRequiredView(source, R.id.iv_example, "method 'OnClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.OnClick(p0);
            }
        });
    }

    public void reset(T target) {
        target.mEtCurrentWeight = null;
        target.mIvWeight = null;
        target.mIvWeightDelete = null;
        target.mIvWhole = null;
        target.mIvWholeDelete = null;
        target.mCbSyncPost = null;
    }
}
