package ru.otus.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class MyCache<K, V> implements Cache<K, V> {

    private final Map<K, V> cache = new WeakHashMap<>();
    private final List<Listener<K, V>> listeners = new ArrayList<>();

    public void notifyListeners(K key, V value, String action) {
        listeners.forEach(listener -> {
            try {
                listener.notify(key, value, action);
            } catch (Exception ex) {
                System.err.println("Exception at the time of notification of the listener: " + listener);//логирование исключения
            }
        });
    }

    @Override
    public void put(K key, V value) {
        cache.put(key, value);
        notifyListeners(key, value, "put");
    }

    @Override
    public void remove(K key) {
        cache.remove(key);
        notifyListeners(key, null, "remove");
    }

    @Override
    public V get(K key) {
        notifyListeners(key, null, "get");
        return cache.get(key);
    }

    @Override
    public void addListener(Listener<K, V> listener) {
        this.listeners.add(listener);
    }

    @Override
    public void removeListener(Listener<K, V> listener) {
        this.listeners.remove(listener);
    }

    @Override
    public boolean checkPresenceIdInCache(K key) {
        return cache.containsKey(key);
    }
}
