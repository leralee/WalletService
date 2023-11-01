package org.example.controller;

import org.example.in.WalletServiceFacade;
import org.example.model.Audit;
import org.example.model.Player;
import org.example.security.JWTUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * Контроллер для управления аудитными записями в административном интерфейсе.
 * Предоставляет функциональность для получения списка записей аудита.
 */
@Controller
@RequestMapping("/admin")
public class AuditController {
    private final JWTUtil jwtUtil;
    private final WalletServiceFacade walletServiceFacade;

    /**
     * Конструктор для {@code AuditController}.
     *
     * @param jwtUtil инструмент для работы с JWT.
     * @param walletServiceFacade фасад для сервиса кошелька.
     */
    public AuditController(JWTUtil jwtUtil, WalletServiceFacade walletServiceFacade) {
        this.jwtUtil = jwtUtil;
        this.walletServiceFacade = walletServiceFacade;
    }

    /**
     * Получить список записей аудита.
     * Этот метод доступен только для пользователей с ролью ADMIN.
     * Возвращает список аудитных записей или сообщение об ошибке, если пользователь не авторизован,
     * его роль не соответствует требуемой, или записи аудита отсутствуют.
     *
     * @param bearerToken токен авторизации, полученный в заголовке запроса.
     * @param httpSession текущая сессия HTTP, используется для получения данных авторизованного пользователя.
     * @return {@code ResponseEntity<?>} состояние HTTP ответа, содержащее список аудитов или сообщение об ошибке.
     */
    @GetMapping("/audits")
    public ResponseEntity<?> getRecords(@RequestHeader("Authorization") String bearerToken,
                                        HttpSession httpSession) {

        Player player = (Player) httpSession.getAttribute("loggedPlayer");

        if (!"ADMIN".equals(player.getRole())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("message", "У вас нет прав доступа к этой странице"));
        }

        if (player == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Пользователь не авторизован"));
        }

        String token = jwtUtil.extractTokenFromHeader(bearerToken);

        if (token == null || !jwtUtil.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Невалидный токен"));
        }


        List<Audit> auditList = walletServiceFacade.getRecords();

        if (auditList != null && !auditList.isEmpty()) {
            return ResponseEntity.ok(Map.of("message", auditList));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Аудитов не найдено"));
        }

    }

}
