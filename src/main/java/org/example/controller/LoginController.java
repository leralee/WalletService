package org.example.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.dto.PlayerDTO;
import org.example.in.WalletServiceFacade;
import org.example.model.Player;
import org.example.model.Player.Role;
import org.example.security.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * Контроллер для аутентификации пользователей в системе.
 * Обеспечивает логику входа в систему и создания JWT токена для аутентифицированных пользователей.
 */
@RestController
public class LoginController {
    private static final Logger LOGGER = LogManager.getLogger(LoginController.class);

    private final JWTUtil jwtUtil;

    private final WalletServiceFacade walletServiceFacade;

    /**
     * Конструктор класса {@code LoginController}.
     *
     * @param jwtUtil Утилита для работы с JSON Web Tokens.
     * @param walletServiceFacade Фасад, предоставляющий сервисы кошелька.
     */
    @Autowired
    public LoginController(JWTUtil jwtUtil, WalletServiceFacade walletServiceFacade) {
        this.jwtUtil = jwtUtil;
        this.walletServiceFacade = walletServiceFacade;
    }

    /**
     * Обрабатывает запрос на аутентификацию пользователя.
     * Если данные аутентификации корректны, создает и возвращает JWT, в противном случае возвращает ошибку аутентификации.
     *
     * @param playerDTO Объект передачи данных, содержащий учетные данные пользователя (имя пользователя и пароль).
     * @param session   Текущая сессия HTTP, в которую добавляется аутентифицированный пользователь.
     * @return {@code ResponseEntity<?>} с JWT в заголовках и сообщением об успешной аутентификации или ошибке.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody PlayerDTO playerDTO, HttpSession session) {
        try {
            Player loggedPlayer = walletServiceFacade.authorizePlayer(playerDTO.getUsername(), playerDTO.getPassword());
            if (loggedPlayer != null) {
                session.setAttribute("loggedPlayer", loggedPlayer);
                Role role = loggedPlayer.getRole();

                String token = jwtUtil.generateToken(loggedPlayer.getUsername(), role.toString());
                HttpHeaders headers = new HttpHeaders();
                headers.set("Authorization", "Bearer " + token);

                return new ResponseEntity<>(Map.of("status", "success", "message", "Успешная авторизация"), headers, HttpStatus.OK);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("status", "error", "message", "Ошибка авторизации. Неправильное имя пользователя или пароль."));
            }
        } catch (Exception e) {
            LOGGER.error("Ошибка на сервере ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("status", "error", "message", "Ошибка на сервере"));
        }
    }
}

