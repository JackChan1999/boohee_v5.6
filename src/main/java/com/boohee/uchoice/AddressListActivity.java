package com.boohee.uchoice;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.boohee.api.ShopApi;
import com.boohee.main.GestureActivity;
import com.boohee.model.Address;
import com.boohee.one.R;
import com.boohee.one.http.JsonCallback;
import com.boohee.uchoice.AddressListAdapter.onAddressUpdateListener;
import com.boohee.utility.App;
import com.boohee.utils.FileUtil;
import com.boohee.utils.Helper;
import com.boohee.utils.Keyboard;
import com.boohee.utils.TextUtil;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

public class AddressListActivity extends GestureActivity implements onAddressUpdateListener {
    static final String TAG = AddressListActivity.class.getName();
    private AddressListAdapter addressListAdapter;
    private List<Address> mAddressList = new ArrayList();
    private LinearLayoutManager mLayoutManager;
    private RecyclerView        recycler_view;
    private String timestampString = "";
    private TextView txt_address_hint;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nb);
        setTitle(R.string.be);
        findView();
        getRegions();
    }

    protected void onResume() {
        super.onResume();
        getAddressList();
    }

    private void findView() {
        this.recycler_view = (RecyclerView) findViewById(R.id.recycler_view);
        this.txt_address_hint = (TextView) findViewById(R.id.txt_address_hint);
        this.recycler_view.setHasFixedSize(true);
        this.mLayoutManager = new LinearLayoutManager(this);
        this.recycler_view.setLayoutManager(this.mLayoutManager);
        this.addressListAdapter = new AddressListAdapter(this.ctx, this.mAddressList);
        this.addressListAdapter.setAddressUpdateListener(this);
        this.recycler_view.setAdapter(this.addressListAdapter);
        this.recycler_view.setItemAnimator(new DefaultItemAnimator());
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, 1, 1, R.string.b0).setShowAsAction(2);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                Intent intent = new Intent(this.ctx, AddressEditActivity.class);
                intent.putExtra("address_type", 1);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getRegions() {
        if (!TextUtil.isEmpty(FileUtil.readStrFromAPP(App.FILE_PATH, App.REGION_NAME))) {
            try {
                this.timestampString = new JSONObject(FileUtil.readStrFromAPP(App.FILE_PATH, App
                        .REGION_NAME)).optString("timestamp");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Helper.showLog(TAG, "本地时间戳:" + this.timestampString);
        ShopApi.getRegions(this.timestampString, this, new JsonCallback(this) {
            public void ok(JSONObject object) {
                super.ok(object);
                String timestamp = object.optString("timestamp");
                Helper.showLog(AddressListActivity.TAG, "服务器时间戳:" + timestamp);
                if (!TextUtil.isEmpty(timestamp) && !timestamp.equals(AddressListActivity.this
                        .timestampString)) {
                    String data = object.toString();
                    if (!TextUtil.isEmpty(data)) {
                        Helper.showLog(AddressListActivity.TAG, "保存地区数据到本地:" + data);
                        FileUtil.saveStrToAPP(data, App.FILE_PATH, App.REGION_NAME);
                    }
                }
            }
        });
    }

    private void getAddressList() {
        ShopApi.getShipmentAddress(this, new JsonCallback(this) {
            public void ok(JSONObject object) {
                super.ok(object);
                List<Address> addresses = Address.parseAddress(object);
                if (addresses == null || addresses.size() <= 0) {
                    AddressListActivity.this.txt_address_hint.setVisibility(0);
                    AddressListActivity.this.recycler_view.setVisibility(8);
                    return;
                }
                AddressListActivity.this.mAddressList.clear();
                AddressListActivity.this.mAddressList.addAll(addresses);
                AddressListActivity.this.addressListAdapter.notifyDataSetChanged();
                AddressListActivity.this.txt_address_hint.setVisibility(8);
                AddressListActivity.this.recycler_view.setVisibility(0);
            }
        });
    }

    protected void onPause() {
        super.onPause();
        Keyboard.closeAll(this.ctx);
    }

    public void addressUpdate(Address address) {
        ShopApi.updateShipmentAddress(address, this, new JsonCallback(this) {
            public void ok(JSONObject object) {
                super.ok(object);
                List<Address> addresses = Address.parseAddress(object);
                if (addresses != null && addresses.size() > 0) {
                    AddressListActivity.this.mAddressList.clear();
                    AddressListActivity.this.mAddressList.addAll(addresses);
                    AddressListActivity.this.addressListAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    public void onBackPressed() {
        setResult(-1);
        finish();
    }
}
