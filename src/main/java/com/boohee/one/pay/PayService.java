package com.boohee.one.pay;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

import com.boohee.one.R;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.http.JsonParams;
import com.boohee.one.http.client.BooheeClient;
import com.boohee.utils.AppUtils;
import com.boohee.utils.Helper;
import com.pingplusplus.android.PaymentActivity;
import com.pingplusplus.android.PingppLog;

import org.json.JSONObject;

public class PayService {
    public static final  String CHANNEL_ALIPAY       = "alipay";
    public static final  String CHANNEL_UPACP        = "upacp";
    public static final  String CHANNEL_WECHAT       = "wx";
    public static final  int    REQUEST_CODE_PAYMENT = 168;
    private static final String URL_PAY              = "/api/v1/payments/%d/pay/pingpp";
    private Activity            mActivity;
    private OnFinishPayListener mListener;
    private int mPayNum = 0;

    public interface OnFinishPayListener {
        void onPayFinished();

        void onPaySuccess();
    }

    private class OnPayOnclickListener implements OnClickListener {
        private String       mChannel;
        private Dialog       mDialog;
        private int          mOrderId;
        private ToggleButton mToggleButton;

        OnPayOnclickListener(int orderId, String channel, ToggleButton toggleButton, Dialog
                dialog) {
            this.mChannel = channel;
            this.mOrderId = orderId;
            this.mDialog = dialog;
            this.mToggleButton = toggleButton;
        }

        public void onClick(View v) {
            this.mToggleButton.setChecked(true);
            boolean isPaySupported = AppUtils.isAppInstalled(PayService.this.mActivity, "com" +
                    ".tencent.mm");
            if (!TextUtils.equals(PayService.CHANNEL_WECHAT, this.mChannel) || isPaySupported) {
                PayService.this.doPaymentTask(this.mOrderId, this.mChannel, false);
            } else {
                Helper.showToast((int) R.string.ad2);
            }
            this.mDialog.dismiss();
        }
    }

    static /* synthetic */ int access$004(PayService x0) {
        int i = x0.mPayNum + 1;
        x0.mPayNum = i;
        return i;
    }

    public PayService(Activity activity) {
        this.mActivity = activity;
    }

    public void startPay(int order_id, String channel, boolean isDushou) {
        PingppLog.DEBUG = true;
        if (TextUtils.equals("alipay", channel)) {
            doPaymentTask(order_id, channel, isDushou);
        } else if (TextUtils.equals(CHANNEL_WECHAT, channel)) {
            doPaymentTask(order_id, channel, false);
        } else if (TextUtils.equals(CHANNEL_UPACP, channel)) {
            doPaymentTask(order_id, channel, false);
        }
    }

    public void startPayWithDialog(int order_id) {
        Dialog dialog = new Dialog(this.mActivity);
        dialog.requestWindowFeature(1);
        View view = LayoutInflater.from(this.mActivity).inflate(R.layout.ez, null);
        view.setVisibility(0);
        View ll_alipay = view.findViewById(R.id.ll_alipay);
        View ll_wechat = view.findViewById(R.id.ll_wechat);
        View ll_upacp = view.findViewById(R.id.ll_upacp);
        ToggleButton tb_wechat = (ToggleButton) view.findViewById(R.id.tb_wechat);
        ToggleButton tb_upacp = (ToggleButton) view.findViewById(R.id.tb_upacp);
        ImageView iv_pay_more = (ImageView) view.findViewById(R.id.iv_pay_more);
        LinearLayout ll_pay_more = (LinearLayout) view.findViewById(R.id.ll_pay_more);
        int i = order_id;
        View view2 = ll_alipay;
        view2.setOnClickListener(new OnPayOnclickListener(i, "alipay", (ToggleButton) view
                .findViewById(R.id.tb_alipay), dialog));
        view2 = ll_wechat;
        view2.setOnClickListener(new OnPayOnclickListener(order_id, CHANNEL_WECHAT, tb_wechat,
                dialog));
        view2 = ll_upacp;
        view2.setOnClickListener(new OnPayOnclickListener(order_id, CHANNEL_UPACP, tb_upacp,
                dialog));
        final ImageView imageView = iv_pay_more;
        final View view3 = ll_wechat;
        ll_pay_more.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (PayService.access$004(PayService.this) % 2 == 0) {
                    imageView.setBackgroundResource(R.drawable.p8);
                    view3.setVisibility(8);
                    return;
                }
                imageView.setBackgroundResource(R.drawable.p7);
                view3.setVisibility(0);
            }
        });
        dialog.setContentView(view);
        dialog.show();
    }

    private void doPaymentTask(int orderId, String channel, boolean isDushou) {
        JsonParams jsonParams = new JsonParams();
        jsonParams.put("pay_channel", channel);
        if (isDushou) {
            BooheeClient.build("status").get(String.format(URL_PAY, new Object[]{Integer.valueOf
                    (orderId)}), jsonParams, new JsonCallback(this.mActivity) {
                public void ok(JSONObject object) {
                    super.ok(object);
                    PayService.this.handlePayResult(object);
                }

                public void onFinish() {
                    super.onFinish();
                    if (PayService.this.mListener != null) {
                        PayService.this.mListener.onPayFinished();
                    }
                }
            }, this.mActivity);
            return;
        }
        BooheeClient.build(BooheeClient.ONE).get(String.format(URL_PAY, new Object[]{Integer
                .valueOf(orderId)}), jsonParams, new JsonCallback(this.mActivity) {
            public void ok(JSONObject object) {
                super.ok(object);
                PayService.this.handlePayResult(object);
            }
        }, this.mActivity);
    }

    private void handlePayResult(JSONObject object) {
        if (object == null) {
            Helper.showToast((CharSequence) "支付失败");
            return;
        }
        String urlString = object.optString("url");
        Helper.showLog("charge", object.toString());
        Intent intent = new Intent(this.mActivity, PaymentActivity.class);
        intent.putExtra(PaymentActivity.EXTRA_CHARGE, urlString);
        this.mActivity.startActivityForResult(intent, 168);
    }

    public void onPaymentResult(Intent data) {
        if (data != null) {
            String result = data.getExtras().getString("pay_result");
            String errorMsg = data.getExtras().getString("error_msg");
            Helper.showLog("PING++", "result:" + result + " errorMsg:" + errorMsg + " extraMsg:"
                    + data.getExtras().getString("extra_msg"));
            if (!TextUtils.equals("success", result)) {
                Helper.showToast((CharSequence) "支付失败");
            } else if (this.mListener != null) {
                this.mListener.onPaySuccess();
            }
            if (this.mListener != null) {
                this.mListener.onPayFinished();
            }
        }
    }

    public void setOnFinishPayLinstener(OnFinishPayListener listener) {
        this.mListener = listener;
    }
}
