package com.boohee.uchoice;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.boohee.api.ShopApi;
import com.boohee.main.GestureActivity;
import com.boohee.model.CartGoods;
import com.boohee.one.R;
import com.boohee.one.http.JsonCallback;
import com.boohee.uchoice.CartListAdapter.onEditGoodQuantityListener;
import com.boohee.utility.Event;
import com.boohee.utils.Helper;
import com.boohee.utils.ShopUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public class CartActivity extends GestureActivity implements OnClickListener,
        OnItemClickListener, onEditGoodQuantityListener {
    static final  String  TAG    = CartActivity.class.getName();
    public static Boolean isEdit = Boolean.valueOf(false);
    private float           allPriceValue;
    private Button          btn_account;
    private Button          btn_cart_go;
    private Button          btn_delete;
    private CartListAdapter cartListAdapter;
    private int             count;
    public ArrayList<CartGoods> goodsList   = new ArrayList();
    public Boolean              isCanCommit = Boolean.valueOf(false);
    private LinearLayout   ll_bottom_bar;
    private LinearLayout   ll_delete;
    private ListView       lv_cart;
    private Menu           mMenu;
    private RelativeLayout rl_cart_hint;
    private TextView       tv_bonus_info;
    private TextView       tv_price_all_value;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nf);
        setTitle(R.string.f3);
        findViews();
    }

    protected void onResume() {
        super.onResume();
        getGoodsList();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        this.mMenu = menu;
        getMenuInflater().inflate(R.menu.g, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                if (isEdit.booleanValue()) {
                    isEdit = Boolean.valueOf(false);
                    this.ll_bottom_bar.setVisibility(0);
                    this.ll_delete.setVisibility(8);
                } else {
                    isEdit = Boolean.valueOf(true);
                    this.ll_bottom_bar.setVisibility(8);
                    this.ll_delete.setVisibility(0);
                }
                this.cartListAdapter.notifyDataSetChanged();
                refreshMenuItem();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void findViews() {
        this.lv_cart = (ListView) findViewById(R.id.lv_cart);
        this.ll_bottom_bar = (LinearLayout) findViewById(R.id.ll_bottom_bar);
        this.ll_delete = (LinearLayout) findViewById(R.id.ll_delete);
        this.btn_delete = (Button) findViewById(R.id.btn_delete);
        this.btn_account = (Button) findViewById(R.id.btn_account);
        this.tv_price_all_value = (TextView) findViewById(R.id.tv_price_all_value);
        this.tv_bonus_info = (TextView) findViewById(R.id.tv_bonus_info);
        this.rl_cart_hint = (RelativeLayout) findViewById(R.id.rl_cart_hint);
        this.btn_cart_go = (Button) findViewById(R.id.btn_cart_go);
        this.lv_cart.setOnItemClickListener(this);
        this.btn_account.setOnClickListener(this);
        this.btn_delete.setOnClickListener(this);
        this.btn_cart_go.setOnClickListener(this);
        this.goodsList.clear();
        this.cartListAdapter = new CartListAdapter(this.ctx, this.goodsList);
        this.lv_cart.setAdapter(this.cartListAdapter);
        this.cartListAdapter.setEditGoodQuantityListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cart_go:
                ShopUtils.scanAnyWhere(this.activity);
                return;
            case R.id.btn_account:
                if (this.isCanCommit.booleanValue()) {
                    MobclickAgent.onEvent(this.ctx, Event.SHOP_CLICK_CALCULATE_BILL);
                    OrderEditActivity.start(this.activity, this.goodsList, true);
                    return;
                }
                new Builder(this.ctx).setMessage(R.string.f4).setCancelable(false)
                        .setPositiveButton(R.string.a11, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                }).show();
                return;
            case R.id.btn_delete:
                ArrayList<String> deleteList = new ArrayList();
                if (this.goodsList != null && this.goodsList.size() != 0) {
                    for (int i = 0; i < this.goodsList.size(); i++) {
                        if (((CartGoods) this.goodsList.get(i)).isChecked.booleanValue()) {
                            deleteList.add(((CartGoods) this.goodsList.get(i)).goods_id + "");
                        }
                    }
                    if (deleteList == null || deleteList.size() <= 0) {
                        Helper.showToast((int) R.string.f5);
                        return;
                    } else {
                        deleteGoodsFromCart(deleteList);
                        return;
                    }
                }
                return;
            default:
                return;
        }
    }

    public void editGoodQuantity(int goodId, int quantity) {
        requestGoodQuantity(goodId, quantity);
    }

    public void onItemClick(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
        if (this.goodsList != null && this.goodsList.size() != 0) {
            if (isEdit.booleanValue()) {
                ((CartGoods) this.goodsList.get(arg2)).isChecked = Boolean.valueOf(!((CartGoods)
                        this.goodsList.get(arg2)).isChecked.booleanValue());
                this.cartListAdapter.notifyDataSetChanged();
                return;
            }
            Intent intent = new Intent(this.ctx, GoodsDetailActivity.class);
            intent.putExtra("goods_id", ((CartGoods) this.goodsList.get(arg2)).goods_id);
            startActivity(intent);
        }
    }

    private void getGoodsList() {
        ShopApi.getCarts(this, new JsonCallback(this) {
            public void ok(JSONObject object) {
                super.ok(object);
                if (object.has("total")) {
                    CartActivity.this.count = object.optInt("total");
                    if (CartActivity.this.count == 0) {
                        CartActivity.this.rl_cart_hint.setVisibility(0);
                        CartActivity.this.lv_cart.setVisibility(8);
                        CartActivity.this.ll_bottom_bar.setVisibility(8);
                        CartActivity.this.ll_delete.setVisibility(8);
                    } else {
                        CartActivity.this.rl_cart_hint.setVisibility(8);
                        CartActivity.this.ll_bottom_bar.setVisibility(0);
                        CartActivity.this.lv_cart.setVisibility(0);
                        CartActivity.this.ll_delete.setVisibility(8);
                        CartActivity.this.initGoods(object);
                    }
                    CartActivity.this.refreshMenuItem();
                }
            }
        });
    }

    private void deleteGoodsFromCart(ArrayList<String> deleteList) {
        ShopApi.deleteCart(deleteList, this, new JsonCallback(this) {
            public void ok(JSONObject object) {
                super.ok(object);
                if (object.has("total")) {
                    CartActivity.this.count = object.optInt("total");
                    if (CartActivity.this.count == 0) {
                        CartActivity.this.allPriceValue = 0.0f;
                        CartActivity.this.tv_price_all_value.setText("");
                        CartActivity.this.rl_cart_hint.setVisibility(0);
                        CartActivity.this.lv_cart.setVisibility(8);
                        CartActivity.this.ll_bottom_bar.setVisibility(8);
                        CartActivity.this.ll_delete.setVisibility(8);
                    } else {
                        CartActivity.this.initGoods(object);
                    }
                    CartActivity.this.refreshMenuItem();
                }
            }
        });
    }

    private void initGoods(JSONObject object) {
        this.allPriceValue = 0.0f;
        this.goodsList.clear();
        List<CartGoods> list = CartGoods.parseCart(object);
        if (list != null && list.size() != 0) {
            this.goodsList.addAll(list);
            if (this.goodsList != null && this.goodsList.size() != 0) {
                for (int i = 0; i < this.goodsList.size(); i++) {
                    ((CartGoods) this.goodsList.get(i)).isChecked = Boolean.valueOf(false);
                }
                this.cartListAdapter.notifyDataSetChanged();
                if (object != null && object.has("total_price")) {
                    this.allPriceValue = Float.parseFloat(object.optString("total_price"));
                    this.tv_price_all_value.setText(getString(R.string.ae4) + String.format("%" +
                            ".2f", new Object[]{Float.valueOf(this.allPriceValue)}));
                }
                if (object != null && object.has("can_commit")) {
                    this.isCanCommit = Boolean.valueOf(object.optBoolean("can_commit"));
                }
                String bonus_info = object.optString("bonus_info");
                if (TextUtils.isEmpty(bonus_info) || "null".equals(bonus_info)) {
                    this.tv_bonus_info.setVisibility(8);
                    return;
                }
                this.tv_bonus_info.setText(String.valueOf(bonus_info));
                this.tv_bonus_info.setVisibility(0);
            }
        }
    }

    private void requestGoodQuantity(int goodId, int quantity) {
        ShopApi.updateCarts(quantity, goodId, this, new JsonCallback(this) {
            public void ok(JSONObject object) {
                super.ok(object);
                if (object.has("total")) {
                    CartActivity.this.count = object.optInt("total");
                    if (CartActivity.this.count == 0) {
                        CartActivity.this.allPriceValue = 0.0f;
                        CartActivity.this.tv_price_all_value.setText("");
                        CartActivity.this.rl_cart_hint.setVisibility(0);
                        CartActivity.this.lv_cart.setVisibility(8);
                        CartActivity.this.ll_bottom_bar.setVisibility(8);
                        CartActivity.this.ll_delete.setVisibility(8);
                    } else {
                        CartActivity.this.initGoods(object);
                    }
                    CartActivity.this.refreshMenuItem();
                }
            }
        });
    }

    private void refreshMenuItem() {
        if (this.mMenu != null && this.mMenu.size() > 0) {
            MenuItem item = this.mMenu.findItem(R.id.action_edit);
            if (item != null) {
                if (this.count > 0) {
                    item.setVisible(true);
                } else {
                    item.setVisible(false);
                }
                if (isEdit.booleanValue()) {
                    item.setTitle(R.string.eq);
                } else {
                    item.setTitle(R.string.k1);
                }
            }
        }
    }

    public void onDestroy() {
        super.onDestroy();
        isEdit = Boolean.valueOf(false);
    }
}
