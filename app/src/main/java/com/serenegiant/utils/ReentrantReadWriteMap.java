package com.serenegiant.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/* loaded from: classes2.dex */
public class ReentrantReadWriteMap<K, V> {
    private final Map<K, V> mMap;
    private final Lock mReadLock;
    private final ReentrantReadWriteLock mSensorLock;
    private final Lock mWriteLock;

    public ReentrantReadWriteMap() {
        ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();
        this.mSensorLock = reentrantReadWriteLock;
        this.mReadLock = reentrantReadWriteLock.readLock();
        this.mWriteLock = this.mSensorLock.writeLock();
        this.mMap = new HashMap();
    }

    public V get(K k) {
        this.mReadLock.lock();
        try {
            return this.mMap.containsKey(k) ? this.mMap.get(k) : null;
        } finally {
            this.mReadLock.unlock();
        }
    }

    public V tryGet(K k) {
        if (!this.mReadLock.tryLock()) {
            return null;
        }
        try {
            return this.mMap.containsKey(k) ? this.mMap.get(k) : null;
        } finally {
            this.mReadLock.unlock();
        }
    }

    public V put(K k, V v) {
        this.mWriteLock.lock();
        try {
            V remove = this.mMap.remove(k);
            this.mMap.put(k, v);
            return remove;
        } finally {
            this.mWriteLock.unlock();
        }
    }

    public V putIfAbsent(K k, V v) {
        this.mWriteLock.lock();
        try {
            V v2 = this.mMap.get(k);
            if (v2 == null) {
                v2 = this.mMap.put(k, v);
            }
            return v2;
        } finally {
            this.mWriteLock.unlock();
        }
    }

    public void putAll(Map<? extends K, ? extends V> map) {
        this.mWriteLock.lock();
        try {
            this.mMap.putAll(map);
        } finally {
            this.mWriteLock.unlock();
        }
    }

    public V remove(K k) {
        this.mWriteLock.lock();
        try {
            return this.mMap.remove(k);
        } finally {
            this.mWriteLock.unlock();
        }
    }

    public V remove(K k, V v) {
        this.mWriteLock.lock();
        try {
            return (this.mMap.containsKey(k) && isEquals(this.mMap.get(k), v)) ? this.mMap.remove(k) : null;
        } finally {
            this.mWriteLock.unlock();
        }
    }

    public Collection<V> removeAll() {
        ArrayList arrayList = new ArrayList();
        this.mWriteLock.lock();
        try {
            arrayList.addAll(this.mMap.values());
            this.mMap.clear();
            return arrayList;
        } finally {
            this.mWriteLock.unlock();
        }
    }

    public void clear() {
        this.mWriteLock.lock();
        try {
            this.mMap.clear();
        } finally {
            this.mWriteLock.unlock();
        }
    }

    public int size() {
        this.mReadLock.lock();
        try {
            return this.mMap.size();
        } finally {
            this.mReadLock.unlock();
        }
    }

    public boolean containsKey(K k) {
        this.mReadLock.lock();
        try {
            return this.mMap.containsKey(k);
        } finally {
            this.mReadLock.unlock();
        }
    }

    public boolean containsValue(Object obj) {
        this.mReadLock.lock();
        try {
            return this.mMap.containsValue(obj);
        } finally {
            this.mReadLock.unlock();
        }
    }

    public V getOrDefault(K k, V v) {
        this.mReadLock.lock();
        try {
            if (this.mMap.containsKey(k)) {
                v = this.mMap.get(k);
            }
            return v;
        } finally {
            this.mReadLock.unlock();
        }
    }

    public boolean isEmpty() {
        this.mReadLock.lock();
        try {
            return this.mMap.isEmpty();
        } finally {
            this.mReadLock.unlock();
        }
    }

    public Collection<K> keys() {
        ArrayList arrayList = new ArrayList();
        this.mReadLock.lock();
        try {
            arrayList.addAll(this.mMap.keySet());
            return arrayList;
        } finally {
            this.mReadLock.unlock();
        }
    }

    public Collection<V> values() {
        ArrayList arrayList = new ArrayList();
        this.mReadLock.lock();
        try {
            if (!this.mMap.isEmpty()) {
                arrayList.addAll(this.mMap.values());
            }
            return arrayList;
        } finally {
            this.mReadLock.unlock();
        }
    }

    public Set<Map.Entry<K, V>> entrySet() {
        HashSet hashSet = new HashSet();
        this.mReadLock.lock();
        try {
            hashSet.addAll(this.mMap.entrySet());
            return hashSet;
        } finally {
            this.mReadLock.unlock();
        }
    }

    private static final boolean isEquals(Object obj, Object obj2) {
        return obj == obj2 || (obj != null && obj.equals(obj2));
    }

    protected void readLock() {
        this.mReadLock.lock();
    }

    protected void readUnlock() {
        this.mReadLock.unlock();
    }

    protected void writeLock() {
        this.mWriteLock.lock();
    }

    protected void writeUnlock() {
        this.mWriteLock.unlock();
    }

    protected Collection<V> valuesLocked() {
        return this.mMap.values();
    }

    protected Set<K> keysLocked() {
        return this.mMap.keySet();
    }

    protected Map<K, V> mapLocked() {
        return this.mMap;
    }
}
