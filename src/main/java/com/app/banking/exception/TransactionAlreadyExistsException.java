package com.app.banking.exception;

public class TransactionAlreadyExistsException extends RuntimeException {
    public TransactionAlreadyExistsException() {
        super("Transaction already exists");
    }
}
