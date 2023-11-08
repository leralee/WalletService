package ylab.ru.application.exception;

/**
 * Исключение, предназначенное для ситуаций, когда указана недопустимая сумма транзакции.
*/
public class InvalidAmountException extends Exception {

    /**
     * Конструктор исключения с сообщением об ошибке.
     *
     * @param message Сообщение об ошибке, описывающее причину исключения.
     */
    public InvalidAmountException(String message) {
        super(message);
    }
}

