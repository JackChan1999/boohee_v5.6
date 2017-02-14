package com.qiniu.android.common;

import com.qiniu.android.dns.DnsManager;

import java.net.URI;
import java.net.URISyntaxException;

public final class ServiceAddress {
    public final URI      address;
    public final String[] backupIps;

    public ServiceAddress(String address, String[] backupIps) {
        this.address = uri(address);
        if (backupIps == null) {
            backupIps = new String[0];
        }
        this.backupIps = backupIps;
    }

    public ServiceAddress(String address) {
        this(address, null);
    }

    private static URI uri(String address) {
        try {
            return new URI(address);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void addIpToDns(DnsManager d) {
        for (String ip : this.backupIps) {
            d.putHosts(this.address.getHost(), ip);
        }
    }
}
