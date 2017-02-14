package com.qiniu.android.dns;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;

public final class Network {
    private static String previousIp = "";

    public static String getIp() {
        IOException e;
        try {
            DatagramSocket socket = new DatagramSocket();
            try {
                socket.connect(InetAddress.getByName("114.114.114.114"), 53);
                InetAddress local = socket.getLocalAddress();
                socket.close();
                return local.getHostAddress();
            } catch (IOException e2) {
                e = e2;
                DatagramSocket datagramSocket = socket;
                e.printStackTrace();
                return "";
            }
        } catch (IOException e3) {
            e = e3;
            e.printStackTrace();
            return "";
        }
    }

    public static synchronized boolean isNetworkChanged() {
        boolean z;
        synchronized (Network.class) {
            String nowIp = getIp();
            if (nowIp.equals(previousIp)) {
                z = false;
            } else {
                previousIp = nowIp;
                z = true;
            }
        }
        return z;
    }
}
