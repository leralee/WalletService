package org.example.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.example.in.WalletServiceFacade;
import org.example.common.Audit;
import org.example.common.Player;
import org.example.security.JWTUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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

        List<Audit> auditList = walletServiceFacade.getRecords();

        if (auditList != null && !auditList.isEmpty()) {
            return ResponseEntity.ok(Map.of("message", auditList));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Аудитов не найдено"));
        }

    }

}
