package com.boohee.uchoice;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boohee.model.Goods;
import com.boohee.one.R;
import com.boohee.utility.DensityUtil;
import com.boohee.utility.ImageLoaderOptions;
import com.boohee.utils.TextUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class ShopTopicAdapter extends BaseAdapter implements OnClickListener {
    private Context mContext;
    private final List<DoubleGoodsItem> mDataList = new ArrayList();
    private List<Goods> mGoodsList;
    private final ImageLoader         mLoader         = ImageLoader.getInstance();
    private final DisplayImageOptions mOptions_normal = ImageLoaderOptions.global((int) R
            .drawable.aa3);
    private final DisplayImageOptions mOptions_small  = ImageLoaderOptions.global((int) R
            .drawable.aa3);
    private int mWidth;

    class DoubleGoodsItem {
        public Goods goods1;
        public Goods goods2;

        DoubleGoodsItem() {
        }
    }

    static class ViewHolder {
        public ImageView    iv_flag_1;
        public ImageView    iv_flag_2;
        public ImageView    iv_photo_1;
        public ImageView    iv_photo_2;
        public TextView     tv_base_price_1;
        public TextView     tv_base_price_2;
        public TextView     tv_market_price_1;
        public TextView     tv_market_price_2;
        public TextView     tv_title_1;
        public TextView     tv_title_2;
        public LinearLayout view_goods_1;
        public LinearLayout view_goods_2;

        ViewHolder() {
        }
    }

    public ShopTopicAdapter(Context context, List goodsList) {
        this.mContext = context;
        this.mWidth = (context.getResources().getDisplayMetrics().widthPixels - (DensityUtil
                .dip2px(context, 16.0f) * 3)) / 2;
        this.mGoodsList = goodsList;
        single2Double(this.mGoodsList, this.mDataList);
    }

    public void notifyDataSetChanged() {
        single2Double(this.mGoodsList, this.mDataList);
        super.notifyDataSetChanged();
    }

    public int getCount() {
        return this.mDataList.size();
    }

    public Object getItem(int position) {
        return Integer.valueOf(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(this.mContext).inflate(R.layout.nr, null);
            holder.view_goods_1 = (LinearLayout) convertView.findViewById(R.id.view_goods_1);
            holder.view_goods_2 = (LinearLayout) convertView.findViewById(R.id.view_goods_2);
            holder.iv_photo_1 = (ImageView) convertView.findViewById(R.id.iv_photo_1);
            holder.iv_photo_2 = (ImageView) convertView.findViewById(R.id.iv_photo_2);
            holder.iv_flag_1 = (ImageView) convertView.findViewById(R.id.iv_flag_1);
            holder.iv_flag_2 = (ImageView) convertView.findViewById(R.id.iv_flag_2);
            holder.tv_title_1 = (TextView) convertView.findViewById(R.id.tv_title_1);
            holder.tv_title_2 = (TextView) convertView.findViewById(R.id.tv_title_2);
            holder.tv_base_price_1 = (TextView) convertView.findViewById(R.id.tv_base_price_1);
            holder.tv_base_price_2 = (TextView) convertView.findViewById(R.id.tv_base_price_2);
            holder.tv_market_price_1 = (TextView) convertView.findViewById(R.id.tv_market_price_1);
            holder.tv_market_price_2 = (TextView) convertView.findViewById(R.id.tv_market_price_2);
            convertView.setTag(holder);
            holder.iv_photo_1.getLayoutParams().height = this.mWidth;
            holder.iv_photo_2.getLayoutParams().height = this.mWidth;
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        DoubleGoodsItem items = (DoubleGoodsItem) this.mDataList.get(position);
        Goods goods1 = items.goods1;
        if (goods1 != null) {
            String basePrice1 = goods1.base_price;
            String marketPrice1 = goods1.market_price;
            holder.view_goods_1.setTag(goods1);
            holder.tv_title_1.setText(goods1.title);
            holder.tv_base_price_1.setText(String.format("￥ %s", new Object[]{basePrice1}));
            if (TextUtils.equals(basePrice1, marketPrice1)) {
                holder.tv_market_price_1.setVisibility(8);
            } else {
                holder.tv_market_price_1.setVisibility(0);
                holder.tv_market_price_1.setText(String.format("￥ %s", new Object[]{marketPrice1}));
                holder.tv_market_price_1.getPaint().setFlags(16);
            }
            this.mLoader.displayImage(goods1.big_photo_url, holder.iv_photo_1, this
                    .mOptions_normal);
            if (TextUtil.isEmpty(goods1.flag_url)) {
                holder.iv_flag_1.setVisibility(8);
            } else {
                this.mLoader.displayImage(goods1.flag_url, holder.iv_flag_1, this.mOptions_small);
                holder.iv_flag_1.setVisibility(0);
            }
            holder.view_goods_1.setOnClickListener(this);
        }
        Goods goods2 = items.goods2;
        if (goods2 != null) {
            String basePrice2 = goods2.base_price;
            String marketPrice2 = goods2.market_price;
            holder.view_goods_2.setTag(goods2);
            holder.view_goods_2.setOnClickListener(this);
            holder.view_goods_2.setVisibility(0);
            holder.tv_title_2.setText(goods2.title);
            holder.tv_base_price_2.setText(String.format("￥ %s", new Object[]{basePrice2}));
            if (TextUtils.equals(basePrice2, marketPrice2)) {
                holder.tv_market_price_2.setVisibility(8);
            } else {
                holder.tv_market_price_2.setVisibility(0);
                holder.tv_market_price_2.setText(String.format("￥ %s", new Object[]{marketPrice2}));
                holder.tv_market_price_2.getPaint().setFlags(16);
            }
            this.mLoader.displayImage(goods2.big_photo_url, holder.iv_photo_2, this
                    .mOptions_normal);
            if (TextUtil.isEmpty(goods2.flag_url)) {
                holder.iv_flag_2.setVisibility(8);
            } else {
                this.mLoader.displayImage(goods2.flag_url, holder.iv_flag_2, this.mOptions_small);
                holder.iv_flag_2.setVisibility(0);
            }
        } else {
            holder.view_goods_2.setOnClickListener(null);
            holder.view_goods_2.setTag(null);
            holder.view_goods_2.setVisibility(4);
        }
        return convertView;
    }

    private void setImageHeight(Context context, ImageView view) {
        view.getLayoutParams().height = this.mWidth;
    }

    private void single2Double(List<Goods> goodsList, List<DoubleGoodsItem> dataList) {
        dataList.clear();
        int count = goodsList.size();
        for (int i = 0; i < count; i += 2) {
            DoubleGoodsItem item = new DoubleGoodsItem();
            item.goods1 = (Goods) goodsList.get(i);
            if (i + 1 < count) {
                item.goods2 = (Goods) goodsList.get(i + 1);
            }
            dataList.add(item);
        }
    }

    public void onClick(View v) {
        GoodsDetailActivity.comeOnBaby(this.mContext, ((Goods) v.getTag()).id);
    }
}
