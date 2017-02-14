package com.boohee.one.pedometer;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;
import butterknife.internal.DebouncingOnClickListener;

import com.boohee.one.R;

public class StepCounterActivity$$ViewInjector<T extends StepCounterActivity> implements
        Injector<T> {
    public void inject(Finder finder, final T target, Object source) {
        target.ivUmcomplete = (ImageView) finder.castView((View) finder.findRequiredView(source,
                R.id.iv_umcomplete, "field 'ivUmcomplete'"), R.id.iv_umcomplete, "field " +
                "'ivUmcomplete'");
        target.ivPointer = (ImageView) finder.castView((View) finder.findRequiredView(source, R
                .id.iv_pointer, "field 'ivPointer'"), R.id.iv_pointer, "field 'ivPointer'");
        target.ivComplete = (ImageView) finder.castView((View) finder.findRequiredView(source, R
                .id.iv_complete, "field 'ivComplete'"), R.id.iv_complete, "field 'ivComplete'");
        target.tvCurrentStep = (TextView) finder.castView((View) finder.findRequiredView(source,
                R.id.tv_current_step, "field 'tvCurrentStep'"), R.id.tv_current_step, "field " +
                "'tvCurrentStep'");
        target.tvTargetStep = (TextView) finder.castView((View) finder.findRequiredView(source, R
                .id.tv_target_step, "field 'tvTargetStep'"), R.id.tv_target_step, "field " +
                "'tvTargetStep'");
        View view = (View) finder.findRequiredView(source, R.id.rl_step, "field 'rlStep' and " +
                "method 'onClick'");
        target.rlStep = (RelativeLayout) finder.castView(view, R.id.rl_step, "field 'rlStep'");
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        target.tvCalory = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_calory, "field 'tvCalory'"), R.id.tv_calory, "field 'tvCalory'");
        target.recyclerView = (RecyclerView) finder.castView((View) finder.findRequiredView
                (source, R.id.recycler_view, "field 'recyclerView'"), R.id.recycler_view, "field " +
                "'recyclerView'");
        target.llContent = (LinearLayout) finder.castView((View) finder.findRequiredView(source,
                R.id.ll_content, "field 'llContent'"), R.id.ll_content, "field 'llContent'");
        target.tvError = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_error, "field 'tvError'"), R.id.tv_error, "field 'tvError'");
    }

    public void reset(T target) {
        target.ivUmcomplete = null;
        target.ivPointer = null;
        target.ivComplete = null;
        target.tvCurrentStep = null;
        target.tvTargetStep = null;
        target.rlStep = null;
        target.tvCalory = null;
        target.recyclerView = null;
        target.llContent = null;
        target.tvError = null;
    }
}
