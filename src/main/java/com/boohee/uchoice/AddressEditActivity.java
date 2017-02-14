package com.boohee.uchoice;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.boohee.api.ShopApi;
import com.boohee.main.GestureActivity;
import com.boohee.model.Address;
import com.boohee.one.R;
import com.boohee.one.http.JsonCallback;
import com.boohee.utility.RegularUtils;
import com.boohee.utils.Helper;
import com.boohee.utils.Keyboard;
import com.boohee.utils.TextUtil;
import com.boohee.widgets.RegionsPopwindow;
import com.boohee.widgets.RegionsPopwindow.onRegionChangeListener;

import org.json.JSONObject;

public class AddressEditActivity extends GestureActivity {
    public static final int    ADDRESS_TYPE_ADD    = 1;
    public static final int    ADDRESS_TYPE_UPDATE = 2;
    static final        String TAG                 = AddressEditActivity.class.getName();
    private Address  address;
    private EditText addressDetailsValue;
    private String   address_city;
    private String   address_district;
    private String   address_province;
    private int address_type = -1;
    private EditText phoneNumValue;
    private EditText postCodeValue;
    private EditText realNameValue;
    private TextView txt_address_regions;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.bd);
        setContentView(R.layout.na);
        handleIntent();
        findView();
        initText();
    }

    private void handleIntent() {
        this.address = (Address) getIntent().getSerializableExtra("address");
        this.address_type = getIntent().getIntExtra("address_type", -1);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        if (menu != null && menu.size() > 0) {
            menu.clear();
        }
        if (this.address_type == 1) {
            menu.add(0, 1, 1, R.string.b0).setShowAsAction(2);
        } else if (this.address_type == 2) {
            menu.add(0, 1, 1, R.string.aat).setShowAsAction(2);
        }
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                init();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initText() {
        if (this.address != null && this.address_type == 2) {
            this.txt_address_regions.setText(this.address.province + " " + this.address.city + " " +
                    "" + this.address.district);
            this.addressDetailsValue.setText(this.address.street);
            this.realNameValue.setText(this.address.real_name);
            this.postCodeValue.setText(this.address.zipcode);
            this.phoneNumValue.setText(this.address.cellphone);
            this.realNameValue.setSelection(this.address.real_name.length());
            this.address_province = this.address.province;
            this.address_city = this.address.city;
            this.address_district = this.address.district;
        }
    }

    private void findView() {
        this.realNameValue = (EditText) findViewById(R.id.real_name_value);
        this.phoneNumValue = (EditText) findViewById(R.id.phone_num_value);
        this.postCodeValue = (EditText) findViewById(R.id.post_code_value);
        this.addressDetailsValue = (EditText) findViewById(R.id.address_details_value);
        this.txt_address_regions = (TextView) findViewById(R.id.txt_address_regions);
        this.txt_address_regions.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Keyboard.closeAll(AddressEditActivity.this.ctx);
                RegionsPopwindow popwindow = RegionsPopwindow.getInstance();
                popwindow.setRegionChangeListener(new onRegionChangeListener() {
                    public void onChange(String provice, String city, String district) {
                        AddressEditActivity.this.address_province = provice;
                        AddressEditActivity.this.address_city = city;
                        AddressEditActivity.this.address_district = district;
                        AddressEditActivity.this.txt_address_regions.setText(provice + " " + city
                                + " " + district);
                    }
                });
                popwindow.showRegionsPopWindow(AddressEditActivity.this.ctx, AddressEditActivity
                        .this.address_province, AddressEditActivity.this.address_city,
                        AddressEditActivity.this.address_district);
            }
        });
    }

    private void getAddressValue() {
        if (this.address == null) {
            this.address = new Address();
        }
        this.address.real_name = this.realNameValue.getText().toString().trim();
        this.address.zipcode = this.postCodeValue.getText().toString().trim();
        this.address.street = this.addressDetailsValue.getText().toString().trim();
        this.address.province = this.address_province;
        this.address.city = this.address_city;
        this.address.district = this.address_district;
        this.address.cellphone = this.phoneNumValue.getText().toString().trim();
    }

    private void init() {
        getAddressValue();
        if (TextUtil.isEmpty(this.address.real_name, this.address.street, this.address.province,
                this.address.city, this.address.cellphone)) {
            Helper.showToast((int) R.string.ke);
        } else if (!RegularUtils.checkCellPhone(this.address.cellphone)) {
            Helper.showToast((int) R.string.ae2);
        } else if (this.address_type == 1) {
            addAddress(this.address);
        } else if (this.address_type == 2) {
            updateAddress(this.address);
        }
    }

    private void addAddress(Address address) {
        ShopApi.createShipmentAddress(address, this, new JsonCallback(this) {
            public void ok(JSONObject object) {
                super.ok(object);
                if (object != null) {
                    AddressEditActivity.this.finish();
                    Helper.showToast((CharSequence) "收货地址新增成功");
                    return;
                }
                Helper.showToast((CharSequence) "收货地址新增失败");
            }
        });
    }

    private void updateAddress(Address address) {
        ShopApi.updateShipmentAddress(address, this, new JsonCallback(this) {
            public void ok(JSONObject object) {
                super.ok(object);
                if (object != null) {
                    Helper.showToast((CharSequence) "地址更新成功");
                    AddressEditActivity.this.finish();
                    return;
                }
                Helper.showToast((CharSequence) "地址更新失败");
            }
        });
    }
}
