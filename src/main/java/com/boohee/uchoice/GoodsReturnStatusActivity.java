package com.boohee.uchoice;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;

import com.boohee.api.ShopApi;
import com.boohee.apn.ApnActivity;
import com.boohee.main.FeedBackSwitcher;
import com.boohee.main.GestureActivity;
import com.boohee.model.RefundResult;
import com.boohee.one.R;
import com.boohee.one.event.RefreshOrderEvent;
import com.boohee.one.http.JsonCallback;
import com.boohee.utils.FastJsonUtils;
import com.boohee.utils.Helper;
import com.boohee.utils.MeiQiaHelper;

import de.greenrobot.event.EventBus;

public class GoodsReturnStatusActivity extends GestureActivity {
    public static final String REFUND_ID     = "refund_id";
    public static final String REFUND_RESULT = "refund_result";
    @InjectView(2131428851)
    EditText     etAccount;
    @InjectView(2131427696)
    EditText     etShipment;
    @InjectView(2131427692)
    View         ivShipmentLine;
    @InjectView(2131427693)
    LinearLayout llDetail;
    @InjectView(2131427694)
    LinearLayout llUpdate;
    private int          refundId;
    private RefundResult result;
    @InjectView(2131427697)
    TextView tvCommit;
    @InjectView(2131427691)
    TextView tvNote;
    @InjectView(2131427690)
    TextView tvStatus;

    public static void startActivity(Context context, RefundResult result) {
        Intent i = new Intent(context, GoodsReturnStatusActivity.class);
        i.putExtra("refund_result", result);
        context.startActivity(i);
    }

    public static void startActivity(Context context, int refundId) {
        Intent i = new Intent(context, GoodsReturnStatusActivity.class);
        i.putExtra("refund_id", refundId);
        context.startActivity(i);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bf);
        ButterKnife.inject((Activity) this);
        initParam();
        initView();
        initData();
    }

    private void initParam() {
        this.result = (RefundResult) getIntent().getParcelableExtra("refund_result");
        this.refundId = getIntent().getIntExtra("refund_id", -1);
    }

    private void initView() {
        this.tvCommit.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (FeedBackSwitcher.isFeedbackTime()) {
                    MeiQiaHelper.startChat(GoodsReturnStatusActivity.this);
                } else {
                    GoodsReturnStatusActivity.this.startActivity(new Intent
                            (GoodsReturnStatusActivity.this, ApnActivity.class));
                }
            }
        });
    }

    private void initData() {
        if (this.result != null) {
            updateView(this.result);
            return;
        }
        showLoading();
        ShopApi.getRefundInfo(this.refundId, this, new JsonCallback(this) {
            public void ok(String response) {
                super.ok(response);
                GoodsReturnStatusActivity.this.result = (RefundResult) FastJsonUtils.fromJson
                        (response, RefundResult.class);
                GoodsReturnStatusActivity.this.updateView(GoodsReturnStatusActivity.this.result);
            }

            public void onFinish() {
                super.onFinish();
                GoodsReturnStatusActivity.this.dismissLoading();
            }
        });
    }

    private void updateView(RefundResult result) {
        if (result != null) {
            this.llDetail.removeAllViews();
            this.llDetail.setVisibility(0);
            this.llUpdate.setVisibility(8);
            this.ivShipmentLine.setVisibility(0);
            this.tvCommit.setVisibility(0);
            this.tvCommit.setText("联系客服");
            this.tvCommit.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    if (FeedBackSwitcher.isFeedbackTime()) {
                        MeiQiaHelper.startChat(GoodsReturnStatusActivity.this);
                    } else {
                        GoodsReturnStatusActivity.this.startActivity(new Intent
                                (GoodsReturnStatusActivity.this, ApnActivity.class));
                    }
                }
            });
            if ("initial".equals(result.state)) {
                showReason(result);
                this.tvStatus.setText("等待商家处理退货申请");
                this.tvStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.a_h, 0, 0, 0);
                this.ivShipmentLine.setVisibility(8);
            } else if ("accepted".equals(result.state)) {
                this.llDetail.setVisibility(8);
                this.llUpdate.setVisibility(0);
                this.tvStatus.setText("商家通过了你的退货申请");
                this.tvStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.a_f, 0, 0, 0);
                this.tvCommit.setText("提交");
                this.tvCommit.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        GoodsReturnStatusActivity.this.updateGoodsReturn();
                    }
                });
            } else if ("payback".equals(result.state)) {
                this.tvStatus.setText("请等待商家确认收货");
                this.tvStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.a_h, 0, 0, 0);
                showShipment(result);
            } else if ("finished".equals(result.state)) {
                this.tvStatus.setText("退货已完成");
                this.tvStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.a_f, 0, 0, 0);
                showShipment(result);
                this.tvCommit.setVisibility(8);
            } else if ("refused".equals(result.state)) {
                this.tvStatus.setText("商家已拒绝你的退货申请");
                showReason(result);
                this.tvStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.a_g, 0, 0, 0);
            } else if ("canceled".equals(result.state)) {
                this.tvStatus.setText("退货申请已取消");
                this.tvStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.a_f, 0, 0, 0);
                if (TextUtils.isEmpty(result.shipment_no)) {
                    showReason(result);
                } else {
                    showShipment(result);
                }
            }
            this.tvNote.setText(result.note);
        }
    }

    private void updateGoodsReturn() {
        String account = this.etAccount.getText().toString();
        String shipment = this.etShipment.getText().toString();
        if (TextUtils.isEmpty(account.trim())) {
            Helper.showToast((CharSequence) "请输入退款账号");
        } else if (TextUtils.isEmpty(shipment.trim())) {
            Helper.showToast((CharSequence) "请输入退货原因");
        } else {
            showLoading();
            ShopApi.updateGoodsReturn(this.refundId, account, shipment, this, new JsonCallback
                    (this) {
                public void ok(String response) {
                    super.ok(response);
                    GoodsReturnStatusActivity.this.result = (RefundResult) FastJsonUtils.fromJson
                            (response, RefundResult.class);
                    GoodsReturnStatusActivity.this.updateView(GoodsReturnStatusActivity.this
                            .result);
                    EventBus.getDefault().postSticky(new RefreshOrderEvent());
                }

                public void onFinish() {
                    super.onFinish();
                    GoodsReturnStatusActivity.this.dismissLoading();
                }
            });
        }
    }

    private void showReason(RefundResult result) {
        this.llDetail.addView(generateItemView("退货商品", result.title));
        this.llDetail.addView(generateItemView("退货金额", getString(R.string.ae4) + result
                .refund_amount));
        this.llDetail.addView(generateItemView("退货原因", result.reason));
    }

    private void showShipment(RefundResult result) {
        this.llDetail.addView(generateItemView("快递单号", result.shipment_no));
        this.llDetail.addView(generateItemView("退款账号", String.format("%s (%s)", new
                Object[]{result.account, result.money_go_where})));
        this.llDetail.addView(generateItemView("退货商品", result.title));
        this.llDetail.addView(generateItemView("退货金额", getString(R.string.ae4) + result
                .refund_amount));
    }

    private View generateItemView(String title, String content) {
        View itemView = LayoutInflater.from(this).inflate(R.layout.ip, this.llDetail, false);
        ((TextView) itemView.findViewById(R.id.tv_title)).setText(title);
        ((TextView) itemView.findViewById(R.id.tv_content)).setText(content);
        return itemView;
    }
}
