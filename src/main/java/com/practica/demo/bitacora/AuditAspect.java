package com.practica.demo.bitacora;

import java.time.LocalDateTime;
import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class AuditAspect {

    private final BitacoraRepository auditRepository;
   
    @Around(
        "execution(* com.practica.demo.controller..*(..)) || " +
        "execution(* com.practica.demo.auth.controller..*(..))"
    )
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();

        String user = getCurrentUser();
        HttpServletRequest request = getRequest();
        String httpMethod = request != null ? request.getMethod() : "N/A";
        String endpoint = request != null ? request.getRequestURI() : "N/A";
        String executedMethod = joinPoint.getSignature().toShortString();
        String parameters = sanitizeParameters(joinPoint.getArgs());

        String result = "OK";
        String errorMessage = null;

        try {
            return joinPoint.proceed();
        } catch (Throwable ex) {
            result = "ERROR";
            errorMessage = ex.getMessage();
            throw ex;
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            saveAuditLog(user, httpMethod, endpoint, executedMethod,
                    parameters, result, errorMessage, duration);
        }
    }

    private void saveAuditLog(String user, String httpMethod, String endpoint,
            String executedMethod, String parameters, String result,
            String errorMessage, long duration) {
        try {
            Bitacora auditLog = Bitacora.builder()
                    .user(user)
                    .httpMethod(httpMethod)
                    .endpoint(endpoint)
                    .executedMethod(executedMethod)
                    .parameters(parameters)
                    .result(result)
                    .errorMessage(errorMessage)
                    .durationMs(duration)
                    .timestamp(LocalDateTime.now())
                    .build();
            auditRepository.save(auditLog);
        } catch (Exception e) {
            log.error("Error saving audit log", e);
        }
    }

    private String getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()
                && !"anonymousUser".equals(auth.getPrincipal())) {
            return auth.getName();
        }
        return "anonymous";
    }

    private HttpServletRequest getRequest() {
        ServletRequestAttributes attrs =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attrs != null ? attrs.getRequest() : null;
    }

    private String sanitizeParameters(Object[] args) {
        return Arrays.stream(args)
                .map(arg -> {
                    String text = String.valueOf(arg);
                    return text.replaceAll("password=[^,\\]]+", "password=****");
                })
                .toList()
                .toString();
    }
}
