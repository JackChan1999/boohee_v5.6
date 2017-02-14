package com.qiniu.android.dns.local;

import com.qiniu.android.dns.Domain;
import com.qiniu.android.dns.NetworkInfo;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

public final class Hosts {
    private final Hashtable<String, ArrayList<Value>> hosts = new Hashtable();

    public static class Value {
        public final String ip;
        public final int    provider;

        public Value(String ip, int provider) {
            this.ip = ip;
            this.provider = provider;
        }

        public Value(String ip) {
            this(ip, 0);
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || !(o instanceof Value)) {
                return false;
            }
            Value another = (Value) o;
            if (this.ip.equals(another.ip) && this.provider == another.provider) {
                return true;
            }
            return false;
        }
    }

    public String[] query(Domain domain, NetworkInfo info) {
        ArrayList<Value> values = (ArrayList) this.hosts.get(domain.domain);
        if (values == null || values.isEmpty()) {
            return null;
        }
        return toIps(filter(values, info));
    }

    private ArrayList<Value> filter(ArrayList<Value> origin, NetworkInfo info) {
        ArrayList<Value> normal = new ArrayList();
        ArrayList<Value> special = new ArrayList();
        Iterator it = origin.iterator();
        while (it.hasNext()) {
            Value v = (Value) it.next();
            if (v.provider == 0) {
                normal.add(v);
            }
            if (info.provider != 0 && v.provider == info.provider) {
                special.add(v);
            }
        }
        return special.size() != 0 ? special : normal;
    }

    public String[] toIps(ArrayList<Value> vals) {
        int size = vals.size();
        String[] r = new String[size];
        for (int i = 0; i < size; i++) {
            r[i] = ((Value) vals.get(i)).ip;
        }
        return r;
    }

    public Hosts put(String domain, Value val) {
        ArrayList<Value> vals = (ArrayList) this.hosts.get(domain);
        if (vals == null) {
            vals = new ArrayList();
        }
        vals.add(val);
        this.hosts.put(domain, vals);
        return this;
    }

    public Hosts put(String domain, String val) {
        put(domain, new Value(val));
        return this;
    }
}
