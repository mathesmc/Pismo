package com.app.banking.exception;

public class OperationTypeNotFoundException extends RuntimeException {
    public OperationTypeNotFoundException() {
        super("Operation type not found");
    }
}
