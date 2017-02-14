package com.boohee.uchoice;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog.Builder;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.boohee.api.ShopApi;
import com.boohee.main.GestureActivity;
import com.boohee.model.CartGoods;
import com.boohee.model.OrderItems;
import com.boohee.model.UchoiceOrder;
import com.boohee.one.R;
import com.boohee.one.event.RefreshOrderEvent;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.pay.PayService;
import com.boohee.one.pay.PayService.OnFinishPayListener;
import com.boohee.utility.Event;
import com.boohee.utils.AppUtils;
import com.boohee.utils.DateHelper;
import com.boohee.utils.Helper;
import com.boohee.utils.NumberUtils;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.common.SocializeConstants;

import de.greenrobot.event.EventBus;

import java.text.DecimalFormat;
import java.util.Iterator;

import org.json.JSONObject;

public class OrderDetailsActivity extends GestureActivity implements OnClickListener,
        OnFinishPayListener {
    static final String TAG = OrderDetailsActivity.class.getName();
    private Button   btnPay;
    private Button   btnReceive;
    private Button   btnRefund;
    private TextView carriage;
    private TextView createdAt;
    private DecimalFormat dFormat = new DecimalFormat("0.0#");
    private View footerView;
    private View headerView;
    private int index = -1;
    private ImageView    ivStatus;
    private View         ll_alipay;
    private LinearLayout ll_bonus;
    private LinearLayout ll_coupon;
    private LinearLayout ll_pay_more;
    private LinearLayout ll_pay_type;
    private View         ll_upacp;
    private View         ll_wechat;
    private MenuItem     mMenuItem;
    private int mPayNum = 0;
    private PayService         mPayService;
    private String             mSuccessUrl;
    private OrderDetailAdapter orderDetailAdapter;
    private ListView           orderDetailList;
    private int                orderId;
    private TextView           orderNum;
    private TextView           orderState;
    private UchoiceOrder       orderUchoice;
    private String payType = "alipay";
    private RelativeLayout rlBottom;
    private TextView       supplement;
    private ToggleButton   tb_alipay;
    private ToggleButton   tb_upacp;
    private ToggleButton   tb_wechat;
    private TextView       tv_address_details;
    private TextView       tv_bonus;
    private TextView       tv_bonus_amount;
    private TextView       tv_coupon;
    private TextView       tv_coupon_amount;
    private TextView       tv_goods_price;
    private TextView       tv_phone;
    private TextView       tv_price_all;
    private TextView       tv_real_name;
    private TextView       tv_unpaid;

    static /* synthetic */ int access$104(OrderDetailsActivity x0) {
        int i = x0.mPayNum + 1;
        x0.mPayNum = i;
        return i;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ni);
        handleIntent();
        initView();
        getOrderDetails();
        EventBus.getDefault().register(this);
    }

    private void handleIntent() {
        this.orderId = getIntent().getIntExtra("order_id", 0);
        this.index = getIntent().getIntExtra("index", -1);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.o, menu);
        this.mMenuItem = menu.findItem(R.id.action_cancel_order);
        if (this.mMenuItem != null) {
            this.mMenuItem.setVisible(false);
        }
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_cancel_order:
                new Builder(this).setMessage((CharSequence) "确定要取消订单吗？").setPositiveButton(
                        (CharSequence) "确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        OrderDetailsActivity.this.cancelOrder();
                    }
                }).setNegativeButton((CharSequence) "取消", null).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initView() {
        this.orderDetailList = (ListView) findViewById(R.id.order_detail_list);
        this.btnPay = (Button) findViewById(R.id.btn_pay);
        this.btnReceive = (Button) findViewById(R.id.btn_receive);
        this.btnRefund = (Button) findViewById(R.id.btn_refund);
        this.tv_unpaid = (TextView) findViewById(R.id.tv_unpaid);
        this.rlBottom = (RelativeLayout) findViewById(R.id.rl_bottom);
        this.btnPay.setOnClickListener(this);
        this.btnRefund.setOnClickListener(this);
        this.btnReceive.setOnClickListener(this);
        this.headerView = LayoutInflater.from(this.ctx).inflate(R.layout.nk, null);
        this.footerView = LayoutInflater.from(this.ctx).inflate(R.layout.nj, null);
    }

    private void updateHeaderView(View headerView) {
        this.orderNum = (TextView) headerView.findViewById(R.id.order_num_value);
        this.createdAt = (TextView) headerView.findViewById(R.id.created_at_value);
        this.orderState = (TextView) headerView.findViewById(R.id.order_state_value);
        this.tv_real_name = (TextView) headerView.findViewById(R.id.tv_real_name);
        this.tv_address_details = (TextView) headerView.findViewById(R.id.tv_address_details);
        this.tv_phone = (TextView) headerView.findViewById(R.id.tv_phone);
        this.ivStatus = (ImageView) headerView.findViewById(R.id.iv_status);
        this.orderNum.setText(String.format("订单号：%s", new Object[]{this.orderUchoice.order_no}));
        this.createdAt.setText(DateHelper.timezoneFormat(this.orderUchoice.created_at,
                "yyyy-MM-dd HH:mm"));
        this.orderState.setText(this.orderUchoice.state_text);
        this.tv_real_name.setText(String.format("收件人：%s", new Object[]{this.orderUchoice
                .real_name}));
        this.tv_phone.setText(String.format("%s", new Object[]{this.orderUchoice.cellphone}));
        this.tv_address_details.setText(String.format("收货地址：%1$s %2$s %3$s %4$s %5$s", new
                Object[]{this.orderUchoice.address_province, this.orderUchoice.address_city, this
                .orderUchoice.address_district, this.orderUchoice.address_street, this
                .orderUchoice.address_zipcode}));
    }

    private void updateFooterView(View footerView) {
        this.carriage = (TextView) footerView.findViewById(R.id.carriage_value);
        this.supplement = (TextView) footerView.findViewById(R.id.supplement_value);
        this.ll_coupon = (LinearLayout) footerView.findViewById(R.id.ll_coupon);
        this.ll_bonus = (LinearLayout) footerView.findViewById(R.id.ll_bonus);
        this.tv_coupon = (TextView) footerView.findViewById(R.id.tv_coupon);
        this.tv_coupon_amount = (TextView) footerView.findViewById(R.id.tv_coupon_amount);
        this.tv_bonus = (TextView) footerView.findViewById(R.id.tv_bonus);
        this.tv_bonus_amount = (TextView) footerView.findViewById(R.id.tv_bonus_amount);
        this.tv_price_all = (TextView) footerView.findViewById(R.id.price_all);
        this.tv_goods_price = (TextView) footerView.findViewById(R.id.goods_price);
        this.tb_alipay = (ToggleButton) footerView.findViewById(R.id.tb_alipay);
        this.tb_wechat = (ToggleButton) footerView.findViewById(R.id.tb_wechat);
        this.tb_upacp = (ToggleButton) footerView.findViewById(R.id.tb_upacp);
        this.ll_pay_type = (LinearLayout) footerView.findViewById(R.id.ll_pay_type);
        this.ll_alipay = footerView.findViewById(R.id.ll_alipay);
        this.ll_wechat = footerView.findViewById(R.id.ll_wechat);
        this.ll_upacp = footerView.findViewById(R.id.ll_upacp);
        final ImageView iv_pay_more = (ImageView) footerView.findViewById(R.id.iv_pay_more);
        this.ll_pay_more = (LinearLayout) footerView.findViewById(R.id.ll_pay_more);
        this.ll_pay_more.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (OrderDetailsActivity.access$104(OrderDetailsActivity.this) % 2 == 0) {
                    iv_pay_more.setBackgroundResource(R.drawable.p8);
                    OrderDetailsActivity.this.ll_wechat.setVisibility(8);
                    return;
                }
                iv_pay_more.setBackgroundResource(R.drawable.p7);
                OrderDetailsActivity.this.ll_wechat.setVisibility(0);
            }
        });
        this.ll_alipay.setOnClickListener(this);
        this.ll_wechat.setOnClickListener(this);
        this.ll_upacp.setOnClickListener(this);
        this.btnReceive.setOnClickListener(this);
        if (TextUtils.equals(this.payType, "alipay")) {
            this.tb_alipay.setChecked(true);
            this.tb_wechat.setChecked(false);
            this.tb_upacp.setChecked(false);
        } else if (TextUtils.equals(this.payType, PayService.CHANNEL_WECHAT)) {
            this.tb_wechat.setChecked(true);
            this.tb_alipay.setChecked(false);
            this.tb_upacp.setChecked(false);
        } else if (TextUtils.equals(this.payType, PayService.CHANNEL_UPACP)) {
            this.tb_upacp.setChecked(true);
            this.tb_wechat.setChecked(false);
            this.tb_alipay.setChecked(false);
        }
        if (this.orderUchoice.note.equals("")) {
            this.supplement.setText(getString(R.string.adx));
        } else {
            this.supplement.setText(this.orderUchoice.note);
        }
        if (this.orderUchoice.coupon == null || TextUtils.isEmpty(this.orderUchoice.coupon
                .amount)) {
            this.ll_coupon.setVisibility(8);
        } else {
            this.ll_coupon.setVisibility(0);
            this.tv_coupon.setText(this.orderUchoice.coupon.title);
            this.tv_coupon_amount.setText(SocializeConstants.OP_DIVIDER_MINUS + getString(R
                    .string.ae4) + String.valueOf(this.orderUchoice.coupon.amount));
        }
        if (this.orderUchoice.order_items != null && this.orderUchoice.order_items.size() > 0) {
            float sum = 0.0f;
            Iterator it = this.orderUchoice.order_items.iterator();
            while (it.hasNext()) {
                OrderItems item = (OrderItems) it.next();
                if (((double) item.base_price) >= 1.0E-5d) {
                    CartGoods good = item.goods;
                    if (good != null) {
                        sum += good.base_price * ((float) item.quantity);
                    }
                }
            }
            this.tv_goods_price.setText(getString(R.string.ae4) + NumberUtils.safeToString(this
                    .dFormat, (double) sum));
        }
        String bonus_info = this.orderUchoice.bonus_info;
        if (TextUtils.isEmpty(bonus_info) || "null".equals(bonus_info)) {
            this.ll_bonus.setVisibility(8);
        } else {
            this.ll_bonus.setVisibility(0);
            this.tv_bonus.setText(bonus_info);
            this.tv_bonus_amount.setText(SocializeConstants.OP_DIVIDER_MINUS + getString(R.string
                    .ae4) + this.orderUchoice.bonus_amount);
        }
        this.carriage.setText(getString(R.string.ae4) + String.valueOf(this.orderUchoice.carriage));
        this.tv_price_all.setText(getString(R.string.ae4) + String.valueOf(this.orderUchoice
                .price));
    }

    private void getOrderDetails() {
        ShopApi.getOrderDetail(this.orderId, this, new JsonCallback(this) {
            public void ok(JSONObject object) {
                super.ok(object);
                OrderDetailsActivity.this.mSuccessUrl = UchoiceOrder.parseOrderSuccessUrl(object);
                OrderDetailsActivity.this.orderUchoice = UchoiceOrder.parseOrderDetail(object);
                if (OrderDetailsActivity.this.orderDetailAdapter == null) {
                    OrderDetailsActivity.this.orderDetailAdapter = new OrderDetailAdapter
                            (OrderDetailsActivity.this.ctx, OrderDetailsActivity.this.orderUchoice);
                    OrderDetailsActivity.this.orderDetailList.addHeaderView(OrderDetailsActivity
                            .this.headerView);
                    OrderDetailsActivity.this.orderDetailList.addFooterView(OrderDetailsActivity
                            .this.footerView);
                    OrderDetailsActivity.this.orderDetailList.setAdapter(OrderDetailsActivity
                            .this.orderDetailAdapter);
                } else {
                    OrderDetailsActivity.this.orderDetailAdapter.refreshOrder
                            (OrderDetailsActivity.this.orderUchoice);
                }
                OrderDetailsActivity.this.updateHeaderView(OrderDetailsActivity.this.headerView);
                OrderDetailsActivity.this.updateFooterView(OrderDetailsActivity.this.footerView);
                OrderDetailsActivity.this.updateOrderView();
                OrderDetailsActivity.this.activity.getWindow().invalidatePanelMenu(0);
            }
        });
    }

    private void updateOrderView() {
        this.ll_pay_type.setVisibility(8);
        this.tv_unpaid.setVisibility(8);
        this.btnPay.setVisibility(8);
        this.btnReceive.setVisibility(8);
        this.btnRefund.setVisibility(8);
        this.rlBottom.setVisibility(0);
        if (this.mMenuItem != null) {
            this.mMenuItem.setVisible(false);
        }
        if ("initial".equals(this.orderUchoice.state)) {
            this.ll_pay_type.setVisibility(0);
            this.btnPay.setVisibility(0);
            this.tv_unpaid.setVisibility(0);
            this.tv_unpaid.setText("需付款：" + getString(R.string.ae4) + String.valueOf(this
                    .orderUchoice.price));
            if (this.mMenuItem != null) {
                this.mMenuItem.setVisible(true);
            }
            this.ivStatus.setImageResource(R.drawable.a5x);
        } else if (UchoiceOrder.PAYED.equals(this.orderUchoice.state)) {
            updateRefundButton();
            this.ivStatus.setImageResource(R.drawable.a5v);
        } else if (UchoiceOrder.SENT.equals(this.orderUchoice.state) || UchoiceOrder.PART_SENT
                .equals(this.orderUchoice.state)) {
            this.ivStatus.setImageResource(R.drawable.a5w);
            if (hasGoodsReturn()) {
                this.rlBottom.setVisibility(8);
            } else {
                this.btnReceive.setVisibility(0);
            }
        } else if ("finished".equals(this.orderUchoice.state)) {
            if ("finished".equals(this.orderUchoice.refund_state)) {
                this.btnRefund.setVisibility(0);
                this.btnRefund.setText("退款完成");
                this.btnRefund.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        RefundStatusActivity.startActivity(OrderDetailsActivity.this,
                                OrderDetailsActivity.this.orderUchoice.rfl_id);
                    }
                });
            } else {
                this.rlBottom.setVisibility(8);
            }
            this.ivStatus.setImageResource(R.drawable.a5u);
        } else {
            this.rlBottom.setVisibility(8);
            this.ivStatus.setImageResource(R.drawable.a5u);
        }
    }

    private boolean hasGoodsReturn() {
        if (this.orderUchoice == null || this.orderUchoice.order_items == null) {
            return false;
        }
        boolean hasRefund = false;
        Iterator it = this.orderUchoice.order_items.iterator();
        while (it.hasNext()) {
            String str = ((OrderItems) it.next()).refund_state;
            Object obj = -1;
            switch (str.hashCode()) {
                case -2146525273:
                    if (str.equals("accepted")) {
                        obj = 1;
                        break;
                    }
                    break;
                case -787013233:
                    if (str.equals("payback")) {
                        obj = 2;
                        break;
                    }
                    break;
                case -673660814:
                    if (str.equals("finished")) {
                        obj = 3;
                        break;
                    }
                    break;
                case 1948342084:
                    if (str.equals("initial")) {
                        obj = null;
                        break;
                    }
                    break;
            }
            switch (obj) {
                case null:
                case 1:
                case 2:
                case 3:
                    hasRefund = true;
                    continue;
                default:
                    break;
            }
            if (hasRefund) {
                return hasRefund;
            }
        }
        return hasRefund;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void updateRefundButton() {
        /*
        r4 = this;
        r0 = 0;
        r1 = "can_be_refund";
        r2 = r4.orderUchoice;
        r2 = r2.refund_state;
        r1 = r1.equals(r2);
        if (r1 == 0) goto L_0x0026;
    L_0x000e:
        r1 = r4.btnRefund;
        r1.setVisibility(r0);
        r0 = r4.btnRefund;
        r1 = "申请退款";
        r0.setText(r1);
        r0 = r4.btnRefund;
        r1 = new com.boohee.uchoice.OrderDetailsActivity$5;
        r1.<init>();
        r0.setOnClickListener(r1);
    L_0x0025:
        return;
    L_0x0026:
        r1 = "forbidden_refund";
        r2 = r4.orderUchoice;
        r2 = r2.refund_state;
        r1 = r1.equals(r2);
        if (r1 != 0) goto L_0x0039;
    L_0x0033:
        r1 = r4.orderUchoice;
        r1 = r1.state;
        if (r1 != 0) goto L_0x0041;
    L_0x0039:
        r0 = r4.rlBottom;
        r1 = 8;
        r0.setVisibility(r1);
        goto L_0x0025;
    L_0x0041:
        r1 = r4.btnRefund;
        r1.setVisibility(r0);
        r1 = r4.btnRefund;
        r2 = new com.boohee.uchoice.OrderDetailsActivity$6;
        r2.<init>();
        r1.setOnClickListener(r2);
        r1 = r4.orderUchoice;
        r2 = r1.refund_state;
        r1 = -1;
        r3 = r2.hashCode();
        switch(r3) {
            case -787013233: goto L_0x0074;
            case -673660814: goto L_0x007f;
            case -123173735: goto L_0x0095;
            case 1085547216: goto L_0x008a;
            case 1948342084: goto L_0x006a;
            default: goto L_0x005c;
        };
    L_0x005c:
        r0 = r1;
    L_0x005d:
        switch(r0) {
            case 0: goto L_0x0061;
            case 1: goto L_0x00a0;
            case 2: goto L_0x00aa;
            case 3: goto L_0x00b4;
            case 4: goto L_0x00be;
            default: goto L_0x0060;
        };
    L_0x0060:
        goto L_0x0025;
    L_0x0061:
        r0 = r4.btnRefund;
        r1 = "等待退款";
        r0.setText(r1);
        goto L_0x0025;
    L_0x006a:
        r3 = "initial";
        r2 = r2.equals(r3);
        if (r2 == 0) goto L_0x005c;
    L_0x0073:
        goto L_0x005d;
    L_0x0074:
        r0 = "payback";
        r0 = r2.equals(r0);
        if (r0 == 0) goto L_0x005c;
    L_0x007d:
        r0 = 1;
        goto L_0x005d;
    L_0x007f:
        r0 = "finished";
        r0 = r2.equals(r0);
        if (r0 == 0) goto L_0x005c;
    L_0x0088:
        r0 = 2;
        goto L_0x005d;
    L_0x008a:
        r0 = "refused";
        r0 = r2.equals(r0);
        if (r0 == 0) goto L_0x005c;
    L_0x0093:
        r0 = 3;
        goto L_0x005d;
    L_0x0095:
        r0 = "canceled";
        r0 = r2.equals(r0);
        if (r0 == 0) goto L_0x005c;
    L_0x009e:
        r0 = 4;
        goto L_0x005d;
    L_0x00a0:
        r0 = r4.btnRefund;
        r1 = "退款中";
        r0.setText(r1);
        goto L_0x0025;
    L_0x00aa:
        r0 = r4.btnRefund;
        r1 = "退款完成";
        r0.setText(r1);
        goto L_0x0025;
    L_0x00b4:
        r0 = r4.btnRefund;
        r1 = "退款被拒绝";
        r0.setText(r1);
        goto L_0x0025;
    L_0x00be:
        r0 = r4.btnRefund;
        r1 = "退款取消";
        r0.setText(r1);
        goto L_0x0025;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.boohee.uchoice" +
                ".OrderDetailsActivity.updateRefundButton():void");
    }

    private void finishOrder() {
        showLoading();
        ShopApi.finishOrder(this.orderId, this, new JsonCallback(this) {
            public void ok(String response) {
                super.ok(response);
                OrderDetailsActivity.this.getOrderDetails();
            }

            public void onFinish() {
                super.onFinish();
                OrderDetailsActivity.this.dismissLoading();
            }
        });
    }

    private void cancelOrder() {
        ShopApi.cancelOrder(this.orderId, this, new JsonCallback(this) {
            public void ok(JSONObject object) {
                super.ok(object);
                Helper.showToast((int) R.string.eu);
                Intent result = new Intent();
                result.putExtra("index", OrderDetailsActivity.this.index);
                OrderDetailsActivity.this.setResult(-1, result);
                OrderDetailsActivity.this.finish();
            }
        });
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_pay:
                doPay(this.orderId);
                return;
            case R.id.ll_alipay:
                this.payType = "alipay";
                this.tb_alipay.setChecked(true);
                this.tb_wechat.setChecked(false);
                this.tb_upacp.setChecked(false);
                return;
            case R.id.ll_wechat:
                this.payType = PayService.CHANNEL_WECHAT;
                this.tb_wechat.setChecked(true);
                this.tb_alipay.setChecked(false);
                this.tb_upacp.setChecked(false);
                return;
            case R.id.ll_upacp:
                this.payType = PayService.CHANNEL_UPACP;
                this.tb_upacp.setChecked(true);
                this.tb_alipay.setChecked(false);
                this.tb_wechat.setChecked(false);
                return;
            case R.id.btn_receive:
                new Builder(this).setTitle((CharSequence) "确认收货").setMessage((CharSequence)
                        "是否确认收货？").setPositiveButton((CharSequence) "确定", new DialogInterface
                        .OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        OrderDetailsActivity.this.finishOrder();
                    }
                }).setNegativeButton((CharSequence) "取消", null).create().show();
                return;
            default:
                return;
        }
    }

    private void doPay(int orderId) {
        if (orderId > 0) {
            MobclickAgent.onEvent(this.ctx, Event.SHOP_CLICK_PAYMENT);
            this.mPayService = new PayService(this);
            this.mPayService.setOnFinishPayLinstener(this);
            boolean isPaySupported = AppUtils.isAppInstalled(this.activity, "com.tencent.mm");
            if (!TextUtils.equals(PayService.CHANNEL_WECHAT, this.payType) || isPaySupported) {
                this.mPayService.startPay(orderId, this.payType, false);
                return;
            } else {
                Helper.showToast((int) R.string.ad2);
                return;
            }
        }
        Helper.showToast((CharSequence) "订单生成失败");
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1 && requestCode == 168 && this.mPayService != null) {
            this.mPayService.onPaymentResult(data);
        }
    }

    public void onEventMainThread(RefreshOrderEvent event) {
        getOrderDetails();
    }

    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void onPaySuccess() {
        PaySuccessActivity.comeOnBaby(this.ctx, this.mSuccessUrl);
        finish();
    }

    public void onPayFinished() {
    }
}
