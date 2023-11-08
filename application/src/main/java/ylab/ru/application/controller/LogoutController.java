package ylab.ru.application.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * REST контроллер для управления выходом пользователей из системы.
 * Позволяет завершить сессию пользователя и удалить его данные из текущего контекста.
 */
@RestController
@RequestMapping("/logout")
public class LogoutController {
    @GetMapping("")
    public ResponseEntity<?> logout() {

        return ResponseEntity.status(HttpStatus.OK).body(Map.of("message", "Вы успешно вышли из системы"));
    }
}
