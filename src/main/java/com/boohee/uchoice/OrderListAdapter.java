package com.boohee.uchoice;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog.Builder;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import com.boohee.api.ShopApi;
import com.boohee.database.UserPreference;
import com.boohee.model.Goods.goods_type;
import com.boohee.model.NiceOrder;
import com.boohee.model.Order;
import com.boohee.model.OrderItems;
import com.boohee.model.RecipeOrder;
import com.boohee.model.UchoiceOrder;
import com.boohee.one.R;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.http.client.BooheeClient;
import com.boohee.one.ui.BaseActivity;
import com.boohee.one.ui.BrowserActivity;
import com.boohee.one.ui.fragment.OrderListFragment.StateType;
import com.boohee.utility.DensityUtil;
import com.boohee.utility.Event;
import com.boohee.utility.ImageLoaderOptions;
import com.boohee.utils.DateHelper;
import com.boohee.utils.Helper;
import com.boohee.utils.ShopUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

import org.json.JSONObject;

public class OrderListAdapter extends BaseAdapter {
    static final String TAG = OrderListAdapter.class.getName();
    private BaseActivity activity;
    private Context      ctx;
    private int          itemSize;
    private ImageLoader loader = ImageLoader.getInstance();
    private ListView      mListView;
    private onPayListener onPayListener;
    private List<Order>   orders;

    public interface onPayListener {
        void payListener(int i, String str);
    }

    public class PayclickListener implements OnClickListener {
        boolean isRecipe;
        String  mSuccessUrl;
        int     order_id;

        public PayclickListener(int order_id, String successUrl, boolean isRecipe) {
            this.order_id = order_id;
            this.isRecipe = isRecipe;
            this.mSuccessUrl = successUrl;
        }

        public void onClick(View v) {
            if (!this.isRecipe) {
                MobclickAgent.onEvent(OrderListAdapter.this.ctx, Event.SHOP_CLICK_PAYMENT);
            }
            OrderListAdapter.this.onPayListener.payListener(this.order_id, this.mSuccessUrl);
        }
    }

    static class ViewHolder {
        public View         bottom_bar;
        public View         bottom_submit;
        public Button       bt_submit;
        public TextView     cancelOrder;
        public TextView     createdTime;
        public LinearLayout image_list_layout;
        public TextView     orderNum;
        public TextView     orderState;
        public Button       payBtn;
        public TextView     price;
        public TextView     title;
        public TextView     tv_carriage;
        public TextView     tv_description;

        ViewHolder() {
        }
    }

    public class cancelOrderListener implements OnClickListener {
        int         order_id;
        RecipeOrder recipeOrder;

        public cancelOrderListener(int order_id, RecipeOrder recipeOrder) {
            this.order_id = order_id;
            this.recipeOrder = recipeOrder;
        }

        public void onClick(View v) {
            OrderListAdapter.this.activity.showLoading();
            ShopApi.cancelOrder(this.order_id, OrderListAdapter.this.activity, new JsonCallback
                    (OrderListAdapter.this.activity) {
                public void ok(JSONObject object) {
                    super.ok(object);
                    OrderListAdapter.this.orders.remove(cancelOrderListener.this.recipeOrder);
                    OrderListAdapter.this.notifyDataSetChanged();
                    Helper.showToast((int) R.string.eu);
                }

                public void onFinish() {
                    super.onFinish();
                    OrderListAdapter.this.activity.dismissLoading();
                }
            });
        }
    }

    private class shipmentDetailsListener implements OnClickListener {
        String url;

        public shipmentDetailsListener(String url) {
            this.url = url;
        }

        public void onClick(View v) {
            Intent intent = new Intent(OrderListAdapter.this.ctx, BrowserActivity.class);
            intent.putExtra("url", this.url);
            intent.putExtra("title", OrderListAdapter.this.ctx.getString(R.string.a4z));
            OrderListAdapter.this.ctx.startActivity(intent);
        }
    }

    public OrderListAdapter(Context ctx, List<Order> orders, BaseActivity activity, ListView
            listView) {
        this.ctx = ctx;
        this.orders = orders;
        this.activity = activity;
        this.mListView = listView;
    }

    public int getCount() {
        return this.orders.size();
    }

