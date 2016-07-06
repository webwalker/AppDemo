package com.webwalker.framework.dns.util;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by bailong on 15/6/18.
 */
public final class LruCache<K, V> {
    private LinkedList<K> list;
    public HashMap<K, V> map;
    private int size;

    public LruCache() {
        this(256);
    }

    public LruCache(int size) {
        list = new LinkedList<>();
        map = new HashMap<>();
        this.size = size;
    }

    public LruCache put(K k, V v) {
        if (list.size() == size) {
            K old = list.pollLast();
            map.remove(old);
        }
        map.put(k, v);
        list.push(k);
        return this;
    }

    public LruCache delete(K k) {
        list.remove(k);
        map.remove(k);
        return this;
    }

    public V get(K k) {
        V v = map.get(k);
        list.remove(k);
        list.push(k);
        return v;
    }

    public void clear() {
        list.clear();
        map.clear();
    }
}
