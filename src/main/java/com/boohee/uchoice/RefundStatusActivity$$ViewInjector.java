package com.boohee.uchoice;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

import com.boohee.one.R;

public class RefundStatusActivity$$ViewInjector<T extends RefundStatusActivity> implements
        Injector<T> {
    public void inject(Finder finder, T target, Object source) {
        target.tvStatus = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_status, "field 'tvStatus'"), R.id.tv_status, "field 'tvStatus'");
        target.tvNote = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_note, "field 'tvNote'"), R.id.tv_note, "field 'tvNote'");
        target.llDetail = (LinearLayout) finder.castView((View) finder.findRequiredView(source, R
                .id.ll_detail, "field 'llDetail'"), R.id.ll_detail, "field 'llDetail'");
        target.tvContact = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_contact, "field 'tvContact'"), R.id.tv_contact, "field 'tvContact'");
    }

    public void reset(T target) {
        target.tvStatus = null;
        target.tvNote = null;
        target.llDetail = null;
        target.tvContact = null;
    }
}
