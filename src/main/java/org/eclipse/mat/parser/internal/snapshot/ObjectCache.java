package org.eclipse.mat.parser.internal.snapshot;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.eclipse.mat.collect.HashMapIntObject;

public abstract class ObjectCache<E> {
    private final List<LinkedList<Entry<E>>> lfus;
    private int lowestNonEmptyLfu = 0;
    private final HashMapIntObject<Entry<E>> map;
    private int maxLfuBuckets = 0;
    private int maxSize;

    static class Entry<E> {
        int key;
        int numUsages;
        E object;

        Entry() {
        }
    }

    protected abstract E load(int i);

    public ObjectCache(int maxSize) {
        this.maxSize = maxSize;
        this.map = new HashMapIntObject(maxSize);
        this.lfus = new ArrayList(5);
        this.maxLfuBuckets = maxSize / 3;
    }

    public synchronized E get(int objectId) {
        Entry<E> e;
        e = (Entry) this.map.get(objectId);
        if (e != null) {
            revalueEntry(e);
        } else {
            e = new Entry();
            e.object = load(objectId);
            e.key = objectId;
            doInsert(e);
            while (this.map.size() > this.maxSize) {
                removeLeastValuableNode();
            }
        }
        return e.object;
    }

    public synchronized void clear() {
        this.map.clear();
        this.lfus.clear();
    }

    protected synchronized void doInsert(Entry<E> e) {
        lfu(e.numUsages).addFirst(e);
        Entry<?> p = (Entry) this.map.put(e.key, e);
        this.lowestNonEmptyLfu = 0;
        if (p != null) {
            lfu(p.numUsages).remove(p);
        }
    }

    protected final LinkedList<Entry<E>> lfu(int numUsageIndex) {
        int lfuIndex = Math.min(this.maxLfuBuckets, numUsageIndex);
        if (lfuIndex < this.lfus.size()) {
            return (LinkedList) this.lfus.get(lfuIndex);
        }
        LinkedList<Entry<E>> lfu = new LinkedList();
        this.lfus.add(lfuIndex, lfu);
        return lfu;
    }

    protected void revalueEntry(Entry<E> entry) {
        LinkedList<Entry<E>> currBucket = lfu(entry.numUsages);
        int i = entry.numUsages + 1;
        entry.numUsages = i;
        LinkedList<Entry<E>> nextBucket = lfu(i);
        currBucket.remove(entry);
        nextBucket.addFirst(entry);
    }

    protected LinkedList<Entry<E>> getLowestNonEmptyLfu() {
        LinkedList<Entry<E>> lfu = null;
        for (int i = this.lowestNonEmptyLfu; i < this.lfus.size(); i++) {
            lfu = lfu(i);
            if (lfu.size() != 0) {
                this.lowestNonEmptyLfu = i;
                return lfu;
            }
        }
        return lfu;
    }

    protected void removeLeastValuableNode() {
        LinkedList<Entry<E>> lfu = getLowestNonEmptyLfu();
        this.map.remove(((Entry) lfu.remove(lfu.size() - 1)).key);
    }
}
