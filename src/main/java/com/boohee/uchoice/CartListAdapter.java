package com.boohee.uchoice;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.boohee.model.CartGoods;
import com.boohee.model.Goods.goods_type;
import com.boohee.one.R;
import com.boohee.one.ui.adapter.SimpleBaseAdapter;
import com.boohee.one.ui.adapter.SimpleBaseAdapter.ViewHolder;
import com.boohee.utility.ImageLoaderOptions;
import com.boohee.utils.ShopUtils;
import com.boohee.utils.TextUtil;
import com.boohee.utils.ViewUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class CartListAdapter extends SimpleBaseAdapter {
    static final String TAG = CartListAdapter.class.getName();
    private Context                    ctx;
    public  onEditGoodQuantityListener editGoodQuantityListener;
    public  List<CartGoods>            goodsList;
    private ImageLoader         loader  = ImageLoader.getInstance();
    private DisplayImageOptions options = ImageLoaderOptions.global((int) R.drawable.aa3);

    public interface onEditGoodQuantityListener {
        void editGoodQuantity(int i, int i2);
    }

    private class onCreaseClickListener implements OnClickListener {
        Button    btn_crease;
        CartGoods goods;
        TextView  tv_goods_num;

        public onCreaseClickListener(TextView tv_goods_num, Button btn_crease, CartGoods goods) {
            this.tv_goods_num = tv_goods_num;
            this.btn_crease = btn_crease;
            this.goods = goods;
        }

        public void onClick(View v) {
            if (!ViewUtils.isFastDoubleClick()) {
                onEditGoodQuantityListener
                        com_boohee_uchoice_CartListAdapter_onEditGoodQuantityListener;
                int i;
                CartGoods cartGoods;
                int i2;
                switch (v.getId()) {
                    case R.id.btn_decrease:
                        if (Integer.parseInt(String.valueOf(this.tv_goods_num.getText())) > 1) {
                            if (CartListAdapter.this.editGoodQuantityListener != null) {
                                com_boohee_uchoice_CartListAdapter_onEditGoodQuantityListener =
                                        CartListAdapter.this.editGoodQuantityListener;
                                i = this.goods.goods_id;
                                cartGoods = this.goods;
                                i2 = cartGoods.quantity - 1;
                                cartGoods.quantity = i2;
                                com_boohee_uchoice_CartListAdapter_onEditGoodQuantityListener
                                        .editGoodQuantity(i, i2);
                            }
                            if (Integer.parseInt(String.valueOf(this.tv_goods_num.getText())) ==
                                    1) {
                                this.btn_crease.setEnabled(false);
                                this.btn_crease.setClickable(false);
                                return;
                            }
                            return;
                        }
                        return;
                    case R.id.btn_increase:
                        if (CartListAdapter.this.editGoodQuantityListener != null) {
                            com_boohee_uchoice_CartListAdapter_onEditGoodQuantityListener =
                                    CartListAdapter.this.editGoodQuantityListener;
                            i = this.goods.goods_id;
                            cartGoods = this.goods;
                            i2 = cartGoods.quantity + 1;
                            cartGoods.quantity = i2;
                            com_boohee_uchoice_CartListAdapter_onEditGoodQuantityListener
                                    .editGoodQuantity(i, i2);
                        }
                        this.btn_crease.setEnabled(true);
                        this.btn_crease.setClickable(true);
                        return;
                    default:
                        return;
                }
            }
        }
    }

    public CartListAdapter(Context ctx, List<CartGoods> goodsList) {
        super(ctx, goodsList);
        this.ctx = ctx;
        this.goodsList = goodsList;
    }

    public int getItemResource() {
        return R.layout.ng;
    }

    public View getItemView(int position, View convertView, ViewHolder holder) {
        Boolean isEdit = CartActivity.isEdit;
        CartGoods goods = (CartGoods) this.goodsList.get(position);
        RelativeLayout rl_title = (RelativeLayout) holder.getView(R.id.rl_title);
        LinearLayout ll_edit = (LinearLayout) holder.getView(R.id.ll_edit);
        TextView tv_goods_state = (TextView) holder.getView(R.id.tv_goods_state);
        ToggleButton checkBox = (ToggleButton) holder.getView(R.id.tb_cart);
        ImageView iv_goods = (ImageView) holder.getView(R.id.iv_goods);
        TextView tv_goods_title = (TextView) holder.getView(R.id.tv_goods_title);
        TextView tv_price_value = (TextView) holder.getView(R.id.tv_price_value);
        Button btn_decrease = (Button) holder.getView(R.id.btn_decrease);
        Button btn_increase = (Button) holder.getView(R.id.btn_increase);
        TextView tv_goods_num = (TextView) holder.getView(R.id.tv_goods_num);
        checkBox.setChecked(goods.isChecked.booleanValue());
        this.loader.displayImage(goods.thumb_photo_url, iv_goods, this.options);
        if (TextUtils.equals(goods_type.SpecGoods.name(), goods.type)) {
            tv_goods_title.setText(goods.title);
        } else {
            tv_goods_title.setText(goods.title + ShopUtils.getFormatInfo(goods.chosen_specs));
        }
        tv_price_value.setText(this.ctx.getString(R.string.a04) + "  " + this.ctx.getString(R
                .string.ae4) + TextUtil.m2(goods.base_price));
        tv_goods_num.setText(String.valueOf(goods.quantity));
        btn_decrease.setOnClickListener(new onCreaseClickListener(tv_goods_num, btn_decrease,
                goods));
        btn_increase.setOnClickListener(new onCreaseClickListener(tv_goods_num, btn_increase,
                goods));
        if (goods.quantity > 1) {
            btn_decrease.setEnabled(true);
            btn_decrease.setBackgroundResource(R.drawable.ij);
        } else {
            btn_decrease.setEnabled(false);
        }
        if (isEdit.booleanValue()) {
            checkBox.setVisibility(0);
            btn_decrease.setVisibility(8);
            btn_increase.setVisibility(8);
        } else {
            checkBox.setVisibility(8);
            btn_decrease.setVisibility(0);
            btn_increase.setVisibility(0);
        }
        if (goods.can_sale) {
            tv_price_value.setVisibility(0);
            tv_goods_state.setVisibility(8);
            ll_edit.setVisibility(0);
            rl_title.setAlpha(1.0f);
        } else {
            if (!TextUtils.isEmpty(goods.msg)) {
                tv_goods_state.setText(goods.msg);
            }
            tv_price_value.setVisibility(8);
            tv_goods_state.setVisibility(0);
            ll_edit.setVisibility(8);
            rl_title.setAlpha(0.5f);
        }
        return convertView;
    }

    public void setEditGoodQuantityListener(onEditGoodQuantityListener editGoodQuantityListener) {
        this.editGoodQuantityListener = editGoodQuantityListener;
    }
}
