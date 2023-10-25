package org.example;

import org.example.in.WalletServiceFacade;
import org.example.interfaces.IAuditRepository;
import org.example.interfaces.IPlayerRepository;
import org.example.interfaces.ITransactionRepository;
import org.example.repository.AuditRepository;
import org.example.repository.DatabaseConnection;
import org.example.repository.PlayerRepository;
import org.example.repository.TransactionRepository;
import org.example.service.AuditService;
import org.example.service.PlayerService;
import org.example.service.TransactionService;

import java.sql.SQLException;


/**
 * Главный класс приложения, отвечающий за инициализацию сервисов и старт пользовательского интерфейса.
 */
public class Main {

    /**
     * Точка входа в приложение.
     * <p>
     * Создает репозитории, инициализирует сервисы и запускает обработчик пользовательского ввода.
     * </p>
     *
     * @param args аргументы командной строки.
     */
    public static void main(String[] args) {

    }
}
