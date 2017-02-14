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

import butterknife.ButterKnife;
import butterknife.InjectView;

import com.boohee.model.Goods.goods_type;
import com.boohee.model.OrderItems;
import com.boohee.model.Shipments;
import com.boohee.model.UchoiceOrder;
import com.boohee.one.R;
import com.boohee.one.ui.BrowserActivity;
import com.boohee.utility.ImageLoaderOptions;
import com.boohee.utils.ShopUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class OrderDetailAdapter extends BaseAdapter {
    public static final String TAG = OrderDetailAdapter.class.getName();
    private Context          ctx;
    private List<OrderItems> itemsList;
    private ImageLoader              loader         = ImageLoader.getInstance();
    private Map<Integer, OrderItems> mOrderItemsMap = new HashMap();
    private List<Shipments>          mShipmentses   = new ArrayList();
    private UchoiceOrder orderUchoice;
    private Map<Integer, Shipments> shipmentsMap = new HashMap();

    static class ViewHolder {
        @InjectView(2131428498)
        TextView       priceValue;
        @InjectView(2131428499)
        TextView       quantityValue;
        @InjectView(2131428500)
        RelativeLayout rlShip;
        @InjectView(2131428497)
        ImageView      thumbPhoto;
        @InjectView(2131427371)
        TextView       title;
        @InjectView(2131428502)
        TextView       tvGoodsReturn;
        @InjectView(2131428503)
        Button         tvShipmentDetails;
        @InjectView(2131428501)
        TextView       tvUnshipped;

        ViewHolder(View view) {
            ButterKnife.inject((Object) this, view);
        }
    }

    public OrderDetailAdapter(Context ctx, UchoiceOrder orderUchoice) {
        this.ctx = ctx;
        this.orderUchoice = orderUchoice;
        processOrder();
    }

    private void processOrder() {
        if (this.orderUchoice != null) {
            this.shipmentsMap.clear();
            this.itemsList = this.orderUchoice.order_items;
            if (this.orderUchoice.shipments != null) {
                Iterator it = this.orderUchoice.shipments.iterator();
                while (it.hasNext()) {
                    Shipments shipment = (Shipments) it.next();
                    if (shipment.goods_order_item_ids != null) {
                        for (int goodsId : shipment.goods_order_item_ids) {
                            this.shipmentsMap.put(Integer.valueOf(goodsId), shipment);
                        }
                    }
                }
            }
        }
    }

    public void refreshOrder(UchoiceOrder orderUchoice) {
        this.orderUchoice = orderUchoice;
        processOrder();
        notifyDataSetChanged();
    }

    public int getCount() {
        return this.itemsList == null ? 0 : this.itemsList.size();
    }

    public Object getItem(int position) {
        return this.itemsList.get(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        final OrderItems orderItems = (OrderItems) this.itemsList.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(this.ctx).inflate(R.layout.ie, null);
            convertView.setTag(new ViewHolder(convertView));
        }
        ViewHolder vh = (ViewHolder) convertView.getTag();
        if (TextUtils.equals(this.orderUchoice.state, UchoiceOrder.SENT) || TextUtils.equals(this
                .orderUchoice.state, UchoiceOrder.PART_SENT)) {
            vh.rlShip.setVisibility(0);
            showShipment(orderItems, vh);
            showRefund(orderItems, vh);
        } else if (TextUtils.equals(this.orderUchoice.state, "finished")) {
            vh.rlShip.setVisibility(0);
            showShipment(orderItems, vh);
            if (TextUtils.equals(orderItems.refund_state, "finished") || TextUtils.equals
                    (orderItems.refund_state, "payback")) {
                vh.tvGoodsReturn.setVisibility(0);
                vh.tvGoodsReturn.setText(orderItems.refundText());
                vh.tvGoodsReturn.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        GoodsReturnStatusActivity.startActivity(OrderDetailAdapter.this.ctx,
                                orderItems.rfl_id);
                    }
                });
            } else {
                vh.tvGoodsReturn.setVisibility(8);
            }
        } else {
            vh.rlShip.setVisibility(8);
        }
        if (TextUtils.equals(goods_type.SpecGoods.name(), orderItems.goods.type)) {
            vh.title.setText(orderItems.goods.title);
        } else {
            vh.title.setText(orderItems.goods.title + ShopUtils.getFormatInfo(orderItems.goods
                    .chosen_specs));
        }
        vh.quantityValue.setText(String.format("x %d", new Object[]{Integer.valueOf(orderItems
                .quantity)}));
        if (orderItems.base_price == 0.0f) {
            vh.priceValue.setText(this.ctx.getString(R.string.ae4) + orderItems.base_price);
        } else {
            vh.priceValue.setText(this.ctx.getString(R.string.ae4) + orderItems.goods.base_price);
        }
        this.loader.displayImage(orderItems.goods.thumb_photo_url, vh.thumbPhoto,
                ImageLoaderOptions.global((int) R.drawable.aa3));
        return convertView;
    }

    private void showShipment(OrderItems orderItems, ViewHolder vh) {
        final Shipments shipments = (Shipments) this.shipmentsMap.get(Integer.valueOf(orderItems
                .id));
        if (shipments == null) {
            vh.tvUnshipped.setVisibility(0);
            vh.tvShipmentDetails.setVisibility(8);
            vh.tvUnshipped.setText("未发货");
        } else if (TextUtils.isEmpty(shipments.url)) {
            vh.tvUnshipped.setVisibility(0);
            vh.tvShipmentDetails.setVisibility(8);
            vh.tvUnshipped.setText(this.ctx.getString(R.string.xy));
        } else {
            vh.tvUnshipped.setVisibility(8);
            vh.tvShipmentDetails.setVisibility(0);
            vh.tvShipmentDetails.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(OrderDetailAdapter.this.ctx, BrowserActivity.class);
                    intent.putExtra("url", shipments.url);
                    intent.putExtra("title", OrderDetailAdapter.this.ctx.getString(R.string.a4z));
                    OrderDetailAdapter.this.ctx.startActivity(intent);
                }
            });
        }
    }

    private boolean showRefund(final OrderItems orderItems, ViewHolder vh) {
        if (orderItems == null || vh == null) {
            return false;
        }
        Shipments shipments = (Shipments) this.shipmentsMap.get(Integer.valueOf(orderItems.id));
        if (((double) orderItems.base_price) < 1.0E-5d) {
            vh.tvGoodsReturn.setVisibility(8);
            return false;
        } else if ("can_be_refund".equals(orderItems.refund_state)) {
            if (shipments == null) {
                vh.tvGoodsReturn.setVisibility(8);
                return false;
            }
            vh.tvGoodsReturn.setVisibility(0);
            vh.tvGoodsReturn.setText("申请退货");
            vh.tvGoodsReturn.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    GoodsReturnApplyActivity.startActivity(OrderDetailAdapter.this.ctx,
                            OrderDetailAdapter.this.orderUchoice.id, orderItems.id);
                }
            });
            return true;
        } else if ("forbidden_refund".equals(orderItems.refund_state) || orderItems.refund_state
                == null) {
            vh.tvGoodsReturn.setVisibility(8);
            return false;
        } else {
            vh.tvGoodsReturn.setVisibility(0);
            vh.tvGoodsReturn.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    GoodsReturnStatusActivity.startActivity(OrderDetailAdapter.this.ctx,
                            orderItems.rfl_id);
                }
            });
            vh.tvGoodsReturn.setText(orderItems.refundText());
            return true;
        }
    }
}
