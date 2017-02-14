package com.boohee.uchoice;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;

import com.boohee.api.ShopApi;
import com.boohee.main.GestureActivity;
import com.boohee.model.RefundResult;
import com.boohee.one.R;
import com.boohee.one.event.RefreshOrderEvent;
import com.boohee.one.http.JsonCallback;
import com.boohee.utils.FastJsonUtils;
import com.boohee.utils.Helper;

import de.greenrobot.event.EventBus;

import org.json.JSONObject;

public class RefundApplyActivity extends GestureActivity {
    public static final String ORDER_ID = "id";
    @InjectView(2131428851)
    EditText etAccount;
    @InjectView(2131427854)
    EditText etReason;
    private int orderId;
    @InjectView(2131427697)
    TextView tvCommit;
    @InjectView(2131427848)
    TextView tvGoods;
    @InjectView(2131427851)
    TextView tvPrice;

    public static void startActivity(Context context, int id) {
        Intent intent = new Intent(context, RefundApplyActivity.class);
        intent.putExtra("id", id);
        context.startActivity(intent);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cn);
        ButterKnife.inject((Activity) this);
        initParam();
        initView();
        initData();
    }

    private void initParam() {
        this.orderId = getIntent().getIntExtra("id", -1);
    }

    private void initView() {
        this.tvCommit.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (RefundApplyActivity.this.checkInputValid()) {
                    RefundApplyActivity.this.sendRequest();
                }
            }
        });
    }

    private void sendRequest() {
        String account = this.etAccount.getText().toString();
        String reason = this.etReason.getText().toString();
        showLoading();
        ShopApi.applyRefund(this.orderId, reason, account, this, new JsonCallback(this) {
            public void ok(String response) {
                super.ok(response);
                RefundStatusActivity.startActivity(RefundApplyActivity.this, (RefundResult)
                        FastJsonUtils.fromJson(response, RefundResult.class));
                EventBus.getDefault().postSticky(new RefreshOrderEvent());
                RefundApplyActivity.this.finish();
            }

            public void onFinish() {
                super.onFinish();
                RefundApplyActivity.this.dismissLoading();
            }
        });
    }

    private boolean checkInputValid() {
        String account = this.etAccount.getText().toString();
        String reason = this.etReason.getText().toString();
        if (TextUtils.isEmpty(account.trim())) {
            Helper.showToast((CharSequence) "请输入退款账号");
            return false;
        } else if (!TextUtils.isEmpty(reason.trim())) {
            return true;
        } else {
            Helper.showToast((CharSequence) "请输入退款原因");
            return false;
        }
    }

    private void initData() {
        showLoading();
        ShopApi.previewRefund(this.orderId, this, new JsonCallback(this) {
            public void ok(JSONObject object) {
                super.ok(object);
                String title = object.optString("title");
                double refundAmount = object.optDouble("refund_amount");
                RefundApplyActivity.this.tvGoods.setText(title);
                RefundApplyActivity.this.tvPrice.setText(RefundApplyActivity.this.getString(R
                        .string.ae4) + String.valueOf(refundAmount));
            }

            public void onFinish() {
                super.onFinish();
                RefundApplyActivity.this.dismissLoading();
            }
        });
    }
}
