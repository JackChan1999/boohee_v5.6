package com.qiniu.android.utils;

import com.boohee.one.http.DnspodFree;

import java.net.InetAddress;
import java.net.UnknownHostException;

public final class Dns {
    public static String[] getAddresses(String hostName) {
        try {
            InetAddress[] ret = InetAddress.getAllByName(hostName);
            String[] strArr = new String[ret.length];
            for (int i = 0; i < strArr.length; i++) {
                strArr[i] = ret[i].getHostAddress();
            }
            return strArr;
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return new String[0];
        }
    }

    public static String getAddress(String hostName) {
        String[] array = getAddresses(hostName);
        if (array == null || array.length == 0) {
            return null;
        }
        return array[0];
    }

    public static String getAddressesString(String hostName) {
        return StringUtils.join(getAddresses(hostName), DnspodFree.IP_SPLIT);
    }
}
