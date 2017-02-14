package com.boohee.uchoice;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

import com.boohee.one.R;

public class RefundApplyActivity$$ViewInjector<T extends RefundApplyActivity> implements
        Injector<T> {
    public void inject(Finder finder, T target, Object source) {
        target.tvGoods = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_goods, "field 'tvGoods'"), R.id.tv_goods, "field 'tvGoods'");
        target.tvPrice = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_price, "field 'tvPrice'"), R.id.tv_price, "field 'tvPrice'");
        target.etAccount = (EditText) finder.castView((View) finder.findRequiredView(source, R.id
                .et_account, "field 'etAccount'"), R.id.et_account, "field 'etAccount'");
        target.etReason = (EditText) finder.castView((View) finder.findRequiredView(source, R.id
                .et_reason, "field 'etReason'"), R.id.et_reason, "field 'etReason'");
        target.tvCommit = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_commit, "field 'tvCommit'"), R.id.tv_commit, "field 'tvCommit'");
    }

    public void reset(T target) {
        target.tvGoods = null;
        target.tvPrice = null;
        target.etAccount = null;
        target.etReason = null;
        target.tvCommit = null;
    }
}
