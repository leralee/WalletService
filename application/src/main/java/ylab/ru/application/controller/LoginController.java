package ylab.ru.application.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import ylab.ru.application.dto.PlayerDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.ylab.common.model.Player;
import ylab.ru.application.security.JWTUtil;
import ru.ylab.common.service.PlayerService;
import java.util.Map;

@Tag(name = "Login Controller", description = "Контроллер для аутентификации пользователя")
@RestController
public class LoginController {
    private static final Logger LOGGER = LogManager.getLogger(LoginController.class);

    private final JWTUtil jwtUtil;
    private final PlayerService playerService;

    @Autowired
    public LoginController(JWTUtil jwtUtil, PlayerService playerService) {
        this.jwtUtil = jwtUtil;
        this.playerService = playerService;
    }

    @Operation(summary = "Авторизация пользователя и получение токена",
            description = "Проверяет учетные данные пользователя и возвращает JWT токен для аутентификации в системе.")
    @ApiResponse(responseCode = "200", description = "Авторизация успешна")
    @ApiResponse(responseCode = "401", description = "Ошибка авторизации. Неправильное имя пользователя или пароль")
    @ApiResponse(responseCode = "500", description = "Ошибка на сервере")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody PlayerDTO playerDTO) {
        try {
            ModelMapper modelMapper = new ModelMapper();
            Player player = modelMapper.map(playerDTO, Player.class);
            Player loggedPlayer = playerService.authorizePlayer(player);
            if (loggedPlayer != null) {
                String token = jwtUtil.generateToken(loggedPlayer.getUsername(), loggedPlayer.getRole().toString());
                return new ResponseEntity<>(Map.of(
                        "status", "success",
                        "message", "Успешная авторизация",
                        "token", token
                ), HttpStatus.OK);
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

