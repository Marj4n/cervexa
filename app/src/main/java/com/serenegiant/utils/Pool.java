package com.serenegiant.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/* loaded from: classes2.dex */
public abstract class Pool<T> {
    private int mCreatedObjects;
    private final int mInitNum;
    private final int mLimitNum;
    private final int mMaxNumInPool;
    private final List<T> mPool;

    protected abstract T createObject(Object... objArr);

    public Pool(int i, int i2) {
        this(i, i2, i2);
    }

    public Pool(int i, int i2, int i3) {
        this.mPool = new ArrayList();
        this.mInitNum = i;
        this.mMaxNumInPool = i2 >= i3 ? i3 : i2;
        this.mLimitNum = i3;
        init();
    }

    public void init() {
        clear();
        for (int i = 0; i < this.mInitNum && i < this.mMaxNumInPool; i++) {
            T createObject = createObject(new Object[0]);
            if (createObject != null) {
                this.mPool.add(createObject);
                this.mCreatedObjects++;
            }
        }
    }

    public T obtain(Object... objArr) {
        T remove;
        synchronized (this.mPool) {
            remove = !this.mPool.isEmpty() ? this.mPool.remove(this.mPool.size() - 1) : null;
            if (remove == null && this.mCreatedObjects < this.mLimitNum && (remove = createObject(objArr)) != null) {
                this.mCreatedObjects++;
            }
        }
        return remove;
    }

    public void recycle(T t) {
        synchronized (this.mPool) {
            if (this.mPool.size() < this.mMaxNumInPool) {
                this.mPool.add(t);
            } else {
                this.mCreatedObjects--;
            }
        }
    }

    public void recycle(Collection<T> collection) {
        for (T t : collection) {
            if (t != null) {
                recycle((T) t);
            }
        }
    }

    public void recycle(T[] tArr) {
        for (T t : tArr) {
            if (t != null) {
                recycle((T) t);
            }
        }
    }

    public void clear() {
        synchronized (this.mPool) {
            this.mPool.clear();
            this.mCreatedObjects = 0;
        }
    }
}
