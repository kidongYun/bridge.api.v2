package com.kidongyun.bridge.api.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Aspect
public class ExecuteLogAspect {
    @Around(value = "@annotation(ExecuteLog)")
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("YKD : log!!!");

        return joinPoint.proceed();
    }
}
