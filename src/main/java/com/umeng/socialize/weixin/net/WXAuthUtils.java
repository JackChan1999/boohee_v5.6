package com.umeng.socialize.weixin.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class WXAuthUtils {
    public static String request(String urlStr) {
        String emptyStr = "";
        try {
            URLConnection conn = new URL(urlStr).openConnection();
            if (conn != null) {
                conn.connect();
                InputStream inputStream = conn.getInputStream();
                if (inputStream != null) {
                    emptyStr = convertStream(inputStream);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return emptyStr;
    }

    private static String convertStream(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while (true) {
            line = reader.readLine();
            if (line == null) {
                return result.trim();
            }
            result = result + line;
        }
    }
}
