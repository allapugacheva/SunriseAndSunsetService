package com.example.sunriseandsunsetservice.aspect;

import com.example.sunriseandsunsetservice.counter.Counter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CounterAspectTest {

    @Mock
    private Counter counter;

    @InjectMocks
    private CounterAspect aspect;

    @BeforeEach
    public void setUp() {

        aspect = new CounterAspect(counter);
    }

    @Test
    void incrementBeforeTest() {

        aspect.incrementBefore();

        verify(counter).inc();
    }
}
