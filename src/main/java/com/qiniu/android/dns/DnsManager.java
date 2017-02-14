package com.qiniu.android.dns;

import com.qiniu.android.dns.local.Hosts;
import com.qiniu.android.dns.local.Hosts.Value;
import com.qiniu.android.dns.util.LruCache;
import com.umeng.socialize.common.SocializeConstants;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public final class DnsManager {
    private final    LruCache<String, Record[]> cache;
    private final    Hosts                      hosts;
    private volatile int                        index;
    private volatile NetworkInfo                info;
    private final    IResolver[]                resolvers;
    private final    IpSorter                   sorter;

    private static class ShuffleIps implements IpSorter {
        private AtomicInteger pos;

        private ShuffleIps() {
            this.pos = new AtomicInteger();
        }

        public String[] sort(String[] ips) {
            if (ips == null || ips.length <= 1) {
                return ips;
            }
            int x = this.pos.getAndIncrement() & 255;
            String[] ret = new String[ips.length];
            for (int i = 0; i < ips.length; i++) {
                ret[i] = ips[(i + x) % ips.length];
            }
            return ret;
        }
    }

    public DnsManager(NetworkInfo info, IResolver[] resolvers) {
        this(info, resolvers, null);
    }

    public DnsManager(NetworkInfo info, IResolver[] resolvers, IpSorter sorter) {
        this.hosts = new Hosts();
        this.info = null;
        this.index = 0;
        if (info == null) {
            info = NetworkInfo.normal;
        }
        this.info = info;
        this.resolvers = (IResolver[]) resolvers.clone();
        this.cache = new LruCache();
        if (sorter == null) {
            sorter = new ShuffleIps();
        }
        this.sorter = sorter;
    }

    private static Record[] trimCname(Record[] records) {
        ArrayList<Record> a = new ArrayList(records.length);
        for (Record r : records) {
            if (r != null && r.type == 1) {
                a.add(r);
            }
        }
        return (Record[]) a.toArray(new Record[a.size()]);
    }

    private static String[] records2Ip(Record[] records) {
        if (records == null || records.length == 0) {
            return null;
        }
        ArrayList<String> a = new ArrayList(records.length);
        for (Record r : records) {
            a.add(r.value);
        }
        if (a.size() != 0) {
            return (String[]) a.toArray(new String[a.size()]);
        }
        return null;
    }

    public static boolean validIP(String ip) {
        if (ip == null || ip.length() < 7 || ip.length() > 15 || ip.contains(SocializeConstants
                .OP_DIVIDER_MINUS)) {
            return false;
        }
        try {
            int y = ip.indexOf(46);
            if (y != -1 && Integer.parseInt(ip.substring(0, y)) > 255) {
                return false;
            }
            y++;
            int x = ip.indexOf(46, y);
            if (x != -1 && Integer.parseInt(ip.substring(y, x)) > 255) {
                return false;
            }
            x++;
            y = ip.indexOf(46, x);
            if (y == -1 || Integer.parseInt(ip.substring(x, y)) <= 255 || Integer.parseInt(ip
                    .substring(y + 1, ip.length() - 1)) <= 255 || ip.charAt(ip.length() - 1) == '
                    .') {
                return true;
            }
            return false;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public String[] query(String domain) throws IOException {
        return query(new Domain(domain));
    }

    public String[] query(Domain domain) throws IOException {
        if (domain == null) {
            throw new IOException("null domain");
        } else if (domain.domain == null || domain.domain.trim().length() == 0) {
            throw new IOException("empty domain " + domain.domain);
        } else if (validIP(domain.domain)) {
            return new String[]{domain.domain};
        } else {
            String[] r = queryInternal(domain);
            return (r == null || r.length <= 1) ? r : this.sorter.sort(r);
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.lang.String[] queryInternal(com.qiniu.android.dns.Domain r16) throws java.io
            .IOException {
        /*
        r15 = this;
        r9 = 0;
        r0 = r16;
        r12 = r0.hostsFirst;
        if (r12 == 0) goto L_0x0017;
    L_0x0007:
        r12 = r15.hosts;
        r13 = r15.info;
        r0 = r16;
        r10 = r12.query(r0, r13);
        if (r10 == 0) goto L_0x0017;
    L_0x0013:
        r12 = r10.length;
        if (r12 == 0) goto L_0x0017;
    L_0x0016:
        return r10;
    L_0x0017:
        r13 = r15.cache;
        monitor-enter(r13);
        r12 = r15.info;	 Catch:{ all -> 0x008a }
        r14 = com.qiniu.android.dns.NetworkInfo.normal;	 Catch:{ all -> 0x008a }
        r12 = r12.equals(r14);	 Catch:{ all -> 0x008a }
        if (r12 == 0) goto L_0x008d;
    L_0x0024:
        r12 = com.qiniu.android.dns.Network.isNetworkChanged();	 Catch:{ all -> 0x008a }
        if (r12 == 0) goto L_0x008d;
    L_0x002a:
        r12 = r15.cache;	 Catch:{ all -> 0x008a }
        r12.clear();	 Catch:{ all -> 0x008a }
        r14 = r15.resolvers;	 Catch:{ all -> 0x008a }
        monitor-enter(r14);	 Catch:{ all -> 0x008a }
        r12 = 0;
        r15.index = r12;	 Catch:{ all -> 0x0087 }
        monitor-exit(r14);	 Catch:{ all -> 0x0087 }
    L_0x0036:
        monitor-exit(r13);	 Catch:{ all -> 0x008a }
        r7 = 0;
        r3 = r15.index;
        r4 = 0;
    L_0x003b:
        r12 = r15.resolvers;
        r12 = r12.length;
        if (r4 >= r12) goto L_0x00bd;
    L_0x0040:
        r12 = r3 + r4;
        r13 = r15.resolvers;
        r13 = r13.length;
        r8 = r12 % r13;
        r1 = r15.info;
        r5 = com.qiniu.android.dns.Network.getIp();
        r12 = r15.resolvers;	 Catch:{ DomainNotOwn -> 0x00b2, IOException -> 0x00b4 }
        r12 = r12[r8];	 Catch:{ DomainNotOwn -> 0x00b2, IOException -> 0x00b4 }
        r13 = r15.info;	 Catch:{ DomainNotOwn -> 0x00b2, IOException -> 0x00b4 }
        r0 = r16;
        r9 = r12.resolve(r0, r13);	 Catch:{ DomainNotOwn -> 0x00b2, IOException -> 0x00b4 }
    L_0x0059:
        r6 = com.qiniu.android.dns.Network.getIp();
        r12 = r15.info;
        if (r12 != r1) goto L_0x00bd;
    L_0x0061:
        if (r9 == 0) goto L_0x0066;
    L_0x0063:
        r12 = r9.length;
        if (r12 != 0) goto L_0x00bd;
    L_0x0066:
        r12 = r5.equals(r6);
        if (r12 == 0) goto L_0x00bd;
    L_0x006c:
        r13 = r15.resolvers;
        monitor-enter(r13);
        r12 = r15.index;	 Catch:{ all -> 0x00ba }
        if (r12 != r3) goto L_0x0083;
    L_0x0073:
        r12 = r15.index;	 Catch:{ all -> 0x00ba }
        r12 = r12 + 1;
        r15.index = r12;	 Catch:{ all -> 0x00ba }
        r12 = r15.index;	 Catch:{ all -> 0x00ba }
        r14 = r15.resolvers;	 Catch:{ all -> 0x00ba }
        r14 = r14.length;	 Catch:{ all -> 0x00ba }
        if (r12 != r14) goto L_0x0083;
    L_0x0080:
        r12 = 0;
        r15.index = r12;	 Catch:{ all -> 0x00ba }
    L_0x0083:
        monitor-exit(r13);	 Catch:{ all -> 0x00ba }
    L_0x0084:
        r4 = r4 + 1;
        goto L_0x003b;
    L_0x0087:
        r12 = move-exception;
        monitor-exit(r14);	 Catch:{ all -> 0x0087 }
        throw r12;	 Catch:{ all -> 0x008a }
    L_0x008a:
        r12 = move-exception;
        monitor-exit(r13);	 Catch:{ all -> 0x008a }
        throw r12;
    L_0x008d:
        r12 = r15.cache;	 Catch:{ all -> 0x008a }
        r0 = r16;
        r14 = r0.domain;	 Catch:{ all -> 0x008a }
        r12 = r12.get(r14);	 Catch:{ all -> 0x008a }
        r0 = r12;
        r0 = (com.qiniu.android.dns.Record[]) r0;	 Catch:{ all -> 0x008a }
        r9 = r0;
        if (r9 == 0) goto L_0x0036;
    L_0x009d:
        r12 = r9.length;	 Catch:{ all -> 0x008a }
        if (r12 == 0) goto L_0x0036;
    L_0x00a0:
        r12 = 0;
        r12 = r9[r12];	 Catch:{ all -> 0x008a }
        r12 = r12.isExpired();	 Catch:{ all -> 0x008a }
        if (r12 != 0) goto L_0x00b0;
    L_0x00a9:
        r10 = records2Ip(r9);	 Catch:{ all -> 0x008a }
        monitor-exit(r13);	 Catch:{ all -> 0x008a }
        goto L_0x0016;
    L_0x00b0:
        r9 = 0;
        goto L_0x0036;
    L_0x00b2:
        r2 = move-exception;
        goto L_0x0084;
    L_0x00b4:
        r2 = move-exception;
        r7 = r2;
        r2.printStackTrace();
        goto L_0x0059;
    L_0x00ba:
        r12 = move-exception;
        monitor-exit(r13);	 Catch:{ all -> 0x00ba }
        throw r12;
    L_0x00bd:
        if (r9 == 0) goto L_0x00c2;
    L_0x00bf:
        r12 = r9.length;
        if (r12 != 0) goto L_0x00e7;
    L_0x00c2:
        r0 = r16;
        r12 = r0.hostsFirst;
        if (r12 != 0) goto L_0x00da;
    L_0x00c8:
        r12 = r15.hosts;
        r13 = r15.info;
        r0 = r16;
        r11 = r12.query(r0, r13);
        if (r11 == 0) goto L_0x00da;
    L_0x00d4:
        r12 = r11.length;
        if (r12 == 0) goto L_0x00da;
    L_0x00d7:
        r10 = r11;
        goto L_0x0016;
    L_0x00da:
        if (r7 == 0) goto L_0x00dd;
    L_0x00dc:
        throw r7;
    L_0x00dd:
        r12 = new java.net.UnknownHostException;
        r0 = r16;
        r13 = r0.domain;
        r12.<init>(r13);
        throw r12;
    L_0x00e7:
        r9 = trimCname(r9);
        r12 = r9.length;
        if (r12 != 0) goto L_0x00f7;
    L_0x00ee:
        r12 = new java.net.UnknownHostException;
        r13 = "no A records";
        r12.<init>(r13);
        throw r12;
    L_0x00f7:
        r13 = r15.cache;
        monitor-enter(r13);
        r12 = r15.cache;	 Catch:{ all -> 0x010a }
        r0 = r16;
        r14 = r0.domain;	 Catch:{ all -> 0x010a }
        r12.put(r14, r9);	 Catch:{ all -> 0x010a }
        monitor-exit(r13);	 Catch:{ all -> 0x010a }
        r10 = records2Ip(r9);
        goto L_0x0016;
    L_0x010a:
        r12 = move-exception;
        monitor-exit(r13);	 Catch:{ all -> 0x010a }
        throw r12;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.qiniu.android.dns" +
                ".DnsManager.queryInternal(com.qiniu.android.dns.Domain):java.lang.String[]");
    }

    public InetAddress[] queryInetAdress(Domain domain) throws IOException {
        String[] ips = query(domain);
        InetAddress[] addresses = new InetAddress[ips.length];
        for (int i = 0; i < ips.length; i++) {
            addresses[i] = InetAddress.getByName(ips[i]);
        }
        return addresses;
    }

    public void onNetworkChange(NetworkInfo info) {
        clearCache();
        if (info == null) {
            info = NetworkInfo.normal;
        }
        this.info = info;
        synchronized (this.resolvers) {
            this.index = 0;
        }
    }

    private void clearCache() {
        synchronized (this.cache) {
            this.cache.clear();
        }
    }

    public DnsManager putHosts(String domain, String ip, int provider) {
        this.hosts.put(domain, new Value(ip, provider));
        return this;
    }

    public DnsManager putHosts(String domain, String ip) {
        this.hosts.put(domain, ip);
        return this;
    }
}
