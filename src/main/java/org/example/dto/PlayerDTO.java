package org.example.dto;


import org.springframework.stereotype.Component;

/**
 * Класс DTO (Data Transfer Object) для передачи данных о пользователе.
 * Используется для получения информации при регистрации и аутентификации игроков.
 */
public class PlayerDTO {
    private String username;
    private String password;

    /**
     * Получить имя пользователя.
     *
     * @return Имя пользователя в системе.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Получить пароль пользователя.
     *
     * @return Пароль пользователя.
     */
    public String getPassword() {
        return password;
    }

}
