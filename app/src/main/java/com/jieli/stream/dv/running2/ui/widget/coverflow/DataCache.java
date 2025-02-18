package com.jieli.stream.dv.running2.ui.widget.coverflow;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/* loaded from: classes.dex */
public class DataCache<K, E> {
    private LinkedHashMap<K, E> mCache;
    private int mCapacity;
    private final ReadWriteLock mReadWriteLock = new ReentrantReadWriteLock();

    public DataCache(int i) {
        this.mCapacity = i;
        this.mCache = new LinkedHashMap<K, E>(this.mCapacity) { // from class: com.jieli.stream.dv.running2.ui.widget.coverflow.DataCache.1
            private static final long serialVersionUID = -9165777183357349715L;

            @Override // java.util.LinkedHashMap
            protected boolean removeEldestEntry(Map.Entry<K, E> entry) {
                if (size() <= DataCache.this.mCapacity) {
                    return false;
                }
                DataCache.this.mReadWriteLock.writeLock().lock();
                remove(entry.getKey());
                DataCache.this.mReadWriteLock.writeLock().unlock();
                return false;
            }
        };
    }

    public E objectForKey(K k) {
        this.mReadWriteLock.readLock().lock();
        E e = this.mCache.get(k);
        this.mReadWriteLock.readLock().unlock();
        return e;
    }

    public void putObjectForKey(K k, E e) {
        if (k == null || e == null) {
            return;
        }
        this.mReadWriteLock.writeLock().lock();
        this.mCache.put(k, e);
        this.mReadWriteLock.writeLock().unlock();
    }

    public boolean containsKey(K k) {
        this.mReadWriteLock.readLock().lock();
        boolean containsKey = this.mCache.containsKey(k);
        this.mReadWriteLock.readLock().unlock();
        return containsKey;
    }

    public void clear() {
        this.mReadWriteLock.writeLock().lock();
        this.mCache.clear();
        this.mReadWriteLock.writeLock().unlock();
    }

    public void setCapacity(int i) {
        this.mCapacity = i;
    }
}
