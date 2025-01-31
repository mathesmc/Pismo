package com.app.banking.exception;

public class AmountOperationTypeMismatchException extends RuntimeException {
    public AmountOperationTypeMismatchException(String message) {
        super(message);
    }
}
