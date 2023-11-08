package ylab.ru.application.security;

import com.auth0.jwt.exceptions.JWTVerificationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.ylab.common.model.Player;
import ru.ylab.common.service.PlayerService;

import java.io.IOException;
import java.util.Optional;


/**
 * Фильтр для административных запросов, требующих аутентификации.
 * Проверяет наличие и валидность JWT-токена в заголовке запроса.
 */
@Component
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    private final PlayerService playerService;

    @Autowired
    public JWTFilter(JWTUtil jwtUtil, PlayerService playerService) {
        this.jwtUtil = jwtUtil;
        this.playerService = playerService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && !authHeader.isBlank() && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);

            try {
                if (!jwt.isBlank()) {
                    String username = jwtUtil.validateTokenAndRetrieveClaim(jwt);
                    Optional<Player> player = playerService.findByUsername(username);

                    player.ifPresent(value -> request.setAttribute("player", value));
                } else {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Пользователь не найден");
                    return;
                }
            } catch (JWTVerificationException e) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Невалидный JWT токен");
                return;
            }
    }
    filterChain.doFilter(request, response);
    }

}
