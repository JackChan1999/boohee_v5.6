package com.boohee.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class Order extends ModelBase {
    private static final long serialVersionUID = -6420156055324974719L;
    public String created_at;
    public String order_no;
    public float  price;
    public String state;
    public String state_text;
    public String suc_url;
    public String type;

    public Order(String created_at, String order_no) {
        this.created_at = created_at;
        this.order_no = order_no;
    }

    public static ArrayList<Order> parseOrders(JSONObject res) {
        ArrayList<Order> orders = new ArrayList();
        try {
            JSONArray arrays = res.getJSONArray("orders");
            for (int i = 0; i < arrays.length(); i++) {
                JSONObject child = arrays.getJSONObject(i);
                String type = child.getString("type");
                if ("GoodsOrder".equals(type)) {
                    UchoiceOrder orderUchoice = UchoiceOrder.parseSelf(child);
                    if (orderUchoice != null) {
                        orders.add(orderUchoice);
                    }
                } else if ("RecipeOrder".equals(type)) {
                    RecipeOrder orderRecipe = RecipeOrder.parseSelf(child);
                    if (orderRecipe != null) {
                        orders.add(orderRecipe);
                    }
                } else if ("NiceOrder".equals(type)) {
                    NiceOrder niceOrder = NiceOrder.parseSelf(child);
                    if (niceOrder != null) {
                        orders.add(niceOrder);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return orders;
    }
}
