package com.serenegiant.utils;

import java.lang.AutoCloseable;

/* loaded from: classes2.dex */
public class RefCountedAutoCloseable<T extends AutoCloseable> implements AutoCloseable {
    private T mObject;
    private long mRefCount = 0;

    public RefCountedAutoCloseable(T t) {
        this.mObject = t;
    }

    public synchronized T getAndRetain() {
        if (this.mRefCount < 0) {
            return null;
        }
        this.mRefCount++;
        return this.mObject;
    }

    public synchronized T get() {
        return this.mObject;
    }

    @Override // java.lang.AutoCloseable
    public synchronized void close() {
        if (this.mRefCount >= 0) {
            long j = this.mRefCount - 1;
            this.mRefCount = j;
            if (j < 0) {
                try {
                    try {
                        this.mObject.close();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                } finally {
                    this.mObject = null;
                }
            }
        }
    }
}
