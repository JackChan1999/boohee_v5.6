package com.qiniu.android.storage;

import com.qiniu.android.common.ServiceAddress;
import com.qiniu.android.common.Zone;
import com.qiniu.android.dns.DnsManager;
import com.qiniu.android.dns.IResolver;
import com.qiniu.android.dns.NetworkInfo;
import com.qiniu.android.dns.local.AndroidDnsServer;
import com.qiniu.android.dns.local.Resolver;
import com.qiniu.android.http.Proxy;
import com.qiniu.android.http.UrlConverter;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;

public final class Configuration {
    public static final int BLOCK_SIZE = 4194304;
    public final int            chunkSize;
    public final int            connectTimeout;
    public       DnsManager     dns;
    public final KeyGenerator   keyGen;
    public final Proxy          proxy;
    public final int            putThreshold;
    public final Recorder       recorder;
    public final int            responseTimeout;
    public final int            retryMax;
    public final ServiceAddress up;
    public final ServiceAddress upBackup;
    public       UrlConverter   urlConverter;

    public static class Builder {
        private int            chunkSize       = 262144;
        private int            connectTimeout  = 10;
        private DnsManager     dns             = null;
        private KeyGenerator   keyGen          = null;
        private Proxy          proxy           = null;
        private int            putThreshold    = 524288;
        private Recorder       recorder        = null;
        private int            responseTimeout = 60;
        private int            retryMax        = 3;
        private ServiceAddress up              = Zone.zone0.up;
        private ServiceAddress upBackup        = Zone.zone0.upBackup;
        private UrlConverter   urlConverter    = null;

        public Builder() {
            IResolver r1 = AndroidDnsServer.defaultResolver();
            IResolver r2 = null;
            try {
                r2 = new Resolver(InetAddress.getByName("119.29.29.29"));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            this.dns = new DnsManager(NetworkInfo.normal, new IResolver[]{r1, r2});
        }

        public Builder zone(Zone zone) {
            this.up = zone.up;
            this.upBackup = zone.upBackup;
            return this;
        }

        public Builder recorder(Recorder recorder) {
            this.recorder = recorder;
            return this;
        }

        public Builder recorder(Recorder recorder, KeyGenerator keyGen) {
            this.recorder = recorder;
            this.keyGen = keyGen;
            return this;
        }

        public Builder proxy(Proxy proxy) {
            this.proxy = proxy;
            return this;
        }

        public Builder chunkSize(int size) {
            this.chunkSize = size;
            return this;
        }

        public Builder putThreshhold(int size) {
            this.putThreshold = size;
            return this;
        }

        public Builder connectTimeout(int timeout) {
            this.connectTimeout = timeout;
            return this;
        }

        public Builder responseTimeout(int timeout) {
            this.responseTimeout = timeout;
            return this;
        }

        public Builder retryMax(int times) {
            this.retryMax = times;
            return this;
        }

        public Builder urlConverter(UrlConverter converter) {
            this.urlConverter = converter;
            return this;
        }

        public Builder dns(DnsManager dns) {
            this.dns = dns;
            return this;
        }

        public Configuration build() {
            return new Configuration();
        }
    }

    private Configuration(Builder builder) {
        this.up = builder.up;
        this.upBackup = builder.upBackup == null ? builder.up : builder.upBackup;
        this.chunkSize = builder.chunkSize;
        this.putThreshold = builder.putThreshold;
        this.connectTimeout = builder.connectTimeout;
        this.responseTimeout = builder.responseTimeout;
        this.recorder = builder.recorder;
        this.keyGen = getKeyGen(builder.keyGen);
        this.retryMax = builder.retryMax;
        this.proxy = builder.proxy;
        this.urlConverter = builder.urlConverter;
        this.dns = initDns(builder);
    }

    private static DnsManager initDns(Builder builder) {
        DnsManager d = builder.dns;
        builder.up.addIpToDns(d);
        if (builder.upBackup != null) {
            builder.upBackup.addIpToDns(d);
        }
        return d;
    }

    private KeyGenerator getKeyGen(KeyGenerator keyGen) {
        if (keyGen == null) {
            return new KeyGenerator() {
                public String gen(String key, File file) {
                    return key + "_._" + new StringBuffer(file.getAbsolutePath()).reverse();
                }
            };
        }
        return keyGen;
    }
}
