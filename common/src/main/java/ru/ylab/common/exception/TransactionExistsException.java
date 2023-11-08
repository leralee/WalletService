package ru.ylab.common.exception;

/**
 * Представляет исключение, которое выбрасывается, когда транзакция уже существует
 * в определенном контексте, где дубликаты не допускаются.
 */
public class TransactionExistsException extends Exception {

    /**
     * Создает новое исключение TransactionExistsException с указанным детальным сообщением.
     *
     * @param message детальное сообщение, объясняющее, почему произошло исключение.
     */
    public TransactionExistsException(String message) {
        super(message);
    }
}
