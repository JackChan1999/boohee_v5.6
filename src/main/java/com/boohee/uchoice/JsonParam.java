package com.boohee.uchoice;

import com.boohee.model.CartGoods;
import com.boohee.one.http.JsonParams;
import com.boohee.utility.BooheeScheme;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonParam {
    static final String TAG = JsonParam.class.getName();

    public static JSONObject creatCarrige(String province, String city, ArrayList<CartGoods>
            goodsList) {
        try {
            JSONObject userObj = new JSONObject();
            userObj.put("address_province", province);
            userObj.put("address_city", city);
            userObj.put("shipment_type", "");
            userObj.put("order_items", creatGoodsParam(goodsList));
            userObj.put("type", BooheeScheme.GOODS);
            JSONObject obj = new JSONObject();
            obj.put("order", userObj);
            return obj;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static JsonParams creatOrderParam(String realName, String cellPhone, String
            addressProvince, String addressCity, String addressDistrict, String addressStreet,
                                             String receiveTime, String shipment_type, String
                                                     type, String postCode, List<CartGoods>
                                                     goodsList, String note, int coupon_id) {
        try {
            JSONObject userObj = new JSONObject();
            userObj.put("real_name", realName);
            userObj.put("cellphone", cellPhone);
            userObj.put("address_province", addressProvince);
            userObj.put("address_city", addressCity);
            userObj.put("address_district", addressDistrict);
            userObj.put("address_zipcode", postCode);
            userObj.put("address_street", addressStreet);
            userObj.put("shipment_type", shipment_type);
            userObj.put("receive_time", receiveTime);
            userObj.put("note", note);
            userObj.put("type", type);
            userObj.put("order_items", creatGoodsParam(goodsList));
            if (coupon_id > 0) {
                userObj.put("coupon_id", coupon_id);
            }
            JsonParams obj = new JsonParams();
            obj.put("order", userObj);
            return obj;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static JSONArray creatGoodsParam(List<CartGoods> goods) {
        JSONArray itemArray = new JSONArray();
        if (!(goods == null || goods.size() == 0)) {
            for (int i = 0; i < goods.size(); i++) {
                JSONObject object = new JSONObject();
                try {
                    object.put("goods_id", ((CartGoods) goods.get(i)).goods_id);
                    object.put("quantity", ((CartGoods) goods.get(i)).quantity);
                    itemArray.put(object);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return itemArray;
    }

    public static JsonParams createPriceParam(String province, String city, String district,
                                              List<CartGoods> goodsList) {
        try {
            JSONObject userObj = new JSONObject();
            userObj.put("address_province", province);
            userObj.put("address_city", city);
            userObj.put("address_district", district);
            userObj.put("shipment_type", "");
            userObj.put("order_items", creatGoodsParam(goodsList));
            userObj.put("type", BooheeScheme.GOODS);
            JsonParams obj = new JsonParams();
            obj.put("order", userObj);
            return obj;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static JSONObject createOrderDetai2l(int orderId) {
        try {
            JSONObject userObj = new JSONObject();
            userObj.put("id", orderId);
            return userObj;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
