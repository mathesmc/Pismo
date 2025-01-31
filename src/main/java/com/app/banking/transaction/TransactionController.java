package com.app.banking.transaction;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<Long> createTransaction(@RequestBody TransactionCreationRecord transaction) {

        TransactionRecord transactionRecord = transactionService.createTransaction(transaction);

        return new ResponseEntity<>(transactionRecord.id(),HttpStatus.CREATED);
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<TransactionRecord> getTransaction(@PathVariable long transactionId) {

        return new ResponseEntity<>(transactionService.getTransaction(transactionId), HttpStatus.OK);

    }
}
