package com.umeng.socialize.net.utils;

import android.text.TextUtils;

import com.boohee.utils.Utils;
import com.loopj.android.http.AsyncHttpClient;
import com.umeng.socialize.net.utils.URequest.FilePair;
import com.umeng.socialize.utils.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;

public class UClient {
    private static final String TAG = UClient.class.getName();
    private Map<String, String> mHeaders;
    private StringBuilder       mRequestInfo;

    public <T extends UResponse> T execute(URequest uRequest, Class<T> cls) {
        JSONObject httpRequestGet;
        String trim = uRequest.getHttpMethod().trim();
        verifyMethod(trim);
        this.mRequestInfo = new StringBuilder();
        if (URequest.GET.equals(trim)) {
            httpRequestGet = httpRequestGet(uRequest);
        } else if (URequest.POST.equals(trim)) {
            httpRequestGet = httpRequestPost(uRequest.mBaseUrl, uRequest);
        } else {
            httpRequestGet = null;
        }
        if (httpRequestGet == null) {
            return null;
        }
        try {
            return (UResponse) cls.getConstructor(new Class[]{JSONObject.class}).newInstance(new
                    Object[]{httpRequestGet});
        } catch (Exception e) {
            Log.e(TAG, "SecurityException", e);
            return null;
        } catch (Exception e2) {
            Log.e(TAG, "NoSuchMethodException", e2);
            return null;
        } catch (Exception e22) {
            Log.e(TAG, "IllegalArgumentException", e22);
            return null;
        } catch (Exception e222) {
            Log.e(TAG, "InstantiationException", e222);
            return null;
        } catch (Exception e2222) {
            Log.e(TAG, "IllegalAccessException", e2222);
            return null;
        } catch (Exception e22222) {
            Log.e(TAG, "InvocationTargetException", e22222);
            return null;
        }
    }

    private JSONObject httpRequestPost(String str, URequest uRequest) {
        String str2;
        if (uRequest.toJson() == null) {
            str2 = "";
        } else {
            str2 = uRequest.toJson().toString();
        }
        int nextInt = new Random().nextInt(1000);
        System.getProperty("line.separator");
        Object httpPost = new HttpPost(str);
        HttpParams basicHttpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(basicHttpParams, 10000);
        HttpConnectionParams.setSoTimeout(basicHttpParams, 30000);
        HttpClient defaultHttpClient = new DefaultHttpClient(basicHttpParams);
        try {
            Map bodyPair = uRequest.getBodyPair();
            if (bodyPair == null || bodyPair.size() <= 0) {
                List arrayList = new ArrayList(1);
                arrayList.add(new BasicNameValuePair(Utils.RESPONSE_CONTENT, str2));
                httpPost.setEntity(new UrlEncodedFormEntity(arrayList, "UTF-8"));
            } else {
                HttpEntity multipartEntity = new MultipartEntity();
                for (String str22 : bodyPair.keySet()) {
                    multipartEntity.addPart(str22, new StringBody(bodyPair.get(str22).toString(),
                            Charset.defaultCharset()));
                }
                Map filePair = uRequest.getFilePair();
                if (filePair != null && filePair.size() > 0) {
                    for (String str222 : filePair.keySet()) {
                        FilePair filePair2 = (FilePair) filePair.get(str222);
                        byte[] bArr = filePair2.mBinaryData;
                        if (bArr != null && bArr.length >= 1) {
                            String str3 = TextUtils.isEmpty(filePair2.mFileName) ? "" + System
                                    .currentTimeMillis() : filePair2.mFileName;
                            ContentBody byteArrayBody = new ByteArrayBody(bArr, str3);
                            multipartEntity.addPart(str222, byteArrayBody);
                            Log.i(TAG, nextInt + ":\tbody:  " + str222 + "=" + byteArrayBody);
                            outprint(nextInt + ":\tbody:  " + str222 + "=[" + str3 + "]");
                        }
                    }
                }
                httpPost.setEntity(multipartEntity);
            }
            HttpResponse execute = defaultHttpClient.execute(httpPost);
            if (execute.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = execute.getEntity();
                if (entity == null) {
                    return null;
                }
                InputStream inputStream;
                InputStream content = entity.getContent();
                Header firstHeader = execute.getFirstHeader(AsyncHttpClient
                        .HEADER_CONTENT_ENCODING);
                if (firstHeader == null || !firstHeader.getValue().equalsIgnoreCase("deflate")) {
                    inputStream = content;
                } else {
                    inputStream = new InflaterInputStream(content);
                }
                Object trim = AesHelper.decryptNoPadding(convertStreamToString(inputStream),
                        "UTF-8").trim();
                if (TextUtils.isEmpty(trim)) {
                    return null;
                }
                return new JSONObject(trim);
            }
            Log.d(TAG, nextInt + ":\tFailed to send message." + str);
            return null;
        } catch (Exception e) {
            Log.d(TAG, nextInt + ":\tClientProtocolException,Failed to send message." + str, e);
            return null;
        } catch (Exception e2) {
            Log.d(TAG, nextInt + ":\tIOException,Failed to send message." + str, e2);
            return null;
        } catch (Exception e22) {
            Log.d(TAG, nextInt + ":\tIOException,Failed to send message." + str, e22);
            e22.printStackTrace();
            return null;
        } catch (Exception e222) {
            Log.e(TAG, "### POST response decrypt Failed!  " + e222.toString());
            e222.printStackTrace();
            return null;
        }
    }

