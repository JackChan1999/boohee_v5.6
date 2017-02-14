package com.boohee.apn;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;
import butterknife.internal.DebouncingOnClickListener;

import com.boohee.one.R;

public class FragmentApn$$ViewInjector<T extends FragmentApn> implements Injector<T> {
    public void inject(Finder finder, final T target, Object source) {
        target.rvMain = (RecyclerView) finder.castView((View) finder.findRequiredView(source, R
                .id.rv_main, "field 'rvMain'"), R.id.rv_main, "field 'rvMain'");
        target.srlRefresh = (SwipeRefreshLayout) finder.castView((View) finder.findRequiredView
                (source, R.id.srl_refresh, "field 'srlRefresh'"), R.id.srl_refresh, "field " +
                "'srlRefresh'");
        View view = (View) finder.findRequiredView(source, R.id.view_select_category, "field " +
                "'viewSelectCategory' and method 'onClick'");
        target.viewSelectCategory = (TextView) finder.castView(view, R.id.view_select_category,
                "field 'viewSelectCategory'");
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        target.etQuestion = (EditText) finder.castView((View) finder.findRequiredView(source, R
                .id.et_question, "field 'etQuestion'"), R.id.et_question, "field 'etQuestion'");
        target.viewImages = (LinearLayout) finder.castView((View) finder.findRequiredView(source,
                R.id.view_images, "field 'viewImages'"), R.id.view_images, "field 'viewImages'");
        target.viewPopupWindow = (RelativeLayout) finder.castView((View) finder.findRequiredView
                (source, R.id.view_popup_window, "field 'viewPopupWindow'"), R.id
                .view_popup_window, "field 'viewPopupWindow'");
        view = (View) finder.findRequiredView(source, R.id.bt_send, "field 'btSend' and method " +
                "'onClick'");
        target.btSend = view;
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        ((View) finder.findRequiredView(source, R.id.view_ask, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        ((View) finder.findRequiredView(source, R.id.view_shade, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        ((View) finder.findRequiredView(source, R.id.view_default, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
    }

    public void reset(T target) {
        target.rvMain = null;
        target.srlRefresh = null;
        target.viewSelectCategory = null;
        target.etQuestion = null;
        target.viewImages = null;
        target.viewPopupWindow = null;
        target.btSend = null;
    }
}
