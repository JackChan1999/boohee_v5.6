package com.umeng.socialize.bean;

import android.text.TextUtils;

public class UMToken extends SNSPair {
    private String a;
    private String b;
    private String c;
    private String d;
    private String e;
    private String f;
    private String g;
    private String h;

    private UMToken(String str, String str2) {
        super(str, str2);
    }

    public String getToken() {
        return this.a;
    }

    public void setToken(String str) {
        this.a = str;
    }

    public String getOpenId() {
        return this.b;
    }

    public void setOpenId(String str) {
        this.b = str;
    }

    public String getAppId() {
        return this.c;
    }

    public void setAppId(String str) {
        this.c = str;
    }

    public String getAppKey() {
        return this.d;
    }

    public void setAppKey(String str) {
        this.d = str;
    }

    public String getExpireIn() {
        return this.e;
    }

    public void setExpireIn(String str) {
        this.e = str;
    }

    public void setRefreshToken(String str) {
        this.f = str;
    }

    public String getRefreshToken() {
        return this.f;
    }

    public void setScope(String str) {
        this.g = str;
    }

    public String getScope() {
        return this.g;
    }

    public void setUmengSecret(String str) {
        this.h = str;
    }

    public String getUmengSecret() {
        return this.h;
    }

    public boolean isValid() {
        return (TextUtils.isEmpty(getToken()) || TextUtils.isEmpty(this.mPaltform) || TextUtils
                .isEmpty(this.mUsid) || ((this.mPaltform.equals(SHARE_MEDIA.QZONE.toString()) ||
                this.mPaltform.equals(SHARE_MEDIA.TENCENT.toString())) && TextUtils.isEmpty
                (getOpenId()))) ? false : true;
    }

    public static UMToken buildToken(SNSPair sNSPair, String str) {
        UMToken uMToken = new UMToken(sNSPair.mPaltform, sNSPair.mUsid);
        uMToken.setToken(str);
        return uMToken;
    }

    public static UMToken buildToken(SNSPair sNSPair, String str, String str2) {
        UMToken uMToken = new UMToken(sNSPair.mPaltform, sNSPair.mUsid);
        uMToken.setToken(str);
        uMToken.setOpenId(str2);
        return uMToken;
    }
}
