package com.example.sunriseandsunsetservice.aspect;

import com.example.sunriseandsunsetservice.counter.Counter;
import lombok.AllArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * Aspect for counter.
 */
@Aspect
@Component
@AllArgsConstructor
public class CounterAspect {

  private Counter counter;

  @Pointcut("execution(* com.example.sunriseandsunsetservice.service"
        + ".SunriseAndSunsetService.*(..))")
  public void mainService() {}

  @Before("mainService()")
  public void incrementBefore() {

    counter.inc();
  }
}
