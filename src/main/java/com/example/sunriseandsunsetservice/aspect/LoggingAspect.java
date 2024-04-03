package com.example.sunriseandsunsetservice.aspect;

import java.util.Arrays;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * Logging aspect class.
 */
@Slf4j
@Aspect
@Component
public class LoggingAspect {

  @Pointcut("execution(* com.example.sunriseandsunsetservice.service.impl.*.*(..))"
         + " || execution(* com.example.sunriseandsunsetservice.service.CommonService.*(..))")
  public void allMethods() {}

  @Pointcut("execution(* com.example.sunriseandsunsetservice.service.SunriseAndSunsetService"
         + ".updateSunriseAndSunset()) || execution(* com.example.sunriseandsunsetservice"
         + ".service.LocationService.updateLocation()) || execution(* com.example"
         + ".sunriseandsunsetservice.service.DateService.updateDate()) || execution(* com"
         + ".example.sunriseandsunsetservice.service.CommonService.notValidLat(..)) ||"
         + "execution(* com.example.sunriseandsunsetservice.service"
         + ".CommonService.notValidLng(..)) || execution(* com.example.sunriseandsunsetservice"
         + ".service.CommonService.get*()) || execution(* com.example.sunriseandsunsetservice"
         + ".service.impl.*.get*())")
  public void excludedMethods() {}

  /**
   * Logging before starting method.
   */
  @Before("allMethods() && !excludedMethods()")
  public void logBefore(JoinPoint joinPoint) {

    if (joinPoint.getArgs().length != 0) {
      log.info("Started method " + joinPoint.getSignature().getName() + " with arguments "
          + Arrays.stream(joinPoint.getArgs())
          .map(Object::toString)
          .collect(Collectors.joining(", ")));
    } else {
      log.info("Started method " + joinPoint.getSignature().getName() + " with no arguments");
    }
  }

  /**
   * Logging after executing method.
   */
  @AfterReturning("allMethods() && !excludedMethods()")
  public void logAfterExecuting(JoinPoint joinPoint) {

    log.info("Method " + joinPoint.getSignature().getName() + " executed.");
  }

  /**
   * Logging after exception.
   */
  @AfterThrowing(pointcut = "allMethods()", throwing = "exception")
  public void logAfterThrowing(JoinPoint joinPoint, Throwable exception) {

    log.info("Method " + joinPoint.getSignature().getName() + " threw an exception "
        + exception.getClass().getSimpleName() + " with message " + exception.getMessage());
  }
}
