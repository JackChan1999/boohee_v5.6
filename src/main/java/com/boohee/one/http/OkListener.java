package com.boohee.one.http;

import android.app.Activity;

import com.android.volley.Response.Listener;
import com.boohee.model.ApiError;
import com.boohee.utils.Helper;

import java.util.List;

import org.json.JSONObject;

public abstract class OkListener implements Listener<JSONObject> {
    static final String TAG = OkListener.class.getSimpleName();
    private Activity mActivity;

    public abstract void ok(JSONObject jSONObject);

    public OkListener(Activity activity) {
        this.mActivity = activity;
    }

    public void onResponse(JSONObject response) {
        if (!hasErrors(response, this.mActivity)) {
            ok(response);
        }
    }

    public static boolean hasErrors(JSONObject object, Activity activity) {
        List<ApiError> errors = ApiError.parseErrors(object);
        if (errors == null || errors.size() <= 0) {
            return false;
        }
        Helper.showLong(((ApiError) errors.get(0)).message);
        for (ApiError error : errors) {
            if (error.code == 610) {
            }
        }
        return true;
    }
}
