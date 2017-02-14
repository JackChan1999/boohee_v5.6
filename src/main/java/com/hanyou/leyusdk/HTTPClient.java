package com.hanyou.leyusdk;

import android.content.Context;

import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class HTTPClient {
    public HTTPClient(Context ctx) {
    }

    public String getInput(String httpUrl) {
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(httpUrl).openConnection();
            conn.setDoInput(true);
            conn.setConnectTimeout(10000);
            conn.setRequestMethod("GET");
            System.out.println(conn.getResponseCode());
            if (conn.getResponseCode() == 200) {
                return new String(readStream(conn.getInputStream()), "UTF-8");
            }
            return null;
        } catch (Exception e) {
            return "";
        }
    }

    public String PostConnect(String httpUrl, String str) {
        String resultData = "";
        HttpPost httpPost = new HttpPost(httpUrl);
        List<NameValuePair> params = new ArrayList();
        params.add(new BasicNameValuePair("data", str));
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            HttpResponse httpResponse = new DefaultHttpClient().execute(httpPost);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                resultData = EntityUtils.toString(httpResponse.getEntity());
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e2) {
            e2.printStackTrace();
        } catch (IOException e3) {
            e3.printStackTrace();
        }
        return resultData;
    }

    public String Connect(String httpUrl) throws Exception {
        String resultData = "";
        URL url = null;
        try {
            url = new URL(httpUrl);
        } catch (MalformedURLException e) {
        }
        if (url == null) {
            return resultData;
        }
        try {
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            urlConn.setConnectTimeout(BaseImageDownloader.DEFAULT_HTTP_CONNECT_TIMEOUT);
            if (urlConn.getResponseCode() == 200) {
                InputStream is = urlConn.getInputStream();
                String resultData2 = new String(readStream(is));
                try {
                    is.close();
                    resultData = resultData2;
                } catch (IOException e2) {
                    return resultData2;
                }
            }
            resultData = "";
            urlConn.disconnect();
            return resultData;
        } catch (IOException e3) {
            return resultData;
        }
    }

    public static byte[] readStream(InputStream inputStream) throws Exception {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        while (true) {
            int len = inputStream.read(buffer);
            if (len == -1) {
                bout.close();
                inputStream.close();
                return bout.toByteArray();
            }
            bout.write(buffer, 0, len);
        }
    }
}
