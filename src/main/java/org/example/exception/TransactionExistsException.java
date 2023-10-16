package org.example.exception;

/**
 * @author valeriali on {16.10.2023}
 * @project walletService
 */
public class TransactionExistsException extends Exception {
    public TransactionExistsException(String message) {
        super(message);
    }
}
