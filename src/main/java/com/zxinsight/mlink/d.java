package com.zxinsight.mlink;

import android.net.Uri;
import android.text.TextUtils;

import com.alipay.sdk.sys.a;
import com.zxinsight.MLink;
import com.zxinsight.MWConfiguration;
import com.zxinsight.common.util.l;
import com.zxinsight.common.util.m;
import com.zxinsight.common.util.n;
import com.zxinsight.mlink.domain.MLinkResult;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONException;
import org.json.JSONObject;

public class d {
    public static MLinkResult a(Uri uri, Uri uri2) {
        if (uri2 == null) {
            return new MLinkResult();
        }
        String scheme = uri2.getScheme();
        String host = uri2.getHost();
        int port = uri2.getPort();
        String encodedPath = uri2.getEncodedPath();
        String encodedQuery = uri2.getEncodedQuery();
        String scheme2 = uri.getScheme();
        String host2 = uri.getHost();
        int port2 = uri.getPort();
        String encodedPath2 = uri.getEncodedPath();
        String encodedQuery2 = uri.getEncodedQuery();
        if (n.a(scheme, scheme2) && n.a(host, host2) && port == port2) {
            return a(encodedPath, encodedPath2, a(encodedQuery2, encodedQuery));
        }
        return new MLinkResult();
    }

    public static MLinkResult a(String str, String str2, Map<String, String> map) {
        String[] split = str.split("/");
        String[] split2 = str2.split("/");
        MLinkResult mLinkResult = new MLinkResult();
        if (split.length != split2.length) {
            mLinkResult.flag = false;
        } else {
            int length = split.length;
            for (int i = 0; i < length; i++) {
                if (split[i].startsWith(":")) {
                    mLinkResult.params.put(Uri.decode(split[i].substring(1)), Uri.decode
                            (split2[i]));
                } else if (!split[i].equals(split2[i])) {
                    mLinkResult.flag = false;
                    break;
                }
            }
            if (l.b(map)) {
                mLinkResult.params.putAll(map);
            }
            mLinkResult.flag = true;
        }
        return mLinkResult;
    }

    private static Map<String, String> a(String str, String str2) {
        List<String> asList;
        List asList2;
        Map<String, String> linkedHashMap = new LinkedHashMap();
        Map linkedHashMap2 = new LinkedHashMap();
        if (str != null) {
            asList = Arrays.asList(str.split(a.b));
            if (l.b(asList)) {
                for (String split : asList) {
                    String split2;
                    asList2 = Arrays.asList(split2.split("="));
                    if (l.b(asList2) && asList2.size() == 2) {
                        linkedHashMap2.put((String) asList2.get(0), (String) asList2.get(1));
                    }
                }
            }
        }
        if (str2 != null) {
            asList = Arrays.asList(str2.split(a.b));
            if (l.b(asList)) {
                for (String split22 : asList) {
                    asList2 = Arrays.asList(split22.split("="));
                    if (l.b(asList2) && asList2.size() == 2) {
                        split22 = (String) asList2.get(0);
                        String str3 = (String) asList2.get(1);
                        if (str3.startsWith(":")) {
                            for (Entry entry : linkedHashMap2.entrySet()) {
                                if (((String) entry.getKey()).equals(str3.substring(1))) {
                                    linkedHashMap.put(Uri.decode(split22), Uri.decode((String)
                                            entry.getValue()));
                                }
                            }
                        } else {
                            linkedHashMap.put(Uri.decode(split22), Uri.decode(str3));
                        }
                    }
                }
            }
        }
        return linkedHashMap;
    }

    public static Map<String, String> a(String str) {
        Map<String, String> hashMap = new HashMap();
        if (l.b(str)) {
            Object split = str.split(a.b);
            if (l.b(split)) {
                for (String split2 : split) {
                    Object split3 = split2.split("=");
                    if (l.b(split3) && split3.length >= 1) {
                        if (split3.length == 2) {
                            hashMap.put(split3[0], Uri.decode(split3[1]));
                        } else {
                            hashMap.put(split3[0], "");
                        }
                    }
                }
            }
        }
        return hashMap;
    }

    public static Map<String, String> b(String str) {
        Map<String, String> hashMap = new HashMap();
        if (l.b(str)) {
            Object split = str.split(a.b);
            if (l.b(split)) {
                for (String split2 : split) {
                    Object split3 = split2.split("=");
                    if (l.b(split3) && split3.length >= 1) {
                        if (split3.length == 2) {
                            hashMap.put(split3[0], split3[1]);
                        } else {
                            hashMap.put(split3[0], "");
                        }
                    }
                }
            }
        }
        return hashMap;
    }

