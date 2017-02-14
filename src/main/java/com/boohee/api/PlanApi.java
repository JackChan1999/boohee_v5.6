package com.boohee.api;

import android.content.Context;

import com.boohee.one.http.JsonCallback;
import com.boohee.one.http.JsonParams;
import com.boohee.one.http.client.BooheeClient;

public class PlanApi {
    public static final String ADD_RECORD = "/api/v2/user_diets/record";
    public static final String GET_DIET   = "/api/v2/user_diets";
    public static final String RESET_DIET = "/api/v2/user_diets/reset";

    public static void getDiet(JsonCallback callback, Context context) {
        BooheeClient.build(BooheeClient.BINGO).get(GET_DIET, callback, context);
    }

    public static void resetDiet(String date, JsonCallback callback, Context context) {
        JsonParams params = new JsonParams();
        params.put("date", date);
        BooheeClient.build(BooheeClient.BINGO).post(RESET_DIET, params, callback, context);
    }

    public static void copyDiet(JsonCallback callback, Context context) {
        BooheeClient.build(BooheeClient.BINGO).post(ADD_RECORD, null, callback, context);
    }
}
