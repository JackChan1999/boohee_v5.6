package com.boohee.uchoice;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RelativeLayout;
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

public class GoodsReturnApplyActivity extends GestureActivity {
    public static final String GOODS_ID = "goods_id";
    public static final String ORDER_ID = "id";
    @InjectView(2131428851)
    EditText etAccount;
    @InjectView(2131427854)
    EditText etReason;
    private int goodsId;
    private int orderId;
    @InjectView(2131428848)
    RelativeLayout rlAccount;
    @InjectView(2131428850)
    TextView       tvAccountTitle;
    @InjectView(2131427697)
    TextView       tvCommit;
    @InjectView(2131427848)
    TextView       tvGoods;
    @InjectView(2131427847)
    TextView       tvGoodsTitle;
    @InjectView(2131427851)
    TextView       tvPrice;
    @InjectView(2131427850)
    TextView       tvPriceTitle;
    @InjectView(2131427853)
    TextView       tvReasonTitle;

    public static void startActivity(Context context, int orderId, int goodsId) {
        Intent intent = new Intent(context, GoodsReturnApplyActivity.class);
        intent.putExtra("id", orderId);
        intent.putExtra("goods_id", goodsId);
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
        this.goodsId = getIntent().getIntExtra("goods_id", -1);
    }

    private void initView() {
        this.rlAccount.setVisibility(8);
        this.tvCommit.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (GoodsReturnApplyActivity.this.checkInputValid()) {
                    GoodsReturnApplyActivity.this.sendRequest();
                }
            }
        });
        this.tvReasonTitle.setText("退货原因");
        this.tvGoodsTitle.setText("退货商品");
        this.tvPriceTitle.setText("退货金额");
    }

    private void sendRequest() {
        String reason = this.etReason.getText().toString();
        showLoading();
        ShopApi.applyGoodsReturn(this.orderId, this.goodsId, reason, this, new JsonCallback(this) {
            public void ok(String response) {
                super.ok(response);
                GoodsReturnStatusActivity.startActivity(GoodsReturnApplyActivity.this,
                        (RefundResult) FastJsonUtils.fromJson(response, RefundResult.class));
                EventBus.getDefault().postSticky(new RefreshOrderEvent());
                GoodsReturnApplyActivity.this.finish();
            }

            public void onFinish() {
                super.onFinish();
                GoodsReturnApplyActivity.this.dismissLoading();
            }
        });
    }

    private boolean checkInputValid() {
        if (!TextUtils.isEmpty(this.etReason.getText().toString().trim())) {
            return true;
        }
        Helper.showToast((CharSequence) "请输入退款原因");
        return false;
    }

    private void initData() {
        showLoading();
        ShopApi.previewGoodsReturn(this.orderId, this.goodsId, this, new JsonCallback(this) {
            public void ok(JSONObject object) {
                super.ok(object);
                String title = object.optString("title");
                double refundAmount = object.optDouble("refund_amount");
                GoodsReturnApplyActivity.this.tvGoods.setText(title);
                GoodsReturnApplyActivity.this.tvPrice.setText(GoodsReturnApplyActivity.this
                        .getString(R.string.ae4) + String.valueOf(refundAmount));
            }

            public void onFinish() {
                super.onFinish();
                GoodsReturnApplyActivity.this.dismissLoading();
            }
        });
    }
}
