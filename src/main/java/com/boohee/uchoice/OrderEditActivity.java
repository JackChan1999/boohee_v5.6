package com.boohee.uchoice;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.boohee.api.ShopApi;
import com.boohee.main.GestureActivity;
import com.boohee.model.Address;
import com.boohee.model.CartGoods;
import com.boohee.model.Coupon;
import com.boohee.model.UchoiceOrder;
import com.boohee.one.R;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.pay.PayService;
import com.boohee.one.pay.PayService.OnFinishPayListener;
import com.boohee.uchoice.OrderEditListAdapter.CheckValueListener;
import com.boohee.utility.Event;
import com.boohee.utils.AppUtils;
import com.boohee.utils.Helper;
import com.boohee.utils.TextUtil;
import com.boohee.utils.ViewUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public class OrderEditActivity extends GestureActivity implements OnFinishPayListener,
        OnClickListener {
    private static final String KEY_GOODS_ID         = "key_goods_id";
    private static final String KEY_GOODS_LIST       = "key_goods_list";
    private static final String KEY_GOODS_QUANTITY   = "quantity";
    private static final String KEY_IS_FROM_CART     = "key_is_from_cart";
    static final         String TAG                  = OrderEditActivity.class.getName();
    private              int    CODE_FOR_IDCARD      = 1;
    private final        int    REQUEST_CODE_ADDRESS = 100;
    private Address address;
    private Button  btn_pay;
    private float   carriageValue;
    private Boolean carrigeOK = Boolean.valueOf(false);
    private ListView             cartList;
    private EditText             et_message;
    private CartGoods            goods;
    private ArrayList<CartGoods> goodsList;
    private int     goods_id      = -1;
    private Boolean hasGetOrderId = Boolean.valueOf(false);
    private Boolean isCart        = Boolean.valueOf(false);
    private boolean      isUseCoupon;
    private ImageView    iv_use_coupon;
    private LinearLayout ll_coupon;
    private LinearLayout ll_coupon_child;
    private LinearLayout ll_order_address_info;
    private LinearLayout ll_pay_more;
    private Coupon       mCoupon;
    private List<Coupon> mCoupons;
    private int mGoodQuantity = 1;
    private int mNum          = 0;
    private int mOrderId;
    private int mPayNum = 0;
    private PayService           mPayService;
    private String               mSuccessUrl;
    private boolean              needIdCard;
    private OrderEditListAdapter orderEditListAdapter;
    private String payType = "alipay";
    private float          priceAllValue;
    private RelativeLayout rl_order_postage;
    private ToggleButton   tb_alipay;
    private ToggleButton   tb_upacp;
    private ToggleButton   tb_use_coupon;
    private ToggleButton   tb_wechat;
    private TextView       tv_address_details;
    private TextView       tv_bonus_info;
    private TextView       tv_good_count;
    private TextView       tv_order_postage;
    private TextView       tv_order_postage_close;
    private TextView       tv_phone;
    private TextView       tv_postage;
    private TextView       tv_price_all_value;
    private TextView       tv_real_name;
    private TextView       tv_total;
    private TextView       tv_use_coupon_type;

    static /* synthetic */ int access$904(OrderEditActivity x0) {
        int i = x0.mPayNum + 1;
        x0.mPayNum = i;
        return i;
    }

    public static void start(Context context, ArrayList<CartGoods> goods, boolean isFromCart) {
        Intent starter = new Intent(context, OrderEditActivity.class);
        starter.putExtra(KEY_GOODS_LIST, goods);
        starter.putExtra(KEY_IS_FROM_CART, isFromCart);
        context.startActivity(starter);
    }

    public static void start(Context context, int good_id, int quantity, boolean isFromCart) {
        Intent starter = new Intent(context, OrderEditActivity.class);
        starter.putExtra(KEY_GOODS_ID, good_id);
        starter.putExtra(KEY_GOODS_QUANTITY, quantity);
        starter.putExtra(KEY_IS_FROM_CART, isFromCart);
        context.startActivity(starter);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nl);
        setTitle(R.string.k_);
        handleIntent();
        findView();
        init();
    }

    private void findView() {
        this.cartList = (ListView) findViewById(R.id.lv_order_edit);
        this.tv_price_all_value = (TextView) findViewById(R.id.tv_price_all_value);
        this.tv_bonus_info = (TextView) findViewById(R.id.tv_bonus_info);
        this.btn_pay = (Button) findViewById(R.id.btn_pay);
        this.rl_order_postage = (RelativeLayout) findViewById(R.id.rl_order_postage);
        this.tv_order_postage = (TextView) findViewById(R.id.tv_order_postage);
        this.tv_order_postage_close = (TextView) findViewById(R.id.tv_order_postage_close);
        this.btn_pay.setOnClickListener(this);
    }

    private void handleIntent() {
        this.isCart = Boolean.valueOf(getIntent().getBooleanExtra(KEY_IS_FROM_CART, false));
        this.goods_id = getIntent().getIntExtra(KEY_GOODS_ID, -1);
        this.mGoodQuantity = getIntent().getIntExtra(KEY_GOODS_QUANTITY, 1);
        this.goodsList = (ArrayList) getIntent().getSerializableExtra(KEY_GOODS_LIST);
    }

    private void init() {
        this.address = new Address();
        if (this.isCart.booleanValue()) {
            this.priceAllValue = calculate(this.goodsList);
            initAdapter();
            getDefaultAddress();
            return;
        }
        requestGoodsById(this.goods_id);
    }

    private float calculate(ArrayList<CartGoods> goods) {
        float priceAllValue = 0.0f;
        if (goods == null || goods.size() == 0) {
            return 0.0f;
        }
        for (int i = 0; i < goods.size(); i++) {
            priceAllValue += ((float) ((CartGoods) goods.get(i)).quantity) * ((CartGoods) goods
                    .get(i)).base_price;
        }
        return priceAllValue;
    }

    private void initAdapter() {
        if (!this.isCart.booleanValue()) {
            this.goodsList = new ArrayList();
            this.goods.quantity = this.mGoodQuantity;
            this.goods.goods_id = this.goods.id;
            this.goodsList.add(this.goods);
            this.priceAllValue = this.goods.base_price;
        }
        this.btn_pay.setEnabled(true);
        this.btn_pay.setBackgroundResource(R.drawable.dk);
        this.orderEditListAdapter = new OrderEditListAdapter(this.ctx, this.goodsList);
        this.orderEditListAdapter.setCheckValueListener(new CheckValueListener() {
            public void onCheckValue(float totaPrice) {
                OrderEditActivity.this.priceAllValue = totaPrice;
                if (!OrderEditActivity.this.carrigeOK.booleanValue()) {
                    OrderEditActivity.this.tv_price_all_value.setText(OrderEditActivity.this
                            .getString(R.string.ae4) + TextUtil.m2(totaPrice));
                } else if (OrderEditActivity.this.address != null) {
                    OrderEditActivity.this.mCoupon = null;
                    OrderEditActivity.this.tv_use_coupon_type.setText("");
                    OrderEditActivity.this.getOrderPreview(OrderEditActivity.this.goodsList,
                            OrderEditActivity.this.address.province, OrderEditActivity.this
                                    .address.city, OrderEditActivity.this.address.district);
                } else {
                    OrderEditActivity.this.tv_price_all_value.setText(OrderEditActivity.this
                            .getString(R.string.ae4) + TextUtil.m2(totaPrice));
                    OrderEditActivity.this.tv_postage.setVisibility(8);
                }
            }
        });
        this.cartList.addHeaderView(getHeaderView());
        this.cartList.addFooterView(getFooterView());
        this.cartList.setAdapter(this.orderEditListAdapter);
    }

    private View getHeaderView() {
        View convertView = LayoutInflater.from(this.ctx).inflate(R.layout.nn, null);
        this.tv_address_details = (TextView) convertView.findViewById(R.id.tv_address_details);
        this.tv_real_name = (TextView) convertView.findViewById(R.id.tv_real_name);
        this.tv_phone = (TextView) convertView.findViewById(R.id.tv_phone);
        this.ll_order_address_info = (LinearLayout) convertView.findViewById(R.id
                .ll_order_address_info);
        this.tv_order_postage_close.setOnClickListener(this);
        this.ll_order_address_info.setOnClickListener(this);
        return convertView;
    }

    private View getFooterView() {
        View convertView = LayoutInflater.from(this.ctx).inflate(R.layout.nm, null);
        this.tv_postage = (TextView) convertView.findViewById(R.id.tv_postage);
        this.tv_total = (TextView) convertView.findViewById(R.id.tv_total);
        this.tv_good_count = (TextView) convertView.findViewById(R.id.tv_good_count);
        this.et_message = (EditText) convertView.findViewById(R.id.et_message);
        this.ll_coupon = (LinearLayout) convertView.findViewById(R.id.ll_coupon);
        this.ll_coupon_child = (LinearLayout) this.ll_coupon.findViewById(R.id.ll_coupon_child);
        this.tb_use_coupon = (ToggleButton) this.ll_coupon.findViewById(R.id.tb_use_coupon);
        this.tv_use_coupon_type = (TextView) this.ll_coupon.findViewById(R.id.tv_use_coupon_type);
        this.iv_use_coupon = (ImageView) this.ll_coupon.findViewById(R.id.iv_use_coupon);
        final ImageView iv_pay_more = (ImageView) convertView.findViewById(R.id.iv_pay_more);
        LinearLayout ll_pay_more = (LinearLayout) convertView.findViewById(R.id.ll_pay_more);
        View ll_alipay = convertView.findViewById(R.id.ll_alipay);
        View ll_upacp = convertView.findViewById(R.id.ll_upacp);
        final View ll_wechat = convertView.findViewById(R.id.ll_wechat);
        this.ll_coupon = (LinearLayout) convertView.findViewById(R.id.ll_coupon);
        this.tb_alipay = (ToggleButton) convertView.findViewById(R.id.tb_alipay);
        this.tb_wechat = (ToggleButton) convertView.findViewById(R.id.tb_wechat);
        this.tb_upacp = (ToggleButton) convertView.findViewById(R.id.tb_upacp);
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
        ll_alipay.setOnClickListener(this);
        ll_wechat.setOnClickListener(this);
        ll_upacp.setOnClickListener(this);
        ll_pay_more.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (OrderEditActivity.access$904(OrderEditActivity.this) % 2 == 0) {
                    iv_pay_more.setBackgroundResource(R.drawable.p8);
                    ll_wechat.setVisibility(8);
                    return;
                }
                iv_pay_more.setBackgroundResource(R.drawable.p7);
                ll_wechat.setVisibility(0);
            }
        });
        this.iv_use_coupon.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (OrderEditActivity.this.mNum = OrderEditActivity.this.mNum + 1 % 2 == 0) {
                    OrderEditActivity.this.iv_use_coupon.setBackgroundResource(R.drawable.p8);
                    OrderEditActivity.this.ll_coupon_child.setVisibility(8);
                    return;
                }
                OrderEditActivity.this.iv_use_coupon.setBackgroundResource(R.drawable.p7);
                OrderEditActivity.this.ll_coupon_child.setVisibility(0);
            }
        });
        ll_pay_more.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (OrderEditActivity.access$904(OrderEditActivity.this) % 2 == 0) {
                    iv_pay_more.setBackgroundResource(R.drawable.p8);
                    ll_wechat.setVisibility(8);
                    return;
                }
                iv_pay_more.setBackgroundResource(R.drawable.p7);
                ll_wechat.setVisibility(0);
            }
        });
        this.tb_use_coupon.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                OrderEditActivity.this.isUseCoupon = isChecked;
                if (isChecked) {
                    OrderEditActivity.this.refreshPayTotal();
                } else {
                    OrderEditActivity.this.tv_price_all_value.setText(OrderEditActivity.this
                            .getString(R.string.ae4) + TextUtil.m2(OrderEditActivity.this
                            .priceAllValue));
                }
            }
        });
        this.isUseCoupon = this.tb_use_coupon.isChecked();
        if (!(this.isCart.booleanValue() || this.goodsList == null || this.goodsList.size() <= 0)) {
            this.tv_good_count.setText(String.format(getString(R.string.n9), new Object[]{Integer
                    .valueOf(this.goodsList.size())}));
        }
        return convertView;
    }

    private void refreshPayTotal() {
        if (this.mCoupon == null) {
            this.tv_price_all_value.setText(getString(R.string.ae4) + TextUtil.m2(this
                    .priceAllValue));
        } else if (this.isUseCoupon) {
            this.tv_price_all_value.setText(getString(R.string.ae4) + TextUtil.m2(this
                    .priceAllValue - Float.valueOf(this.mCoupon.amount).floatValue()));
        }
    }

    private void requestGoodsById(int id) {
        if (id != -1) {
            showLoading();
            ShopApi.getGoodsDetail(id, this, new JsonCallback(this) {
                public void ok(JSONObject object) {
                    super.ok(object);
                    OrderEditActivity.this.goods = CartGoods.parseGoodsFromJson(object);
                    OrderEditActivity.this.initAdapter();
                    OrderEditActivity.this.getDefaultAddress();
                }

                public void onFinish() {
                    super.onFinish();
                    OrderEditActivity.this.dismissLoading();
                }
            });
        }
    }

    private void getOrderPreview(final List<CartGoods> goodsList, String province, String city,
                                 String district) {
        ShopApi.getOrdersPreview(JsonParam.createPriceParam(province, city, district, goodsList),
                this, new JsonCallback(this) {
            public void ok(JSONObject object) {
                super.ok(object);
                OrderEditActivity.this.carriageValue = Float.parseFloat(object.optString
                        ("carriage"));
                OrderEditActivity.this.priceAllValue = Float.parseFloat(object.optString("price"));
                OrderEditActivity.this.needIdCard = object.optBoolean("need_id_card");
                float consume_more = Float.parseFloat(object.optString("consume_more"));
                if (OrderEditActivity.this.carriageValue == 0.0f) {
                    OrderEditActivity.this.setTranslateAnim(false);
                } else if (consume_more > 0.0f) {
                    OrderEditActivity.this.setTranslateAnim(true);
                    OrderEditActivity.this.tv_order_postage.setText(String.format
                            (OrderEditActivity.this.getString(R.string.ye), new Object[]{Integer
                                    .valueOf((int) consume_more)}));
                }
                OrderEditActivity.this.tv_postage.setVisibility(0);
                OrderEditActivity.this.tv_postage.setText(OrderEditActivity.this.getString(R
                        .string.ae4) + TextUtil.m2(OrderEditActivity.this.carriageValue));
                OrderEditActivity.this.tv_total.setText(OrderEditActivity.this.getString(R.string
                        .ae4) + TextUtil.m2(OrderEditActivity.this.priceAllValue));
                OrderEditActivity.this.refreshPayTotal();
                if (goodsList != null && goodsList.size() > 0) {
                    OrderEditActivity.this.tv_good_count.setText(String.format(OrderEditActivity
                            .this.getString(R.string.n9), new Object[]{Integer.valueOf(goodsList
                            .size())}));
                }
                OrderEditActivity.this.carrigeOK = Boolean.valueOf(true);
                OrderEditActivity.this.mCoupons = Coupon.parse(object);
                if (OrderEditActivity.this.mCoupons == null || OrderEditActivity.this.mCoupons
                        .size() <= 0) {
                    OrderEditActivity.this.ll_coupon.setVisibility(8);
                } else {
                    OrderEditActivity.this.ll_coupon.setVisibility(0);
                    OrderEditActivity.this.initCoupon();
                }
                String bonus_info = object.optString("bonus_info");
                if (TextUtils.isEmpty(bonus_info) || "null".equals(bonus_info)) {
                    OrderEditActivity.this.tv_bonus_info.setVisibility(8);
                    return;
                }
                OrderEditActivity.this.tv_bonus_info.setText(String.valueOf(bonus_info));
                OrderEditActivity.this.tv_bonus_info.setVisibility(0);
            }
        });
    }

    private void initCoupon() {
        if (this.ll_coupon_child != null) {
            this.ll_coupon_child.removeAllViews();
            if (this.mCoupons != null && this.mCoupons.size() > 0) {
                String couponFormat = getString(R.string.h7);
                for (final Coupon coupon : this.mCoupons) {
                    String couponInfo = String.format(couponFormat, new Object[]{coupon.amount,
                            coupon.title});
                    View view_coupon_child = LayoutInflater.from(this.ctx).inflate(R.layout.ol,
                            null);
                    if (coupon.isChecked.booleanValue()) {
                        this.mCoupon = coupon;
                        refreshPayTotal();
                        this.tv_use_coupon_type.setText(couponInfo);
                    }
                    ToggleButton tb_coupon = (ToggleButton) view_coupon_child.findViewById(R.id
                            .tb_coupon);
                    TextView tv_coupon_type = (TextView) view_coupon_child.findViewById(R.id
                            .tv_coupon_type);
                    tb_coupon.setChecked(coupon.isChecked.booleanValue());
                    tb_coupon.setClickable(false);
                    tv_coupon_type.setText(couponInfo);
                    view_coupon_child.setOnClickListener(new OnClickListener() {
                        public void onClick(View v) {
                            OrderEditActivity.this.tb_use_coupon.setChecked(true);
                            for (Coupon cp : OrderEditActivity.this.mCoupons) {
                                if (coupon == cp) {
                                    cp.isChecked = Boolean.valueOf(true);
                                } else {
                                    cp.isChecked = Boolean.valueOf(false);
                                }
                            }
                            OrderEditActivity.this.initCoupon();
                        }
                    });
                    this.ll_coupon_child.addView(view_coupon_child);
                }
            }
        }
    }

    private void setTranslateAnim(final boolean isOpen) {
        int start;
        int end;
        int height = ViewUtils.dip2px(this.ctx, 40.0f);
        if (isOpen) {
            start = -height;
            end = 0;
        } else {
            start = 0;
            end = -height;
        }
        ObjectAnimator transY = ObjectAnimator.ofFloat(this.rl_order_postage, "translationY", new
                float[]{(float) start, (float) end});
        transY.setInterpolator(new DecelerateInterpolator());
        transY.setDuration(800);
        transY.start();
        transY.addListener(new AnimatorListener() {
            public void onAnimationStart(Animator animation) {
            }

            public void onAnimationRepeat(Animator animation) {
            }

            public void onAnimationEnd(Animator animation) {
                if (isOpen) {
                    OrderEditActivity.this.rl_order_postage.setVisibility(0);
                } else {
                    OrderEditActivity.this.rl_order_postage.setVisibility(4);
                }
            }

            public void onAnimationCancel(Animator animation) {
            }
        });
    }

    private void getDefaultAddress() {
        showLoading();
        ShopApi.getShipmentAddress(this, new JsonCallback(this) {
            public void ok(JSONObject object) {
                super.ok(object);
                Helper.showLog(OrderEditActivity.TAG, object.toString());
                OrderEditActivity.this.address = OrderEditActivity.this.getDefaultAddress(Address
                        .parseAddress(object));
                OrderEditActivity.this.refreshAddress(OrderEditActivity.this.address);
            }

            public void onFinish() {
                super.onFinish();
                OrderEditActivity.this.dismissLoading();
            }
        });
    }

    private void refreshAddress(Address address) {
        if (address != null) {
            this.tv_real_name.setText(String.format("收件人：%s", new Object[]{address.real_name}));
            this.tv_real_name.setTextColor(getResources().getColor(R.color.c4));
            this.tv_phone.setText(address.cellphone);
            this.tv_address_details.setText(String.format("收货地址：%1$s %2$s %3$s %4$s", new
                    Object[]{address.province, address.city, address.district, address.street}));
            if (!TextUtils.isEmpty(address.province) && !TextUtils.isEmpty(address.city)) {
                getOrderPreview(this.goodsList, address.province, address.city, address.district);
                return;
            }
            return;
        }
        this.tv_real_name.setText("请填写收货地址");
        this.tv_real_name.setTextColor(getResources().getColor(R.color.gn));
        this.tv_phone.setText("");
        this.tv_address_details.setText("");
        this.tv_postage.setText("");
        this.tv_total.setText("");
        this.tv_price_all_value.setText("");
        setTranslateAnim(false);
    }

    private Address getDefaultAddress(List<Address> addresses) {
        if (addresses != null && addresses.size() > 0) {
            for (int i = 0; i < addresses.size(); i++) {
                if (((Address) addresses.get(i)).isDefault) {
                    return (Address) addresses.get(i);
                }
            }
        }
        return null;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_pay:
                if (!ViewUtils.isFastDoubleClick()) {
                    performOrder();
                    return;
                }
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
            case R.id.tv_order_postage_close:
                setTranslateAnim(false);
                return;
            case R.id.ll_order_address_info:
                startActivityForResult(new Intent(this.ctx, AddressListActivity.class), 100);
                return;
            default:
                return;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1) {
            if (requestCode == 100) {
                getDefaultAddress();
            } else if (requestCode == 168 && this.mPayService != null) {
                this.mPayService.onPaymentResult(data);
            } else if (requestCode == this.CODE_FOR_IDCARD) {
                getOrderPreview(this.goodsList, this.address.province, this.address.city, this
                        .address.district);
            }
        }
    }

    private void performOrder() {
        if (this.address != null) {
            if (!TextUtil.isEmpty(this.address.real_name, this.address.cellphone, this.address
                    .street)) {
                int coupon_id = -1;
                if (this.isUseCoupon && this.mCoupon != null) {
                    coupon_id = this.mCoupon.id;
                }
                if (this.goodsList != null && this.goodsList.size() != 0) {
                    if (this.needIdCard) {
                        startActivityForResult(new Intent(this.ctx, IdCardInfoActivity.class),
                                this.CODE_FOR_IDCARD);
                        return;
                    }
                    MobclickAgent.onEvent(this.ctx, Event.SHOP_CLICK_SUBMITORDER);
                    boolean isPaySupported = AppUtils.isAppInstalled(this.activity, "com.tencent" +
                            ".mm");
                    if (!TextUtils.equals(PayService.CHANNEL_WECHAT, this.payType) ||
                            isPaySupported) {
                        try {
                            getOrder(this.address.real_name, this.address.cellphone, this.address
                                    .province, this.address.city, this.address.district, this
                                    .address.street, "", "", "GoodsOrder", this.address.zipcode,
                                    this.goodsList, this.et_message.getText().toString(),
                                    coupon_id);
                            return;
                        } catch (Exception e) {
                            e.printStackTrace();
                            return;
                        }
                    }
                    Helper.showToast((int) R.string.ad2);
                    return;
                }
                return;
            }
        }
        Helper.showToast((int) R.string.zo);
    }

    private void getOrder(String realName, String cellPhone, String addressProvince, String
            addressCity, String addressDistrict, String addressStreet, String receiveTime, String
            shipment_type, String type, String postCode, List<CartGoods> goodsList, String note,
                          int coupon_id) {
        this.btn_pay.setEnabled(false);
        this.btn_pay.setBackgroundResource(R.drawable.df);
        showLoading();
        ShopApi.createOrders(JsonParam.creatOrderParam(realName, cellPhone, addressProvince,
                addressCity, addressDistrict, addressStreet, receiveTime, shipment_type, type,
                postCode, goodsList, note, coupon_id), this, new JsonCallback(this) {
            public void ok(JSONObject object) {
                super.ok(object);
                OrderEditActivity.this.mCoupon = null;
                OrderEditActivity.this.tv_use_coupon_type.setText("");
                OrderEditActivity.this.hasGetOrderId = Boolean.valueOf(true);
                OrderEditActivity.this.mSuccessUrl = UchoiceOrder.parseOrderSuccessUrl(object);
                OrderEditActivity.this.mOrderId = UchoiceOrder.parseOrderId(object);
                OrderEditActivity.this.doPay(OrderEditActivity.this.mOrderId);
                if (OrderEditActivity.this.isCart.booleanValue()) {
                    ShopApi.clearCarts(OrderEditActivity.this, new JsonCallback(OrderEditActivity
                            .this));
                }
            }

            public void onFinish() {
                super.onFinish();
                if (!OrderEditActivity.this.hasGetOrderId.booleanValue()) {
                    OrderEditActivity.this.btn_pay.setEnabled(true);
                    OrderEditActivity.this.btn_pay.setBackgroundResource(R.drawable.dk);
                }
                OrderEditActivity.this.dismissLoading();
            }
        });
    }

    private void doPay(int orderId) {
        if (orderId > 0) {
            MobclickAgent.onEvent(this.ctx, Event.SHOP_CLICK_PAYMENT);
            this.mPayService = new PayService(this);
            this.mPayService.setOnFinishPayLinstener(this);
            this.mPayService.startPay(orderId, this.payType, false);
            return;
        }
        Helper.showToast((CharSequence) "订单生成失败");
    }

    public void onPaySuccess() {
        PaySuccessActivity.comeOnBaby(this.ctx, this.mSuccessUrl);
        finish();
    }

    public void onPayFinished() {
        this.btn_pay.setEnabled(true);
        this.btn_pay.setBackgroundResource(R.drawable.dk);
        finish();
    }
}
