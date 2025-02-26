package com.serenegiant.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/* loaded from: classes2.dex */
public class CollectionMap<K, V> implements Map<K, Collection<V>> {
    private final Map<K, Collection<V>> contents = createContentsMap();

    protected Map<K, Collection<V>> createContentsMap() {
        return new HashMap();
    }

    protected Collection<V> createCollection() {
        return new ArrayList();
    }

    @Override // java.util.Map
    public void clear() {
        this.contents.clear();
    }

    @Override // java.util.Map
    public boolean containsKey(Object obj) {
        return this.contents.containsKey(obj);
    }

    @Override // java.util.Map
    public boolean containsValue(Object obj) {
        return this.contents.containsValue(obj);
    }

    public boolean containsInValue(V v) {
        Iterator<Collection<V>> it = this.contents.values().iterator();
        while (it.hasNext()) {
            if (it.next().contains(v)) {
                return true;
            }
        }
        return false;
    }

    @Override // java.util.Map
    public Set<Map.Entry<K, Collection<V>>> entrySet() {
        return this.contents.entrySet();
    }

    @Override // java.util.Map
    public Collection<V> get(Object obj) {
        return this.contents.get(obj);
    }

    @Override // java.util.Map
    public boolean isEmpty() {
        return this.contents.isEmpty();
    }

    @Override // java.util.Map
    public Set<K> keySet() {
        return this.contents.keySet();
    }

    public Collection<V> put(K k, Collection<V> collection) {
        return this.contents.put(k, collection);
    }

    public boolean add(K k, V v) {
        Collection<V> collection = this.contents.get(k);
        if (collection == null) {
            collection = createCollection();
            this.contents.put(k, collection);
        }
        return collection.add(v);
    }

    @Override // java.util.Map
    public void putAll(Map<? extends K, ? extends Collection<V>> map) {
        this.contents.putAll(map);
    }

    public void addAll(Map<? extends K, ? extends Collection<V>> map) {
        for (Map.Entry<? extends K, ? extends Collection<V>> entry : map.entrySet()) {
            addAll(entry.getKey(), entry.getValue());
        }
    }

    public boolean addAll(K k, Collection<? extends V> collection) {
        Collection<V> collection2 = this.contents.get(k);
        if (collection2 == null) {
            collection2 = createCollection();
            this.contents.put(k, collection2);
        }
        return collection2.addAll(collection);
    }

    @Override // java.util.Map
    public Collection<V> remove(Object obj) {
        return this.contents.remove(obj);
    }

    @Override // java.util.Map
    public boolean remove(Object obj, Object obj2) {
        Collection<V> collection = this.contents.get(obj);
        return collection != null && collection.remove(obj2);
    }

    @Override // java.util.Map
    public int size() {
        return this.contents.size();
    }

    public int size(K k) {
        Collection<V> collection = this.contents.containsKey(k) ? this.contents.get(k) : null;
        if (collection != null) {
            return collection.size();
        }
        return 0;
    }

    @Override // java.util.Map
    public Collection<Collection<V>> values() {
        return this.contents.values();
    }

    public Collection<V> valuesAll() {
        Collection<V> createCollection = createCollection();
        Iterator<Collection<V>> it = values().iterator();
        while (it.hasNext()) {
            createCollection.addAll(it.next());
        }
        return createCollection;
    }
}