    public static Uri a(Uri uri) {
        String uri2 = uri.toString();
        if (TextUtils.isEmpty(uri2)) {
            return Uri.EMPTY;
        }
        m.a().q(uri.getQueryParameter("mw_ck"));
        Object encodedQuery = uri.getEncodedQuery();
        if (l.b(encodedQuery)) {
            String replace = uri2.replace(encodedQuery, "");
            Map b = b(encodedQuery);
            if (l.b(b)) {
                b.remove("mw_mlink_k");
                b.remove("mw_mlink_ak");
                b.remove("mw_mlink_appid");
                b.remove("mw_mk");
                b.remove("mw_slk");
                b.remove("mw_ck");
                b.remove("mw_tags");
                b.remove("mw_ulp");
                b.remove("mw_tk");
                MLink.getInstance(MWConfiguration.getContext()).urlCB = Uri.decode((String) b.get
                        ("mw_mlink_cb"));
                b.remove("mw_mlink_cb");
                StringBuilder stringBuilder = new StringBuilder();
                if (l.b(b)) {
                    for (Entry entry : b.entrySet()) {
                        stringBuilder.append((String) entry.getKey()).append("=").append((String)
                                entry.getValue()).append(a.b);
                    }
                    stringBuilder.deleteCharAt(stringBuilder.length() - 1);
                    uri2 = replace + stringBuilder.toString();
                }
            }
            uri2 = replace;
        }
        return Uri.parse(uri2);
    }

    public static String c(String str) {
        if (str == null) {
            return null;
        }
        Uri parse = Uri.parse(str);
        if (parse == null) {
            return null;
        }
        String str2;
        List<String> linkedList = new LinkedList(parse.getPathSegments());
        if (l.b(linkedList)) {
            for (String str22 : linkedList) {
                if (str22.startsWith(":")) {
                    return str22.substring(1);
                }
            }
        }
        linkedList = Arrays.asList(str.split(a.b));
        if (l.b(linkedList)) {
            for (String str222 : linkedList) {
                List asList = Arrays.asList(str222.split("="));
                if (l.b(asList) && asList.size() == 2) {
                    str222 = (String) asList.get(1);
                    if (str222.startsWith(":")) {
                        return str222.substring(1);
                    }
                }
            }
        }
        return "";
    }

    public static String a(String str, JSONObject jSONObject) {
        return a(str, jSONObject, 0);
    }

