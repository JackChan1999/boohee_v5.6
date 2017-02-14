package com.xiaomi.account.openauth;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.text.TextUtils;

import com.boohee.model.UploadFood;
import com.tencent.connect.common.Constants;
import com.umeng.socialize.net.utils.SocializeProtocolConstants;
import com.xiaomi.account.openauth.utils.Base64Coder;
import com.xiaomi.account.openauth.utils.Network;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

public class AuthorizeHelper {
    protected static final String ACTION_AUTH_ACTIVITY = "com.xiaomi.account.openauth.action.AUTH";
    protected static final String AUTHORIZATION_CODE   = "authorization_code";
    public static          String AUTH_ACTIVITY_NAME   = "com.xiaomi.account.openauth" +
            ".AuthorizeActivity";
    protected static final String HEADER_FLAG          = "&&&START&&&";
    private static final   String HMAC_SHA1            = "HmacSHA1";
    public static final String OAUTH2_HOST;
    protected static final String  REFRESH_TOKEN = "refresh_token";
    protected static final String  TOKEN_PATH    = (OAUTH2_HOST + "/oauth2/token");
    public static final    boolean USE_PREVIEW   = new File("/data/system/oauth_staging_preview")
            .exists();
    private static final   String  UTF8          = "UTF-8";
    private static         Random  random        = new Random();

    static {
        String str;
        if (USE_PREVIEW) {
            str = "http://account.preview.n.xiaomi.net";
        } else {
            str = "https://account.xiaomi.com";
        }
        OAUTH2_HOST = str;
    }

    protected static void startAuthorizeActivityForResult(Activity activity, long clientId,
                                                          String redirecURI, String responseType,
                                                          String scope, String state, int code) {
        if (clientId < 0) {
            throw new IllegalArgumentException("client id is error!!!");
        } else if (TextUtils.isEmpty(redirecURI)) {
            throw new IllegalArgumentException("redirect url is empty!!!");
        } else if (TextUtils.isEmpty(responseType)) {
            throw new IllegalArgumentException("responseType is empty!!!");
        } else {
            Intent intent = getIntent(activity);
            intent.setPackage(activity.getPackageName());
            Bundle basicBundle = new Bundle();
            basicBundle.putString(Constants.PARAM_CLIENT_ID, String.valueOf(clientId));
            basicBundle.putString("redirect_uri", redirecURI);
            basicBundle.putString("response_type", responseType);
            basicBundle.putString("scope", scope);
            basicBundle.putString(UploadFood.STATE, state);
            intent.putExtra("url_param", basicBundle);
            activity.startActivityForResult(intent, code);
        }
    }

