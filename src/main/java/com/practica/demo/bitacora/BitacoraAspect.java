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
public class BitacoraAspect {

    private final BitacoraRepository bitacoraRepository;
   
    @Around(
        "execution(* com.practica.demo.controller..*(..)) || " +
        "execution(* com.practica.demo.auth.controller..*(..))"
    )
    public Object registrar(ProceedingJoinPoint joinPoint) throws Throwable {
        long inicio = System.currentTimeMillis();

        String usuario = obtenerUsuarioActual();
        HttpServletRequest request = obtenerRequest();
        String metodoHttp = request != null ? request.getMethod() : "N/A";
        String endpoint = request != null ? request.getRequestURI() : "N/A";
        String metodoEjecutado = joinPoint.getSignature().toShortString();
        String parametros = sanitizarParametros(joinPoint.getArgs());

        String resultado = "OK";
        String mensajeError = null;

        try {
            return joinPoint.proceed();
        } catch (Throwable ex) {
            resultado = "ERROR";
            mensajeError = ex.getMessage();
            throw ex;
        } finally {
            long duracion = System.currentTimeMillis() - inicio;
            guardarBitacora(usuario, metodoHttp, endpoint, metodoEjecutado,
                    parametros, resultado, mensajeError, duracion);
        }
    }

    private void guardarBitacora(String usuario, String metodoHttp, String endpoint,
            String metodoEjecutado, String parametros, String resultado,
            String mensajeError, long duracion) {
        try {
            Bitacora bitacora = Bitacora.builder()
                    .usuario(usuario)
                    .metodoHttp(metodoHttp)
                    .endpoint(endpoint)
                    .metodoEjecutado(metodoEjecutado)
                    .parametros(parametros)
                    .resultado(resultado)
                    .mensajeError(mensajeError)
                    .duracionMs(duracion)
                    .fecha(LocalDateTime.now())
                    .build();
            bitacoraRepository.save(bitacora);
        } catch (Exception e) {
            log.error("Error al guardar la bitácora", e);
        }
    }

    private String obtenerUsuarioActual() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()
                && !"anonymousUser".equals(auth.getPrincipal())) {
            return auth.getName();
        }
        return "anonimo";
    }

    private HttpServletRequest obtenerRequest() {
        ServletRequestAttributes attrs =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attrs != null ? attrs.getRequest() : null;
    }

    private String sanitizarParametros(Object[] args) {
    return Arrays.stream(args)
            .map(arg -> {
                String texto = String.valueOf(arg);
                return texto.replaceAll("password=[^,\\]]+", "password=****");
            })
            .toList()
            .toString();
}
}