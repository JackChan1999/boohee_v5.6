package com.umeng.socialize.bean;

import com.umeng.socialize.exception.SocializeException;

public class SNSPair {
    public String mPaltform;
    public String mUsid;

    public SNSPair(String str, String str2) {
        this.mPaltform = str;
        this.mUsid = str2;
    }

    public String toFormat() throws SocializeException {
        if (this.mPaltform != null) {
            if (this.mUsid == null) {
                this.mUsid = "";
            }
            return "{" + this.mPaltform.toString() + ":" + this.mUsid + "}";
        }
        throw new SocializeException("can`t format snspair string.");
    }
}
