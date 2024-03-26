package com.example.sunriseandsunsetservice.cache;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Custom linked hash map class.
 */
public class CustomLinkedHashMap<K, V> extends LinkedHashMap<K, V> {

  private final Integer maxSize;

  public CustomLinkedHashMap(Integer maxSize) {
    super(maxSize, 0.75f, true);
    this.maxSize = maxSize;
  }

  @Override
  protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
    return size() > maxSize;
  }
}
