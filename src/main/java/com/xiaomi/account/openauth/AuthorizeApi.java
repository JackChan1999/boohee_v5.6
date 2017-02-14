package com.xiaomi.account.openauth;

import android.content.Context;

import com.xiaomi.account.openauth.utils.Network;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

public class AuthorizeApi {
    private static       String HOST          = "open.account.xiaomi.com";
    private static       String HTTP_PROTOCOL = "https://";
    private static       String METHOD_GET    = "GET";
    private static final String UTF8          = "UTF-8";

    public static String doHttpGet(Context context, String path, long clientId, String
            accessToken) throws XMAuthericationException {
        List<NameValuePair> params = new ArrayList();
        params.add(new BasicNameValuePair("clientId", String.valueOf(clientId)));
        params.add(new BasicNameValuePair("token", accessToken));
        try {
            return Network.downloadXml(context, new URL(AuthorizeHelper.generateUrl(HTTP_PROTOCOL
                    + HOST + path, params)), null, null, null, null);
        } catch (Throwable e) {
            throw new XMAuthericationException(e);
        } catch (Throwable e2) {
            throw new XMAuthericationException(e2);
        }
    }

    public static String doHttpGet(Context context, String path, long clientId, String
            accessToken, String macKey, String macAlgorithm) throws XMAuthericationException {
        List<NameValuePair> params = new ArrayList();
        params.add(new BasicNameValuePair("clientId", String.valueOf(clientId)));
        params.add(new BasicNameValuePair("token", accessToken));
        String nonce = AuthorizeHelper.generateNonce();
        try {
            return Network.downloadXml(context, new URL(AuthorizeHelper.generateUrl(HTTP_PROTOCOL
                    + HOST + path, params)), null, null, AuthorizeHelper.buildMacRequestHead
                    (accessToken, nonce, AuthorizeHelper.getMacAccessTokenSignatureString(nonce,
                            METHOD_GET, HOST, path, URLEncodedUtils.format(params, "UTF-8"),
                            macKey, macAlgorithm)), null);
        } catch (Throwable e) {
            throw new XMAuthericationException(e);
        } catch (Throwable e2) {
            throw new XMAuthericationException(e2);
        } catch (Throwable e22) {
            throw new XMAuthericationException(e22);
        } catch (Throwable e222) {
            throw new XMAuthericationException(e222);
        }
    }
}
