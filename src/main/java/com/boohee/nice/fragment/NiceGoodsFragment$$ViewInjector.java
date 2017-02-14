package com.boohee.nice.fragment;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;
import butterknife.internal.DebouncingOnClickListener;

import com.boohee.one.R;

public class NiceGoodsFragment$$ViewInjector<T extends NiceGoodsFragment> implements Injector<T> {
    public void inject(Finder finder, final T target, Object source) {
        target.tvNiceTitle = (TextView) finder.castView((View) finder.findRequiredView(source, R
                .id.tv_nice_title, "field 'tvNiceTitle'"), R.id.tv_nice_title, "field " +
                "'tvNiceTitle'");
        target.llCover = (LinearLayout) finder.castView((View) finder.findRequiredView(source, R
                .id.ll_cover, "field 'llCover'"), R.id.ll_cover, "field 'llCover'");
        target.tvDescOne = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_desc_one, "field 'tvDescOne'"), R.id.tv_desc_one, "field 'tvDescOne'");
        target.tvDescTwo = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_desc_two, "field 'tvDescTwo'"), R.id.tv_desc_two, "field 'tvDescTwo'");
        target.tvDescThree = (TextView) finder.castView((View) finder.findRequiredView(source, R
                .id.tv_desc_three, "field 'tvDescThree'"), R.id.tv_desc_three, "field " +
                "'tvDescThree'");
        target.tvDescFour = (TextView) finder.castView((View) finder.findRequiredView(source, R
                .id.tv_desc_four, "field 'tvDescFour'"), R.id.tv_desc_four, "field 'tvDescFour'");
        target.tvNicePrice = (TextView) finder.castView((View) finder.findRequiredView(source, R
                .id.tv_nice_price, "field 'tvNicePrice'"), R.id.tv_nice_price, "field " +
                "'tvNicePrice'");
        target.vLine = (View) finder.findRequiredView(source, R.id.v_line, "field 'vLine'");
        View view = (View) finder.findRequiredView(source, R.id.bt_buy, "field 'btBuy' and method" +
                " 'onClick'");
        target.btBuy = (Button) finder.castView(view, R.id.bt_buy, "field 'btBuy'");
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick();
            }
        });
        target.llBuyDesc = (LinearLayout) finder.castView((View) finder.findRequiredView(source,
                R.id.ll_buy_desc, "field 'llBuyDesc'"), R.id.ll_buy_desc, "field 'llBuyDesc'");
        target.tvRenewDesc = (TextView) finder.castView((View) finder.findRequiredView(source, R
                .id.tv_renew_desc, "field 'tvRenewDesc'"), R.id.tv_renew_desc, "field " +
                "'tvRenewDesc'");
    }

    public void reset(T target) {
        target.tvNiceTitle = null;
        target.llCover = null;
        target.tvDescOne = null;
        target.tvDescTwo = null;
        target.tvDescThree = null;
        target.tvDescFour = null;
        target.tvNicePrice = null;
        target.vLine = null;
        target.btBuy = null;
        target.llBuyDesc = null;
        target.tvRenewDesc = null;
    }
}
