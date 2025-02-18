package com.serenegiant.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/* loaded from: classes2.dex */
public class ReentrantReadWriteList<V> implements List<V> {
    private final List<V> mList;
    private final Lock mReadLock;
    private final ReentrantReadWriteLock mSensorLock;
    private final Lock mWriteLock;

    public ReentrantReadWriteList() {
        ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();
        this.mSensorLock = reentrantReadWriteLock;
        this.mReadLock = reentrantReadWriteLock.readLock();
        this.mWriteLock = this.mSensorLock.writeLock();
        this.mList = new ArrayList();
    }

    @Override // java.util.List
    public V get(int i) throws IndexOutOfBoundsException {
        this.mReadLock.lock();
        try {
            return this.mList.get(i);
        } finally {
            this.mReadLock.unlock();
        }
    }

    public V tryGet(int i) {
        V v = null;
        if (this.mReadLock.tryLock()) {
            if (i >= 0) {
                try {
                    if (i < this.mList.size()) {
                        v = this.mList.get(i);
                    }
                } finally {
                    this.mReadLock.unlock();
                }
            }
        }
        return v;
    }

    @Override // java.util.List
    public V set(int i, V v) throws IndexOutOfBoundsException {
        this.mWriteLock.lock();
        try {
            return this.mList.set(i, v);
        } finally {
            this.mWriteLock.unlock();
        }
    }

    @Override // java.util.List
    public void add(int i, V v) throws IndexOutOfBoundsException {
        this.mWriteLock.lock();
        try {
            this.mList.add(i, v);
        } finally {
            this.mWriteLock.unlock();
        }
    }

    @Override // java.util.List
    public V remove(int i) {
        this.mWriteLock.lock();
        try {
            return this.mList.remove(i);
        } finally {
            this.mWriteLock.unlock();
        }
    }

    @Override // java.util.List
    public int indexOf(Object obj) {
        this.mReadLock.lock();
        try {
            return this.mList.indexOf(obj);
        } finally {
            this.mReadLock.unlock();
        }
    }

    @Override // java.util.List
    public int lastIndexOf(Object obj) {
        this.mReadLock.lock();
        try {
            return this.mList.lastIndexOf(obj);
        } finally {
            this.mReadLock.unlock();
        }
    }

    @Override // java.util.List
    public ListIterator<V> listIterator() {
        this.mReadLock.lock();
        try {
            return Collections.unmodifiableList(this.mList).listIterator();
        } finally {
            this.mReadLock.unlock();
        }
    }

    @Override // java.util.List
    public ListIterator<V> listIterator(int i) {
        this.mReadLock.lock();
        try {
            return Collections.unmodifiableList(this.mList).listIterator(i);
        } finally {
            this.mReadLock.unlock();
        }
    }

    @Override // java.util.List
    public List<V> subList(int i, int i2) {
        this.mReadLock.lock();
        try {
            return Collections.unmodifiableList(this.mList).subList(i, i2);
        } finally {
            this.mReadLock.unlock();
        }
    }

    @Override // java.util.List, java.util.Collection
    public boolean add(V v) {
        this.mWriteLock.lock();
        try {
            Boolean valueOf = Boolean.valueOf(this.mList.add(v));
            this.mWriteLock.unlock();
            return valueOf.booleanValue();
        } catch (Throwable th) {
            this.mWriteLock.unlock();
            throw th;
        }
    }

    @Override // java.util.List, java.util.Collection
    public boolean remove(Object obj) {
        this.mWriteLock.lock();
        try {
            Boolean valueOf = Boolean.valueOf(this.mList.remove(obj));
            this.mWriteLock.unlock();
            return valueOf.booleanValue();
        } catch (Throwable th) {
            this.mWriteLock.unlock();
            throw th;
        }
    }

    @Override // java.util.List, java.util.Collection
    public boolean containsAll(Collection<?> collection) {
        this.mReadLock.lock();
        try {
            return this.mList.containsAll(collection);
        } finally {
            this.mReadLock.unlock();
        }
    }

