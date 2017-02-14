package com.boohee.model;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONObject;

public class Address extends ModelBase implements Serializable {
    private static final long    serialVersionUID = 2364899071377527291L;
    public               String  cellphone        = "";
    public               String  city             = "";
    public               String  district         = "";
    public               String  email            = "";
    @SerializedName("default")
    public               boolean isDefault        = false;
    public               String  province         = "";
    public               String  real_name        = "";
    public               String  street           = "";
    public               String  zipcode          = "";

    public Address(String real_name, String email, String cellphone, String address_province,
                   String address_city, String address_district, String address_street, String
                           address_zipcode, boolean isDefault) {
        this.real_name = real_name;
        this.email = email;
        this.cellphone = cellphone;
        this.province = address_province;
        this.city = address_city;
        this.district = address_district;
        this.street = address_street;
        this.zipcode = address_zipcode;
        this.isDefault = isDefault;
    }

    public static Address parseAddressFromJson(JSONObject object) {
        Address address = null;
        try {
            return (Address) new Gson().fromJson(object.toString(), Address.class);
        } catch (Exception e) {
            e.printStackTrace();
            return address;
        }
    }

    public static ArrayList<Address> parseAddress(JSONObject res) {
        try {
            return (ArrayList) new Gson().fromJson(res.getString("addresses").toString(), new
                    TypeToken<ArrayList<Address>>() {
            }.getType());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String toString() {
        return "Address [real_name=" + this.real_name + ", email=" + this.email + ", cellphone=" + this.cellphone + ", province=" + this.province + ", city=" + this.city + ", street=" + this.street + ", zipcode=" + this.zipcode + ", isDefault=" + this.isDefault + ", id=" + this.id + "]";
    }
}
