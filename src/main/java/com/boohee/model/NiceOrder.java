package com.boohee.model;

import com.google.gson.Gson;

import org.json.JSONObject;

public class NiceOrder extends RecipeOrder {
    public String cellphone;
    public String email;
    public float  height;
    public float  weight;
    public String weixin;

    public NiceOrder(String created_at, String order_no) {
        super(created_at, order_no);
    }

    public static NiceOrder parseSelf(JSONObject object) {
        return (NiceOrder) new Gson().fromJson(object.toString(), NiceOrder.class);
    }
}
