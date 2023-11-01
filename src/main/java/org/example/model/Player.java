package org.example.model;

import java.math.BigDecimal;

/**
 * Класс представляющий собой игрока в системе. Содержит информацию о пользователе, такую как
 * идентификатор, роль, имя пользователя, пароль и баланс.
 */
public class Player {
    private Long id;
    private Role role;
    private String username;
    private String password;
    private BigDecimal balance = BigDecimal.ZERO;

    /**
     * Перечисление возможных ролей игроков в системе.
     */
    public enum Role {
        USER, ADMIN
    }

    /**
     * Конструктор по умолчанию для класса Player.
     */
    public Player() {

    }

    /**
     * @return Идентификатор игрока.
     */
    public Long getId() {
        return id;
    }

    /**
     * Устанавливает идентификатор для игрока.
     *
     * @param id Идентификатор игрока.
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return Имя пользователя игрока.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Устанавливает имя пользователя для игрока.
     *
     * @param username Имя пользователя.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return Пароль игрока.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Устанавливает пароль для игрока.
     *
     * @param password Пароль.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return Баланс игрока.
     */
    public BigDecimal getBalance() {
        return balance;
    }

    /**
     * Устанавливает баланс для игрока.
     *
     * @param balance Баланс.
     */
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    /**
     * @return Роль игрока в системе.
     */
    public Role getRole() {
        return role;
    }

    /**
     * Устанавливает роль для игрока.
     *
     * @param role Роль в системе.
     */
    public void setRole(Role role) {
        this.role = role;
    }

    /**
     * Представляет объект в виде строки.
     *
     * @return Строковое представление объекта.
     */
    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", balance=" + balance +
                '}';
    }
}