    private static Intent getIntent(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, AuthorizeActivity.class);
        return intent;
    }

    protected static String getAccessTokenByAuthorizationCode(Context context, String code, long
            clientId, String clientSecret, String redirectUri) throws IOException {
        List<NameValuePair> params = new ArrayList();
        params.add(new BasicNameValuePair(Constants.PARAM_CLIENT_ID, String.valueOf(clientId)));
        params.add(new BasicNameValuePair("grant_type", AUTHORIZATION_CODE));
        params.add(new BasicNameValuePair("client_secret", clientSecret));
        params.add(new BasicNameValuePair("token_type", SocializeProtocolConstants
                .PROTOCOL_KEY_MAC));
        params.add(new BasicNameValuePair("redirect_uri", redirectUri));
        params.add(new BasicNameValuePair("code", code));
        String result = Network.downloadXml(context, new URL(generateUrl(TOKEN_PATH, params)));
        if (TextUtils.isEmpty(result)) {
            return result;
        }
        return result.replace(HEADER_FLAG, "");
    }

    protected static String getAccessTokenByRefreshToken(Context context, String refreshToken,
                                                         long clientId, String clientSecret,
                                                         String redirectUri, String tokenType,
                                                         String macKey, String macAlgorithm)
            throws IOException {
        List<NameValuePair> params = new ArrayList();
        params.add(new BasicNameValuePair(Constants.PARAM_CLIENT_ID, String.valueOf(clientId)));
        params.add(new BasicNameValuePair("grant_type", "refresh_token"));
        params.add(new BasicNameValuePair("client_secret", clientSecret));
        params.add(new BasicNameValuePair("token_type", SocializeProtocolConstants
                .PROTOCOL_KEY_MAC));
        params.add(new BasicNameValuePair("redirect_uri", redirectUri));
        params.add(new BasicNameValuePair("refresh_token", refreshToken));
        params.add(new BasicNameValuePair("token_type", tokenType));
        params.add(new BasicNameValuePair("mac_key", macKey));
        params.add(new BasicNameValuePair("mac_algorithm", macAlgorithm));
        String result = Network.downloadXml(context, new URL(generateUrl(TOKEN_PATH, params)));
        if (TextUtils.isEmpty(result)) {
            return result;
        }
        return result.replace(HEADER_FLAG, "");
    }

    protected static String getMacAccessTokenSignatureString(String nonce, String method, String
            host, String uriPath, String query, String macKey, String macAlgorithm) throws
            InvalidKeyException, NoSuchAlgorithmException, UnsupportedEncodingException {
        if ("HmacSHA1".equalsIgnoreCase(macAlgorithm)) {
            StringBuilder joined = new StringBuilder("");
            joined.append(new StringBuilder(String.valueOf(nonce)).append("\n").toString());
            joined.append(method.toUpperCase() + "\n");
            joined.append(new StringBuilder(String.valueOf(host)).append("\n").toString());
            joined.append(new StringBuilder(String.valueOf(uriPath)).append("\n").toString());
            if (!TextUtils.isEmpty(query)) {
                StringBuffer sb = new StringBuffer();
                List<NameValuePair> paramList = new ArrayList();
                URLEncodedUtils.parse(paramList, new Scanner(query), "UTF-8");
                Collections.sort(paramList, new Comparator<NameValuePair>() {
                    public int compare(NameValuePair p1, NameValuePair p2) {
                        return p1.getName().compareTo(p2.getName());
                    }
                });
                sb.append(URLEncodedUtils.format(paramList, "UTF-8"));
                joined.append(sb.toString() + "\n");
            }
            return encodeSign(encryptHMACSha1(joined.toString().getBytes("UTF-8"), macKey
                    .getBytes("UTF-8")));
        }
        throw new NoSuchAlgorithmException("error mac algorithm : " + macAlgorithm);
    }

    protected static HashMap<String, String> buildMacRequestHead(String accessTokenId, String
            nonce, String mac) throws UnsupportedEncodingException {
        String headContent = String.format("MAC access_token=\"%s\", nonce=\"%s\",mac=\"%s\"",
                new Object[]{URLEncoder.encode(accessTokenId, "UTF-8"), URLEncoder.encode(nonce,
                        "UTF-8"), URLEncoder.encode(mac, "UTF-8")});
        HashMap<String, String> header = new HashMap();
        header.put("Authorization", headContent);
        return header;
    }

    protected static String generateNonce() {
        return new StringBuilder(String.valueOf(random.nextLong())).append(":").append((int)
                (System.currentTimeMillis() / 60000)).toString();
    }

    protected static String encodeSign(byte[] key) {
        return new String(Base64Coder.encode(key));
    }

    protected static byte[] encryptHMACSha1(byte[] data, byte[] key) throws
            NoSuchAlgorithmException, InvalidKeyException {
        SecretKeySpec signingKey = new SecretKeySpec(key, "HmacSHA1");
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(signingKey);
        mac.update(data);
        return mac.doFinal();
    }

    protected static String generateUrl(String url, List<NameValuePair> parameters) {
        String finalURL = url;
        if (parameters == null || parameters.size() <= 0) {
            return finalURL;
        }
        Builder builder = Uri.parse(finalURL).buildUpon();
        for (NameValuePair p : parameters) {
            builder.appendQueryParameter(p.getName(), p.getValue());
        }
        return builder.build().toString();
    }
}
