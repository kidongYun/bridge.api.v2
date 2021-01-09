package com.kidongyun.bridge.api.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;

@Slf4j
@Component
@Aspect
public class ExecuteLogAspect {
    @Around(value = "@annotation(ExecuteLog)")
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long end = System.currentTimeMillis();

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        log.info("Method Name : " + method.getName());
        log.info("Input : " + Arrays.toString(signature.getParameterNames()));
        log.info("Output : " + result.toString());
        log.info("Execute Time : " + (end - start) + " ms");

        return result;
    }
}
