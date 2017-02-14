package com.qiniu.android.dns;

public final class Domain {
    public final String  domain;
    public final boolean hasCname;
    public final boolean hostsFirst;
    public final int     maxTtl;

    public Domain(String domain, boolean hasCname, boolean hostsFirst) {
        this(domain, hasCname, hostsFirst, 0);
    }

    public Domain(String domain, boolean hasCname, boolean hostsFirst, int maxTtl) {
        this.domain = domain;
        this.hasCname = hasCname;
        this.hostsFirst = hostsFirst;
        this.maxTtl = maxTtl;
    }

    public Domain(String domain, boolean hasCname) {
        this(domain, hasCname, false, 0);
    }

    public Domain(String domain) {
        this(domain, false, false, 0);
    }
}
