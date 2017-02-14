package com.boohee.one.video.fragment;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

import com.boohee.one.R;

public class MentionPreviewFragment$$ViewInjector<T extends MentionPreviewFragment> implements
        Injector<T> {
    public void inject(Finder finder, T target, Object source) {
        target.tvEssentials1 = (TextView) finder.castView((View) finder.findRequiredView(source,
                R.id.tv_essentials_1, "field 'tvEssentials1'"), R.id.tv_essentials_1, "field " +
                "'tvEssentials1'");
        target.tvEssentials2 = (TextView) finder.castView((View) finder.findRequiredView(source,
                R.id.tv_essentials_2, "field 'tvEssentials2'"), R.id.tv_essentials_2, "field " +
                "'tvEssentials2'");
        target.tvEssentials3 = (TextView) finder.castView((View) finder.findRequiredView(source,
                R.id.tv_essentials_3, "field 'tvEssentials3'"), R.id.tv_essentials_3, "field " +
                "'tvEssentials3'");
        target.tvCommonError = (TextView) finder.castView((View) finder.findRequiredView(source,
                R.id.tv_common_error, "field 'tvCommonError'"), R.id.tv_common_error, "field " +
                "'tvCommonError'");
        target.essentials1Layout = (LinearLayout) finder.castView((View) finder.findRequiredView
                (source, R.id.essentials_1_layout, "field 'essentials1Layout'"), R.id
                .essentials_1_layout, "field 'essentials1Layout'");
        target.essentials2Layout = (LinearLayout) finder.castView((View) finder.findRequiredView
                (source, R.id.essentials_2_layout, "field 'essentials2Layout'"), R.id
                .essentials_2_layout, "field 'essentials2Layout'");
        target.essentials3Layout = (LinearLayout) finder.castView((View) finder.findRequiredView
                (source, R.id.essentials_3_layout, "field 'essentials3Layout'"), R.id
                .essentials_3_layout, "field 'essentials3Layout'");
        target.commonErrorLayout = (LinearLayout) finder.castView((View) finder.findRequiredView
                (source, R.id.common_error_layout, "field 'commonErrorLayout'"), R.id
                .common_error_layout, "field 'commonErrorLayout'");
        target.breathLayout = (LinearLayout) finder.castView((View) finder.findRequiredView
                (source, R.id.breath_layout, "field 'breathLayout'"), R.id.breath_layout, "field " +
                "'breathLayout'");
        target.tvName = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_name, "field 'tvName'"), R.id.tv_name, "field 'tvName'");
        target.tvBreath = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_breath, "field 'tvBreath'"), R.id.tv_breath, "field 'tvBreath'");
    }

    public void reset(T target) {
        target.tvEssentials1 = null;
        target.tvEssentials2 = null;
        target.tvEssentials3 = null;
        target.tvCommonError = null;
        target.essentials1Layout = null;
        target.essentials2Layout = null;
        target.essentials3Layout = null;
        target.commonErrorLayout = null;
        target.breathLayout = null;
        target.tvName = null;
        target.tvBreath = null;
    }
}
