package org.example.security;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.example.configuration.ConfigurationUtil;

import java.time.ZonedDateTime;
import java.util.Date;

/**
 * Утилитный класс для работы с JWT-токенами.
 * Отвечает за генерацию и валидацию токенов.
 */
public class JWTUtil {

    private static final String SECRET_KEY = ConfigurationUtil.get("jwt_secret");

    /**
     * Генерирует JWT-токен для указанных имени пользователя и роли.
     *
     * @param username Имя пользователя для которого генерируется токен.
     * @param role Роль пользователя для которого генерируется токен.
     * @return Сгенерированный JWT-токен.
     */
    public String generateToken(String username, String role) {
        Date expirationDate = Date.from(ZonedDateTime.now().plusMinutes(60).toInstant());

        return JWT.create()
                .withClaim("username", username)
                .withClaim("role", role)
                .withIssuedAt(new Date())
                .withIssuer("ylab")
                .withExpiresAt(expirationDate)
                .sign(Algorithm.HMAC256(SECRET_KEY));
    }

    /**
     * Валидирует предоставленный JWT-токен.
     *
     * @param token JWT-токен, который необходимо проверить.
     * @return true, если токен действителен, иначе false.
     */
    public static boolean validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("ylab")
                    .build();
            verifier.verify(token);
            return true;
        } catch (JWTDecodeException exception) {
            return false;
        }
    }
}
