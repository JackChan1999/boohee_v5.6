package com.qiniu.android.dns.local;

import com.qiniu.android.dns.Domain;
import com.qiniu.android.dns.IResolver;
import com.qiniu.android.dns.NetworkInfo;
import com.qiniu.android.dns.Record;

import java.io.IOException;

public final class HijackingDetectWrapper implements IResolver {
    private final Resolver resolver;

    public HijackingDetectWrapper(Resolver r) {
        this.resolver = r;
    }

    public Record[] resolve(Domain domain, NetworkInfo info) throws IOException {
        int length;
        Record r;
        int i = 0;
        Record[] records = this.resolver.resolve(domain, info);
        if (domain.hasCname) {
            boolean cname = false;
            for (Record r2 : records) {
                if (r2.isCname()) {
                    cname = true;
                    break;
                }
            }
            if (!cname) {
                throw new DnshijackingException(domain.domain, this.resolver.address
                        .getHostAddress());
            }
        }
        if (domain.maxTtl != 0) {
            length = records.length;
            while (i < length) {
                r2 = records[i];
                if (r2.isCname() || r2.ttl <= domain.maxTtl) {
                    i++;
                } else {
                    throw new DnshijackingException(domain.domain, this.resolver.address
                            .getHostAddress(), r2.ttl);
                }
            }
        }
        return records;
    }
}
