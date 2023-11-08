package org.example.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.dto.PlayerDTO;

import org.example.model.Audit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Контроллер для аутентификации пользователей в системе.
 * Обеспечивает логику входа в систему и создания JWT токена для аутентифицированных пользователей.
 */
@RestController
public class LoginController {
    private static final Logger LOGGER = LogManager.getLogger(LoginController.class);

//    private final JWTUtil jwtUtil;
//
//    private final WalletServiceFacade walletServiceFacade;

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public LoginController(JdbcTemplate jdbcTemplate) {
//        this.jwtUtil = jwtUtil;
//        this.walletServiceFacade = walletServiceFacade;
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Обрабатывает запрос на аутентификацию пользователя.
     * Если данные аутентификации корректны, создает и возвращает JWT, в противном случае возвращает ошибку аутентификации.
     *
     * @param playerDTO Объект передачи данных, содержащий учетные данные пользователя (имя пользователя и пароль).
     * @return {@code ResponseEntity<?>} с JWT в заголовках и сообщением об успешной аутентификации или ошибке.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody PlayerDTO playerDTO) {
        String query = "SELECT * FROM wallet.audit";
        return (ResponseEntity<?>) jdbcTemplate.query(query, new BeanPropertyRowMapper<>(Audit.class));

//        try {
//            ModelMapper modelMapper = new ModelMapper();
//            Player player = modelMapper.map(playerDTO, Player.class);
//            Player loggedPlayer = walletServiceFacade.authorizePlayer(player);
//            if (loggedPlayer != null) {
//                String token = jwtUtil.generateToken(loggedPlayer.getUsername(), loggedPlayer.getRole().toString());
//                return new ResponseEntity<>(Map.of(
//                        "status", "success",
//                        "message", "Успешная авторизация",
//                        "token", token
//                ), HttpStatus.OK);
//            } else {
//                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                        .body(Map.of("status", "error", "message", "Ошибка авторизации. Неправильное имя пользователя или пароль."));
//            }
//        } catch (Exception e) {
//            LOGGER.error("Ошибка на сервере ", e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(Map.of("status", "error", "message", "Ошибка на сервере"));
//        }
    }
}

