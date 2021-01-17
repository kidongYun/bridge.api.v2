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
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        log.info("Method Name : " + method.getName());
        log.info("Input : " + Arrays.toString(signature.getParameterNames()));

        long start = System.currentTimeMillis();
        Object result = null;

        try {
            result = joinPoint.proceed();
            log.info("Output : " + result.toString());

        } catch(Exception e) {
            log.info("Output : " + e.getClass().getSimpleName() + " - " + e.getMessage());
            throw e;

        } finally {
            log.info("Execute Time : " + (System.currentTimeMillis() - start) + " ms");
        }

        return result;
    }
}
