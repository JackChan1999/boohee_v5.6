package com.boohee.uchoice;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

import com.boohee.one.R;

public class OrderDetailAdapter$ViewHolder$$ViewInjector<T extends ViewHolder> implements
        Injector<T> {
    public void inject(Finder finder, T target, Object source) {
        target.thumbPhoto = (ImageView) finder.castView((View) finder.findRequiredView(source, R
                .id.thumb_photo, "field 'thumbPhoto'"), R.id.thumb_photo, "field 'thumbPhoto'");
        target.title = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .title, "field 'title'"), R.id.title, "field 'title'");
        target.priceValue = (TextView) finder.castView((View) finder.findRequiredView(source, R
                .id.price_value, "field 'priceValue'"), R.id.price_value, "field 'priceValue'");
        target.quantityValue = (TextView) finder.castView((View) finder.findRequiredView(source,
                R.id.quantity_value, "field 'quantityValue'"), R.id.quantity_value, "field " +
                "'quantityValue'");
        target.tvUnshipped = (TextView) finder.castView((View) finder.findRequiredView(source, R
                .id.tv_unshipped, "field 'tvUnshipped'"), R.id.tv_unshipped, "field 'tvUnshipped'");
        target.tvGoodsReturn = (TextView) finder.castView((View) finder.findRequiredView(source,
                R.id.tv_goods_return, "field 'tvGoodsReturn'"), R.id.tv_goods_return, "field " +
                "'tvGoodsReturn'");
        target.tvShipmentDetails = (Button) finder.castView((View) finder.findRequiredView
                (source, R.id.tv_shipment_details, "field 'tvShipmentDetails'"), R.id
                .tv_shipment_details, "field 'tvShipmentDetails'");
        target.rlShip = (RelativeLayout) finder.castView((View) finder.findRequiredView(source, R
                .id.rl_ship, "field 'rlShip'"), R.id.rl_ship, "field 'rlShip'");
    }

    public void reset(T target) {
        target.thumbPhoto = null;
        target.title = null;
        target.priceValue = null;
        target.quantityValue = null;
        target.tvUnshipped = null;
        target.tvGoodsReturn = null;
        target.tvShipmentDetails = null;
        target.rlShip = null;
    }
}