    @Override // java.util.List, java.util.Collection
    public boolean addAll(Collection<? extends V> collection) {
        this.mWriteLock.lock();
        try {
            Boolean valueOf = Boolean.valueOf(this.mList.addAll(collection));
            this.mWriteLock.unlock();
            return valueOf.booleanValue();
        } catch (Throwable th) {
            this.mWriteLock.unlock();
            throw th;
        }
    }

    @Override // java.util.List
    public boolean addAll(int i, Collection<? extends V> collection) {
        this.mWriteLock.lock();
        try {
            Boolean valueOf = Boolean.valueOf(this.mList.addAll(i, collection));
            this.mWriteLock.unlock();
            return valueOf.booleanValue();
        } catch (Throwable th) {
            this.mWriteLock.unlock();
            throw th;
        }
    }

    @Override // java.util.List, java.util.Collection
    public boolean removeAll(Collection<?> collection) {
        this.mWriteLock.lock();
        try {
            Boolean valueOf = Boolean.valueOf(this.mList.removeAll(collection));
            this.mWriteLock.unlock();
            return valueOf.booleanValue();
        } catch (Throwable th) {
            this.mWriteLock.unlock();
            throw th;
        }
    }

    @Override // java.util.List, java.util.Collection
    public boolean retainAll(Collection<?> collection) {
        this.mWriteLock.lock();
        try {
            Boolean valueOf = Boolean.valueOf(this.mList.retainAll(collection));
            this.mWriteLock.unlock();
            return valueOf.booleanValue();
        } catch (Throwable th) {
            this.mWriteLock.unlock();
            throw th;
        }
    }

    public boolean addIfAbsent(V v) {
        this.mWriteLock.lock();
        try {
            boolean z = !this.mList.contains(v);
            if (!z) {
                this.mList.add(v);
            }
            return z;
        } finally {
            this.mWriteLock.unlock();
        }
    }

    @Override // java.util.List, java.util.Collection
    public void clear() {
        this.mWriteLock.lock();
        try {
            this.mList.clear();
        } finally {
            this.mWriteLock.unlock();
        }
    }

    @Override // java.util.List, java.util.Collection
    public int size() {
        this.mReadLock.lock();
        try {
            return this.mList.size();
        } finally {
            this.mReadLock.unlock();
        }
    }

    @Override // java.util.List, java.util.Collection
    public boolean contains(Object obj) {
        this.mReadLock.lock();
        try {
            return this.mList.contains(obj);
        } finally {
            this.mReadLock.unlock();
        }
    }

    @Override // java.util.List, java.util.Collection
    public boolean isEmpty() {
        this.mReadLock.lock();
        try {
            return this.mList.isEmpty();
        } finally {
            this.mReadLock.unlock();
        }
    }

    @Override // java.util.List, java.util.Collection, java.lang.Iterable
    public Iterator<V> iterator() {
        this.mReadLock.lock();
        try {
            return Collections.unmodifiableList(this.mList).iterator();
        } finally {
            this.mReadLock.unlock();
        }
    }

    @Override // java.util.List, java.util.Collection
    public Object[] toArray() {
        Object[] objArr;
        this.mReadLock.lock();
        try {
            int i = 0;
            if (this.mList.isEmpty()) {
                objArr = new Object[0];
            } else {
                objArr = new Object[this.mList.size()];
                Iterator<V> it = this.mList.iterator();
                while (it.hasNext()) {
                    int i2 = i + 1;
                    objArr[i] = it.next();
                    i = i2;
                }
            }
            return objArr;
        } finally {
            this.mReadLock.unlock();
        }
    }

    @Override // java.util.List, java.util.Collection
    public <T> T[] toArray(T[] tArr) {
        this.mReadLock.lock();
        try {
            return (T[]) this.mList.toArray(tArr);
        } finally {
            this.mReadLock.unlock();
        }
    }

    public Collection<V> values() {
        this.mReadLock.lock();
        try {
            return Collections.unmodifiableCollection(this.mList);
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

    protected List<V> Locked() {
        return this.mList;
    }
}
