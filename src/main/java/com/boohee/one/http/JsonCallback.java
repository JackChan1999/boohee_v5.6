package com.boohee.one.http;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.boohee.model.ApiError;
import com.boohee.utility.Event;
import com.boohee.utils.Helper;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class JsonCallback implements Listener<String>, ErrorListener {
    private static final String TAG = JsonCallback.class.getSimpleName();
    private int errorCode;
    private boolean hasErrorMsg = false;
    private Context    mContext;
    private JSONObject mMockObject;

    public JsonCallback(Context context) {
        this.mContext = context;
    }

    public void onResponse(String responseBody) {
        Object result = null;
        responseBody = responseBody.trim();
        if (!TextUtils.isEmpty(responseBody) && this.mContext != null) {
            Object obj;
            Helper.logJson("Volley/Response", responseBody);
            ok(responseBody);
            try {
                if (responseBody.startsWith("{") || responseBody.startsWith("[")) {
                    result = new JSONTokener(responseBody).nextValue();
                }
                if (result == null) {
                    result = responseBody;
                }
                obj = result;
            } catch (JSONException e) {
                e.printStackTrace();
                obj = null;
            }
            if (obj instanceof JSONObject) {
                if (((JSONObject) obj).has("errors")) {
                    this.hasErrorMsg = true;
                    ok((JSONObject) obj, true);
                } else {
                    ok((JSONObject) obj);
                }
            } else if (obj instanceof JSONArray) {
                ok((JSONArray) obj);
            }
            onFinish();
        }
    }

    public void onErrorResponse(VolleyError volleyError) {
        JSONException e;
        String errorMessage = volleyError.getMessage();
        if (volleyError instanceof NoConnectionError) {
            MobclickAgent.onEvent(this.mContext, Event.AndroidNoConnectionError);
            errorMessage = "NoConnectionError：连接错误，请检查您的网络稍后重试";
        } else if (volleyError instanceof ServerError) {
            MobclickAgent.onEvent(this.mContext, Event.AndroidServerError);
            errorMessage = "ServerError：服务器出错啦，请稍后重试";
        } else if (volleyError instanceof AuthFailureError) {
            Helper.showLog(TAG, "AuthFailureError");
        } else if (volleyError instanceof ParseError) {
            Helper.showLog(TAG, "ParseError");
        } else if (volleyError instanceof NetworkError) {
            MobclickAgent.onEvent(this.mContext, Event.AndroidNetworkError);
            errorMessage = "NetworkError：网络出错啦，请检查您的网络稍后重试";
        } else if (volleyError instanceof TimeoutError) {
            MobclickAgent.onEvent(this.mContext, Event.AndroidTimeoutError);
            errorMessage = "TimeoutError：请求超时，请检查您的网络稍后重试";
        }
        Helper.showLog("Volley/ErrorResponse", errorMessage);
        NetworkResponse networkResponse = volleyError.networkResponse;
        if (!(networkResponse == null || networkResponse.data == null)) {
            try {
                JSONObject error = new JSONObject(new String(networkResponse.data));
                try {
                    if (error.has("errors")) {
                        this.hasErrorMsg = true;
                        errorMessage = ApiError.getErrorMessage(error);
                        this.errorCode = ApiError.getErrorCode(error);
                    }
                } catch (JSONException e2) {
                    e = e2;
                    JSONObject jSONObject = error;
                    e.printStackTrace();
                    fail(errorMessage);
                    fail(errorMessage, this.hasErrorMsg, this.errorCode);
                    onFinish();
                }
            } catch (JSONException e3) {
                e = e3;
                e.printStackTrace();
                fail(errorMessage);
                fail(errorMessage, this.hasErrorMsg, this.errorCode);
                onFinish();
            }
        }
        fail(errorMessage);
        fail(errorMessage, this.hasErrorMsg, this.errorCode);
        onFinish();
    }

    public void ok(String response) {
    }

    public void ok(JSONObject object) {
        if (object == null) {
        }
    }

    public void ok(JSONObject object, boolean hasError) {
        if (hasError && !TextUtils.isEmpty(ApiError.getErrorMessage(object))) {
            Helper.showToast(ApiError.getErrorMessage(object));
        }
    }

    public void ok(JSONArray array) {
        if (array == null) {
        }
    }

    public void fail(String message) {
        Helper.showToast((CharSequence) message);
    }

    public void fail(String message, boolean hasError, int errorCode) {
    }

    public void onFinish() {
        if (this.mMockObject != null) {
            finishWithMock(this.mMockObject);
        }
    }

    public void finishWithMock(JSONObject object) {
    }

    public void setMock(JSONObject object) {
        this.mMockObject = object;
    }
}
