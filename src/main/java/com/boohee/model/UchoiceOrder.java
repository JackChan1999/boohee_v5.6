package com.boohee.model;

import com.boohee.utils.FastJsonUtils;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public class UchoiceOrder extends Order {
    public static final  String CANCELED         = "canceled";
    public static final  String EXPIRED          = "expired";
    public static final  String FINISHED         = "finished";
    public static final  String INITIAL          = "initial";
    public static final  String PART_SENT        = "part_sent";
    public static final  String PAYED            = "payed";
    public static final  String REFUND_CAN       = "can_be_refund";
    public static final  String REFUND_CANCELED  = "canceled";
    public static final  String REFUND_FINISHED  = "finished";
    public static final  String REFUND_FORBIDDEN = "forbidden_refund";
    public static final  String REFUND_INITIAL   = "initial";
    public static final  String REFUND_PAYBACK   = "payback";
    public static final  String REFUND_REFUSED   = "refused";
    public static final  String SENT             = "sent";
    private static final long   serialVersionUID = -1588752621820750105L;
    public String                address_city;
    public String                address_district;
    public String                address_province;
    public String                address_street;
    public String                address_zipcode;
    public String                bonus_amount;
    public String                bonus_info;
    public float                 carriage;
    public String                cellphone;
    public Coupon                coupon;
    public String                email;
    public String                note;
    public ArrayList<OrderItems> order_items;
    public String                real_name;
    public String                receive_time;
    public String                refund_state;
    public int                   rfl_id;
    public String                shipment_type;
    public ArrayList<Shipments>  shipments;
    public int[]                 unshipped_order_item_ids;

    public UchoiceOrder(String created_at, String order_no) {
        super(created_at, order_no);
    }

    public static UchoiceOrder parseOrderDetail(JSONObject res) {
        UchoiceOrder orderUchoice = null;
        try {
            return (UchoiceOrder) FastJsonUtils.fromJson(res.getJSONObject("order").toString(),
                    UchoiceOrder.class);
        } catch (Exception e) {
            e.printStackTrace();
            return orderUchoice;
        }
    }

    public static int parseOrderId(JSONObject res) {
        int orderId = -1;
        try {
            orderId = res.getJSONObject("order").getInt("id");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return orderId;
    }

    public static String parseOrderSuccessUrl(JSONObject object) {
        try {
            return object.optJSONObject("order").optString("suc_url");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static UchoiceOrder parseSelf(JSONObject object) {
        return (UchoiceOrder) FastJsonUtils.fromJson(object.toString(), UchoiceOrder.class);
    }

    public static String toJson(List<UchoiceOrder> orders) {
        return FastJsonUtils.toJson(orders);
    }

    public static List<UchoiceOrder> fromJson(String orders) {
        return FastJsonUtils.parseList(orders, UchoiceOrder.class);
    }
}
