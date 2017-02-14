package com.qiniu.android.dns;

public final class Record {
    public static final int TTL_MIN_SECONDS = 600;
    public static final int TYPE_A          = 1;
    public static final int TYPE_CNAME      = 5;
    public final long   timeStamp;
    public final int    ttl;
    public final int    type;
    public final String value;

    public Record(String value, int type, int ttl, long timeStamp) {
        this.value = value;
        this.type = type;
        if (ttl < TTL_MIN_SECONDS) {
            ttl = TTL_MIN_SECONDS;
        }
        this.ttl = ttl;
        this.timeStamp = timeStamp;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || !(o instanceof Record)) {
            return false;
        }
        Record another = (Record) o;
        if (this.value.equals(another.value) && this.type == another.type && this.ttl == another
                .ttl && this.timeStamp == another.timeStamp) {
            return true;
        }
        return false;
    }

    public boolean isA() {
        return this.type == 1;
    }

    public boolean isCname() {
        return this.type == 5;
    }

    public boolean isExpired() {
        return isExpired(System.currentTimeMillis() / 1000);
    }

    public boolean isExpired(long time) {
        return this.timeStamp + ((long) this.ttl) < time;
    }
}
