package com.boohee.food;

import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;
import butterknife.internal.DebouncingOnClickListener;

import com.boohee.one.R;

public class FoodDetailActivity$$ViewInjector<T extends FoodDetailActivity> implements Injector<T> {
    public void inject(Finder finder, final T target, Object source) {
        View view = (View) finder.findRequiredView(source, R.id.iv_food_icon, "field 'ivFoodIcon'" +
                " and method 'onClick'");
        target.ivFoodIcon = (ImageView) finder.castView(view, R.id.iv_food_icon, "field " +
                "'ivFoodIcon'");
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        target.tvFoodName = (TextView) finder.castView((View) finder.findRequiredView(source, R
                .id.tv_food_name, "field 'tvFoodName'"), R.id.tv_food_name, "field 'tvFoodName'");
        target.tvCalory = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_calory, "field 'tvCalory'"), R.id.tv_calory, "field 'tvCalory'");
        target.tvWeight = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_weight, "field 'tvWeight'"), R.id.tv_weight, "field 'tvWeight'");
        target.rgUnit = (RadioGroup) finder.castView((View) finder.findRequiredView(source, R.id
                .rg_unit, "field 'rgUnit'"), R.id.rg_unit, "field 'rgUnit'");
        target.rbCalory = (RadioButton) finder.castView((View) finder.findRequiredView(source, R
                .id.rb_calory, "field 'rbCalory'"), R.id.rb_calory, "field 'rbCalory'");
        target.rbKjoule = (RadioButton) finder.castView((View) finder.findRequiredView(source, R
                .id.rb_kjoule, "field 'rbKjoule'"), R.id.rb_kjoule, "field 'rbKjoule'");
        view = (View) finder.findRequiredView(source, R.id.iv_light, "field 'ivHealthLight' and " +
                "method 'onClick'");
        target.ivHealthLight = (ImageView) finder.castView(view, R.id.iv_light, "field " +
                "'ivHealthLight'");
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        view = (View) finder.findRequiredView(source, R.id.ll_recipe, "field 'llRecipe' and " +
                "method 'onClick'");
        target.llRecipe = (LinearLayout) finder.castView(view, R.id.ll_recipe, "field 'llRecipe'");
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        target.llCompare = (LinearLayout) finder.castView((View) finder.findRequiredView(source,
                R.id.ll_compare, "field 'llCompare'"), R.id.ll_compare, "field 'llCompare'");
        target.llCompareContent = (LinearLayout) finder.castView((View) finder.findRequiredView
                (source, R.id.ll_compare_content, "field 'llCompareContent'"), R.id
                .ll_compare_content, "field 'llCompareContent'");
        target.llUnits = (LinearLayout) finder.castView((View) finder.findRequiredView(source, R
                .id.ll_units, "field 'llUnits'"), R.id.ll_units, "field 'llUnits'");
        target.llUnitsCheckbox = (LinearLayout) finder.castView((View) finder.findRequiredView
                (source, R.id.ll_units_checkbox, "field 'llUnitsCheckbox'"), R.id
                .ll_units_checkbox, "field 'llUnitsCheckbox'");
        view = (View) finder.findRequiredView(source, R.id.cb_units, "field 'cbUnits' and method " +
                "'onCheckedChanged'");
        target.cbUnits = (CheckBox) finder.castView(view, R.id.cb_units, "field 'cbUnits'");
        ((CompoundButton) view).setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton p0, boolean p1) {
                target.onCheckedChanged(p0, p1);
            }
        });
        target.llUnitsItem = (LinearLayout) finder.castView((View) finder.findRequiredView
                (source, R.id.ll_units_item, "field 'llUnitsItem'"), R.id.ll_units_item, "field " +
                "'llUnitsItem'");
        target.tvAppraise = (TextView) finder.castView((View) finder.findRequiredView(source, R
                .id.tv_appraise, "field 'tvAppraise'"), R.id.tv_appraise, "field 'tvAppraise'");
        target.ivCompare = (ImageView) finder.castView((View) finder.findRequiredView(source, R
                .id.iv_compare, "field 'ivCompare'"), R.id.iv_compare, "field 'ivCompare'");
        target.tvCompateAmount = (TextView) finder.castView((View) finder.findRequiredView
                (source, R.id.tv_compare_amount, "field 'tvCompateAmount'"), R.id
                .tv_compare_amount, "field 'tvCompateAmount'");
        target.tvCompareInfo = (TextView) finder.castView((View) finder.findRequiredView(source,
                R.id.tv_compare_info, "field 'tvCompareInfo'"), R.id.tv_compare_info, "field " +
                "'tvCompareInfo'");
        target.lvMain = (ListView) finder.castView((View) finder.findRequiredView(source, R.id
                .lv_main, "field 'lvMain'"), R.id.lv_main, "field 'lvMain'");
        target.tvUploader = (TextView) finder.castView((View) finder.findRequiredView(source, R
                .id.tv_uploader, "field 'tvUploader'"), R.id.tv_uploader, "field 'tvUploader'");
        target.llUploader = (LinearLayout) finder.castView((View) finder.findRequiredView(source,
                R.id.ll_uploader, "field 'llUploader'"), R.id.ll_uploader, "field 'llUploader'");
        view = (View) finder.findRequiredView(source, R.id.ll_food_record, "field 'llRecord' and " +
                "method 'onClick'");
        target.llRecord = (RelativeLayout) finder.castView(view, R.id.ll_food_record, "field " +
                "'llRecord'");
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        target.vBottom = (View) finder.findRequiredView(source, R.id.v_bottom, "field 'vBottom'");
        ((View) finder.findRequiredView(source, R.id.tv_see_more, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        ((View) finder.findRequiredView(source, R.id.tv_how_assessment, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        ((View) finder.findRequiredView(source, R.id.iv_food_app, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        ((View) finder.findRequiredView(source, R.id.tv_error, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
    }

    public void reset(T target) {
        target.ivFoodIcon = null;
        target.tvFoodName = null;
        target.tvCalory = null;
        target.tvWeight = null;
        target.rgUnit = null;
        target.rbCalory = null;
        target.rbKjoule = null;
        target.ivHealthLight = null;
        target.llRecipe = null;
        target.llCompare = null;
        target.llCompareContent = null;
        target.llUnits = null;
        target.llUnitsCheckbox = null;
        target.cbUnits = null;
        target.llUnitsItem = null;
        target.tvAppraise = null;
        target.ivCompare = null;
        target.tvCompateAmount = null;
        target.tvCompareInfo = null;
        target.lvMain = null;
        target.tvUploader = null;
        target.llUploader = null;
        target.llRecord = null;
        target.vBottom = null;
    }
}
