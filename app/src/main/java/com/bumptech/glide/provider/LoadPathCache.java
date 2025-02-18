package com.bumptech.glide.provider;

import androidx.collection.ArrayMap;
import com.bumptech.glide.load.engine.LoadPath;
import com.bumptech.glide.util.MultiClassKey;
import java.util.concurrent.atomic.AtomicReference;

/* loaded from: classes.dex */
public class LoadPathCache {
    private final ArrayMap<MultiClassKey, LoadPath<?, ?, ?>> cache = new ArrayMap<>();
    private final AtomicReference<MultiClassKey> keyRef = new AtomicReference<>();

    public boolean contains(Class<?> cls, Class<?> cls2, Class<?> cls3) {
        boolean containsKey;
        MultiClassKey key = getKey(cls, cls2, cls3);
        synchronized (this.cache) {
            containsKey = this.cache.containsKey(key);
        }
        this.keyRef.set(key);
        return containsKey;
    }

    public <Data, TResource, Transcode> LoadPath<Data, TResource, Transcode> get(Class<Data> cls, Class<TResource> cls2, Class<Transcode> cls3) {
        LoadPath<Data, TResource, Transcode> loadPath;
        MultiClassKey key = getKey(cls, cls2, cls3);
        synchronized (this.cache) {
            loadPath = (LoadPath) this.cache.get(key);
        }
        this.keyRef.set(key);
        return loadPath;
    }

    public void put(Class<?> cls, Class<?> cls2, Class<?> cls3, LoadPath<?, ?, ?> loadPath) {
        synchronized (this.cache) {
            this.cache.put(new MultiClassKey(cls, cls2, cls3), loadPath);
        }
    }

    private MultiClassKey getKey(Class<?> cls, Class<?> cls2, Class<?> cls3) {
        MultiClassKey andSet = this.keyRef.getAndSet(null);
        if (andSet == null) {
            andSet = new MultiClassKey();
        }
        andSet.set(cls, cls2, cls3);
        return andSet;
    }
}
