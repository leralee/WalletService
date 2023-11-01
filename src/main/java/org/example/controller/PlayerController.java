package org.example.controller;

import org.example.dto.PlayerDTO;

import org.example.in.WalletServiceFacade;
import org.example.model.Player;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

/**
 * REST контроллер для управления игроками в системе.
 * Обеспечивает регистрацию новых игроков в системе.
 */
@RestController
@RequestMapping("/players")
public class PlayerController {
    private final WalletServiceFacade walletServiceFacade;

    /**
     * Конструктор для {@link PlayerController}.
     *
     * @param walletServiceFacade Фасад сервиса кошелька, используемый для бизнес-логики, связанной с игроками.
     */
    public PlayerController(WalletServiceFacade walletServiceFacade) {
        this.walletServiceFacade = walletServiceFacade;
    }


    /**
     * Регистрирует нового игрока в системе на основе предоставленных данных.
     *
     * @param playerDTO DTO (Data Transfer Object) игрока, содержащий имя пользователя и пароль для регистрации.
     * @return {@code ResponseEntity<?>} с соответствующим HTTP статусом и сообщением о результате операции.
     * @throws IOException В случае возникновения ошибки ввода/вывода.
     */
    @PostMapping()
    public ResponseEntity<?> registerPlayer(@RequestBody PlayerDTO playerDTO) throws IOException {
        try {
            Optional<Player> registerPlayer = walletServiceFacade
                    .registerPlayer(playerDTO.getUsername(), playerDTO.getPassword());
            if (registerPlayer.isPresent()) {
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(Map.of("status", "success", "message", "Регистрация прошла успешно"));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("status", "error", "message", "Ошибка регистрации"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("status", "error", "message", "Ошибка на сервере"));
        }
    }
}
