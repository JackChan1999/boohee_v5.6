package com.boohee.one.http;

import android.text.TextUtils;

import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.boohee.model.ApiError;
import com.boohee.utils.Helper;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class FailListener implements ErrorListener {
    static final String TAG = FailListener.class.getSimpleName();

    public abstract void fail(VolleyError volleyError);

    public void onErrorResponse(VolleyError volleyError) {
        if (!(volleyError.networkResponse == null || volleyError.networkResponse.data == null)) {
            try {
                List<ApiError> apiErrors = ApiError.parseErrors(new JSONObject(new String
                        (volleyError.networkResponse.data)));
                if (apiErrors == null || apiErrors.size() <= 0) {
                    volleyError = new VolleyError("网络出错啦~");
                } else {
                    volleyError = new VolleyError(((ApiError) apiErrors.get(0)).message);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (volleyError != null) {
            CharSequence charSequence;
            if (TextUtils.isEmpty(volleyError.getMessage())) {
                charSequence = "网络出错啦~";
            } else {
                charSequence = volleyError.getMessage();
            }
            Helper.showLong(charSequence);
        }
        fail(volleyError);
    }
}
