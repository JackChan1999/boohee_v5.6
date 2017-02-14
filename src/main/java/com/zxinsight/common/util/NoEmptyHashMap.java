package com.zxinsight.common.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class NoEmptyHashMap<K, V> extends HashMap<K, V> {
    private static final long serialVersionUID = 3483228055540355317L;

    public NoEmptyHashMap(Map<K, V> map) {
        putAll(map);
    }

    public V put(K k, V v) {
        if (l.a(k) || l.a(v)) {
            return null;
        }
        return super.put(k, v);
    }

    public void putAll(Map<? extends K, ? extends V> map) {
        for (Entry entry : map.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }
}
