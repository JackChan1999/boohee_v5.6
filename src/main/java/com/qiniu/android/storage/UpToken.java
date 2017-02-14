package com.qiniu.android.storage;

import com.qiniu.android.utils.UrlSafeBase64;

import org.json.JSONException;
import org.json.JSONObject;

public final class UpToken {
    private String returnUrl = null;
    public final String token;

    private UpToken(JSONObject obj, String token) {
        this.returnUrl = obj.optString("returnUrl");
        this.token = token;
    }

    public static UpToken parse(String token) {
        try {
            String[] t = token.split(":");
            if (t.length != 3) {
                return null;
            }
            try {
                JSONObject obj = new JSONObject(new String(UrlSafeBase64.decode(t[2])));
                if (obj.optString("scope").equals("") || obj.optInt("deadline") == 0) {
                    return null;
                }
                return new UpToken(obj, token);
            } catch (JSONException e) {
                return null;
            }
        } catch (Exception e2) {
            return null;
        }
    }

    public String toString() {
        return this.token;
    }

    public boolean hasReturnUrl() {
        return !this.returnUrl.equals("");
    }
}
