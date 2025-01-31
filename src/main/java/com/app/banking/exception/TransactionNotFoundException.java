package com.app.banking.exception;

public class TransactionNotFoundException extends RuntimeException {
    public TransactionNotFoundException() {
        super("transaction not found");
    }
}
