package com.kitnew.ble.utils;

import android.content.Context;
import android.os.AsyncTask;

import com.boohee.utility.TimeLinePatterns;
import com.tencent.connect.common.Constants;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FileUtils {
    static final  String  QING_NIU_DIR     = "/qingniu/";
    static final  String  URL_CHECK_APP_ID = "commons/get_business_app_id";
    static final  String  URL_POST_DATA    = "measurements/synchronize_business.json";
    static final  String  encoding         = "UTF-8";
    static        String  host             = "api.yolanda.hk";
    static        String  host_sit         = "api.sit.yolanda.hk";
    public static boolean isRelease        = false;

    public interface NetCallback {
        void onFailure(Throwable th);

        void onSuccess(String str);
    }

    public static void writerStringToFile(String str, String filename) {
        File file = new File(filename);
        try {
            if (!file.exists()) {
                new File(file.getParent()).mkdirs();
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(str.getBytes());
            fos.close();
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
    }

    public static void deleteFile(String filename) {
        deleteFile(new File(filename));
    }

    public static void deleteFile(File file) {
        if (file != null && file.exists()) {
            file.delete();
        }
    }

    public static void writeStringToFileWithEncrypt(String str, String filename) {
        writerStringToFile(EncryptUtils.encrypt(str), filename);
    }

    public static void appendStringToFile(String fileName, String content) {
        try {
            FileWriter writer = new FileWriter(fileName, true);
            writer.write(content);
            writer.close();
        } catch (IOException e) {
        }
    }

    public static String getStringFromFileWithDecrypt(String filename) {
        return EncryptUtils.decrypt(getStringFromFile(new File(filename)));
    }

    public static String getStringFromFile(String filename) {
        return getStringFromFile(new File(filename));
    }

    public static String getStringFromFileWithDecrypt(File file) {
        return EncryptUtils.decrypt(getStringFromFile(file));
    }

    public static String getStringFromFile(File file) {
        String str = null;
        try {
            if (file.exists() && file.canRead()) {
                BufferedReader br = new BufferedReader(new FileReader(file));
                StringBuilder sb = new StringBuilder();
                while (true) {
                    String line = br.readLine();
                    if (line == null) {
                        break;
                    }
                    sb.append(line);
                }
                br.close();
                str = sb.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    public static String getDirectPath(Context context) {
        return context.getFilesDir() + QING_NIU_DIR;
    }

    private static String url(String path) {
        return TimeLinePatterns.WEB_SCHEME + (isRelease ? host : host_sit) + "/api/v5/" + path;
    }

    public static void checkAppId(String appId, NetCallback callback) {
        Map<String, Object> params = new HashMap();
        params.put("business_app_id", appId);
        params.put("current_sdk_revision", "2.0");
        doPost(url(URL_CHECK_APP_ID), params, callback);
    }

    public static void postData(String appId, JSONObject data, NetCallback callback) {
        postData(appId, Collections.singletonList(data), callback);
    }

    public static void postData(String appId, List<JSONObject> datas, NetCallback callback) {
        Map<String, Object> params = new HashMap();
        params.put("business_app_id", appId);
        JSONArray jsonArray = new JSONArray();
        for (JSONObject qnData : datas) {
            jsonArray.put(qnData);
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("measurements", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        params.put("json", jsonObject);
        doPost(url(URL_POST_DATA), params, callback);
    }

    public static void doPost(String urlString, final Map<String, Object> params, final
    NetCallback callback) {
        new AsyncTask<String, Integer, String>() {
            protected String doInBackground(String... ps) {
                try {
                    HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(ps[0])
                            .openConnection();
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setConnectTimeout(20000);
                    httpURLConnection.setReadTimeout(20000);
                    httpURLConnection.setRequestMethod(Constants.HTTP_POST);
                    httpURLConnection.setRequestProperty("Content-type", "text/plain");
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    outputStream.write(FileUtils.formatParams(params).getBytes("UTF-8"));
                    outputStream.flush();
                    outputStream.close();
                    return new String(FileUtils.readInputStream(httpURLConnection.getInputStream
                            ()), "UTF-8");
                } catch (Exception e) {
                    callback.onFailure(e);
                    return null;
                }
            }

            protected void onPostExecute(String s) {
                if (s != null) {
                    callback.onSuccess(s);
                }
            }
        }.execute(new String[]{urlString});
    }

    static byte[] readInputStream(InputStream inStream) throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        while (true) {
            int len = inStream.read(buffer);
            if (len != -1) {
                outStream.write(buffer, 0, len);
            } else {
                byte[] data = outStream.toByteArray();
                outStream.close();
                inStream.close();
                return data;
            }
        }
    }

    static String formatParams(Map<String, Object> params) {
        JSONObject jsonObject = new JSONObject();
        for (Entry<String, Object> entry : params.entrySet()) {
            if (entry.getValue() != null) {
                try {
                    jsonObject.put((String) entry.getKey(), entry.getValue().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return EncryptUtils.encrypt(jsonObject.toString());
    }
}
