package ylab.ru.application.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import ru.ylab.common.service.AuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ru.ylab.common.model.Audit;
import ru.ylab.common.model.Player;
import ylab.ru.application.security.JWTUtil;

import java.util.List;
import java.util.Map;

/**
 * Контроллер для управления аудитными записями в административном интерфейсе.
 * Предоставляет функциональность для получения списка записей аудита.
 */
@Tag(name = "User Controller", description = "Контроллер для аудита действий в системе")
@Controller
@RequestMapping("/admin")
public class AuditController {
    private final JWTUtil jwtUtil;
    private final AuditService auditService;

    /**
     * Конструктор для AuditController.
     *
     * @param jwtUtil        утилита для работы с JWT токенами
     * @param auditService   сервис для получения аудитных записей
     */
    @Autowired
    public AuditController(JWTUtil jwtUtil, AuditService auditService) {
        this.jwtUtil = jwtUtil;
        this.auditService = auditService;
    }

    /**
     * Получение списка аудитных записей.
     * Этот метод требует авторизации и доступен только для пользователей с ролью ADMIN.
     *
     * @param request информация о запросе, используется для аутентификации пользователя
     * @return список аудитных записей или сообщение об ошибке, если пользователь не авторизован
     *         или не имеет соответствующих прав доступа
     */
    @Operation(
            summary = "Получение списка аудитных записей",
            description = "Получение списка всех аудитных записей. Доступно только для администраторов.",
            security = @SecurityRequirement(name = "Bearer"),
            tags = { "Audit" }
    )
    @GetMapping("/audits")
    public ResponseEntity<?> getRecords(HttpServletRequest request) {

        Player player = (Player) request.getAttribute("player");

        if (player == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Пользователь не авторизован"));
        }

        if (!"ADMIN".equals(player.getRole().toString())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("message", "У вас нет прав доступа к этой странице"));
        }

        List<Audit> auditList = auditService.getRecords();

        if (auditList != null && !auditList.isEmpty()) {
            return ResponseEntity.ok(Map.of("message", auditList));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Аудитов не найдено"));
        }
    }
}
