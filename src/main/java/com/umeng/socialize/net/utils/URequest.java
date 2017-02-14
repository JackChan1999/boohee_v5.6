package com.umeng.socialize.net.utils;

import com.tencent.connect.common.Constants;

import java.util.Map;

import org.json.JSONObject;

public abstract class URequest {
    protected static String GET  = "GET";
    protected static String POST = Constants.HTTP_POST;
    protected String mBaseUrl;

    public static class FilePair {
        byte[] mBinaryData;
        String mFileName;

        public FilePair(String str, byte[] bArr) {
            this.mFileName = str;
            this.mBinaryData = bArr;
        }
    }

    public abstract String toGetUrl();

    public abstract JSONObject toJson();

    protected String getHttpMethod() {
        return POST;
    }

    public URequest(String str) {
        this.mBaseUrl = str;
    }

    public void setBaseUrl(String str) {
        this.mBaseUrl = str;
    }

    public String getBaseUrl() {
        return this.mBaseUrl;
    }

    public Map<String, Object> getBodyPair() {
        return null;
    }

    public Map<String, FilePair> getFilePair() {
        return null;
    }
}
