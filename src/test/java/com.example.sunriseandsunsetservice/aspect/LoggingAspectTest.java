package com.example.sunriseandsunsetservice.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class LoggingAspectTest {

    @Mock
    private Signature signature;

    @Mock
    private JoinPoint joinPoint;

    @InjectMocks
    private LoggingAspect aspect;

    @Test
    void logBeforeTest() {

        when(joinPoint.getArgs()).thenReturn(new Object[]{"arg1", "arg2"});
        when(joinPoint.getSignature()).thenReturn(signature);
        when(signature.getName()).thenReturn("Name");

        aspect.logBefore(joinPoint);

        verify(joinPoint, times(2)).getArgs();
        verify(joinPoint).getSignature();
        verify(signature).getName();
    }

    @Test
    void logAfterExecutionTest() {

        when(joinPoint.getSignature()).thenReturn(signature);
        when(signature.getName()).thenReturn("Name");

        aspect.logAfterExecuting(joinPoint);

        verify(joinPoint).getSignature();
        verify(signature).getName();
    }

    @Test
    void logAfterThrowingTest() {

        when(joinPoint.getSignature()).thenReturn(signature);
        when(signature.getName()).thenReturn("Name");

        aspect.logAfterThrowing(joinPoint, new Throwable("Message"));

        verify(joinPoint).getSignature();
        verify(signature).getName();
    }
}
