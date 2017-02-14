package com.boohee.nice;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ToggleButton;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.boohee.api.ShopApi;
import com.boohee.main.GestureActivity;
import com.boohee.model.Address;
import com.boohee.myview.NiceCheckBox;
import com.boohee.nice.model.NicePayOrder;
import com.boohee.nice.model.NiceServices.ServicesBean;
import com.boohee.one.R;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.pay.PayService;
import com.boohee.one.pay.PayService.OnFinishPayListener;
import com.boohee.uchoice.AddressListActivity;
import com.boohee.utility.Event;
import com.boohee.utils.AppUtils;
import com.boohee.utils.FastJsonUtils;
import com.boohee.utils.Helper;
import com.boohee.utils.TextUtil;
import com.boohee.utils.ViewUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

import org.json.JSONObject;

public class NiceConfirmOrderActivity extends GestureActivity implements OnFinishPayListener {
    private static final String KEY_NICE_SERVICE     = "KEY_NICE_SERVICE";
    public static final  String TAG                  = NiceConfirmOrderActivity.class
            .getSimpleName();
    private final        int    REQUEST_CODE_ADDRESS = 100;
    private Address address;
    @InjectView(2131428489)
    NiceCheckBox dietCheckBox;
    @InjectView(2131428486)
    NiceCheckBox diseaseCheckBox;
    @InjectView(2131428477)
    EditText     etNiceHeight;
    @InjectView(2131428475)
    EditText     etNicePhone;
    @InjectView(2131428476)
    EditText     etNiceWeight;
    @InjectView(2131428480)
    NiceCheckBox genderCheckBox;
    private LinearLayout ll_order_address_info;
    private PayService   mPayService;
    private String payType = "alipay";
    @InjectView(2131428483)
    NiceCheckBox pregnantCheckBox;
    private ServicesBean servicesBean;
    @InjectView(2131427819)
    ScrollView   svNice;
    @InjectView(2131428493)
    ToggleButton tbAlipay;
    @InjectView(2131428496)
    ToggleButton tbWechat;
    @InjectView(2131427820)
    TextView     tvNiceTitle;
    @InjectView(2131427822)
    TextView     tvPriceAllValue;
    private TextView tv_address_details;
    private TextView tv_phone;
    private TextView tv_real_name;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cc);
        ButterKnife.inject((Activity) this);
        handleIntent();
        initView();
    }

    private void initView() {
        if (this.servicesBean != null) {
            this.tvNiceTitle.setText(this.servicesBean.title);
            this.tvPriceAllValue.setText(getString(R.string.ae4) + this.servicesBean.base_price);
            if (TextUtils.equals(this.payType, "alipay")) {
                this.tbAlipay.setChecked(true);
                this.tbWechat.setChecked(false);
            } else if (TextUtils.equals(this.payType, PayService.CHANNEL_WECHAT)) {
                this.tbWechat.setChecked(true);
                this.tbAlipay.setChecked(false);
            }
            this.genderCheckBox.clearCheck();
            this.pregnantCheckBox.clearCheck();
            this.diseaseCheckBox.clearCheck();
            this.dietCheckBox.clearCheck();
            initAddressView();
        }
    }

    private void initAddressView() {
        this.tv_address_details = (TextView) findViewById(R.id.tv_address_details);
        this.tv_real_name = (TextView) findViewById(R.id.tv_real_name);
        this.tv_phone = (TextView) findViewById(R.id.tv_phone);
        this.ll_order_address_info = (LinearLayout) findViewById(R.id.ll_order_address_info);
        this.ll_order_address_info.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                NiceConfirmOrderActivity.this.startActivityForResult(new Intent
                        (NiceConfirmOrderActivity.this.ctx, AddressListActivity.class), 100);
            }
        });
        getDefaultAddress();
    }

    private void handleIntent() {
        this.servicesBean = (ServicesBean) getIntent().getParcelableExtra(KEY_NICE_SERVICE);
    }

    public static void startActivity(Context context, ServicesBean servicesBean) {
        Intent i = new Intent(context, NiceConfirmOrderActivity.class);
        i.putExtra(KEY_NICE_SERVICE, servicesBean);
        context.startActivity(i);
    }

    @OnClick({2131428490, 2131428494, 2131427823})
    public void onClick(View view) {
        if (!ViewUtils.isFastDoubleClick()) {
            switch (view.getId()) {
                case R.id.btn_pay:
                    if (this.address != null) {
                        if (!TextUtil.isEmpty(this.address.real_name, this.address.cellphone,
                                this.address.street)) {
                            boolean isPaySupported = AppUtils.isAppInstalled(this.activity, "com" +
                                    ".tencent.mm");
                            if (!TextUtils.equals(PayService.CHANNEL_WECHAT, this.payType) ||
                                    isPaySupported) {
                                try {
                                    nicePay();
                                    return;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    return;
                                }
                            }
                            Helper.showToast((int) R.string.ad2);
                            return;
                        }
                    }
                    Helper.showToast((int) R.string.zo);
                    return;
                case R.id.ll_alipay:
                    this.payType = "alipay";
                    this.tbAlipay.setChecked(true);
                    this.tbWechat.setChecked(false);
                    return;
                case R.id.ll_wechat:
                    this.payType = PayService.CHANNEL_WECHAT;
                    this.tbWechat.setChecked(true);
                    this.tbAlipay.setChecked(false);
                    return;
                default:
                    return;
            }
        }
    }

    private void getDefaultAddress() {
        showLoading();
        ShopApi.getShipmentAddress(this, new JsonCallback(this) {
            public void ok(JSONObject object) {
                super.ok(object);
                Helper.showLog(NiceConfirmOrderActivity.TAG, object.toString());
                NiceConfirmOrderActivity.this.address = NiceConfirmOrderActivity.this
                        .getDefaultAddress(Address.parseAddress(object));
                NiceConfirmOrderActivity.this.refreshAddress(NiceConfirmOrderActivity.this.address);
            }

            public void onFinish() {
                super.onFinish();
                NiceConfirmOrderActivity.this.dismissLoading();
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
            if (!TextUtils.isEmpty(address.province) && TextUtils.isEmpty(address.city)) {
                return;
            }
            return;
        }
        this.tv_real_name.setText("请填写收货地址");
        this.tv_real_name.setTextColor(getResources().getColor(R.color.gn));
        this.tv_phone.setText("");
        this.tv_address_details.setText("");
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

    private void nicePay() {
        String phone = this.etNicePhone.getText().toString();
        String sHeight = this.etNiceHeight.getText().toString();
        String sWeight = this.etNiceWeight.getText().toString();
        if (!TextUtil.isPhoneNumber(phone)) {
            Helper.showToast((CharSequence) "请输入正确的手机号");
        } else if (TextUtils.isEmpty(sHeight)) {
            Helper.showToast((CharSequence) "请输身高");
        } else if (TextUtils.isEmpty(sWeight)) {
            Helper.showToast((CharSequence) "请输入体重");
        } else {
            float height = Float.parseFloat(sHeight);
            float weight = Float.parseFloat(sWeight);
            if (calcBmi(height, weight) < 19.0f) {
                Helper.showToast((CharSequence)
                        "您的体重已经低于健康下限，不建议再追求体重数量的减少，可以适当增加塑形及力量训练，改善线条体型即可");
            } else if (this.genderCheckBox.isNotCheck() || this.pregnantCheckBox.isNotCheck() ||
                    this.diseaseCheckBox.isNotCheck() || this.dietCheckBox.isNotCheck()) {
                Helper.showToast((CharSequence) "请先确认您的个人信息");
                this.svNice.fullScroll(130);
            } else if (!this.genderCheckBox.isChecked()) {
                Helper.showToast((CharSequence) "未满18岁，身体尚在生长发育阶段，为了您的健康，建议均衡饮食，适当运动即可");
                this.svNice.fullScroll(130);
            } else if (this.pregnantCheckBox.isChecked()) {
                Helper.showToast((CharSequence) "当前身体情况比较特殊，建议在医生指导下控制体重建议在产后2个月以上再考虑正式开展减重");
            } else if (this.diseaseCheckBox.isChecked()) {
                Helper.showToast((CharSequence) "您的身体情况比较特殊，建议在医生指导下合理饮食，适当活动，控制体重为佳");
            } else if (this.dietCheckBox.isChecked()) {
                Helper.showToast((CharSequence) "执行减重计划期间会产生一定压力，有可能会加重您的饮食紊乱情况，建议先通过心理治疗，改善情况");
            } else {
                ShopApi.createNiceOrders(this.address, phone, weight, height, this.servicesBean
                        .sku, this.activity, new JsonCallback(this.activity) {
                    public void ok(JSONObject object) {
                        super.ok(object);
                        NicePayOrder niceOrder = (NicePayOrder) FastJsonUtils.fromJson(object,
                                NicePayOrder.class);
                        if (niceOrder != null) {
                            NiceConfirmOrderActivity.this.doPay(niceOrder.order.id);
                        }
                    }
                });
            }
        }
    }

    private float calcBmi(float height, float weight) {
        float h = height / 100.0f;
        return (float) (((double) Math.round(10.0f * (weight / (h * h)))) / 10.0d);
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1) {
            if (requestCode == 100) {
                getDefaultAddress();
            } else if (requestCode == 168 && this.mPayService != null) {
                this.mPayService.onPaymentResult(data);
            }
        }
    }

    public void onPaySuccess() {
        Helper.showToast((CharSequence) "支付成功");
        NiceServiceActivity.startActivity(this.activity);
    }

    public void onPayFinished() {
    }
}
