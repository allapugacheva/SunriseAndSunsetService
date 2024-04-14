package com.example.sunriseandsunsetservice.aspect;

import com.example.sunriseandsunsetservice.counter.Counter;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * Aspect for counter.
 */
@Aspect
@Component
public class CounterAspect {

  private final Counter counter = new Counter();

  @Pointcut("execution(* com.example.sunriseandsunsetservice.service"
        + ".SunriseAndSunsetService.*(..))")
  public void mainService() {}

  @Before("mainService()")
  public void incrementBefore() {

    counter.inc();
  }
}
