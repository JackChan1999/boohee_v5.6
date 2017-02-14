package com.boohee.uploader;

import com.qiniu.android.storage.UploadManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class QiniuConfig {
    private static final String API_URL    = "/api/v1/uploaders/multi_credential.json";
    public static final  String DOMAIN_PRO = "http://one.boohee.com";
    public static final  String DOMAIN_QA  = "http://one.iboohee.cn";
    public static final  String DOMAIN_RC  = "http://one-rc.iboohee.cn";
    public static final  int    MAX_HEIGHT = 800;
    public static final  int    MAX_SIZE   = 512000;
    public static final  int    MAX_WIDTH  = 480;
    private static UploadManager mUploadManager;
    private static String        mUrl;

    public @interface Domain {
    }

    public enum Prefix {
        boohee,
        mboohee,
        lightweb,
        light,
        record,
        passport,
        ifood,
        one,
        plan,
        bingo,
        status,
        food,
        messenger,
        openapp
    }

    public static void init(@Domain String uploadDomain) {
        mUrl = uploadDomain;
    }

    public static String getURL() {
        return mUrl + API_URL;
    }

    public static String createFileName(Prefix prefix) {
        String name = UUID.randomUUID().toString();
        return String.format("%s/%s/%s", new Object[]{prefix.name(), getDateString(), name});
    }

    private static String getDateString() {
        try {
            return new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(new Date());
        } catch (Exception e) {
            return "1970/01/01";
        }
    }

    public static UploadManager getUploadManager() {
        if (mUploadManager == null) {
            mUploadManager = new UploadManager();
        }
        return mUploadManager;
    }
}
