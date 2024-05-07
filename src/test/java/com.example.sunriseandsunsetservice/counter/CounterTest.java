package com.example.sunriseandsunsetservice.counter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class CounterTest {

    @InjectMocks
    private Counter counter;

    @BeforeEach
    void setUp() {
        counter = new Counter();
    }

    @Test
    void incTest() {

        counter.inc();

        assertEquals(1, counter.getCount());
    }

    @Test
    void getTest() {

        assertEquals(0, counter.getCount());
    }
}
