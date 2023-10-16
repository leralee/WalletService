package org.example.in;

import org.example.exception.InvalidAmountException;
import org.example.exception.TransactionExistsException;
import org.example.model.Audit;
import org.example.model.Player;



import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.UUID;

/**
 * Класс, отвечающий за обработку взаимодействия пользователя в приложении Wallet Service.
 * Предоставляет консольный интерфейс для регистрации, авторизации, просмотра баланса,
 * пополнения и снятия средств, а также, при наличии соответствующих прав, просмотра журнала аудита.
 */
public class WalletServiceHandler {


    private final WalletServiceFacade walletServiceFacade;
    private Player currentPlayer;
    private final Scanner scanner = new Scanner(System.in);


    public WalletServiceHandler(WalletServiceFacade walletServiceFacade) {
        this.walletServiceFacade = walletServiceFacade;
    }

    private int getUserChoice() {
        while (!scanner.hasNextInt()) {
            System.out.println("Пожалуйста, введите корректный номер операции.");
            scanner.nextLine();
        }
        int choice = scanner.nextInt();
        scanner.nextLine();
        return choice;
    }


    public void start() {
        while (true) {
            showMenu();
            int choice = getUserChoice();
            switch (choice) {
                case 1 -> registerPlayer();
                case 2 -> authorizePlayer();
                case 0 -> {
                    return;
                }
                default -> System.out.println("Неверный выбор. Попробуйте снова.");
            }
        }
    }

    private void showMenu() {
        System.out.println("\nВыберите действие:");
        System.out.println("1. Регистрация");
        System.out.println("2. Авторизация");
        System.out.println("0. Закрыть приложение");

        System.out.print("Ваш выбор: ");
    }

    private void showAuthorizedMenu() {
        System.out.println("\nВыберите действие:");
        System.out.println("1. Просмотр баланса");
        System.out.println("2. Пополнение");
        System.out.println("3. Снятие");
        if (currentPlayer != null) {
            if (currentPlayer.getRole() == Player.Role.ADMIN) {
                System.out.println("4. Просмотр аудита");
            }
        }
        System.out.println("5. Выход из профиля");
        System.out.print("Ваш выбор: ");
    }

    private void handleAuthorizedActions() {
        while (true) {
            showAuthorizedMenu();
            int choice = getUserChoice();

            switch (choice) {
                case 1 -> viewBalance();
                case 2 -> credit();
                case 3 -> withdraw();
                case 4 -> viewAudit();
                case 5 -> {
                    logout();
                    return;
                }

                default -> System.out.println("Неверный выбор. Попробуйте снова.");
            }
        }
    }

    private void registerPlayer() {
        System.out.print("Введите имя пользователя для регистрации: ");
        String username = scanner.nextLine();
        System.out.print("Введите пароль: ");
        String password = scanner.nextLine();

        Optional<Player> player = walletServiceFacade.registerPlayer(username, password);
        if (player.isPresent()) {
            System.out.println("Игрок успешно зарегистрирован.");
        } else {
            System.out.println("Ошибка регистрации. Игрок с таким именем уже существует.");
        }
    }

    private void authorizePlayer() {
        System.out.print("Введите имя пользователя: ");
        String username = scanner.nextLine();
        System.out.print("Введите пароль: ");
        String password = scanner.nextLine();

        Player authorizedPlayer = walletServiceFacade.authorizePlayer(username, password);
        if (authorizedPlayer != null) {
            currentPlayer = authorizedPlayer;
            System.out.println("Игрок " + currentPlayer.getUsername() + " успешно авторизован.");
            handleAuthorizedActions();
        } else {
            System.out.println("Авторизация не удалась. Неправильное имя пользователя или пароль.");
        }
    }

    private void viewBalance() {
        if (currentPlayer != null) {
            BigDecimal balance = walletServiceFacade.getPlayerBalance(currentPlayer.getId());
            System.out.println("Баланс игрока " + currentPlayer.getUsername() + ": " + balance);
        } else {
            System.out.println("Пожалуйста, авторизуйтесь для просмотра баланса.");
        }
    }

    private UUID requestTransactionId() {
        int attemptCount = 0;
        final int MAX_ATTEMPTS = 3;

        while (attemptCount < MAX_ATTEMPTS) {
            System.out.print("Введите идентификатор транзакции (формат UUID): ");
            String uuidString = scanner.nextLine();
            try {
                return UUID.fromString(uuidString);
            } catch (IllegalArgumentException e) {
                attemptCount++; // Увеличиваем счетчик попыток
                if (attemptCount < MAX_ATTEMPTS) {
                    System.out.println("Введен неверный формат UUID. У вас осталось " + (MAX_ATTEMPTS - attemptCount) + " попыток.");
                } else {
                    System.out.println("Введен неверный формат UUID. Превышено количество попыток.");
                }
            }
        }
        return null;
    }

    private void credit() {
        if (currentPlayer != null) {
            System.out.println("Введите сумму для кредита: ");
            BigDecimal amount = scanner.nextBigDecimal();
            scanner.nextLine();

            UUID transactionId = requestTransactionId();
            if (transactionId != null) {
                try {
                    walletServiceFacade.credit(currentPlayer, amount, transactionId);
                    System.out.println("Сумма успешно зачислена на ваш счет.");
                } catch (TransactionExistsException | InvalidAmountException e) {
                    System.out.println(e.getMessage());
                }
            }
        } else {
            System.out.println("Пожалуйста, авторизуйтесь для выполнения этой операции.");
        }
    }

    private void withdraw() {
        if (currentPlayer != null) {
            System.out.print("Введите сумму для снятия: ");
            BigDecimal amount = scanner.nextBigDecimal();
            scanner.nextLine();

            UUID transactionId = requestTransactionId();
            if (transactionId != null) {
                try {
                    walletServiceFacade.withdraw(currentPlayer, amount, transactionId);
                    System.out.println("Сумма успешно снята с вашего счета.");
                } catch (TransactionExistsException | InvalidAmountException e) {
                    System.out.println(e.getMessage());
                }
            }
        } else {
            System.out.println("Пожалуйста, авторизуйтесь для выполнения этой операции.");
        }
    }

    private void viewAudit() {
        List<Audit> audits = walletServiceFacade.getRecords();
        for (Audit audit : audits) {
            System.out.println(audit);
        }
    }

    private void logout() {
        if (currentPlayer != null) {
            walletServiceFacade.logout(currentPlayer.getId());
            currentPlayer = null;
            System.out.println("Вы успешно вышли из системы.");
        } else {
            System.out.println("Никто не авторизован в системе.");
        }
    }
}
