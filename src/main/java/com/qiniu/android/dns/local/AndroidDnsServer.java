package com.qiniu.android.dns.local;

import com.qiniu.android.dns.Domain;
import com.qiniu.android.dns.IResolver;
import com.qiniu.android.dns.NetworkInfo;
import com.qiniu.android.dns.Record;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class AndroidDnsServer {
    public static InetAddress[] getByCommand() {
        try {
            LineNumberReader lnr = new LineNumberReader(new InputStreamReader(Runtime.getRuntime
                    ().exec("getprop").getInputStream()));
            ArrayList<InetAddress> servers = new ArrayList(5);
            while (true) {
                String line = lnr.readLine();
                if (line == null) {
                    break;
                }
                int split = line.indexOf("]: [");
                if (split != -1) {
                    String property = line.substring(1, split);
                    String value = line.substring(split + 4, line.length() - 1);
                    if (property.endsWith(".dns") || property.endsWith(".dns1") || property
                            .endsWith(".dns2") || property.endsWith(".dns3") || property.endsWith
                            (".dns4")) {
                        InetAddress ip = InetAddress.getByName(value);
                        if (ip != null) {
                            value = ip.getHostAddress();
                            if (!(value == null || value.length() == 0)) {
                                servers.add(ip);
                            }
                        }
                    }
                }
            }
            if (servers.size() > 0) {
                return (InetAddress[]) servers.toArray(new InetAddress[servers.size()]);
            }
        } catch (IOException e) {
            Logger.getLogger("AndroidDnsServer").log(Level.WARNING, "Exception in findDNSByExec",
                    e);
        }
        return null;
    }

    public static InetAddress[] getByReflection() {
        try {
            Method method = Class.forName("android.os.SystemProperties").getMethod("get", new
                    Class[]{String.class});
            ArrayList<InetAddress> servers = new ArrayList(5);
            int length = new String[]{"net.dns1", "net.dns2", "net.dns3", "net.dns4"}.length;
            for (int i = 0; i < length; i++) {
                String value = (String) method.invoke(null, new Object[]{r9[i]});
                if (!(value == null || value.length() == 0)) {
                    InetAddress ip = InetAddress.getByName(value);
                    if (ip != null) {
                        value = ip.getHostAddress();
                        if (!(value == null || value.length() == 0 || servers.contains(ip))) {
                            servers.add(ip);
                        }
                    }
                }
            }
            if (servers.size() > 0) {
                return (InetAddress[]) servers.toArray(new InetAddress[servers.size()]);
            }
        } catch (Exception e) {
            Logger.getLogger("AndroidDnsServer").log(Level.WARNING, "Exception in " +
                    "findDNSByReflection", e);
        }
        return null;
    }

    public static IResolver defaultResolver() {
        return new IResolver() {
            public Record[] resolve(Domain domain, NetworkInfo info) throws IOException {
                InetAddress[] addresses = AndroidDnsServer.getByReflection();
                if (addresses == null) {
                    addresses = AndroidDnsServer.getByCommand();
                }
                if (addresses == null) {
                    throw new IOException("cant get local dns server");
                }
                int i;
                int length;
                Record[] records = new HijackingDetectWrapper(new Resolver(addresses[0])).resolve
                        (domain, info);
                if (domain.hasCname) {
                    boolean cname = false;
                    for (Record r : records) {
                        Record r2;
                        if (r2.isCname()) {
                            cname = true;
                            break;
                        }
                    }
                    if (!cname) {
                        throw new DnshijackingException(domain.domain, addresses[0]
                                .getHostAddress());
                    }
                }
                if (domain.maxTtl != 0) {
                    length = records.length;
                    i = 0;
                    while (i < length) {
                        r2 = records[i];
                        if (r2.isCname() || r2.ttl <= domain.maxTtl) {
                            i++;
                        } else {
                            throw new DnshijackingException(domain.domain, addresses[0]
                                    .getHostAddress(), r2.ttl);
                        }
                    }
                }
                return records;
            }
        };
    }
}
