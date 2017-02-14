package com.boohee.uchoice;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.boohee.one.http.JsonCallback;
import com.boohee.utils.FastJsonUtils;
import com.boohee.utils.MeiQiaHelper;

public class RefundStatusActivity extends GestureActivity {
    public static final String REFUND_ID     = "refund_id";
    public static final String REFUND_RESULT = "refund_result";
    @InjectView(2131427693)
    LinearLayout llDetail;
    private int          refundId;
    private RefundResult result;
    @InjectView(2131427855)
    TextView tvContact;
    @InjectView(2131427691)
    TextView tvNote;
    @InjectView(2131427690)
    TextView tvStatus;

    public static void startActivity(Context context, RefundResult result) {
        Intent i = new Intent(context, RefundStatusActivity.class);
        i.putExtra("refund_result", result);
        context.startActivity(i);
    }

    public static void startActivity(Context context, int refundId) {
        Intent i = new Intent(context, RefundStatusActivity.class);
        i.putExtra("refund_id", refundId);
        context.startActivity(i);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.co);
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
        this.tvContact.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (FeedBackSwitcher.isFeedbackTime()) {
                    MeiQiaHelper.startChat(RefundStatusActivity.this);
                } else {
                    RefundStatusActivity.this.startActivity(new Intent(RefundStatusActivity.this,
                            ApnActivity.class));
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
                RefundStatusActivity.this.result = (RefundResult) FastJsonUtils.fromJson
                        (response, RefundResult.class);
                RefundStatusActivity.this.updateView(RefundStatusActivity.this.result);
            }

            public void onFinish() {
                super.onFinish();
                RefundStatusActivity.this.dismissLoading();
            }
        });
    }

    private void updateView(RefundResult result) {
        this.llDetail.removeAllViews();
        this.llDetail.addView(generateItemView("退款商品", result.title));
        this.llDetail.addView(generateItemView("退款金额", getString(R.string.ae4) + result
                .refund_amount));
        this.llDetail.addView(generateItemView("退款账号", String.format("%s (%s)", new
                Object[]{result.account, result.money_go_where})));
        this.llDetail.addView(generateItemView("退款原因", result.reason));
        if ("initial".equals(result.state)) {
            this.tvStatus.setText("等待商家处理退款申请");
            this.tvStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.a_h, 0, 0, 0);
        } else if ("payback".equals(result.state)) {
            this.tvStatus.setText("退款申请已通过，商家退款中");
            this.tvStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.a_h, 0, 0, 0);
        } else if ("finished".equals(result.state)) {
            this.tvStatus.setText("退款已完成");
            this.tvStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.a_f, 0, 0, 0);
        } else if ("canceled".equals(result.state)) {
            this.tvStatus.setText("退款申请已取消");
            this.tvStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.a_f, 0, 0, 0);
        } else if ("refused".equals(result.state)) {
            this.tvStatus.setText("商家已拒绝你的退款申请");
            this.tvStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.a_g, 0, 0, 0);
        }
        this.tvNote.setText(result.note);
    }

    private View generateItemView(String title, String content) {
        View itemView = LayoutInflater.from(this).inflate(R.layout.ip, this.llDetail, false);
        ((TextView) itemView.findViewById(R.id.tv_title)).setText(title);
        ((TextView) itemView.findViewById(R.id.tv_content)).setText(content);
        return itemView;
    }
}
