package com.example.sunriseandsunsetservice.cache;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof CustomLinkedHashMap<?, ?> that)) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    return Objects.equals(maxSize, that.maxSize);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), maxSize);
  }
}
