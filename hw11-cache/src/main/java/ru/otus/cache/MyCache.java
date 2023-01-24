package ru.otus.cache;

public class MyCache<K, V> implements Cache<K, V> {
//Надо реализовать эти методы

    @Override
    public void put(K key, V value) {

    }

    @Override
    public void remove(K key) {

    }

    @Override
    public V get(K key) {
        return null;
    }

    @Override
    public void addListener(Listener<K, V> listener) {

    }

    @Override
    public void removeListener(Listener<K, V> listener) {

    }
}
