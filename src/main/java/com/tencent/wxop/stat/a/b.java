package com.tencent.wxop.stat.a;

import org.json.JSONArray;
import org.json.JSONObject;

public final class b {
    public String    a;
    public JSONArray bl;
    public JSONObject bm = null;

    public b(String str) {
        this.a = str;
        this.bm = new JSONObject();
    }

    public final boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof b)) {
            return false;
        }
        return toString().equals(((b) obj).toString());
    }

    public final int hashCode() {
        return toString().hashCode();
    }

    public final String toString() {
        StringBuilder stringBuilder = new StringBuilder(32);
        stringBuilder.append(this.a).append(",");
        if (this.bl != null) {
            stringBuilder.append(this.bl.toString());
        }
        if (this.bm != null) {
            stringBuilder.append(this.bm.toString());
        }
        return stringBuilder.toString();
    }
}
