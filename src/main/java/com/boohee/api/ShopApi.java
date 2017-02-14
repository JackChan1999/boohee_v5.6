package com.boohee.api;

import android.content.Context;

import com.boohee.model.Address;
import com.boohee.model.UploadFood;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.http.JsonParams;
import com.boohee.one.http.client.BooheeClient;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ShopApi {
    public static final String CANCEL_ORDER           = "/api/v1/orders/%1$d/cancel.json";
    public static final String CLEAR_CARTS            = "/api/v1/carts/clear.json";
    public static final String CONFIRM_DELIVERY       = "/api/v1/orders/%d/finish";
    public static final String DELETE_CARTS           = "/api/v1/carts/batch_delete.json";
    public static final String GET_CARTS              = "/api/v1/carts.json";
    public static final String GET_CATEGORIES         = "/api/v1/categories/%1$d";
    public static final String GET_CATEGORIES_SUBS    = "/api/v1/categories/%1$d/subs/%2$d";
    public static final String GET_CHANNELS_PICK      = "/api/v1/channels/pick?slug=%1$s";
    public static final String GET_COUPONS            = "/api/v1/coupons";
    public static final String GET_GOODS_DETAIL       = "/api/v1/goods/%1$d";
    public static final String GET_LABELS_DETAIL      = "/api/v1/labels/%1$d?page=%2$d";
    public static final String GET_LABELS_HOT         = "/api/v1/labels/hot";
    public static final String GET_MORE_GOODS         = "/api/v1/shop_home/more_goods";
    public static final String GET_NICE_SERVICE_LIST  = "/api/v1/services.json?type=%1$s";
    public static final String GET_ORDERS             = "/api/v1/orders.json";
    public static final String GET_ORDER_DETAIL       = "/api/v1/orders/%1$d";
    public static final String GET_SHIPMENT_ADDRESS   = "/api/v1/shipment_addresses.json";
    public static final String GET_SHOP_HOME          = "/api/v1/shop_home";
    public static final String GOOD_RETURN_APPLY      = "/api/v1/orders/%d/refund_applications/%d";
    public static final String GOOD_RETURN_PREVIEW    =
            "/api/v1/orders/%d/refund_applications/%d/preview";
    public static final String GOOD_RETURN_UPDATE     = "/api/v1/refund_applications/%d";
    public static final String ORDERS_PREVIEW         = "/api/v1/orders/preview";
    public static final String ORDERS_STATS           = "/api/v1/orders/stats.json";
    public static final String POST_NICE_ORDER        = "/api/v1/orders";
    public static final String POST__SHIPMENT_ADDRESS = "/api/v1/shipment_addresses/%1$s.json";
    public static final String REFUND_APPLY           = "/api/v1/orders/%d/refund_applications";
    public static final String REFUND_INFO            = "/api/v1/refund_applications/%d";
    public static final String REFUND_PREVIEW         =
            "/api/v1/orders/%d/refund_applications/preview";
    public static final String TABBAR_STATUS          = "/api/v1/tabbar_status";
    public static final String UPDATE_CARTS           = "/api/v1/carts/%1$s.json";
    public static final String URL_REGIONS            = "/api/v1/regions/more.json";

    public static void createNiceOrders(Address address, String phone, float weight, float
            height, String sku, Context context, JsonCallback callback) {
        JSONObject userObj = new JSONObject();
        try {
            userObj.put("type", BooheeClient.NICE);
            userObj.put("cellphone", phone);
            userObj.put("sku", sku);
            userObj.put("height", String.format("%.2f", new Object[]{Float.valueOf(height)}));
            userObj.put("weight", String.format("%.2f", new Object[]{Float.valueOf(weight)}));
            userObj.put("real_name", address.real_name);
            userObj.put("address_province", address.province);
            userObj.put("address_city", address.city);
            userObj.put("address_district", address.district);
            userObj.put("address_zipcode", address.zipcode);
            userObj.put("address_street", address.street);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonParams params = new JsonParams();
        params.put("order", userObj);
        BooheeClient.build(BooheeClient.ONE).post(POST_NICE_ORDER, params, callback, context);
    }

    public static void getNiceServiceList(String type, Context context, JsonCallback callback) {
        BooheeClient.build(BooheeClient.ONE).get(String.format(GET_NICE_SERVICE_LIST, new
                Object[]{type}), callback, context);
    }

    public static void getShipmentAddress(Context context, JsonCallback callback) {
        BooheeClient.build(BooheeClient.ONE).get(GET_SHIPMENT_ADDRESS, callback, context);
    }

    public static void createShipmentAddress(Address address, Context context, JsonCallback
            callback) {
        JsonParams params = new JsonParams();
        params.put("real_name", address.real_name);
        params.put("email", address.email);
        params.put("province", address.province);
        params.put("cellphone", address.cellphone);
        params.put("city", address.city);
        params.put("district", address.district);
        params.put("street", address.street);
        params.put("zipcode", address.zipcode);
        params.put("default", address.isDefault);
        BooheeClient.build(BooheeClient.ONE).post(GET_SHIPMENT_ADDRESS, params.with
                ("shipment_address"), callback, context);
    }

    public static void updateShipmentAddress(Address address, Context context, JsonCallback
            callback) {
        JsonParams params = new JsonParams();
        params.put("real_name", address.real_name);
        params.put("email", address.email);
        params.put("province", address.province);
        params.put("cellphone", address.cellphone);
        params.put("city", address.city);
        params.put("district", address.district);
        params.put("street", address.street);
        params.put("zipcode", address.zipcode);
        params.put("default", address.isDefault);
        BooheeClient.build(BooheeClient.ONE).put(String.format(POST__SHIPMENT_ADDRESS, new
                Object[]{Integer.valueOf(address.id)}), params.with("shipment_address"),
                callback, context);
    }

    public static void deleteShipmentAddress(int addressId, Context context, JsonCallback
            callback) {
        BooheeClient.build(BooheeClient.ONE).delete(String.format(POST__SHIPMENT_ADDRESS, new
                Object[]{Integer.valueOf(addressId)}), null, callback, context);
    }

    public static void updateCarts(int quantity, int goodsId, Context context, JsonCallback
            callback) {
        JsonParams params = new JsonParams();
        params.put("quantity", quantity);
        BooheeClient.build(BooheeClient.ONE).put(String.format(UPDATE_CARTS, new Object[]{Integer
                .valueOf(goodsId)}), params, callback, context);
    }

    public static void getRegions(String timestamp, Context context, JsonCallback callback) {
        JsonParams params = new JsonParams();
        params.put("timestamp", timestamp);
        BooheeClient.build(BooheeClient.ONE).get(URL_REGIONS, params, callback, context);
    }

    public static void getCarts(Context context, JsonCallback callback) {
        BooheeClient.build(BooheeClient.ONE).get(GET_CARTS, callback, context);
    }

    public static void deleteCart(ArrayList<String> deleteList, Context context, JsonCallback
            callback) {
        JsonParams params = new JsonParams();
        params.put("ids", deleteList.toString());
        BooheeClient.build(BooheeClient.ONE).post(DELETE_CARTS, params, callback, context);
    }

    public static void clearCarts(Context context, JsonCallback callback) {
        BooheeClient.build(BooheeClient.ONE).post(CLEAR_CARTS, null, callback, context);
    }

    public static void addCarts(int goodsId, int quantity, Context context, JsonCallback callback) {
        JsonParams params = new JsonParams();
        try {
            JSONArray jsonArray = new JSONArray();
            JSONObject object = new JSONObject();
            object.put("id", goodsId + "");
            object.put("quantity", quantity);
            jsonArray.put(object);
            params.put("cart_item", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        BooheeClient.build(BooheeClient.ONE).post(GET_CARTS, params, callback, context);
    }

    public static void getTabbarStatus(Context context, JsonCallback callback) {
        BooheeClient.build(BooheeClient.ONE).get(TABBAR_STATUS, callback, context);
    }

    public static void getCoupons(Context context, JsonCallback callback) {
        BooheeClient.build(BooheeClient.ONE).get(GET_COUPONS, callback, context);
    }

    public static void getOrders(String state, int page, Context context, JsonCallback callback) {
        JsonParams params = new JsonParams();
        params.put("type", "GoodsOrder");
        params.put(UploadFood.STATE, state);
        params.put("page", page);
        BooheeClient.build(BooheeClient.ONE).get(GET_ORDERS, params, callback, context);
    }

    public static void getOrderDetail(int id, Context context, JsonCallback callback) {
        BooheeClient.build(BooheeClient.ONE).get(String.format(GET_ORDER_DETAIL, new
                Object[]{Integer.valueOf(id)}), callback, context);
    }

    public static void cancelOrder(int id, Context context, JsonCallback callback) {
        BooheeClient.build(BooheeClient.ONE).post(String.format(CANCEL_ORDER, new
                Object[]{Integer.valueOf(id)}), null, callback, context);
    }

    public static void getOrdersStats(Context context, JsonCallback callback) {
        BooheeClient.build(BooheeClient.ONE).get(ORDERS_STATS, callback, context);
    }

    public static void getOrdersPreview(JsonParams params, Context context, JsonCallback callback) {
        BooheeClient.build(BooheeClient.ONE).post(ORDERS_PREVIEW, params, callback, context);
    }

    public static void createOrders(JsonParams params, Context context, JsonCallback callback) {
        BooheeClient.build(BooheeClient.ONE).post(GET_ORDERS, params, callback, context);
    }

    public static void getGoodsDetail(int goodsId, Context context, JsonCallback callback) {
        BooheeClient.build(BooheeClient.ONE).get(String.format(GET_GOODS_DETAIL, new
                Object[]{Integer.valueOf(goodsId)}), callback, context);
    }

    public static void getLablesHot(Context context, JsonCallback callback) {
        BooheeClient.build(BooheeClient.ONE).get(GET_LABELS_HOT, callback, context);
    }

    public static void getGoodsDetailEvaluate(String slug, Context context, JsonCallback callback) {
        BooheeClient.build("status").get(String.format(GET_CHANNELS_PICK, new Object[]{slug}),
                callback, context);
    }

    public static void getShopHomePages(Context context, JsonCallback callback) {
        JsonParams params = new JsonParams();
        params.put("version", 4);
        BooheeClient.build(BooheeClient.ONE).get(GET_SHOP_HOME, params, callback, context);
    }

    public static void getShopHomeMoreGoods(Context context, int page, JsonCallback callback) {
        JsonParams params = new JsonParams();
        params.put("page", page);
        params.put("version", 4);
        BooheeClient.build(BooheeClient.ONE).get(GET_MORE_GOODS, params, callback, context);
    }

    public static void getLablesDetail(int labelId, int page, Context context, JsonCallback
            callback) {
        BooheeClient.build(BooheeClient.ONE).get(String.format(GET_LABELS_DETAIL, new
                Object[]{Integer.valueOf(labelId), Integer.valueOf(page)}), callback, context);
    }

    public static void getCatetgories(int labelId, Context context, JsonCallback callback) {
        BooheeClient.build(BooheeClient.ONE).get(String.format(GET_CATEGORIES, new
                Object[]{Integer.valueOf(labelId)}), callback, context);
    }

    public static void getCategoriesSubs(int labelId, int childLabelId, int page, Context
            context, JsonCallback callback) {
        JsonParams params = new JsonParams();
        params.put("page", page);
        BooheeClient.build(BooheeClient.ONE).get(String.format(GET_CATEGORIES_SUBS, new
                Object[]{Integer.valueOf(labelId), Integer.valueOf(childLabelId)}), params,
                callback, context);
    }

    public static void previewRefund(int id, Context context, JsonCallback callback) {
        BooheeClient.build(BooheeClient.ONE).get(String.format(REFUND_PREVIEW, new
                Object[]{Integer.valueOf(id)}), null, callback, context);
    }

    public static void applyRefund(int id, String reason, String account, Context context,
                                   JsonCallback callback) {
        JsonParams params = new JsonParams();
        JsonParams refund = new JsonParams();
        refund.put("reason", reason);
        refund.put("account", account);
        params.put("refund", refund);
        BooheeClient.build(BooheeClient.ONE).post(String.format(REFUND_APPLY, new
                Object[]{Integer.valueOf(id)}), params, callback, context);
    }

    public static void getRefundInfo(int id, Context context, JsonCallback callback) {
        BooheeClient.build(BooheeClient.ONE).get(String.format("/api/v1/refund_applications/%d",
                new Object[]{Integer.valueOf(id)}), null, callback, context);
    }

    public static void previewGoodsReturn(int orderId, int goodsId, Context context, JsonCallback
            callback) {
        BooheeClient.build(BooheeClient.ONE).get(String.format(GOOD_RETURN_PREVIEW, new
                Object[]{Integer.valueOf(orderId), Integer.valueOf(goodsId)}), callback, context);
    }

    public static void applyGoodsReturn(int orderId, int goodsId, String reason, Context context,
                                        JsonCallback callback) {
        JsonParams params = new JsonParams();
        JsonParams refund = new JsonParams();
        refund.put("reason", reason);
        params.put("refund", refund);
        BooheeClient.build(BooheeClient.ONE).post(String.format(GOOD_RETURN_APPLY, new
                Object[]{Integer.valueOf(orderId), Integer.valueOf(goodsId)}), params, callback,
                context);
    }

    public static void updateGoodsReturn(int refundId, String account, String shipmentNo, Context
            context, JsonCallback callback) {
        JsonParams refund = new JsonParams();
        refund.put("account", account);
        refund.put("shipment_no", shipmentNo);
        JsonParams params = new JsonParams();
        params.put("refund", refund);
        BooheeClient.build(BooheeClient.ONE).put(String.format("/api/v1/refund_applications/%d",
                new Object[]{Integer.valueOf(refundId)}), params, callback, context);
    }

    public static void finishOrder(int orderId, Context context, JsonCallback callback) {
        BooheeClient.build(BooheeClient.ONE).post(String.format(CONFIRM_DELIVERY, new Object[]{Integer.valueOf(orderId)}), null, callback, context);
    }
}
