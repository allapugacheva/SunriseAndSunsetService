package com.example.sunriseandsunsetservice.aspect;

import com.example.sunriseandsunsetservice.counter.Counter;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * Aspect for counter.
 */
@Aspect
@Getter
@Setter
@Slf4j
@Component
public class CounterAspect {

  private final Counter counter = new Counter();

  @Pointcut("execution(* com.example.sunriseandsunsetservice.service.impl.*.*(..))")
  public void mainService() {}

  /**
    * Incrementation of counter.
    */
  @Before("mainService()")
  public void incrementBefore() {

    counter.inc();
    log.info("Current number of service calls: {}.", counter.getCount());
  }
}
