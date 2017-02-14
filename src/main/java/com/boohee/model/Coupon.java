package com.boohee.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public class Coupon {
    public String amount;
    public String description;
    public String expired_at;
    public int    id;
    public Boolean isChecked = Boolean.valueOf(false);
    public String order_amount;
    public String started_at;
    public String state;
    public String title;

    public enum coupon_state {
        INITIAL,
        LOCK,
        USED,
        EXPIRED
    }

    public static ArrayList<Coupon> parse(JSONObject res) {
        try {
            return (ArrayList) new Gson().fromJson(res.getString("coupons").toString(), new
                    TypeToken<ArrayList<Coupon>>() {
            }.getType());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<Coupon> parseLists(String str) {
        return (List) new Gson().fromJson(str, new TypeToken<List<Coupon>>() {
        }.getType());
    }

    public static Coupon parseTopic(JSONObject object) {
        if (object == null) {
            return null;
        }
        return (Coupon) new Gson().fromJson(object.toString(), Coupon.class);
    }

    public String toString() {
        return new Gson().toJson((Object) this);
    }
}
