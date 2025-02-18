package com.nineoldandroids.util;

/* loaded from: classes.dex */
public abstract class Property<T, V> {
    private final String mName;
    private final Class<V> mType;

    public abstract V get(T t);

    public boolean isReadOnly() {
        return false;
    }

    public static <T, V> Property<T, V> of(Class<T> cls, Class<V> cls2, String str) {
        return new ReflectiveProperty(cls, cls2, str);
    }

    public Property(Class<V> cls, String str) {
        this.mName = str;
        this.mType = cls;
    }

    public void set(T t, V v) {
        throw new UnsupportedOperationException("Property " + getName() + " is read-only");
    }

    public String getName() {
        return this.mName;
    }

    public Class<V> getType() {
        return this.mType;
    }
}
