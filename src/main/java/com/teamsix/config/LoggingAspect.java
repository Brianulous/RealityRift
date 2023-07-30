package com.teamsix.config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.teamsix.model.bean.Member;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


@Aspect
@Component
public class LoggingAspect {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Pointcut("within(@org.springframework.stereotype.Controller *) || within(@org.springframework.web.bind.annotation.RestController *)")
    public void anyController() {
    }

    @Around("anyController()")
    public Object logAroundController(ProceedingJoinPoint joinPoint) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpSession session = attributes.getRequest().getSession();
        
        Member sessionMember = (Member) session.getAttribute("member");
        String user;
        if (sessionMember == null) {
            user = "visitor";
        } else {
            user = sessionMember.getEmail();
        }

        log.info("User: {}, Entering method: {} of class: {}", user, joinPoint.getSignature().getName(), joinPoint.getTarget().getClass().getSimpleName());
        try {
            Object result = joinPoint.proceed();
            log.info("User: {}, Leaving method: {} of class: {}", user, joinPoint.getSignature().getName(), joinPoint.getTarget().getClass().getSimpleName());
            return result;
        } catch (Exception e) {
            log.error("User: {}, Error in method: {} of class: {}", user, joinPoint.getSignature().getName(), joinPoint.getTarget().getClass().getSimpleName(), e);
            throw e;
        }
    }


}
