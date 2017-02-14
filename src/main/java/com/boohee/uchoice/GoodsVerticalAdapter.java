package com.boohee.uchoice;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boohee.model.Goods;
import com.boohee.one.R;
import com.boohee.one.ui.adapter.SimpleBaseAdapter;
import com.boohee.one.ui.adapter.SimpleBaseAdapter.ViewHolder;
import com.boohee.utility.DensityUtil;
import com.boohee.utility.ImageLoaderOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class GoodsVerticalAdapter extends SimpleBaseAdapter<Goods> {
    private int mWidth;

    public GoodsVerticalAdapter(Context context, List<Goods> data) {
        super(context, data);
        this.mWidth = (context.getResources().getDisplayMetrics().widthPixels - (DensityUtil
                .dip2px(context, 16.0f) * 3)) / 2;
    }

    public int getItemResource() {
        return R.layout.i5;
    }

    public View getItemView(int position, View convertView, ViewHolder holder) {
        final Goods goods = (Goods) this.data.get(position);
        LinearLayout view_goods_1 = (LinearLayout) holder.getView(R.id.view_goods_1);
        ImageView iv_photo_1 = (ImageView) holder.getView(R.id.iv_photo_1);
        ImageView iv_flag_1 = (ImageView) holder.getView(R.id.iv_flag_1);
        TextView tv_title_1 = (TextView) holder.getView(R.id.tv_title_1);
        TextView tv_base_price_1 = (TextView) holder.getView(R.id.tv_base_price_1);
        TextView tv_market_price_1 = (TextView) holder.getView(R.id.tv_market_price_1);
        iv_photo_1.getLayoutParams().height = this.mWidth;
        ImageLoader.getInstance().displayImage(goods.big_photo_url, iv_photo_1,
                ImageLoaderOptions.global((int) R.drawable.aa3));
        if (!TextUtils.isEmpty(goods.flag_url)) {
            ImageLoader.getInstance().displayImage(goods.flag_url, iv_flag_1, ImageLoaderOptions
                    .global((int) R.color.in));
        }
        tv_title_1.setText(goods.title);
        tv_base_price_1.setText(String.format("￥ %s", new Object[]{goods.base_price}));
        if (TextUtils.equals(goods.base_price, goods.market_price)) {
            tv_market_price_1.setVisibility(8);
        } else {
            tv_market_price_1.setVisibility(0);
            tv_market_price_1.setText(String.format("￥ %s", new Object[]{marketPrice}));
            tv_market_price_1.getPaint().setFlags(16);
        }
        view_goods_1.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (goods != null) {
                    GoodsDetailActivity.comeOnBaby(GoodsVerticalAdapter.this.context, goods.id);
                }
            }
        });
        return convertView;
    }
}
