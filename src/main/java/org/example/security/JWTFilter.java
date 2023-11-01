package org.example.security;

import com.auth0.jwt.exceptions.JWTVerificationException;

import org.example.model.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Фильтр для административных запросов, требующих аутентификации.
 * Проверяет наличие и валидность JWT-токена в заголовке запроса.
 */
@Component
public class JWTFilter extends OncePerRequestFilter {
    private final Set<String> includedPaths = new HashSet<>(Arrays.asList("/secure-endpoint1", "/secure-endpoint2"));

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if (includedPaths.contains(request.getServletPath())) {

            HttpSession session = request.getSession();
            Player player = (Player) session.getAttribute("loggedPlayer");
            if (player == null) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Пользователь не авторизован");
                return;
            }

            String authHeader = request.getHeader("Authorization");
            if (authHeader != null && !authHeader.isBlank() && authHeader.startsWith("Bearer ")) {
                String jwt = authHeader.substring(7);
                if (jwt.isBlank()) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Невалидный JWT токен");
                    return;
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}
