package com.boohee.one.http;

import android.net.Uri;
import android.text.TextUtils;

import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.boohee.database.OnePreference;
import com.boohee.one.MyApplication;
import com.boohee.one.http.client.BooheeClient;
import com.boohee.utility.RegularUtils;
import com.boohee.utility.TimeLinePatterns;
import com.boohee.utils.BlackTech;
import com.boohee.utils.DateFormatUtils;
import com.boohee.utils.Helper;

import java.util.Arrays;
import java.util.Date;

public class DnspodFree {
    public static       String   BH_ALL      = BooheeClient.getHost(BooheeClient.BH_ALL);
    public static final long     CACHED_TIME = 3600000;
    public static final String   DATE_SPLIT  = ",";
    public static final String   IP_SPLIT    = ";";
    static final        String   TAG         = DnspodFree.class.getSimpleName();
    public static       String   freeUrl     = "http://119.29.29.29/d?dn=%s";
    public static       String[] hosts       = new String[]{BooheeClient.getHost(BooheeClient
            .API), BooheeClient.getHost(BooheeClient.FOOD), BooheeClient.getHost("ifood"),
            BooheeClient.getHost(BooheeClient.MESSENGER), BooheeClient.getHost(BooheeClient.ONE),
            BooheeClient.getHost(BooheeClient.BINGO), BooheeClient.getHost(BooheeClient.PASSPORT)
            , BooheeClient.getHost("record"), BooheeClient.getHost("status")};

    public interface OnIpGetListener {
        void onIpGet();
    }

    public static void getIpWithHost(String host) {
        getIpWithHost(host, null);
    }

    public static void getIpWithHost(final String host, final OnIpGetListener listener) {
        String now = DateFormatUtils.date2string(new Date(), "yyyy-MM-dd'T'HH:mm:ss");
        if (getCachedDate(host) == null || DateFormatUtils.getDifference(getCachedDate(host),
                now) > 3600000) {
            RequestManager.addRequest(new StringRequest(String.format(freeUrl, new
                    Object[]{host}), new Listener<String>() {
                public void onResponse(String response) {
                    Helper.showLog(DnspodFree.TAG, host + "," + response);
                    if (!TextUtils.isEmpty(response)) {
                        String ip = response.split(DnspodFree.IP_SPLIT)[0];
                        if (!TextUtils.isEmpty(ip)) {
                            ip = ip.trim();
                        }
                        if (RegularUtils.isIP(ip)) {
                            OnePreference.getInstance(MyApplication.getContext()).putString(host,
                                    ip + "," + DateFormatUtils.currentTimeString());
                        }
                    }
                    if (listener != null) {
                        listener.onIpGet();
                    }
                }
            }, null), null);
        }
    }

    public static void getIpWithHostNoCache(final String host, final boolean isSplit) {
        RequestManager.addRequest(new StringRequest(String.format(freeUrl, new Object[]{host}),
                new Listener<String>() {
            public void onResponse(String response) {
                String ip = response;
                if (!TextUtils.isEmpty(ip)) {
                    if (isSplit) {
                        ip = response.split(DnspodFree.IP_SPLIT)[0];
                    }
                    if (!TextUtils.isEmpty(ip)) {
                        ip = ip.trim();
                    }
                    OnePreference.getInstance(MyApplication.getContext()).putString(host, ip);
                }
            }
        }, null), null);
    }

    public static String getBestUrl(String originUrl) {
        String url = originUrl;
        if (!url.startsWith(TimeLinePatterns.WEB_SCHEME)) {
            url = TimeLinePatterns.WEB_SCHEME + url;
        }
        if (!BlackTech.isIPConnectOpen() || !BlackTech.isCanIPConnect()) {
            return url;
        }
        String host = Uri.parse(url).getHost();
        String ip = getCachedIp(host);
        if (!TextUtils.isEmpty(BlackTech.getCurrentIp())) {
            ip = BlackTech.getCurrentIp();
        }
        if (RegularUtils.isIP(ip)) {
            return url.replace(host, ip);
        }
        return url;
    }

    public static String getCachedIp(String host) {
        String ip = "";
        String prefString = OnePreference.getInstance(MyApplication.getContext()).getString(host);
        if (TextUtils.isEmpty(prefString)) {
            return ip;
        }
        String[] temp = prefString.split(",");
        return temp != null ? temp[0] : "";
    }

    public static String getCachedDate(String host) {
        String date = null;
        String prefString = OnePreference.getInstance(MyApplication.getContext()).getString(host);
        if (TextUtils.isEmpty(prefString)) {
            return null;
        }
        String[] temp = prefString.split(",");
        if (temp != null && temp.length > 1) {
            date = temp != null ? temp[1] : "";
        }
        return date;
    }

    public static String getNextCacheIp() {
        String currentIp = BlackTech.getCurrentIp();
        String ips = getCachedIp(BooheeClient.getHost(BooheeClient.BH_ALL));
        if (TextUtils.isEmpty(ips) || TextUtils.isEmpty(currentIp)) {
            return "";
        }
        String[] ipList = ips.split(IP_SPLIT);
        if (ipList == null || ipList.length <= 0) {
            return "";
        }
        int i;
        Arrays.sort(ipList);
        int nextIndex = 0;
        for (i = 0; i < ipList.length; i++) {
            if (currentIp.equalsIgnoreCase(ipList[i])) {
                nextIndex = i + 1;
                if (nextIndex >= ipList.length) {
                    nextIndex = 0;
                }
            }
        }
        for (i = 0; i < ipList.length; i++) {
            if (!currentIp.split("\\.")[0].equalsIgnoreCase(ipList[nextIndex].split("\\.")[0])) {
                return ipList[nextIndex];
            }
            nextIndex++;
            if (nextIndex >= ipList.length) {
                nextIndex = 0;
            }
        }
        return ipList[nextIndex];
    }

    public static void clearIpCache() {
        OnePreference prefs = OnePreference.getInstance(MyApplication.getContext());
        for (String host : hosts) {
            prefs.remove(host);
        }
        prefs.remove(BooheeClient.getHost(BooheeClient.BH_ALL));
        BlackTech.removeCurrentIp();
    }
}
