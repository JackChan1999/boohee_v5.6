package com.alibaba.fastjson.serializer;

public final class JSONSerializerContext {
    public static final int DEFAULT_TABLE_SIZE = 128;
    private final Entry[] buckets;
    private final int indexMask;

    protected static final class Entry {
        public final int hashCode;
        public Entry next;
        public final Object object;

        public Entry(Object object, int hash, Entry next) {
            this.object = object;
            this.next = next;
            this.hashCode = hash;
        }
    }

    public JSONSerializerContext() {
        this(128);
    }

    public JSONSerializerContext(int tableSize) {
        this.indexMask = tableSize - 1;
        this.buckets = new Entry[tableSize];
    }

    public final boolean put(Object o) {
        int hash = System.identityHashCode(o);
        int bucket = hash & this.indexMask;
        for (Entry entry = this.buckets[bucket]; entry != null; entry = entry.next) {
            if (o == entry.object) {
                return true;
            }
        }
        this.buckets[bucket] = new Entry(o, hash, this.buckets[bucket]);
        return false;
    }
}
