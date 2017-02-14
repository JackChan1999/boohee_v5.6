package com.umeng.analytics.social;

import android.text.TextUtils;

import com.umeng.analytics.a;

public class UMPlatformData {
    private UMedia a;
    private String b = "";
    private String c = "";
    private String d;
    private GENDER e;

    public UMPlatformData(UMedia uMedia, String str) {
        if (uMedia == null || TextUtils.isEmpty(str)) {
            b.b(a.e, "parameter is not valid");
            return;
        }
        this.a = uMedia;
        this.b = str;
    }

    public String getWeiboId() {
        return this.c;
    }

    public void setWeiboId(String str) {
        this.c = str;
    }

    public UMedia getMeida() {
        return this.a;
    }

    public String getUsid() {
        return this.b;
    }

    public String getName() {
        return this.d;
    }

    public void setName(String str) {
        this.d = str;
    }

    public GENDER getGender() {
        return this.e;
    }

    public void setGender(GENDER gender) {
        this.e = gender;
    }

    public boolean isValid() {
        if (this.a == null || TextUtils.isEmpty(this.b)) {
            return false;
        }
        return true;
    }

    public String toString() {
        return "UMPlatformData [meida=" + this.a + ", usid=" + this.b + ", weiboId=" + this.c +
                ", name=" + this.d + ", gender=" + this.e + "]";
    }
}
