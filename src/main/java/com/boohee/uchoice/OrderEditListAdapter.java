package com.boohee.uchoice;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.boohee.model.CartGoods;
import com.boohee.model.Goods.goods_type;
import com.boohee.one.R;
import com.boohee.utility.ImageLoaderOptions;
import com.boohee.utils.ShopUtils;
import com.boohee.utils.TextUtil;
import com.boohee.utils.ViewUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class OrderEditListAdapter extends BaseAdapter {
    static final String TAG = OrderEditListAdapter.class.getName();
    public  int             addPriceValue;
    private Context         ctx;
    public  List<CartGoods> goodsList;
    public Boolean isEdit = Boolean.valueOf(false);
    private CheckValueListener  listener;
    private ImageLoader         loader;
    private DisplayImageOptions options;

    public interface CheckValueListener {
        void onCheckValue(float f);
    }

    public class AddBtnClickListener implements OnClickListener {
        Button   decreseBtn;
        int      id;
        int      position;
        TextView quantity;
        float    totaPrice;

        public AddBtnClickListener(TextView quantity, Button decreseBtn, int position, float
                totaPrice, int id) {
            this.quantity = quantity;
            this.decreseBtn = decreseBtn;
            this.position = position;
            this.totaPrice = totaPrice;
            this.id = id;
        }

        public void onClick(View v) {
            if (!ViewUtils.isFastDoubleClick()) {
                TextView textView;
                CartGoods cartGoods;
                int i;
                switch (v.getId()) {
                    case R.id.into_goods_detail:
                        Intent intent = new Intent(OrderEditListAdapter.this.ctx,
                                GoodsDetailActivity.class);
                        intent.putExtra("goods_id", this.id);
                        OrderEditListAdapter.this.ctx.startActivity(intent);
                        return;
                    case R.id.increase_btn:
                        textView = this.quantity;
                        cartGoods = (CartGoods) OrderEditListAdapter.this.goodsList.get(this
                                .position);
                        i = cartGoods.quantity + 1;
                        cartGoods.quantity = i;
                        textView.setText(String.valueOf(i));
                        this.totaPrice = OrderEditListAdapter.this.calculate(OrderEditListAdapter
                                .this.goodsList);
                        if (OrderEditListAdapter.this.listener != null) {
                            OrderEditListAdapter.this.listener.onCheckValue(this.totaPrice);
                        }
                        this.decreseBtn.setClickable(true);
                        this.decreseBtn.setEnabled(true);
                        return;
                    case R.id.decrease_btn:
                        if (Integer.parseInt(String.valueOf(this.quantity.getText())) > 1) {
                            textView = this.quantity;
                            cartGoods = (CartGoods) OrderEditListAdapter.this.goodsList.get(this
                                    .position);
                            i = cartGoods.quantity - 1;
                            cartGoods.quantity = i;
                            textView.setText(String.valueOf(i));
                            this.totaPrice = OrderEditListAdapter.this.calculate
                                    (OrderEditListAdapter.this.goodsList);
                            if (OrderEditListAdapter.this.listener != null) {
                                OrderEditListAdapter.this.listener.onCheckValue(this.totaPrice);
                            }
                            if (Integer.parseInt(String.valueOf(this.quantity.getText())) == 1) {
                                this.decreseBtn.setClickable(false);
                                this.decreseBtn.setEnabled(false);
                                return;
                            }
                            return;
                        }
                        return;
                    default:
                        return;
                }
            }
        }
    }

    public OrderEditListAdapter(Context ctx, List<CartGoods> goodsList) {
        this.ctx = ctx;
        this.goodsList = goodsList;
        this.loader = ImageLoader.getInstance();
        this.options = ImageLoaderOptions.global((int) R.drawable.aa3);
    }

    public int getCount() {
        if (this.goodsList == null) {
            return 0;
        }
        return this.goodsList.size();
    }

    public Object getItem(int position) {
        return Integer.valueOf(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(this.ctx).inflate(R.layout.no, null);
        }
        Button decreseBtn = (Button) convertView.findViewById(R.id.decrease_btn);
        Button increaseBtn = (Button) convertView.findViewById(R.id.increase_btn);
        TextView quantity = (TextView) convertView.findViewById(R.id.quantity_value);
        TextView Title = (TextView) convertView.findViewById(R.id.title);
        TextView price = (TextView) convertView.findViewById(R.id.price_value);
        RelativeLayout intoGoodsDetails = (RelativeLayout) convertView.findViewById(R.id
                .into_goods_detail);
        CartGoods goods = (CartGoods) this.goodsList.get(position);
        quantity.setText(String.valueOf(goods.quantity));
        float priceVaule = goods.base_price;
        price.setText(this.ctx.getString(R.string.ae4) + TextUtil.m2(priceVaule));
        this.loader.displayImage(goods.thumb_photo_url, (ImageView) convertView.findViewById(R.id
                .default_photo), this.options);
        AddBtnClickListener buttonListener = new AddBtnClickListener(quantity, decreseBtn,
                position, priceVaule, goods.goods_id);
        intoGoodsDetails.setOnClickListener(buttonListener);
        decreseBtn.setVisibility(4);
        increaseBtn.setVisibility(4);
        decreseBtn.setOnClickListener(buttonListener);
        if (Integer.parseInt(String.valueOf(quantity.getText())) == 1) {
            decreseBtn.setClickable(false);
            decreseBtn.setEnabled(false);
        }
        increaseBtn.setOnClickListener(buttonListener);
        if (TextUtils.equals(goods_type.SpecGoods.name(), goods.type)) {
            Title.setText(goods.title);
        } else {
            Title.setText(goods.title + ShopUtils.getFormatInfo(goods.chosen_specs));
        }
        return convertView;
    }

    private float calculate(List<CartGoods> goods) {
        float priceAllValue = 0.0f;
        for (int i = 0; i < goods.size(); i++) {
            priceAllValue += ((float) ((CartGoods) goods.get(i)).quantity) * ((CartGoods) goods
                    .get(i)).base_price;
        }
        return priceAllValue;
    }

    public void setCheckValueListener(CheckValueListener listener) {
        this.listener = listener;
    }
}
