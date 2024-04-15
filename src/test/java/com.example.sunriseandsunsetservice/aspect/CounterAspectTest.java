package com.example.sunriseandsunsetservice.aspect;

import com.example.sunriseandsunsetservice.counter.Counter;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CounterAspectTest {

    @Mock
    private Counter counter;

    @InjectMocks
    private CounterAspect aspect;

    @BeforeEach
    @SneakyThrows
    public void setUp() {

        Field field = CounterAspect.class.getDeclaredField("counter");
        field.setAccessible(true);
        field.set(aspect, counter);
    }

    @Test
    void incrementBeforeTest() {

        aspect.incrementBefore();

        verify(counter).inc();
    }
}
