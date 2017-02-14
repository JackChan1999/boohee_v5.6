package com.android.volley.toolbox;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.qiniu.android.common.Constants;
import org.json.JSONArray;
import org.json.JSONObject;

public class JsonArrayRequest extends JsonRequest<JSONArray> {
    public JsonArrayRequest(int method, String url, String requestBody, Listener<JSONArray> listener, ErrorListener errorListener) {
        super(method, url, requestBody, listener, errorListener);
    }

    public JsonArrayRequest(String url, Listener<JSONArray> listener, ErrorListener errorListener) {
        super(0, url, null, listener, errorListener);
    }

    public JsonArrayRequest(int method, String url, Listener<JSONArray> listener, ErrorListener errorListener) {
        super(method, url, null, listener, errorListener);
    }

    public JsonArrayRequest(int method, String url, JSONArray jsonRequest, Listener<JSONArray> listener, ErrorListener errorListener) {
        super(method, url, jsonRequest == null ? null : jsonRequest.toString(), listener, errorListener);
    }

    public JsonArrayRequest(int method, String url, JSONObject jsonRequest, Listener<JSONArray> listener, ErrorListener errorListener) {
        super(method, url, jsonRequest == null ? null : jsonRequest.toString(), listener, errorListener);
    }

    public JsonArrayRequest(String url, JSONArray jsonRequest, Listener<JSONArray> listener, ErrorListener errorListener) {
        this(jsonRequest == null ? 0 : 1, url, jsonRequest, (Listener) listener, errorListener);
    }

    public JsonArrayRequest(String url, JSONObject jsonRequest, Listener<JSONArray> listener, ErrorListener errorListener) {
        this(jsonRequest == null ? 0 : 1, url, jsonRequest, (Listener) listener, errorListener);
    }

    protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
        try {
            return Response.success(new JSONArray(new String(response.data, HttpHeaderParser.parseCharset(response.headers, Constants.UTF_8))), HttpHeaderParser.parseCacheHeaders(response));
        } catch (Throwable e) {
            return Response.error(new ParseError(e));
        } catch (Throwable je) {
            return Response.error(new ParseError(je));
        }
    }
}
