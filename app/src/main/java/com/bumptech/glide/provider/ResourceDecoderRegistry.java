package com.bumptech.glide.provider;

import com.bumptech.glide.load.ResourceDecoder;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class ResourceDecoderRegistry {
    private final List<Entry<?, ?>> decoders = new ArrayList();

    public synchronized <T, R> List<ResourceDecoder<T, R>> getDecoders(Class<T> cls, Class<R> cls2) {
        ArrayList arrayList;
        arrayList = new ArrayList();
        for (Entry<?, ?> entry : this.decoders) {
            if (entry.handles(cls, cls2)) {
                arrayList.add(entry.decoder);
            }
        }
        return arrayList;
    }

    public synchronized <T, R> List<Class<R>> getResourceClasses(Class<T> cls, Class<R> cls2) {
        ArrayList arrayList;
        arrayList = new ArrayList();
        for (Entry<?, ?> entry : this.decoders) {
            if (entry.handles(cls, cls2)) {
                arrayList.add(entry.resourceClass);
            }
        }
        return arrayList;
    }

    public synchronized <T, R> void append(ResourceDecoder<T, R> resourceDecoder, Class<T> cls, Class<R> cls2) {
        this.decoders.add(new Entry<>(cls, cls2, resourceDecoder));
    }

    public synchronized <T, R> void prepend(ResourceDecoder<T, R> resourceDecoder, Class<T> cls, Class<R> cls2) {
        this.decoders.add(0, new Entry<>(cls, cls2, resourceDecoder));
    }

    private static class Entry<T, R> {
        private final Class<T> dataClass;
        final ResourceDecoder<T, R> decoder;
        final Class<R> resourceClass;

        public Entry(Class<T> cls, Class<R> cls2, ResourceDecoder<T, R> resourceDecoder) {
            this.dataClass = cls;
            this.resourceClass = cls2;
            this.decoder = resourceDecoder;
        }

        public boolean handles(Class<?> cls, Class<?> cls2) {
            return this.dataClass.isAssignableFrom(cls) && cls2.isAssignableFrom(this.resourceClass);
        }
    }
}
