package com.zxinsight.analytics.domain.response;

import android.net.Uri;

import java.util.Map;

public class Marketing {
    public String ak = "";
    public String au = "";
    public String dc = "";
    public String dt = "";
    public String et = "";
    public String fp = "0";
    public String fu = "";
    public String iu = "";
    public String iw = "";
    public String k  = "";
    public Map<String, String> lp;
    public String mk   = "";
    public String mlcb = "0";
    public Map<String, String> mp;
    public String rl = "0";
    public String sc = "";
    public String sh = "";
    public String ss = "";
    public String st = "";
    public String su = "";
    public Style sy;
    public String t  = "";
    public String tu = "";
    public String vt = "";

    public String getIu() {
        return Uri.decode(this.iu);
    }

    public String getIw() {
        return Uri.decode(this.iw);
    }

    public String getTu() {
        return Uri.decode(this.tu);
    }

    public String getAu() {
        return Uri.decode(this.au);
    }

    public String getSu() {
        return Uri.decode(this.su);
    }

    public String getFu() {
        return Uri.decode(this.fu);
    }

    public Style getSy() {
        return this.sy != null ? this.sy : new Style();
    }
}
