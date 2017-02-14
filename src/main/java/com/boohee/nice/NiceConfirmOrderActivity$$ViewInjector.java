package com.boohee.nice;

import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ToggleButton;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;
import butterknife.internal.DebouncingOnClickListener;

import com.boohee.myview.NiceCheckBox;
import com.boohee.one.R;

public class NiceConfirmOrderActivity$$ViewInjector<T extends NiceConfirmOrderActivity>
        implements Injector<T> {
    public void inject(Finder finder, final T target, Object source) {
        target.tbAlipay = (ToggleButton) finder.castView((View) finder.findRequiredView(source, R
                .id.tb_alipay, "field 'tbAlipay'"), R.id.tb_alipay, "field 'tbAlipay'");
        target.tbWechat = (ToggleButton) finder.castView((View) finder.findRequiredView(source, R
                .id.tb_wechat, "field 'tbWechat'"), R.id.tb_wechat, "field 'tbWechat'");
        target.tvNiceTitle = (TextView) finder.castView((View) finder.findRequiredView(source, R
                .id.tv_nice_title, "field 'tvNiceTitle'"), R.id.tv_nice_title, "field " +
                "'tvNiceTitle'");
        target.tvPriceAllValue = (TextView) finder.castView((View) finder.findRequiredView
                (source, R.id.tv_price_all_value, "field 'tvPriceAllValue'"), R.id
                .tv_price_all_value, "field 'tvPriceAllValue'");
        target.genderCheckBox = (NiceCheckBox) finder.castView((View) finder.findRequiredView
                (source, R.id.gender_check_box, "field 'genderCheckBox'"), R.id.gender_check_box,
                "field 'genderCheckBox'");
        target.pregnantCheckBox = (NiceCheckBox) finder.castView((View) finder.findRequiredView
                (source, R.id.pregnant_check_box, "field 'pregnantCheckBox'"), R.id
                .pregnant_check_box, "field 'pregnantCheckBox'");
        target.diseaseCheckBox = (NiceCheckBox) finder.castView((View) finder.findRequiredView
                (source, R.id.disease_check_box, "field 'diseaseCheckBox'"), R.id
                .disease_check_box, "field 'diseaseCheckBox'");
        target.dietCheckBox = (NiceCheckBox) finder.castView((View) finder.findRequiredView
                (source, R.id.diet_check_box, "field 'dietCheckBox'"), R.id.diet_check_box,
                "field 'dietCheckBox'");
        target.etNicePhone = (EditText) finder.castView((View) finder.findRequiredView(source, R
                .id.et_nice_phone, "field 'etNicePhone'"), R.id.et_nice_phone, "field " +
                "'etNicePhone'");
        target.etNiceWeight = (EditText) finder.castView((View) finder.findRequiredView(source, R
                .id.et_nice_weight, "field 'etNiceWeight'"), R.id.et_nice_weight, "field " +
                "'etNiceWeight'");
        target.etNiceHeight = (EditText) finder.castView((View) finder.findRequiredView(source, R
                .id.et_nice_height, "field 'etNiceHeight'"), R.id.et_nice_height, "field " +
                "'etNiceHeight'");
        target.svNice = (ScrollView) finder.castView((View) finder.findRequiredView(source, R.id
                .sv_nice, "field 'svNice'"), R.id.sv_nice, "field 'svNice'");
        ((View) finder.findRequiredView(source, R.id.ll_alipay, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        ((View) finder.findRequiredView(source, R.id.ll_wechat, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        ((View) finder.findRequiredView(source, R.id.btn_pay, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
    }

    public void reset(T target) {
        target.tbAlipay = null;
        target.tbWechat = null;
        target.tvNiceTitle = null;
        target.tvPriceAllValue = null;
        target.genderCheckBox = null;
        target.pregnantCheckBox = null;
        target.diseaseCheckBox = null;
        target.dietCheckBox = null;
        target.etNicePhone = null;
        target.etNiceWeight = null;
        target.etNiceHeight = null;
        target.svNice = null;
    }
}
