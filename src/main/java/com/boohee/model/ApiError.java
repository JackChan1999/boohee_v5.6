package com.boohee.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class ApiError {
    public int    code;
    public String message;

    public static ArrayList<ApiError> parseErrors(JSONObject object) {
        if (!object.has("errors")) {
            return null;
        }
        JSONArray array = object.optJSONArray("errors");
        if (array == null) {
            return null;
        }
        return (ArrayList) new Gson().fromJson(array.toString(), new
                TypeToken<ArrayList<ApiError>>() {
        }.getType());
    }

    public static String getErrorMessage(JSONObject object) {
        String message = "";
        JSONArray array = object.optJSONArray("errors");
        if (array == null || array.length() <= 0) {
            return message;
        }
        JSONObject apiError = array.optJSONObject(0);
        if (apiError != null) {
            return apiError.optString("message");
        }
        return message;
    }

    public static int getErrorCode(JSONObject object) {
        JSONArray array = object.optJSONArray("errors");
        if (array == null || array.length() <= 0) {
            return 0;
        }
        JSONObject apiError = array.optJSONObject(0);
        if (apiError != null) {
            return apiError.optInt("code");
        }
        return 0;
    }
}
