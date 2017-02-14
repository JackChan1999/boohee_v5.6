package com.boohee.nice;

import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;
import butterknife.internal.DebouncingOnClickListener;

import com.boohee.one.R;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import de.hdodenhof.circleimageview.CircleImageView;

public class NiceAdviserActivity$$ViewInjector<T extends NiceAdviserActivity> implements
        Injector<T> {
    public void inject(Finder finder, final T target, Object source) {
        target.avatar = (CircleImageView) finder.castView((View) finder.findRequiredView(source,
                R.id.avatar, "field 'avatar'"), R.id.avatar, "field 'avatar'");
        target.tvAdviser = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_adviser, "field 'tvAdviser'"), R.id.tv_adviser, "field 'tvAdviser'");
        target.tvWechat = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_wechat, "field 'tvWechat'"), R.id.tv_wechat, "field 'tvWechat'");
        target.viewRefresh = (PullToRefreshListView) finder.castView((View) finder
                .findRequiredView(source, R.id.view_refresh, "field 'viewRefresh'"), R.id
                .view_refresh, "field 'viewRefresh'");
        target.etInput = (EditText) finder.castView((View) finder.findRequiredView(source, R.id
                .et_input, "field 'etInput'"), R.id.et_input, "field 'etInput'");
        target.viewBottom = (LinearLayout) finder.castView((View) finder.findRequiredView(source,
                R.id.view_bottom, "field 'viewBottom'"), R.id.view_bottom, "field 'viewBottom'");
        ((View) finder.findRequiredView(source, R.id.bt_send, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick();
            }
        });
    }

    public void reset(T target) {
        target.avatar = null;
        target.tvAdviser = null;
        target.tvWechat = null;
        target.viewRefresh = null;
        target.etInput = null;
        target.viewBottom = null;
    }
}
