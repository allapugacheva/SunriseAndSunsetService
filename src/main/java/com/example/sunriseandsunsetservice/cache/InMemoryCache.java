package com.example.sunriseandsunsetservice.cache;

import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 * Cache realisation.
 */
@Component
public class InMemoryCache {

  private final Map<String, Object> cache = new CustomLinkedHashMap<>(20);

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
