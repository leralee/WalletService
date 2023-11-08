package org.example.controller;

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

    /**
     * Обрабатывает запрос на выход пользователя из системы.
     * Удаляет пользователя из текущей сессии и делает сессию недействительной.
     *
     * @param session Текущая сессия HTTP, из которой необходимо удалить пользователя.
     * @return {@code ResponseEntity<?>} со статусом OK и сообщением об успешном выходе из системы.
     */
//    @GetMapping("")
//    public ResponseEntity<?> logout(HttpSession session) {
//        session.removeAttribute("loggedPlayer");
//        session.invalidate();
//
//        return ResponseEntity.status(HttpStatus.OK).body(Map.of("message", "Вы успешно вышли из системы"));
//    }
}
