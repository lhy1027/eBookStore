package com.ebookstore.aspect;

import com.ebookstore.entity.OperationLog;
import com.ebookstore.mapper.OperationLogMapper;
import com.ebookstore.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Aspect
@Component
@RequiredArgsConstructor
public class LogAspect {

    private final OperationLogMapper operationLogMapper;

    @Around("@annotation(com.ebookstore.annotation.Log)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        com.ebookstore.annotation.Log logAnnotation = signature.getMethod().getAnnotation(com.ebookstore.annotation.Log.class);
        String operation = logAnnotation.value();
        if (operation.isEmpty()) {
            operation = signature.getName();
        }
        OperationLog log = new OperationLog();
        Long userId = SecurityUtil.getCurrentUserId();
        log.setAdminId(userId);
        log.setOperation(operation);
        log.setTarget(signature.getDeclaringTypeName() + "." + signature.getName());
        log.setCreateTime(LocalDateTime.now());
        try {
            Object result = joinPoint.proceed();
            log.setResult("成功");
            return result;
        } catch (Exception e) {
            log.setResult("失败: " + e.getMessage());
            throw e;
        } finally {
            operationLogMapper.insert(log);
        }
    }
}
