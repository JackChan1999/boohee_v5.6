package com.boohee.food;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

import com.boohee.one.R;

public class BooheeRecipeActivity$$ViewInjector<T extends BooheeRecipeActivity> implements
        Injector<T> {
    public void inject(Finder finder, T target, Object source) {
        target.llRawContent = (LinearLayout) finder.castView((View) finder.findRequiredView
                (source, R.id.ll_raw_content, "field 'llRawContent'"), R.id.ll_raw_content,
                "field 'llRawContent'");
        target.llRaw = (LinearLayout) finder.castView((View) finder.findRequiredView(source, R.id
                .ll_raw, "field 'llRaw'"), R.id.ll_raw, "field 'llRaw'");
        target.llMajorContent = (LinearLayout) finder.castView((View) finder.findRequiredView
                (source, R.id.ll_major_content, "field 'llMajorContent'"), R.id.ll_major_content,
                "field 'llMajorContent'");
        target.llMajor = (LinearLayout) finder.castView((View) finder.findRequiredView(source, R
                .id.ll_major, "field 'llMajor'"), R.id.ll_major, "field 'llMajor'");
        target.llMinorContent = (LinearLayout) finder.castView((View) finder.findRequiredView
                (source, R.id.ll_minor_content, "field 'llMinorContent'"), R.id.ll_minor_content,
                "field 'llMinorContent'");
        target.llMinor = (LinearLayout) finder.castView((View) finder.findRequiredView(source, R
                .id.ll_minor, "field 'llMinor'"), R.id.ll_minor, "field 'llMinor'");
        target.llSeasoningContent = (LinearLayout) finder.castView((View) finder.findRequiredView
                (source, R.id.ll_seasoning_content, "field 'llSeasoningContent'"), R.id
                .ll_seasoning_content, "field 'llSeasoningContent'");
        target.llSeasoning = (LinearLayout) finder.castView((View) finder.findRequiredView
                (source, R.id.ll_seasoning, "field 'llSeasoning'"), R.id.ll_seasoning, "field " +
                "'llSeasoning'");
        target.tvStep = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_step, "field 'tvStep'"), R.id.tv_step, "field 'tvStep'");
        target.llSteps = (LinearLayout) finder.castView((View) finder.findRequiredView(source, R
                .id.ll_steps, "field 'llSteps'"), R.id.ll_steps, "field 'llSteps'");
    }

    public void reset(T target) {
        target.llRawContent = null;
        target.llRaw = null;
        target.llMajorContent = null;
        target.llMajor = null;
        target.llMinorContent = null;
        target.llMinor = null;
        target.llSeasoningContent = null;
        target.llSeasoning = null;
        target.tvStep = null;
        target.llSteps = null;
    }
}
