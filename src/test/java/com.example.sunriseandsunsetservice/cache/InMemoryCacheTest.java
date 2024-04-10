package com.example.sunriseandsunsetservice.cache;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
public class InMemoryCacheTest {

    @InjectMocks
    private InMemoryCache cache;

    @Test
    void testGetAndPut() {

        cache.put("key", "value");

        assertEquals("value", cache.get("key"));
    }

    @Test
    void testRemove() {

        cache.put("key", "value");

        cache.remove("key");

        assertNull(cache.get("key"));
    }
}
