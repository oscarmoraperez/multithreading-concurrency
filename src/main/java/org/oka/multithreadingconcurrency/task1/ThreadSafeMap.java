package org.oka.multithreadingconcurrency.task1;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ThreadSafeMap<K, V> {
    private final Map<K, V> innerMap = new HashMap();

    public synchronized void put(K key, V value) {
        this.innerMap.put(key, value);
    }

    public synchronized Set<Map.Entry<K, V>> entrySet() {
        Set<Map.Entry<K, V>> setOfEntries = new HashSet<Map.Entry<K, V>>();
        for (Map.Entry<K, V> entry : innerMap.entrySet()) {
            setOfEntries.add(new AbstractMap.SimpleEntry<K, V>(entry.getKey(), entry.getValue()));
        }
        return setOfEntries;
    }
}
