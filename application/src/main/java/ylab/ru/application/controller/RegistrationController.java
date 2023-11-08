package ylab.ru.application.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.ylab.common.exception.PlayerExistsException;
import ru.ylab.common.model.Player;
import ru.ylab.common.service.PlayerService;
import ylab.ru.application.dto.PlayerDTO;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/auth")
@Tag(name = "Registration Controller", description = "Контроллер для регистрации пользователя")
public class RegistrationController {

    private final PlayerService playerService;

    @Autowired
    public RegistrationController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @Operation(summary = "Регистрация нового игрока",
            description = "Создает новую учетную запись игрока с предоставленными данными.")
    @ApiResponse(responseCode = "201", description = "Регистрация прошла успешно")
    @ApiResponse(responseCode = "400", description = "Ошибка регистрации или такой игрок уже существует")
    @ApiResponse(responseCode = "500", description = "Ошибка на сервере")
    @PostMapping()
    public ResponseEntity<?> save(@RequestBody PlayerDTO playerDTO) {
        try {
            ModelMapper modelMapper = new ModelMapper();
            Player player = modelMapper.map(playerDTO, Player.class);
            Optional<Player> registerPlayer = playerService.save(player);
            if (registerPlayer.isPresent()) {
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(Map.of("status", "success", "message", "Регистрация прошла успешно"));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("status", "error", "message", "Ошибка регистрации"));
            }
        } catch (PlayerExistsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("status", "error", "message", "Ошибка на сервере"));
        }
    }
}
