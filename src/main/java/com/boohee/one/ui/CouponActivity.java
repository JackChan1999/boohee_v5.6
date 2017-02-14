package com.boohee.one.ui;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.boohee.api.ApiUrls;
import com.boohee.api.ShopApi;
import com.boohee.database.UserPreference;
import com.boohee.model.Coupon;
import com.boohee.myview.swipeback.SwipeBackActivity;
import com.boohee.one.R;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.http.client.BooheeClient;
import com.boohee.one.ui.adapter.CouponListAdapter;
import com.boohee.utils.WheelUtils;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public class CouponActivity extends SwipeBackActivity {
    private CouponListAdapter mAdapter;
    private List<Coupon> mDatas = new ArrayList();
    private ListView mListView;
    private TextView tvNoCoupon;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.az);
        findView();
        loadData();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.h, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (WheelUtils.isFastDoubleClick()) {
            return true;
        }
        switch (item.getItemId()) {
            case R.id.action_coupon_exchange:
                BrowserActivity.comeOnBaby(this.activity, getString(R.string.a94), BooheeClient
                        .build("status").getDefaultURL(String.format(ApiUrls.COUPON_EXCHANGE, new
                                Object[]{UserPreference.getToken(this.activity)})));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void findView() {
        this.tvNoCoupon = (TextView) findViewById(R.id.tv_no_coupon);
        this.mListView = (ListView) findViewById(R.id.lv_coupon);
        this.mAdapter = new CouponListAdapter(this, this.mDatas);
        this.mListView.setAdapter(this.mAdapter);
        showContent(false);
    }

    private void loadData() {
        showLoading();
        ShopApi.getCoupons(this, new JsonCallback(this) {
            public void ok(JSONObject object) {
                super.ok(object);
                List<Coupon> coupons = Coupon.parseLists(object.optString("coupons"));
                boolean isShowContent = coupons != null && coupons.size() > 0;
                if (isShowContent) {
                    CouponActivity.this.mDatas.addAll(coupons);
                    CouponActivity.this.mAdapter.notifyDataSetInvalidated();
                }
                CouponActivity.this.showContent(isShowContent);
            }

            public void onFinish() {
                super.onFinish();
                CouponActivity.this.dismissLoading();
            }
        });
    }

    private void showContent(boolean showContent) {
        int i;
        int i2 = 0;
        TextView textView = this.tvNoCoupon;
        if (showContent) {
            i = 8;
        } else {
            i = 0;
        }
        textView.setVisibility(i);
        ListView listView = this.mListView;
        if (!showContent) {
            i2 = 8;
        }
        listView.setVisibility(i2);
    }
}
