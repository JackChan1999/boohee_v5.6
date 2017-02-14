package com.boohee.api;

import android.content.Context;

import com.boohee.model.status.Notification;
import com.boohee.modeldao.WeightRecordDao;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.http.JsonParams;
import com.boohee.one.http.client.BooheeClient;
import com.boohee.utils.Utils;

import org.json.JSONArray;

public class MessengerApi {
    public static final String API_BIND        = "/api/v1/devices/android_dove_connect";
    public static final String API_UNBIND      = "/api/v1/devices/android_dove_disconnect";
    public static final String V2_CATEGORIES   = "/api/v2/feedbacks/categories";
    public static final String V2_CHECK_UNREAD = "/api/v2/feedbacks/check_unread";
    public static final String V2_MESSAGES     = "/api/v2/feedbacks/messages";
    public static final String V2_SEND_MESSAGE = "/api/v2/feedbacks/send_message";

    public static void v2GetMessage(Context context, long last_id, JsonCallback callback) {
        v2GetMessage(context, last_id, 0, callback);
    }

    public static void v2GetMessage(Context context, long last_id, int per_page, JsonCallback
            callback) {
        JsonParams params = new JsonParams();
        if (last_id > 0) {
            params.put("last_id", String.valueOf(last_id));
        }
        if (per_page > 0) {
            params.put("per_page", String.valueOf(per_page));
        }
        BooheeClient.build(BooheeClient.MESSENGER).get(V2_MESSAGES, params, callback, context);
    }

    public static void v2SendMessage(Context context, String content, String category, JSONArray
            photos, JsonCallback callback) {
        JsonParams params = new JsonParams();
        params.put(Utils.RESPONSE_CONTENT, content);
        params.put("category", category);
        if (photos != null && photos.length() > 0) {
            params.put(WeightRecordDao.PHOTOS, photos);
        }
        JsonParams feedBack = new JsonParams();
        feedBack.put(Notification.FEEDBACK, params);
        BooheeClient.build(BooheeClient.MESSENGER).post(V2_SEND_MESSAGE, feedBack, callback,
                context);
    }

    public static void v2GetCategories(Context context, JsonCallback callback) {
        BooheeClient.build(BooheeClient.MESSENGER).get(V2_CATEGORIES, null, callback, context);
    }

    public static void v2CheckUnread(Context context, JsonCallback callback) {
        BooheeClient.build(BooheeClient.MESSENGER).get(V2_CHECK_UNREAD, null, callback, context);
    }
}
