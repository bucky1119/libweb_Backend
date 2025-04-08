package com.cau.web.aop;

import com.cau.web.service.AuditLogService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Aspect
@Component
public class AuditLogAspect {

    @Autowired
    private AuditLogService auditLogService;

    // 定义切入点，拦截所有的Post、Put、Delete请求
    @Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping) || "
            + "@annotation(org.springframework.web.bind.annotation.PutMapping) || "
            + "@annotation(org.springframework.web.bind.annotation.DeleteMapping)")
    public void loggableOperations() {}

    // 拦截成功操作并记录日志
    @AfterReturning(pointcut = "loggableOperations()", returning = "result")
    public void logSuccess(JoinPoint joinPoint, Object result) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String username = (request.getUserPrincipal() != null) ? request.getUserPrincipal().getName() : "Anonymous";  // 如果用户未登录，使用匿名用户
        String operation = joinPoint.getSignature().getName();   // 获取方法名作为操作类型
        String details = "Parameters: " + Arrays.toString(joinPoint.getArgs()) + ", Result: " + (result != null ? result.toString() : "null"); // 请求参数和结果

        // 保存成功操作日志
        auditLogService.saveLog(username, operation, details);
    }

    // 拦截失败操作并记录日志
    @AfterThrowing(pointcut = "loggableOperations()", throwing = "ex")
    public void logFailure(JoinPoint joinPoint, Throwable ex) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String username = (request.getUserPrincipal() != null) ? request.getUserPrincipal().getName() : "Anonymous";  // 如果用户未登录，使用匿名用户
        String operation = joinPoint.getSignature().getName();   // 获取方法名作为操作类型
        String details = "Parameters: " + Arrays.toString(joinPoint.getArgs()) + ", Exception: " + ex.getMessage();  // 请求参数和异常信息

        // 保存失败操作日志
        auditLogService.saveLog(username, operation, details);
    }
}
