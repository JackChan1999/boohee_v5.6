package com.boohee.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import org.json.JSONObject;

public class GoodsType extends ModelBase {
    public ArrayList<Goods> goods;
    public String           type;

    public static ArrayList<GoodsType> parseGoodsType(JSONObject res) {
        try {
            return (ArrayList) new Gson().fromJson(res.getString("types").toString(), new
                    TypeToken<ArrayList<GoodsType>>() {
            }.getType());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
