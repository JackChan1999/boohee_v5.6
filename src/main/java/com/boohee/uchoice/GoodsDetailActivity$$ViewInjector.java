package com.boohee.uchoice;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;
import butterknife.internal.DebouncingOnClickListener;

import com.boohee.one.R;

public class GoodsDetailActivity$$ViewInjector<T extends GoodsDetailActivity> implements
        Injector<T> {
    public void inject(Finder finder, final T target, Object source) {
        target.viewpager = (ViewPager) finder.castView((View) finder.findRequiredView(source, R
                .id.viewpager, "field 'viewpager'"), R.id.viewpager, "field 'viewpager'");
        View view = (View) finder.findRequiredView(source, R.id.btn_cart_add, "field " +
                "'btn_cart_add' and method 'onClick'");
        target.btn_cart_add = (Button) finder.castView(view, R.id.btn_cart_add, "field " +
                "'btn_cart_add'");
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        view = (View) finder.findRequiredView(source, R.id.btn_buy_immediately, "field " +
                "'btn_buy_immediately' and method 'onClick'");
        target.btn_buy_immediately = (Button) finder.castView(view, R.id.btn_buy_immediately,
                "field 'btn_buy_immediately'");
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        target.iv_shopping_cart = (ImageView) finder.castView((View) finder.findRequiredView
                (source, R.id.iv_shopping_cart, "field 'iv_shopping_cart'"), R.id
                .iv_shopping_cart, "field 'iv_shopping_cart'");
        target.viewBuy = (LinearLayout) finder.castView((View) finder.findRequiredView(source, R
                .id.view_buy, "field 'viewBuy'"), R.id.view_buy, "field 'viewBuy'");
        view = (View) finder.findRequiredView(source, R.id.view_cart, "field 'viewCart' and " +
                "method 'onClick'");
        target.viewCart = (ImageView) finder.castView(view, R.id.view_cart, "field 'viewCart'");
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        ((View) finder.findRequiredView(source, R.id.btn_buy_now, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        ((View) finder.findRequiredView(source, R.id.view_contact_us, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
    }

    public void reset(T target) {
        target.viewpager = null;
        target.btn_cart_add = null;
        target.btn_buy_immediately = null;
        target.iv_shopping_cart = null;
        target.viewBuy = null;
        target.viewCart = null;
    }
}
