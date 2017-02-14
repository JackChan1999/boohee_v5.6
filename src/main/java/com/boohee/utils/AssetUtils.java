package com.boohee.utils;

import android.content.Context;

import com.qiniu.android.common.Constants;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.util.EncodingUtils;
import org.json.JSONObject;

public class AssetUtils {
    public static String readStringFromFile(Context context, String fileName) {
        if (context == null) {
            return null;
        }
        InputStream in = null;
        try {
            in = context.getAssets().open(fileName);
            byte[] buffer = new byte[in.available()];
            in.read(buffer);
            String source = EncodingUtils.getString(buffer, Constants.UTF_8);
            if (in == null) {
                return source;
            }
            try {
                in.close();
                return source;
            } catch (IOException e) {
                e.printStackTrace();
                return source;
            }
        } catch (IOException e2) {
            e2.printStackTrace();
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e22) {
                    e22.printStackTrace();
                }
            }
            return null;
        } catch (Throwable th) {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e222) {
                    e222.printStackTrace();
                }
            }
        }
    }

    public static JSONObject loadJSONFromAsset(Context context, String fileName) throws
            IOException {
        try {
            return new JSONObject(readStringFromFile(context, fileName));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
