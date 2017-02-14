package com.boohee.status;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ViewFlipper;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;
import butterknife.internal.DebouncingOnClickListener;

import com.boohee.myview.ViewPagerHeaderScroll.widget.TouchCallbackLayout;
import com.boohee.one.R;

public class TopicActivity$$ViewInjector<T extends TopicActivity> implements Injector<T> {
    public void inject(Finder finder, final T target, Object source) {
        target.frameContent = (FrameLayout) finder.castView((View) finder.findRequiredView
                (source, R.id.frame_content, "field 'frameContent'"), R.id.frame_content, "field " +
                "'frameContent'");
        target.ivTop = (ImageView) finder.castView((View) finder.findRequiredView(source, R.id
                .iv_top, "field 'ivTop'"), R.id.iv_top, "field 'ivTop'");
        target.viewTabs = (TabLayout) finder.castView((View) finder.findRequiredView(source, R.id
                .view_tabs, "field 'viewTabs'"), R.id.view_tabs, "field 'viewTabs'");
        target.headerLayout = (LinearLayout) finder.castView((View) finder.findRequiredView
                (source, R.id.header_layout, "field 'headerLayout'"), R.id.header_layout, "field " +
                "'headerLayout'");
        target.layout = (TouchCallbackLayout) finder.castView((View) finder.findRequiredView
                (source, R.id.layout, "field 'layout'"), R.id.layout, "field 'layout'");
        View view = (View) finder.findRequiredView(source, R.id.fab_button, "field 'fabButton' " +
                "and method 'onClick'");
        target.fabButton = (FloatingActionButton) finder.castView(view, R.id.fab_button, "field " +
                "'fabButton'");
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        target.vfNotice = (ViewFlipper) finder.castView((View) finder.findRequiredView(source, R
                .id.vf_notice, "field 'vfNotice'"), R.id.vf_notice, "field 'vfNotice'");
    }

    public void reset(T target) {
        target.frameContent = null;
        target.ivTop = null;
        target.viewTabs = null;
        target.headerLayout = null;
        target.layout = null;
        target.fabButton = null;
        target.vfNotice = null;
    }
}
