package com.boohee.one.ui.fragment;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;
import butterknife.internal.DebouncingOnClickListener;

import com.boohee.myview.BooheeRulerView;
import com.boohee.one.R;
import com.boohee.widgets.BooheeRippleLayout;
import com.boohee.widgets.tablayout.BooheeTabLayout;
import com.makeramen.roundedimageview.RoundedImageView;

public class BaseDietFragment$$ViewInjector<T extends BaseDietFragment> implements Injector<T> {
    public void inject(Finder finder, final T target, Object source) {
        View view = (View) finder.findRequiredView(source, R.id.tv_title, "field 'tvTitle' and " +
                "method 'onClickView'");
        target.tvTitle = (TextView) finder.castView(view, R.id.tv_title, "field 'tvTitle'");
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClickView(p0);
            }
        });
        target.ivDiet = (RoundedImageView) finder.castView((View) finder.findRequiredView(source,
                R.id.iv_diet, "field 'ivDiet'"), R.id.iv_diet, "field 'ivDiet'");
        target.tvDietName = (TextView) finder.castView((View) finder.findRequiredView(source, R
                .id.tv_diet_name, "field 'tvDietName'"), R.id.tv_diet_name, "field 'tvDietName'");
        target.tvCaloryPer = (TextView) finder.castView((View) finder.findRequiredView(source, R
                .id.tv_calory_per, "field 'tvCaloryPer'"), R.id.tv_calory_per, "field " +
                "'tvCaloryPer'");
        target.ivCaloryStatus = (ImageView) finder.castView((View) finder.findRequiredView
                (source, R.id.iv_calory_status, "field 'ivCaloryStatus'"), R.id.iv_calory_status,
                "field 'ivCaloryStatus'");
        target.tvCalory = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_calory, "field 'tvCalory'"), R.id.tv_calory, "field 'tvCalory'");
        target.tvWeight = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_weight, "field 'tvWeight'"), R.id.tv_weight, "field 'tvWeight'");
        target.tvUnitValue = (TextView) finder.castView((View) finder.findRequiredView(source, R
                .id.tv_unit_value, "field 'tvUnitValue'"), R.id.tv_unit_value, "field " +
                "'tvUnitValue'");
        view = (View) finder.findRequiredView(source, R.id.tv_estimate, "field 'tvEstimate' and " +
                "method 'onClickView'");
        target.tvEstimate = (TextView) finder.castView(view, R.id.tv_estimate, "field " +
                "'tvEstimate'");
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClickView(p0);
            }
        });
        target.ruler = (BooheeRulerView) finder.castView((View) finder.findRequiredView(source, R
                .id.ruler, "field 'ruler'"), R.id.ruler, "field 'ruler'");
        target.dietUnitTab = (BooheeTabLayout) finder.castView((View) finder.findRequiredView
                (source, R.id.diet_unit_tab, "field 'dietUnitTab'"), R.id.diet_unit_tab, "field " +
                "'dietUnitTab'");
        view = (View) finder.findRequiredView(source, R.id.tv_delete, "field 'tvDelete' and " +
                "method 'onClickView'");
        target.tvDelete = (TextView) finder.castView(view, R.id.tv_delete, "field 'tvDelete'");
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClickView(p0);
            }
        });
        view = (View) finder.findRequiredView(source, R.id.tv_cancel, "field 'tvCancel' and " +
                "method 'onClickView'");
        target.tvCancel = (TextView) finder.castView(view, R.id.tv_cancel, "field 'tvCancel'");
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClickView(p0);
            }
        });
        target.rippleCancel = (BooheeRippleLayout) finder.castView((View) finder.findRequiredView
                (source, R.id.ripple_cancel, "field 'rippleCancel'"), R.id.ripple_cancel, "field " +
                "'rippleCancel'");
        view = (View) finder.findRequiredView(source, R.id.tv_send, "field 'tvSend' and method " +
                "'onClickView'");
        target.tvSend = (TextView) finder.castView(view, R.id.tv_send, "field 'tvSend'");
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClickView(p0);
            }
        });
        target.rippleSend = (BooheeRippleLayout) finder.castView((View) finder.findRequiredView
                (source, R.id.ripple_send, "field 'rippleSend'"), R.id.ripple_send, "field " +
                "'rippleSend'");
        view = (View) finder.findRequiredView(source, R.id.ll_diet_info, "field 'llDietInfo' and " +
                "method 'onClickView'");
        target.llDietInfo = (RelativeLayout) finder.castView(view, R.id.ll_diet_info, "field " +
                "'llDietInfo'");
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClickView(p0);
            }
        });
        target.tvUnitName = (TextView) finder.castView((View) finder.findRequiredView(source, R
                .id.tv_unit_name, "field 'tvUnitName'"), R.id.tv_unit_name, "field 'tvUnitName'");
    }

    public void reset(T target) {
        target.tvTitle = null;
        target.ivDiet = null;
        target.tvDietName = null;
        target.tvCaloryPer = null;
        target.ivCaloryStatus = null;
        target.tvCalory = null;
        target.tvWeight = null;
        target.tvUnitValue = null;
        target.tvEstimate = null;
        target.ruler = null;
        target.dietUnitTab = null;
        target.tvDelete = null;
        target.tvCancel = null;
        target.rippleCancel = null;
        target.tvSend = null;
        target.rippleSend = null;
        target.llDietInfo = null;
        target.tvUnitName = null;
    }
}
