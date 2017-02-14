package com.meiqia.core.a.a.e;

import java.util.Collections;
import java.util.Iterator;
import java.util.TreeMap;

public class g implements c {
    private byte[] a;
    private TreeMap<String, String> b = new TreeMap(String.CASE_INSENSITIVE_ORDER);

    public void a(String str, String str2) {
        this.b.put(str, str2);
    }

    public void a(byte[] bArr) {
        this.a = bArr;
    }

    public String b(String str) {
        String str2 = (String) this.b.get(str);
        return str2 == null ? "" : str2;
    }

    public Iterator<String> b() {
        return Collections.unmodifiableSet(this.b.keySet()).iterator();
    }

    public boolean c(String str) {
        return this.b.containsKey(str);
    }

    public byte[] c() {
        return this.a;
    }
}
