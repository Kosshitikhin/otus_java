package ru.otus.cache;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.HashSet;
import java.util.Set;
import java.util.WeakHashMap;

@Slf4j
public class MyCache<K, V> implements HwCache<K, V> {
    //Надо реализовать эти методы

    private final Set<HwListener<K, V>> listeners;
    private final WeakHashMap<K, V> cache;

    public MyCache() {
        this.listeners = new HashSet<>();
        this.cache = new WeakHashMap<>();
    }

    @Override
    public void put(K key, V value) {
        cache.put(key, value);
        notifyAll(key, value, "PUT value to cache. Cache size = " + cache.size());
    }

    @Override
    public void remove(K key) {
        if (cache.containsKey(key)) {
            var value = cache.remove(key);
            notifyAll(key, value, "REMOVE value from cache. Cache size =  " + cache.size());
        }
    }

    @Override
    public V get(K key) {
        if (cache.containsKey(key)) {
            var value = cache.get(key);
            notifyAll(key, value, "GET value from cache. Cache size = " + cache.size());
            return value;
        }
        return null;
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        listeners.remove(listener);
    }

    private void notifyAll(K key, V value, String action) {
        listeners.forEach(listener -> {
            try {
                listener.notify(key, value, action);
            } catch (Exception e) {
                log.info("Exception in notify: {}", ExceptionUtils.getThrowableCount(e));
            }
        });
    }
}
