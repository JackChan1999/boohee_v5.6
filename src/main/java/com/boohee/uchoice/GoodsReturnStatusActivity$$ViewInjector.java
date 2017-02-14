package com.boohee.uchoice;

import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

import com.boohee.one.R;

public class GoodsReturnStatusActivity$$ViewInjector<T extends GoodsReturnStatusActivity>
        implements Injector<T> {
    public void inject(Finder finder, T target, Object source) {
        target.tvStatus = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_status, "field 'tvStatus'"), R.id.tv_status, "field 'tvStatus'");
        target.tvNote = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_note, "field 'tvNote'"), R.id.tv_note, "field 'tvNote'");
        target.llDetail = (LinearLayout) finder.castView((View) finder.findRequiredView(source, R
                .id.ll_detail, "field 'llDetail'"), R.id.ll_detail, "field 'llDetail'");
        target.tvCommit = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_commit, "field 'tvCommit'"), R.id.tv_commit, "field 'tvCommit'");
        target.llUpdate = (LinearLayout) finder.castView((View) finder.findRequiredView(source, R
                .id.ll_update, "field 'llUpdate'"), R.id.ll_update, "field 'llUpdate'");
        target.etShipment = (EditText) finder.castView((View) finder.findRequiredView(source, R
                .id.et_shipment, "field 'etShipment'"), R.id.et_shipment, "field 'etShipment'");
        target.etAccount = (EditText) finder.castView((View) finder.findRequiredView(source, R.id
                .et_account, "field 'etAccount'"), R.id.et_account, "field 'etAccount'");
        target.ivShipmentLine = (View) finder.findRequiredView(source, R.id.iv_shipment_line,
                "field 'ivShipmentLine'");
    }

    public void reset(T target) {
        target.tvStatus = null;
        target.tvNote = null;
        target.llDetail = null;
        target.tvCommit = null;
        target.llUpdate = null;
        target.etShipment = null;
        target.etAccount = null;
        target.ivShipmentLine = null;
    }
}