    private void outprint(String str) {
        this.mRequestInfo.append(str);
    }

    private static String convertStreamToString(InputStream inputStream) {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream),
                8192);
        StringBuilder stringBuilder = new StringBuilder();
        while (true) {
            try {
                String readLine = bufferedReader.readLine();
                if (readLine == null) {
                    break;
                }
                stringBuilder.append(readLine + "\n");
            } catch (Exception e) {
                stringBuilder = TAG;
                Log.e(stringBuilder, "Caught IOException in convertStreamToString()", e);
                return null;
            } finally {
                try {
                    inputStream.close();
                } catch (Exception e2) {
                    Log.e(TAG, "Caught IOException in convertStreamToString()", e2);
                    return null;
                }
            }
        }
        return stringBuilder.toString();
    }

    private JSONObject httpRequestGet(URequest uRequest) {
        int nextInt = new Random().nextInt(1000);
        String toGetUrl = uRequest.toGetUrl();
        try {
            if (toGetUrl.length() <= 1) {
                Log.e(TAG, nextInt + ":\tInvalid baseUrl.");
                return null;
            }
            outprint(nextInt + ":\tget: " + toGetUrl);
            HttpUriRequest httpGet = new HttpGet(toGetUrl);
            httpGet.addHeader(AsyncHttpClient.HEADER_ACCEPT_ENCODING, AsyncHttpClient
                    .ENCODING_GZIP);
            if (this.mHeaders != null && this.mHeaders.size() > 0) {
                for (String str : this.mHeaders.keySet()) {
                    httpGet.addHeader(str, (String) this.mHeaders.get(str));
                }
            }
            HttpParams basicHttpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(basicHttpParams, 10000);
            HttpConnectionParams.setSoTimeout(basicHttpParams, 20000);
            HttpResponse execute = new DefaultHttpClient(basicHttpParams).execute(httpGet);
            if (execute.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = execute.getEntity();
                if (entity != null) {
                    InputStream gZIPInputStream;
                    InputStream content = entity.getContent();
                    Header firstHeader = execute.getFirstHeader(AsyncHttpClient
                            .HEADER_CONTENT_ENCODING);
                    if (firstHeader != null && firstHeader.getValue().equalsIgnoreCase
                            (AsyncHttpClient.ENCODING_GZIP)) {
                        Log.i(TAG, nextInt + "  Use GZIPInputStream get data....");
                        gZIPInputStream = new GZIPInputStream(content);
                    } else if (firstHeader == null || !firstHeader.getValue().equalsIgnoreCase
                            ("deflate")) {
                        gZIPInputStream = content;
                    } else {
                        Log.i(TAG, nextInt + "  Use InflaterInputStream get data....");
                        gZIPInputStream = new InflaterInputStream(content);
                    }
                    String trim = AesHelper.decryptNoPadding(convertStreamToString
                            (gZIPInputStream), "UTF-8").trim();
                    if (trim == null) {
                        return null;
                    }
                    return new JSONObject(trim);
                }
            }
            Log.d(TAG, nextInt + ":\tFailed to get message." + toGetUrl);
            return null;
        } catch (Exception e) {
            Log.d(TAG, nextInt + ":\tClientProtocolException,Failed to send message." + toGetUrl,
                    e);
            return null;
        } catch (Exception e2) {
            Log.d(TAG, nextInt + ":\tIOException,Failed to send message." + toGetUrl, e2);
            return null;
        }
    }

    public UClient setHeader(Map<String, String> map) {
        this.mHeaders = map;
        return this;
    }

    private void verifyMethod(String str) {
        if (TextUtils.isEmpty(str) || (URequest.GET.equals(str.trim()) ^ URequest.POST.equals(str
                .trim())) == 0) {
            throw new RuntimeException("验证请求方式失败[" + str + "]");
        }
    }
}