    public static String a(String str, JSONObject jSONObject, int i) {
        String decode = Uri.decode(str);
        if (!e(decode)) {
            return "";
        }
        Uri parse = Uri.parse(decode);
        if (parse == null) {
            return null;
        }
        Map b;
        if (jSONObject == null) {
            jSONObject = new JSONObject();
        }
        String scheme = parse.getScheme();
        String host = parse.getHost();
        decode = parse.getPath();
        String encodedQuery = parse.getEncodedQuery();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(scheme).append("://").append(host);
        Map a = a(new LinkedList(parse.getPathSegments()), jSONObject);
        if (l.b(a)) {
            decode = decode + "/";
            String str2 = decode;
            for (Entry entry : a.entrySet()) {
                str2 = str2.replace(":" + entry.getKey() + "/", entry.getValue() + "/");
            }
            decode = str2.substring(0, str2.length() - 1);
        }
        stringBuilder.append(decode);
        if (i == 1) {
            b = b(encodedQuery, jSONObject);
        } else if (i == 2) {
            b = d(encodedQuery, jSONObject);
        } else {
            b = c(encodedQuery, jSONObject);
        }
        stringBuilder.append("?");
        try {
            if (l.b(jSONObject.get("mw_mlink_appid"))) {
                stringBuilder.append("mw_mlink_appid").append("=").append(jSONObject.get
                        ("mw_mlink_appid")).append(a.b);
            }
            if (l.b(jSONObject.get("mw_mlink_k"))) {
                stringBuilder.append("mw_mlink_k").append("=").append(jSONObject.get
                        ("mw_mlink_k")).append(a.b);
            }
            if (l.b(jSONObject.get("mw_mlink_ak"))) {
                stringBuilder.append("mw_mlink_ak").append("=").append(jSONObject.get
                        ("mw_mlink_ak")).append(a.b);
            }
            if (l.b(jSONObject.get("mw_mk"))) {
                stringBuilder.append("mw_mk").append("=").append(jSONObject.get("mw_mk")).append
                        (a.b);
            }
        } catch (JSONException e) {
        }
        if (l.b(b)) {
            for (Entry entry2 : b.entrySet()) {
                stringBuilder.append((String) entry2.getKey()).append("=").append((String) entry2
                        .getValue()).append(a.b);
            }
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        return stringBuilder.toString();
    }

    private static Map<String, String> b(String str, JSONObject jSONObject) {
        if (jSONObject == null) {
            jSONObject = new JSONObject();
        }
        Map<String, String> linkedHashMap = new LinkedHashMap();
        if (str != null) {
            List<String> asList = Arrays.asList(str.split(a.b));
            if (l.b(asList)) {
                for (String split : asList) {
                    String split2;
                    List asList2 = Arrays.asList(split2.split("="));
                    if (l.b(asList2) && asList2.size() == 2) {
                        split2 = (String) asList2.get(0);
                        String str2 = (String) asList2.get(1);
                        linkedHashMap.put(split2, d(str2));
                        if (str2.startsWith(":")) {
                            Iterator keys = jSONObject.keys();
                            while (keys.hasNext()) {
                                String str3 = (String) keys.next();
                                if (str3.equals(str2.substring(1))) {
                                    linkedHashMap.put(split2, d(String.valueOf(jSONObject.opt
                                            (str3))));
                                }
                            }
                        }
                    }
                }
            }
        }
        return linkedHashMap;
    }

    private static Map<String, String> c(String str, JSONObject jSONObject) {
        if (jSONObject == null) {
            jSONObject = new JSONObject();
        }
        Map<String, String> linkedHashMap = new LinkedHashMap();
        if (str != null) {
            List<String> asList = Arrays.asList(str.split(a.b));
            if (l.b(asList)) {
                for (String split : asList) {
                    String split2;
                    List asList2 = Arrays.asList(split2.split("="));
                    if (l.b(asList2) && asList2.size() == 2) {
                        split2 = (String) asList2.get(0);
                        String str2 = (String) asList2.get(1);
                        if (str2.startsWith(":")) {
                            Iterator keys = jSONObject.keys();
                            while (keys.hasNext()) {
                                String str3 = (String) keys.next();
                                if (str3.equals(str2.substring(1))) {
                                    linkedHashMap.put(split2, d(String.valueOf(jSONObject.opt
                                            (str3))));
                                }
                            }
                        } else {
                            linkedHashMap.put(split2, d(str2));
                        }
                    }
                }
            }
        }
        return linkedHashMap;
    }

    private static Map<String, String> d(String str, JSONObject jSONObject) {
        String str2;
        if (jSONObject == null) {
            jSONObject = new JSONObject();
        }
        Map<String, String> linkedHashMap = new LinkedHashMap();
        if (str != null) {
            List<String> asList = Arrays.asList(str.split(a.b));
            if (l.b(asList)) {
                for (String str22 : asList) {
                    List asList2 = Arrays.asList(str22.split("="));
                    if (l.b(asList2) && asList2.size() == 2) {
                        str22 = (String) asList2.get(0);
                        String str3 = (String) asList2.get(1);
                        if (!str3.startsWith(":")) {
                            linkedHashMap.put(str22, d(str3));
                        }
                    }
                }
            }
        }
        Iterator keys = jSONObject.keys();
        while (keys.hasNext()) {
            str22 = (String) keys.next();
            linkedHashMap.put(str22, d(String.valueOf(jSONObject.opt(str22))));
        }
        return linkedHashMap;
    }

    private static Map<String, String> a(List<String> list, JSONObject jSONObject) {
        if (jSONObject == null || l.a(list)) {
            return null;
        }
        Map<String, String> hashMap = new HashMap();
        for (String str : list) {
            if (str.startsWith(":")) {
                String substring = str.substring(1);
                if (e(substring, jSONObject)) {
                    hashMap.put(substring, d(jSONObject.optString(substring)));
                } else {
                    hashMap.put(substring, str);
                }
            }
        }
        return hashMap;
    }

    private static boolean e(String str, JSONObject jSONObject) {
        return l.b(jSONObject.optString(str));
    }

    private static String d(String str) {
        return e(str) ? Uri.encode(str) : str;
    }

    private static boolean e(String str) {
        return str.contains("://") || str.contains("?") || str.contains(a.b) || str.contains("=")
                || str.contains("\\");
    }
}
