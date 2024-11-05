package com.cadify.cadifyWAS.security.exception;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.time.Instant;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write(
                "{\"code\": \"UN_AUTHORIZED\"," + "\n"
                        + "\"status\": \"401\"," + "\n"
                        + "\"message\": \"인증되지 않은 사용자 입니다.\"," + "\n"
                        + "\"occurredAt\": \"" + Instant.now() + "\"}");
    }
}
