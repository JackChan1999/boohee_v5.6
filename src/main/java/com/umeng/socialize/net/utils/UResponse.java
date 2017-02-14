package com.umeng.socialize.net.utils;

import org.json.JSONObject;

public abstract class UResponse {

    public enum STATUS {
        SUCCESS,
        FAIL
    }

    public UResponse(JSONObject jSONObject) {
    }
}
