package com.tencent.stat;

import com.umeng.socialize.net.utils.SocializeProtocolConstants;

import org.json.JSONException;
import org.json.JSONObject;

public class NetworkMonitor {
    private long   a = 0;
    private int    b = 0;
    private String c = "";
    private int    d = 0;
    private String e = "";

    public String getDomain() {
        return this.c;
    }

    public long getMillisecondsConsume() {
        return this.a;
    }

    public int getPort() {
        return this.d;
    }

    public String getRemoteIp() {
        return this.e;
    }

    public int getStatusCode() {
        return this.b;
    }

    public void setDomain(String str) {
        this.c = str;
    }

    public void setMillisecondsConsume(long j) {
        this.a = j;
    }

    public void setPort(int i) {
        this.d = i;
    }

    public void setRemoteIp(String str) {
        this.e = str;
    }

    public void setStatusCode(int i) {
        this.b = i;
    }

    public JSONObject toJSONObject() {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("tm", this.a);
            jSONObject.put(SocializeProtocolConstants.PROTOCOL_KEY_ST, this.b);
            if (this.c != null) {
                jSONObject.put("dm", this.c);
            }
            jSONObject.put(SocializeProtocolConstants.PROTOCOL_KEY_SNSACCOUNT_ICON, this.d);
            if (this.e != null) {
                jSONObject.put("rip", this.e);
            }
            jSONObject.put(DeviceInfo.TAG_TIMESTAMPS, System.currentTimeMillis() / 1000);
        } catch (JSONException e) {
        }
        return jSONObject;
    }
}
