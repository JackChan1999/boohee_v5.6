package com.boohee.uchoice;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.boohee.api.ShopApi;
import com.boohee.model.Address;
import com.boohee.one.R;
import com.boohee.one.http.JsonCallback;
import com.boohee.utils.Helper;

import java.util.List;

import org.json.JSONObject;

public class AddressListAdapter extends Adapter<ViewHolder> {
    static final String TAG = AddressListAdapter.class.getName();
    public  onAddressUpdateListener addressUpdateListener;
    private List<Address>           mAddresses;
    private Context                 mContext;

    public interface onAddressUpdateListener {
        void addressUpdate(Address address);
    }

    public static class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
        public LinearLayout ll_address_delete;
        public LinearLayout ll_address_edit;
        public LinearLayout ll_address_toggle;
        public ToggleButton toggle_address;
        public TextView     txt_addres;
        public TextView     txt_cellphone;
        public TextView     txt_real_name;
        public TextView     txt_street;

        public ViewHolder(View v) {
            super(v);
            this.txt_real_name = (TextView) v.findViewById(R.id.txt_real_name);
            this.txt_cellphone = (TextView) v.findViewById(R.id.txt_cellphone);
            this.txt_addres = (TextView) v.findViewById(R.id.txt_address);
            this.txt_street = (TextView) v.findViewById(R.id.txt_street);
            this.toggle_address = (ToggleButton) v.findViewById(R.id.toggle_address);
            this.toggle_address.setClickable(false);
            this.ll_address_edit = (LinearLayout) v.findViewById(R.id.ll_address_edit);
            this.ll_address_delete = (LinearLayout) v.findViewById(R.id.ll_address_delete);
            this.ll_address_toggle = (LinearLayout) v.findViewById(R.id.ll_address_toggle);
        }
    }

    public AddressListAdapter(Context mContext, List<Address> addresses) {
        this.mContext = mContext;
        this.mAddresses = addresses;
    }

    private void deleteAddressById(final int position, int id) {
        ShopApi.deleteShipmentAddress(id, this.mContext, new JsonCallback(this.mContext) {
            public void ok(JSONObject object) {
                super.ok(object);
                if (AddressListAdapter.this.mAddresses != null && AddressListAdapter.this
                        .mAddresses.size() > 0) {
                    AddressListAdapter.this.mAddresses.remove(position);
                    AddressListAdapter.this.notifyItemRemoved(position);
                    Helper.showToast((CharSequence) "删除成功");
                }
            }
        });
    }

    public int getItemCount() {
        return this.mAddresses.size();
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.nc,
                parent, false));
    }

    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Address address = (Address) this.mAddresses.get(position);
        holder.txt_real_name.setText(address.real_name);
        holder.txt_cellphone.setText(address.cellphone);
        holder.txt_addres.setText(address.province + " " + address.city + " " + address.district);
        holder.txt_street.setText(address.street);
        holder.toggle_address.setChecked(address.isDefault);
        holder.ll_address_toggle.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                address.isDefault = !address.isDefault;
                holder.toggle_address.setChecked(address.isDefault);
                if (AddressListAdapter.this.addressUpdateListener != null) {
                    AddressListAdapter.this.addressUpdateListener.addressUpdate(address);
                }
            }
        });
        holder.ll_address_edit.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(AddressListAdapter.this.mContext, AddressEditActivity
                        .class);
                intent.putExtra("address", address);
                intent.putExtra("address_type", 2);
                AddressListAdapter.this.mContext.startActivity(intent);
            }
        });
        holder.ll_address_delete.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                new Builder(AddressListAdapter.this.mContext).setMessage("确定要删除这个收货地址吗？")
                        .setCancelable(false).setPositiveButton("删除", new DialogInterface
                        .OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        AddressListAdapter.this.deleteAddressById(position, address.id);
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                }).show();
            }
        });
    }

    public void setAddressUpdateListener(onAddressUpdateListener addressUpdateListener) {
        this.addressUpdateListener = addressUpdateListener;
    }
}
