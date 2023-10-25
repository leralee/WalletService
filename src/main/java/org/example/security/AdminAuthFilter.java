package org.example.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.apache.commons.lang3.StringUtils;
import org.example.configuration.ConfigurationUtil;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Фильтр для административных запросов, требующих аутентификации.
 * Проверяет наличие и валидность JWT-токена в заголовке запроса и его соответствие роли "ADMIN".
 */
@WebFilter(filterName = "AdminAuthFilter", urlPatterns = {"/admin/*"})
public class AdminAuthFilter implements Filter {

    /**
     * Выполняет фильтрацию входящих запросов, проверяя JWT-токен.
     *
     * @param req Входящий запрос.
     * @param resp Ответ сервера.
     * @param chain Цепочка фильтров.
     * @throws IOException В случае ошибки ввода-вывода.
     * @throws ServletException В случае ошибки сервлета.
     */
    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        String token = extractTokenFromHeader(request);
        if (!isAdmin(token)) {
            HttpServletResponse response = (HttpServletResponse) resp;
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Access denied");
            return;
        }
        chain.doFilter(req, resp);
    }

    /**
     * Извлекает JWT-токен из заголовка авторизации в запросе.
     *
     * @param request HTTP-запрос.
     * @return JWT-токен или null, если токен отсутствует или некорректен.
     */
    public static String extractTokenFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.isNotEmpty(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    /**
     * Проверяет, является ли предоставленный JWT-токен токеном для роли "ADMIN".
     *
     * @param token JWT-токен для проверки.
     * @return true, если токен соответствует роли "ADMIN", иначе false.
     */
    private boolean isAdmin(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(ConfigurationUtil.get("jwt_secret"));
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("ylab")
                    .build();
            DecodedJWT jwt = verifier.verify(token);


            String role = jwt.getClaim("role").asString();
            System.out.println(role);

            return "ADMIN".equals(role);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
