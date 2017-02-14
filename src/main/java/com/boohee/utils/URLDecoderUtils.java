package com.boohee.utils;

import com.qiniu.android.common.Constants;

import java.net.URLDecoder;

public class URLDecoderUtils {
    public static String replaceAndDecode(String data) {
        try {
            data = URLDecoder.decode(data.replaceAll("%(?![0-9a-fA-F]{2})", "%25").replaceAll
                    ("\\+", "%2B"), Constants.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
}
