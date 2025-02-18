package com.serenegiant.utils;

import java.lang.ref.WeakReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/* loaded from: classes2.dex */
public class ReentrantReadWriteReference<T> {
    private final Lock mReadLock;
    private T mRef;
    private final ReentrantReadWriteLock mSensorLock;
    private final Lock mWriteLock;

    public ReentrantReadWriteReference() {
        ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();
        this.mSensorLock = reentrantReadWriteLock;
        this.mReadLock = reentrantReadWriteLock.readLock();
        this.mWriteLock = this.mSensorLock.writeLock();
    }

    public ReentrantReadWriteReference(T t) {
        ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();
        this.mSensorLock = reentrantReadWriteLock;
        this.mReadLock = reentrantReadWriteLock.readLock();
        this.mWriteLock = this.mSensorLock.writeLock();
        set((ReentrantReadWriteReference<T>) t);
    }

    public ReentrantReadWriteReference(WeakReference<T> weakReference) {
        ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();
        this.mSensorLock = reentrantReadWriteLock;
        this.mReadLock = reentrantReadWriteLock.readLock();
        this.mWriteLock = this.mSensorLock.writeLock();
        set((WeakReference) weakReference);
    }

    public ReentrantReadWriteReference(ReentrantReadWriteReference<T> reentrantReadWriteReference) {
        ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();
        this.mSensorLock = reentrantReadWriteLock;
        this.mReadLock = reentrantReadWriteLock.readLock();
        this.mWriteLock = this.mSensorLock.writeLock();
        if (reentrantReadWriteReference != null) {
            set((ReentrantReadWriteReference<T>) reentrantReadWriteReference.get());
        }
    }

    public T get() {
        this.mReadLock.lock();
        try {
            return this.mRef;
        } finally {
            this.mReadLock.unlock();
        }
    }

    public T tryGet() {
        if (!this.mReadLock.tryLock()) {
            return null;
        }
        try {
            return this.mRef;
        } finally {
            this.mReadLock.unlock();
        }
    }

    public T set(T t) {
        this.mWriteLock.lock();
        try {
            T t2 = this.mRef;
            this.mRef = t;
            return t2;
        } finally {
            this.mWriteLock.unlock();
        }
    }

    public T set(WeakReference<T> weakReference) {
        T t = weakReference != null ? weakReference.get() : null;
        this.mWriteLock.lock();
        try {
            T t2 = this.mRef;
            this.mRef = t;
            return t2;
        } finally {
            this.mWriteLock.unlock();
        }
    }

    public T set(ReentrantReadWriteReference<T> reentrantReadWriteReference) {
        return set((ReentrantReadWriteReference<T>) (reentrantReadWriteReference != null ? reentrantReadWriteReference.get() : null));
    }

    public T clear() {
        return set((ReentrantReadWriteReference<T>) null);
    }

    public T swap(T t) {
        return set((ReentrantReadWriteReference<T>) t);
    }

    public boolean isEmpty() {
        return tryGet() == null;
    }

    public void readLock() {
        this.mReadLock.lock();
    }

    public void readUnlock() {
        this.mReadLock.unlock();
    }

    public void writeLock() {
        this.mWriteLock.lock();
    }

    public void writeUnlock() {
        this.mWriteLock.unlock();
    }
}
