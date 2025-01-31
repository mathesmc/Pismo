package com.app.banking.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<Map<String, Object>> createErrorResponse(Exception e, HttpStatus status) {
        Map<String, Object> body = new HashMap<>();
        body.put("message", e.getMessage());
        body.put("status", status.value());
        body.put("timestamp", System.currentTimeMillis());
        return new ResponseEntity<>(body, status);
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleAccountDoesNotExists(AccountNotFoundException e) {
        return createErrorResponse(e, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AccountAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handleAccountAlreadyExists(AccountAlreadyExistsException e) {
        return createErrorResponse(e, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(AmountOperationTypeMismatchException.class)
    public ResponseEntity<Map<String, Object>> handleAmountOperationTypeMismatch(AmountOperationTypeMismatchException e) {
        return createErrorResponse(e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalAmountScaleException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalAmountScale(IllegalAmountScaleException e) {
        return createErrorResponse(e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(OperationTypeNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleOperationTypeNotFound(OperationTypeNotFoundException e) {
        return createErrorResponse(e, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TransactionAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handleTransactionAlreadyExists(TransactionAlreadyExistsException e) {
        return createErrorResponse(e, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(TransactionNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleTransactionNotFound(TransactionNotFoundException e) {
        return createErrorResponse(e, HttpStatus.NOT_FOUND);
    }
}