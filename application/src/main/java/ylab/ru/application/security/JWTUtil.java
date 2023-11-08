package ylab.ru.application.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Date;

/**
 * Утилитный класс для работы с JWT-токенами.
 * Отвечает за генерацию и валидацию токенов.
 */
@Component
public class JWTUtil {

    @Value("${jwt.secret}")
    private String SECRET_KEY;

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
                .withSubject("User details")
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
    public String validateTokenAndRetrieveClaim(String token) throws JWTVerificationException {
        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
        JWTVerifier verifier = JWT.require(algorithm)
                .withSubject("User details")
                .withIssuer("ylab")
                .build();

        DecodedJWT jwt = verifier.verify(token);
        return jwt.getClaim("username").asString();
    }


}
