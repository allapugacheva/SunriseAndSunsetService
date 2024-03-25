package com.example.sunriseandsunsetservice.cache;

import org.springframework.stereotype.Component;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class InMemoryCache {

    private final Integer capacity = 20;

    private final Map<String, Object> cache = new LinkedHashMap<>(capacity, 0.75f, true) {
        @Override
        protected boolean removeEldestEntry(Map.Entry<String, Object> eldest) {
            return size() > capacity;
        }
    };

    public void put(String key, Object value) {
        cache.put(key, value);
    }

    public Object get(String key) {
        return cache.get(key);
    }

    public void remove(String key) {
        cache.remove(key);
    }

}
