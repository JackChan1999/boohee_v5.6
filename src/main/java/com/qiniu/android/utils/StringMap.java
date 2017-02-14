package com.qiniu.android.utils;

import com.alipay.sdk.sys.a;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public final class StringMap {
    private Map<String, Object> map;

    public interface Consumer {
        void accept(String str, Object obj);
    }

    public StringMap() {
        this(new HashMap());
    }

    public StringMap(Map<String, Object> map) {
        this.map = map;
    }

    public StringMap put(String key, Object value) {
        this.map.put(key, value);
        return this;
    }

    public StringMap putNotEmpty(String key, String value) {
        if (!StringUtils.isNullOrEmpty(value)) {
            this.map.put(key, value);
        }
        return this;
    }

    public StringMap putNotNull(String key, Object value) {
        if (value != null) {
            this.map.put(key, value);
        }
        return this;
    }

    public StringMap putWhen(String key, Object val, boolean when) {
        if (when) {
            this.map.put(key, val);
        }
        return this;
    }

    public StringMap putAll(Map<String, Object> map) {
        this.map.putAll(map);
        return this;
    }

    public StringMap putFileds(Map<String, String> map) {
        this.map.putAll(map);
        return this;
    }

    public StringMap putAll(StringMap map) {
        this.map.putAll(map.map);
        return this;
    }

    public void forEach(Consumer imp) {
        for (Entry<String, Object> i : this.map.entrySet()) {
            imp.accept((String) i.getKey(), i.getValue());
        }
    }

    public int size() {
        return this.map.size();
    }

    public Map<String, Object> map() {
        return this.map;
    }

    public Object get(String key) {
        return this.map.get(key);
    }

    public String formString() {
        final StringBuilder b = new StringBuilder();
        forEach(new Consumer() {
            private boolean notStart = false;

            public void accept(String key, Object value) {
                if (this.notStart) {
                    b.append(a.b);
                }
                try {
                    b.append(URLEncoder.encode(key, "UTF-8")).append('=').append(URLEncoder
                            .encode(value.toString(), "UTF-8"));
                    this.notStart = true;
                } catch (UnsupportedEncodingException e) {
                    throw new AssertionError(e);
                }
            }
        });
        return b.toString();
    }
}
