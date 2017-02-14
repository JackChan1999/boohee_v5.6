package com.boohee.api;

import android.content.Context;
import android.text.TextUtils;

import com.boohee.one.http.JsonCallback;
import com.boohee.one.http.JsonParams;
import com.boohee.one.http.client.BooheeClient;
import com.boohee.utils.Utils;

public class NiceApi {
    public static final  String GET_NICE_DETAIL     = "/api/v1/user/nice";
    public static final  String GET_NICE_STATUS     = "/api/v1/user/status";
    private static final String NICE_MESSAGE        = "/api/v1/messages";
    private static final String NICE_MESSAGE_MORE   = "/api/v1/messages/%s/history?limit=10";
    private static final String NICE_MESSAGE_STATUS = "/api/v1/messages/status";

    public static void getMessageStatus(Context context, JsonCallback callback) {
        BooheeClient.build(BooheeClient.NICE).get(NICE_MESSAGE_STATUS, callback, context);
    }

    public static void getNiceDetail(Context context, JsonCallback callback) {
        BooheeClient.build(BooheeClient.NICE).get(GET_NICE_DETAIL, callback, context);
    }

    public static void getNiceStatus(Context context, JsonCallback callback) {
        BooheeClient.build(BooheeClient.NICE).get(GET_NICE_STATUS, callback, context);
    }

    public static void getNiceMessage(String id, Context context, JsonCallback callback) {
        String url;
        if (TextUtils.isEmpty(id)) {
            url = NICE_MESSAGE;
        } else {
            url = String.format(NICE_MESSAGE_MORE, new Object[]{id});
        }
        BooheeClient.build(BooheeClient.NICE).get(url, callback, context);
    }

    public static void sendNiceMessage(String msg, Context context, JsonCallback callback) {
        if (!TextUtils.isEmpty(msg)) {
            JsonParams params = new JsonParams();
            params.put(Utils.RESPONSE_CONTENT, msg);
            BooheeClient.build(BooheeClient.NICE).post(NICE_MESSAGE, params, callback, context);
        }
    }
}