    public Object getItem(int position) {
        return this.orders.get(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(this.ctx).inflate(R.layout.np, null);
            holder = new ViewHolder();
            holder.orderNum = (TextView) convertView.findViewById(R.id.order_num_value);
            holder.createdTime = (TextView) convertView.findViewById(R.id.created_at_value);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.image_list_layout = (LinearLayout) convertView.findViewById(R.id.image_list);
            holder.price = (TextView) convertView.findViewById(R.id.price_all_value);
            holder.payBtn = (Button) convertView.findViewById(R.id.pay_btn);
            holder.orderState = (TextView) convertView.findViewById(R.id.order_state_value);
            holder.cancelOrder = (TextView) convertView.findViewById(R.id.cancel_order);
            holder.tv_description = (TextView) convertView.findViewById(R.id.tv_description);
            holder.tv_carriage = (TextView) convertView.findViewById(R.id.tv_carriage);
            holder.bottom_bar = convertView.findViewById(R.id.bottom_bar);
            holder.bottom_submit = convertView.findViewById(R.id.bottom_submit);
            holder.bt_submit = (Button) convertView.findViewById(R.id.bt_submit);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (((Order) this.orders.get(position)).type.equals("GoodsOrder")) {
            setUchoiceOderView((UchoiceOrder) this.orders.get(position), holder, convertView,
                    position);
        } else if (((Order) this.orders.get(position)).type.equals("RecipeOrder")) {
            setRecipeOrderView((RecipeOrder) getItem(position), holder);
        } else if (((Order) this.orders.get(position)).type.equals("NiceOrder")) {
            setRecipeOrderView((NiceOrder) getItem(position), holder);
        }
        return convertView;
    }

    public void setUchoiceOderView(final UchoiceOrder orderUchoice, ViewHolder holder, View
            convertView, final int position) {
        holder.cancelOrder.setVisibility(8);
        holder.orderNum.setText(String.valueOf(orderUchoice.order_no));
        holder.createdTime.setText(DateHelper.timezoneFormat(orderUchoice.created_at, "yyyy-MM-dd" +
                "  HH:mm:ss"));
        holder.price.setText(this.ctx.getString(R.string.ae4) + String.valueOf(orderUchoice.price));
        holder.orderState.setText(orderUchoice.state_text);
        holder.image_list_layout.removeAllViews();
        this.itemSize = orderUchoice.order_items.size();
        if (this.itemSize != 0) {
            for (int i = 0; i < this.itemSize; i++) {
                ImageView image = generateImageView();
                this.loader.displayImage(((OrderItems) orderUchoice.order_items.get(i)).goods
                        .thumb_photo_url, image, ImageLoaderOptions.global((int) R.drawable.aa3));
                if (i < 3) {
                    holder.image_list_layout.addView(image);
                }
            }
            if (this.itemSize == 1) {
                if (TextUtils.equals(goods_type.SpecGoods.name(), ((OrderItems) orderUchoice
                        .order_items.get(0)).goods.type)) {
                    holder.title.setText(((OrderItems) orderUchoice.order_items.get(0)).goods
                            .title);
                } else {
                    holder.title.setText(((OrderItems) orderUchoice.order_items.get(0)).goods
                            .title + ShopUtils.getFormatInfo(((OrderItems) orderUchoice
                            .order_items.get(0)).goods.chosen_specs));
                }
                holder.title.setTextColor(this.ctx.getResources().getColor(R.color.e4));
                holder.title.setLines(2);
                holder.title.setEllipsize(TruncateAt.valueOf("END"));
            } else {
                holder.title.setText(". . .");
                holder.title.setLines(1);
                holder.title.setTextColor(-7829368);
            }
            if (orderUchoice.state.equals("initial") || orderUchoice.state.equals(UchoiceOrder
                    .SENT)) {
                holder.payBtn.setVisibility(0);
                if (orderUchoice.state.equals("initial")) {
                    holder.payBtn.setOnClickListener(new PayclickListener(orderUchoice.id,
                            orderUchoice.suc_url, false));
                    holder.payBtn.setText(this.ctx.getResources().getString(R.string.yz));
                } else {
                    holder.payBtn.setText(this.ctx.getResources().getString(R.string.a4z));
                    holder.payBtn.setOnClickListener(new shipmentDetailsListener(BooheeClient
                            .build(BooheeClient.ONE).getDefaultURL("/api/v1/orders/" +
                                    orderUchoice.id + "/shipment?token=" + UserPreference
                                    .getToken(this.ctx))));
                }
            } else {
                holder.payBtn.setVisibility(8);
            }
            if (orderUchoice.order_items.size() > 1) {
                holder.tv_description.setVisibility(0);
                holder.tv_description.setText(String.format("共 %d 件商品", new Object[]{Integer
                        .valueOf(orderUchoice.order_items.size())}));
            } else {
                holder.tv_description.setVisibility(4);
            }
            holder.tv_carriage.setText(String.format("(包含邮费 %d 元)", new Object[]{Integer.valueOf(
                    (int) orderUchoice.carriage)}));
            holder.bottom_bar.setVisibility(8);
            holder.cancelOrder.setVisibility(8);
            holder.bottom_submit.setVisibility(8);
            String state = orderUchoice.state;
            if (state.equals(StateType.initial.name())) {
                holder.bottom_bar.setVisibility(0);
            } else if (!state.equals(StateType.payed.name())) {
                if (state.equals(StateType.sent.name())) {
                    holder.bt_submit.setOnClickListener(new OnClickListener() {
                        public void onClick(View v) {
                            OrderListAdapter.this.submitOrder(orderUchoice.id, position);
                        }
                    });
                } else {
                    holder.bottom_bar.setVisibility(8);
                }
            }
        }
    }

    private void submitOrder(final int orderId, final int position) {
        new Builder(this.ctx).setTitle((CharSequence) "确认收货").setMessage((CharSequence)
                "是否确认收货？").setPositiveButton((CharSequence) "确定", new DialogInterface
                .OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                ShopApi.finishOrder(orderId, OrderListAdapter.this.ctx, new JsonCallback
                        (OrderListAdapter.this.ctx) {
                    public void onFinish() {
                        OrderListAdapter.this.orders.remove(position);
                        OrderListAdapter.this.notifyDataSetChanged();
                    }

                    public void fail(String message) {
                        Helper.showToast((CharSequence) message);
                    }
                });
            }
        }).setNegativeButton((CharSequence) "取消", null).show();
    }

    public void setRecipeOrderView(RecipeOrder orderRecipe, ViewHolder holder) {
        if (orderRecipe instanceof NiceOrder) {
            holder.title.setText(String.format("%s %d个月", new Object[]{orderRecipe.combo.title,
                    Integer.valueOf(orderRecipe.combo.month)}));
        } else {
            holder.title.setText(orderRecipe.combo.title);
        }
        holder.price.setText(this.ctx.getString(R.string.ae4) + orderRecipe.price);
        holder.orderNum.setText(orderRecipe.order_no);
        holder.createdTime.setText(DateHelper.parseStringFromZone(orderRecipe.created_at, Boolean
                .valueOf(true)));
        holder.image_list_layout.removeAllViews();
        ImageView image = generateImageView();
        if ("NiceOrder".equals(orderRecipe.type)) {
            image.setBackgroundResource(R.drawable.ql);
        } else {
            image.setBackgroundResource(R.drawable.mn);
        }
        holder.image_list_layout.addView(image);
        holder.title.setTextColor(this.ctx.getResources().getColor(R.color.e4));
        holder.orderState.setText(orderRecipe.state_text);
        holder.bottom_submit.setVisibility(8);
        if (orderRecipe.isNeedPay()) {
            holder.bottom_bar.setVisibility(0);
            holder.cancelOrder.setVisibility(0);
            holder.payBtn.setVisibility(0);
            holder.payBtn.setText(this.ctx.getResources().getString(R.string.yz));
            holder.payBtn.setOnClickListener(new PayclickListener(orderRecipe.id, orderRecipe
                    .suc_url, true));
            holder.cancelOrder.setOnClickListener(new cancelOrderListener(orderRecipe.id,
                    orderRecipe));
            return;
        }
        holder.bottom_bar.setVisibility(8);
        if (orderRecipe.contract != null && orderRecipe.contract.end_at != null) {
            if (RecipeOrder.isExpired(orderRecipe.contract.end_at)) {
                holder.orderState.setText(this.ctx.getString(R.string.yi) + orderRecipe.period());
            } else {
                holder.orderState.setText(this.ctx.getString(R.string.oy) + orderRecipe.period());
            }
        }
    }

    private ImageView generateImageView() {
        ImageView image = new ImageView(this.ctx);
        LayoutParams params = new LayoutParams(DensityUtil.dip2px(this.ctx, 72.0f), DensityUtil
                .dip2px(this.ctx, 72.0f));
        params.gravity = 16;
        params.rightMargin = 10;
        image.setLayoutParams(params);
        return image;
    }

    public void setOnPayListener(onPayListener payListener) {
        this.onPayListener = payListener;
    }
}
