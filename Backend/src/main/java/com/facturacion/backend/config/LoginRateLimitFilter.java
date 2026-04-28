package com.facturacion.backend.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Filtro de rate limiting para el endpoint de login.
 * Limita a 10 intentos por IP por minuto para prevenir ataques de fuerza bruta.
 */
@Component
public class LoginRateLimitFilter extends OncePerRequestFilter {

    private static final int MAX_ATTEMPTS = 10;
    private static final long WINDOW_MILLIS = 60_000L; // 1 minuto

    private final Map<String, AtomicInteger> attempts = new ConcurrentHashMap<>();
    private final Map<String, Long> windowStart = new ConcurrentHashMap<>();

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // Solo aplica al endpoint de login (POST)
        if (!"/api/auth/login".equals(request.getRequestURI())
                || !"POST".equals(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        String ip = getClientIp(request);
        long now = System.currentTimeMillis();

        windowStart.putIfAbsent(ip, now);
        attempts.putIfAbsent(ip, new AtomicInteger(0));

        // Reiniciar ventana si ya pasó el tiempo
        if (now - windowStart.get(ip) > WINDOW_MILLIS) {
            windowStart.put(ip, now);
            attempts.get(ip).set(0);
        }

        int count = attempts.get(ip).incrementAndGet();
        if (count > MAX_ATTEMPTS) {
            response.setStatus(429);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(
                "{\"error\":\"Demasiados intentos fallidos. Espere 1 minuto antes de intentar nuevamente.\"}"
            );
            return;
        }

        filterChain.doFilter(request, response);
    }

    private String getClientIp(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        if (forwarded != null && !forwarded.isBlank()) {
            // Tomar solo la primera IP (puede ser lista separada por comas)
            return forwarded.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
