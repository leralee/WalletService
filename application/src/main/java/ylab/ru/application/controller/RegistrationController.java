//package org.example.controller;
//
//import org.example.common.Player;
//import org.example.dto.PlayerDTO;
//
//import org.example.exception.PlayerExistsException;
//import org.example.in.WalletServiceFacade;
//
//import org.example.security.JWTUtil;
//import org.modelmapper.ModelMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.Map;
//import java.util.Optional;
//
///**
// * REST контроллер для управления игроками в системе.
// * Обеспечивает регистрацию новых игроков в системе.
// */
//@RestController
//@RequestMapping("/auth")
//public class RegistrationController {
//    private final WalletServiceFacade walletServiceFacade;
//    private final JWTUtil jwtUtil;
//
//    /**
//     * Конструктор для {@link RegistrationController}.
//     *
//     * @param walletServiceFacade Фасад сервиса кошелька, используемый для бизнес-логики, связанной с игроками.
//     * @param jwtUtil
//     */
//    @Autowired
//    public RegistrationController(WalletServiceFacade walletServiceFacade, JWTUtil jwtUtil) {
//        this.walletServiceFacade = walletServiceFacade;
//        this.jwtUtil = jwtUtil;
//    }
//
//    /**
//     * Регистрирует нового игрока в системе на основе предоставленных данных.
//     *
//     * @param playerDTO DTO (Data Transfer Object) игрока, содержащий имя пользователя и пароль для регистрации.
//     * @return {@code ResponseEntity<?>} с соответствующим HTTP статусом и сообщением о результате операции.
//     */
//    @PostMapping()
//    public ResponseEntity<?> save(@RequestBody PlayerDTO playerDTO) {
//        try {
//            ModelMapper modelMapper = new ModelMapper();
//            Player player = modelMapper.map(playerDTO, Player.class);
//            Optional<Player> registerPlayer = walletServiceFacade
//                    .save(player);
//            if (registerPlayer.isPresent()) {
//                return ResponseEntity.status(HttpStatus.CREATED)
//                        .body(Map.of("status", "success", "message", "Регистрация прошла успешно"));
//            } else {
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                        .body(Map.of("status", "error", "message", "Ошибка регистрации"));
//            }
//        } catch (PlayerExistsException e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                    .body(Map.of("message", e.getMessage()));
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(Map.of("status", "error", "message", "Ошибка на сервере"));
//        }
//    }
//}
